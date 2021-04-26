package cn.pioneeruniverse.common.nexus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.StringUtil;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;

import cn.pioneeruniverse.common.utils.HttpUtil;
import cn.pioneeruniverse.dev.entity.TblToolInfo;

/**
 * Nexus Rest API 基础公共类
 * @author zhoudu
 *
 */
public class NexusUtil {
	private static final Logger logger = LoggerFactory.getLogger(NexusUtil.class);
	
	private String baseUrl; //包件存储url
	private String username;//登录用户
	private String password;//登录密码
	
	private static final String TOKEN_REGEX = "^.+\"continuationToken\".+?(?=\"(.+?)\"|null|NULL).+$";//用来判断返回请求是否还有后缀内容
	
	/**
	 * 构建方法
	 * @param baseUrl
	 * @param username
	 * @param password
	 */
	public NexusUtil(String baseUrl, String username, String password) {
		if (!baseUrl.endsWith("/")) {
			baseUrl += "/";
		}
		this.baseUrl = baseUrl;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * 构建方法
	 * @param bean
	 */
	public NexusUtil(TblToolInfo bean) {
		String baseUrl = "http://" + bean.getIp() + ":" + bean.getPort() + bean.getContext();// http://localhost:8081/nexus/
		this.baseUrl = baseUrl;
		this.username = bean.getUserName();
		this.password = bean.getPassword();
		
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 发送GET请求,返回查询LIST的Json字符串
	 * @param componentList
	 * @param url
	 * @param clazz
	 * @return
	 */
	private String sendGetListUrl(List list, String url, Class clazz) throws Exception {
		String continuationToken;
		String resultJson = HttpUtil.httpRequest(url, "GET", null);
		continuationToken = resultJson.replaceAll(NexusUtil.TOKEN_REGEX, "$1");
		if (StringUtil.isNotEmpty(resultJson)) {
			resultJson = resultJson.substring(resultJson.indexOf("["), resultJson.lastIndexOf("]") + 1);
			list.addAll(JSON.parseArray(resultJson, clazz));
		}
		return continuationToken;
	}

	/**
	 * 将参数对象转换成URL参数连接串
	 * @param searchVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getParameterWithVO(NexusSearchVO searchVO) throws Exception {
		if (searchVO != null) {
			searchVO.setAllFieldEmptyToNull();
		}
		String paraStr = "";
		if (searchVO != null) {
			Map paraMap = JSON.parseObject(JSON.toJSONString(searchVO), Map.class);
			if (paraMap.size() > 0) {
				Collection collection = Collections2.transform(paraMap.entrySet(), new Function<Map.Entry<String, String>, String>(){
					@Override
					public String apply(Entry<String, String> entry) {
						Escaper escaper = UrlEscapers.urlFormParameterEscaper();
						//&maven.groupId=111&maven.artifactId=222&maven.baseVersion=333&maven.extension=444&maven.classifier=555
						String key = entry.getKey();
						if ("groupId,artifactId,baseVersion,extension,classifier".indexOf(key) != -1) {
							key = "maven." + key;
						}
						return escaper.escape(key) + "=" + escaper.escape(entry.getValue());
					}
				});
				paraStr = StringUtils.join(collection, "&");
				if (StringUtil.isNotEmpty(paraStr)) {
					paraStr = "?" + paraStr;
				}
			}
		}
		return paraStr;
	}
	
	/**
	 * 获取仓库列表
	 * http://localhost:8081/nexus/service/rest/v1/repositories
	 * @return
	 */
	public List<NexusRepositoryBO> getRepositoryList() throws Exception {
		List<NexusRepositoryBO> repositoryList = new ArrayList<NexusRepositoryBO>();
		String url = this.baseUrl + "service/rest/v1/repositories";
		String assetsJson = HttpUtil.httpRequest(url, "GET", null);
		if (StringUtil.isNotEmpty(assetsJson)) {
			repositoryList = JSON.parseArray(assetsJson, NexusRepositoryBO.class);
		}
		return repositoryList;
	}

	/**
	 * 获取仓库Component列表
	 * http://localhost:8081/nexus/service/rest/v1/assets?repository=maven-releases&continuationToken=5818005de2eb2bd1081fb5e1ddaaf23c
	 * @return
	 */
	public List<NexusComponentBO> getComponentList(String repositoryName) throws Exception {
		List<NexusComponentBO> componentList = new ArrayList<NexusComponentBO>();
		String continuationToken = null;
		String url = null;
		if (StringUtil.isNotEmpty(repositoryName)) {
			do {
				if (StringUtil.isEmpty(continuationToken)) {
					url = this.baseUrl + "service/rest/v1/components?repository=" + repositoryName;
				} else {
					url = this.baseUrl + "service/rest/v1/components?repository=" + repositoryName + "&continuationToken=" + continuationToken;
				}
				continuationToken = sendGetListUrl(componentList, url, NexusComponentBO.class);
			} while (StringUtil.isNotEmpty(continuationToken));
		}
		return componentList;
	}

	/**
	 * 根据componentId获取Component
	 * @param assetId
	 * @return
	 */
	public NexusComponentBO getComponent(String componentId) throws Exception {
		NexusComponentBO component = null;
		String url = null;
		if (StringUtil.isNotEmpty(componentId)) {
			url = this.baseUrl + "service/rest/v1/components/" + componentId;
			String componentJson = HttpUtil.httpRequest(url, "GET", null);
			if (StringUtil.isNotEmpty(componentJson)) {
				component = JSON.parseObject(componentJson, NexusComponentBO.class);
			}
		}
		return component;
	}
	
	/**
	 * 删除包件component
	 * @param componentId
	 * @return
	 * @throws Exception
	 */
	public String deleteComponent(String componentId) throws Exception {
		String resultStr = "";
		if (StringUtil.isNotEmpty(componentId)) {
			String url = this.baseUrl + "service/rest/v1/components/" + componentId;
			Integer result = HttpUtil.doDelete(url, null, null,null, null, username, password);
//			204 Component was successfully deleted
//			403 Insufficient permissions to delete component
//			404 Component not found
//			422 Malformed ID
			if (result != null) {
				if (result == 204) {
					resultStr = "SUCCESS";
				} else if (result == 403) {
					resultStr = "权限不足";
				} else if (result == 404) {
					resultStr = "Nexus不存在此包件";
				} else if (result == 422) {
					resultStr = "包件编码无效";
				} else {
					resultStr = "调用NexusAPI失败";
				}
			}
		}
		return resultStr;
	}
	
	/**
	 * 获取仓库assets列表
	 * http://localhost:8081/nexus/service/rest/v1/assets?repository=maven-releases&continuationToken=5818005de2eb2bd1081fb5e1ddaaf23c
	 * @return
	 */
	public List<NexusAssetBO> getAssetsList(String repositoryName) throws Exception {
		List<NexusAssetBO> assetList = new ArrayList<NexusAssetBO>();
		String continuationToken = null;
		String url = null;
		if (StringUtil.isNotEmpty(repositoryName)) {
			do {
				if (StringUtil.isEmpty(continuationToken)) {
					url = this.baseUrl + "service/rest/v1/assets?repository=" + repositoryName;
				} else {
					url = this.baseUrl + "service/rest/v1/assets?repository=" + repositoryName + "&continuationToken=" + continuationToken;
				}
				continuationToken = sendGetListUrl(assetList, url, NexusAssetBO.class);
			} while (StringUtil.isNotEmpty(continuationToken));
		}
		return assetList;
	}
	
	/**
	 * 根据assetId获取assets
	 * @param assetId
	 * @return
	 */
	public NexusAssetBO getAssets(String assetId) throws Exception {
		NexusAssetBO asset = null;
		String url = null;
		if (StringUtil.isNotEmpty(assetId)) {
			url = this.baseUrl + "service/rest/v1/assets/" + assetId;
			String assetsJson = HttpUtil.httpRequest(url, "GET", null);
			if (StringUtil.isNotEmpty(assetsJson)) {
				asset = JSON.parseObject(assetsJson, NexusAssetBO.class);
			}
		}
		return asset;
	}
	
	/**
	 * 搜索仓库Component列表
	 * http://localhost:8081/nexus/service/rest/v1/search?group=com.zd&name=test
	 * @param paraMap
	 * @return
	 */
	public List<NexusComponentBO> searchComponentList(NexusSearchVO searchVO) throws Exception {
		List<NexusComponentBO> componentList = new ArrayList<NexusComponentBO>();
		String continuationToken = null;
		String url = null;
		String paraStr = getParameterWithVO(searchVO);
		do {
			if (StringUtil.isEmpty(continuationToken)) {
				url = this.baseUrl + "service/rest/v1/search" + paraStr;
			} else {
				url = this.baseUrl + "service/rest/v1/search" + paraStr + "&continuationToken=" + continuationToken;
			}
			continuationToken = sendGetListUrl(componentList, url, NexusComponentBO.class);
		} while (StringUtil.isNotEmpty(continuationToken));
		return componentList;
	}
	
	/**
	 * 搜索仓库Asset列表
	 * http://localhost:8081/nexus/service/rest/v1/search/assets?group=com.zd&name=test
	 * @param paraMap
	 * @return
	 */
	public List<NexusAssetBO> searchAssetList(NexusSearchVO searchVO) throws Exception {
		if (searchVO == null) {
			searchVO = new NexusSearchVO();
		}
		/*以jar搜索一次，获取数据信息*/
		List<NexusAssetBO> assetList = new ArrayList<NexusAssetBO>();
		String continuationToken = null;
		String url = null;
		searchVO.setExtension("jar");
		String paraStr = getParameterWithVO(searchVO);
		do {
			if (StringUtil.isEmpty(continuationToken)) {
				url = this.baseUrl + "service/rest/v1/search/assets" + paraStr;
			} else {
				url = this.baseUrl + "service/rest/v1/search/assets" + paraStr + "&continuationToken=" + continuationToken;
			}
			continuationToken = sendGetListUrl(assetList, url, NexusAssetBO.class);
		} while (StringUtil.isNotEmpty(continuationToken));
		logger.info("Nexus包件URL：" + url);
		
		/*以war搜索一次，获取数据信息*/
		continuationToken = null;
		searchVO.setExtension("war");
		paraStr = getParameterWithVO(searchVO);
		do {
			if (StringUtil.isEmpty(continuationToken)) {
				url = this.baseUrl + "service/rest/v1/search/assets" + paraStr;
			} else {
				url = this.baseUrl + "service/rest/v1/search/assets" + paraStr + "&continuationToken=" + continuationToken;
			}
			continuationToken = sendGetListUrl(assetList, url, NexusAssetBO.class);
		} while (StringUtil.isNotEmpty(continuationToken));
		logger.info("Nexus包件URL：" + url);
		
		/*以zip搜索一次，获取数据信息*/
		continuationToken = null;
		searchVO.setExtension("zip");
		paraStr = getParameterWithVO(searchVO);
		do {
			if (StringUtil.isEmpty(continuationToken)) {
				url = this.baseUrl + "service/rest/v1/search/assets" + paraStr;
			} else {
				url = this.baseUrl + "service/rest/v1/search/assets" + paraStr + "&continuationToken=" + continuationToken;
			}
			continuationToken = sendGetListUrl(assetList, url, NexusAssetBO.class);
		} while (StringUtil.isNotEmpty(continuationToken));
		logger.info("Nexus包件URL：" + url);
		
		assetList = this.filterInvalidNexusAsset(assetList);//将搜索的数据合并
		return assetList;
	}
	
	/**
	 * 获取的Nexus数据有很多无效数据，去除无效的。
	 * 整理出有效数据
	 * @param componentList
	 * @return 
	 */
	private List<NexusAssetBO> filterInvalidNexusAsset(List<NexusAssetBO> assetList) throws Exception {
		List<NexusAssetBO> resultList = new ArrayList<NexusAssetBO>();
		String[] pathArr = null;
		String[] dateArr = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));//Nexus时区相差8小时
		String path = null;
		String prePath = null;
		int lastSplit = 0;
//		int packageLastSplit = 0;
		String artifactId = null;
		String group = null;
		String packageStr = null;
		String dateStr = null;
		String version = null;
		for (NexusAssetBO asset : assetList) {
			//实例：com/zd2/test2/0.0.1-SNAPSHOT/test2-0.0.1-20181228.035125-9.war
			//cn/pioneerservice/system/0.0.1/system-0.0.1.jar
			//com/wms/wms/1.0.0/wms-1.0.0.zip
			//com/sinosoft/claimbpm/UT-0.0.1-SNAPSHOT/claimbpm-UT-0.0.1-20190817.040031-5-sources.jar
			//io/xiqiao/ccic/regulation/common-services/1.0.0-SNAPSHOT/common-services-1.0.0-20190726.121130-10-javadoc.jar
			path = asset.getPath();
			if (path.endsWith("jar") || path.endsWith("war") || path.endsWith("zip")) {//只处理这些后缀的包件
				try {
					pathArr = path.split("/");
					lastSplit = pathArr.length - 1;
					artifactId = pathArr[lastSplit - 2];
					prePath = path.substring(0, path.lastIndexOf("/"));
					group = prePath.substring(0, prePath.lastIndexOf(artifactId) - 1);
					group = group.replaceAll("/", "\\.");
					packageStr = pathArr[lastSplit].substring(artifactId.length() + 1);//claimbpm-UT-0.0.1-20xxxx-5-sources.jar>>UT-0.0.1-20xxxx-5-sources.jar
					if (asset.getRepository().toLowerCase().indexOf("snapshot") != -1 || path.indexOf("SNAPSHOT") != -1) {//快照
						if (packageStr.indexOf("-sources") != -1 || packageStr.indexOf("-javadoc") != -1) {
							version = packageStr.substring(0, packageStr.lastIndexOf("-"));//UT-0.0.1-20xxxx-5-sources.jar>>UT-0.0.1-20xxxx-5
						} else {
							version = packageStr.substring(0, packageStr.lastIndexOf("."));//0.0.1-20181228.035125-9.war>>0.0.1-20181228.035125-9
						}
						dateArr = version.split("-");//test2-0.0.1-20181228.035125-9
						dateStr = dateArr[dateArr.length - 2];
						asset.setCreateTime(sdf.parse(dateStr));
					} else {//releases
						version = packageStr.substring(0, packageStr.lastIndexOf("."));;
					}
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("Nexus包件处理失败PATH:" + path);
			        logger.error("Nexus包件处理失败:" + e.getMessage(), e);
				}
				asset.setGroup(group);
				asset.setArtifactId(artifactId);
				asset.setVersion(version);
				resultList.add(asset);
			}
		}
		return resultList;
	}
	
	/**
	 * 从Nexus下载包件
	 * @param tblToolInfo
	 * @param repositoryName
	 * @param path
	 */
	public String downloadPackage(HttpServletRequest request,HttpServletResponse response, TblToolInfo bean, String repositoryName, String path) throws Exception {
		String downloadUrl = "http://" + bean.getIp() + ":" + bean.getPort() + "/nexus/repository/" + repositoryName + "/" + path;
		// 浏览器下载后的文件名称showValue,从url中截取到源文件名称以及，以及文件类型，如board.docx;
		String[] pathArr = path.split("/");
		String fileName = pathArr[pathArr.length - 1];//test2-0.0.1-20181228.035125-9.war
		return HttpUtil.downloadPackage(request, response, fileName, downloadUrl);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
//	public static void main(String[] args) throws Exception {
//		NexusUtil nexus = new NexusUtil("http://10.1.12.38:8081/nexus", "admin", "admin123");
//		NexusUtil nexus = new NexusUtil("http://10.1.15.52:40091/nexus", "admin", "Asdfghjkl15a!");
//		NexusSearchVO searchVO = new NexusSearchVO();
//		searchVO.setRepository("Snapshots-Dev4");//maven-snapshots maven-releases
//		searchVO.setQ("");
//		searchVO.setGroup("");
//		searchVO.setGroupId("com.sinosoft");
//		List<NexusAssetBO> list = nexus.searchAssetList(searchVO);
//	
//		NexusAssetBO asset = nexus.getAssets("bWF2ZW4tc25hcHNob3RzOjNmNWNhZTAxNzYwMjMzYjYzMDg5OGJmNmZlMWQ5MTY0");
//		List<NexusComponentBO> list = nexus.getComponentList("maven-releases");
//		NexusSearchVO vo = new NexusSearchVO();
//		vo.setGroup("com.zd");
//		vo.setName("test");
//		List<NexusComponentBO> list = nexus.searchComponentList(vo);
//		
//		System.out.println("ok");
//
//	}
	
}
