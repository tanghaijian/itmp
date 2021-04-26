package cn.pioneeruniverse.common.jenkins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpConnection;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;

import cn.pioneeruniverse.dev.entity.TblToolInfo;

/**
 * Jenkins 基础公共类
 * @author zhoudu
 *
 */
public class JenkinsUtil {
//	private volatile static JenkinsUtil jenkinsUtil;
	private static final Logger logger = LoggerFactory.getLogger(JenkinsUtil.class);
	
	/*Jenkins用户信息内容*/
	private JenkinsServer server;
	private String url;
	private String username;
	private String password;
	
	private static final String ENCODE = "UTF-8";
	
	public static final String JENKINS_JOB_SNAPSHOTS2RELEASE = "snapshots2Release";//已废弃
	public static final String JOB_TYPE_FREE = "<project>";//自由配置
	public static final String JOB_TYPE_MAVEN = "maven2-moduleset";//基于Maven配置
	public static final String JOB_TYPE_MATRIX = "matrix-project";//多配置
	public static final String JOB_TYPE_WORKFLOW = "flow-definition";//流水线配置
	public static final String JOB_TYPE_WORKFLOW_MULTIBRANCH = "WorkflowMultiBranchProject";//多分支流水线
	
	public static final String PARAMETER_DEFINITION_PROPERTY = "hudson.model.ParametersDefinitionProperty"; //Jenkins参数化构建ParametersDefinitionProperty
	public static final String PARAMETER_DEFINITIONS = "parameterDefinitions"; //Jenkins参数化构建parameterDefinitions
	
	public static final String PARAMETER_TYPE_STRING = "hudson.model.StringParameterDefinition"; //Jenkins参数化构建String类型
	public static final String PARAMETER_TYPE_BOOLEAN = "hudson.model.BooleanParameterDefinition"; //Jenkins参数化构建Boolean类型
	public static final String PARAMETER_TYPE_TEXT = "hudson.model.TextParameterDefinition"; //Jenkins参数化构建Text类型
	public static final String PARAMETER_TYPE_FILE = "hudson.model.FileParameterDefinition"; //Jenkins参数化构建File类型
	public static final String PARAMETER_TYPE_PASSWD = "hudson.model.PasswordParameterDefinition"; //Jenkins参数化构建Password类型
	public static final String PARAMETER_TYPE_CHOICE = "hudson.model.ChoiceParameterDefinition"; //Jenkins参数化构建Choice类型
	public static final String PARAMETER_TYPE_EXTENDED_CHOICE = "ExtendedChoiceParameterDefinition"; //Jenkins参数化构建扩展Choice类型
	
	public static final String PARA_NAME = "elementName";//参数化构建-类型名称
	public static final String PARA_TYPE = "type";//参数化构建-类型
	public static final String PARA_TEXT_NAME = "name";//参数化构建-参数名称
	public static final String PARA_DESCRIPTION = "description";//参数化构建-描述
	public static final String PARA_DEFAULT_VALUE = "defaultValue";//参数化构建-值
	public static final String PARA_TRIM = "trim";//参数化构建-去除Trim
	
	public static final String SHELL_NAME = "hudson.tasks.Shell";//shell命令
	public static final String SHELL_COMMAND = "command";//shell command
	
	public JenkinsUtil(String url, String username, String password) throws URISyntaxException {
		server = new JenkinsServer(new URI(url), username, password);
		this.url = url;
		this.username = username;
		this.password = password; 
	}

	/**
	 * 判断是否运行
	 * @return
	 */
	public boolean isRunning() {
		if (server == null) {
			return false;
		}
		return true;
	}

	/**
	 * 获取连接
	 * @return
	 * @throws IOException
	 */
	public JenkinsHttpConnection getJenkinsHttpConnection() throws IOException {
		return server.getQueue().getClient();
	}
	
	/**
	 * 关闭连接
	 */
	public void closeJenkins() {
		if (server != null) {
			server.close();
		}
	}
	
	/**
	 * 记录日志
	 * @param jobName
	 * @param jobPath
	 */
	private void writeLog(String jobName, String jobPath) {
		logger.info("jobName:" + jobName + ",jobPath:" + jobPath);
	}
	
	/**
	 * 转义字符串
	 * @param str
	 * @return
	 */
	public static String encode(String str) {
		try {
			if (StringUtil.isNotEmpty(str) && str.indexOf("%") == -1) {//带%表示已经经过转义
				str = URLEncoder.encode(str, JenkinsUtil.ENCODE);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
		return str;
	}
	
	/**
	 * 通过地址获取基于SAX的XMLElemet
	 * @param pipelineType 1为普通流水线模板，2 用于快照转releases的Jenkins模板 
	 * @return
	 * @throws Exception
	 */
	public Element getSAXElementByXml(int pipelineType) throws Exception {
		String xml = getJenkinsJobPipelineXml(pipelineType);
		Element root = getSAXElementByXml(xml);
		return root;
	}
	
	/**
	 * 通过configXml获取基于SAX的XMLElemet
	 * @param configXml
	 * @return
	 * @throws Exception
	 */
	public Element getSAXElementByXml(String configXml) throws Exception {
		StringReader stringReader = new StringReader(configXml);  
		SAXReader reader = new SAXReader();
		Document document = reader.read(stringReader);
		Element root = document.getRootElement();
		return root;
	}
	
	/**
	 * 判断当前用户能不能登录访问Jenkins
	 * @param idCode
	 * @return
	 * @throws IOException
	 */
	public Boolean isExistUser() throws IOException {
		Boolean exist = true;
		try {
			JenkinsHttpConnection client = server.getQueue().getClient();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			exist = false;
		}
		return exist;
	}
	
	
	/**
	 * 判断用户有没有连接Jenkins
	 * @param username
	 * @param password
	 * @param url 
	 * @return
	 */
	public boolean hasConnected(TblToolInfo bean, String url) {
		boolean connected = true;
		if (this.username == null || this.password == null || this.url == null) {
			connected = false;
		} else if (!this.username.equals(bean.getUserName()) || !this.password.equals(bean.getPassword()) || !this.url.equals(url)) {
			connected = false;
		} else if (!isRunning()) {
			connected = false;
		}
		return connected;
	}

	/**
	 * 创建Jenkins Job
	 * http://localhost:8087/createItem/api/json?name=test_maven
	 * @param jobName
	 * @param configXml
	 * @throws IOException
	 */
	public void creatJenkinsJob(String jobName, String configXml) throws IOException {
		jobName = encode(jobName);
		server.createJob(jobName, configXml);
	}
	
	/**
	 * 更新Jenkins Job，需要完整的配置XML，直接根据XML重新生成一个Job配置。
	 * http://localhost:8087/job/itmp_maven/config.xml
	 * @param jobName
	 * @param configXml
	 * @throws IOException
	 */
	public void updateJenkinsJob(String jobName, String configXml) throws IOException {
		jobName = encode(jobName);
		updateJenkinsJob("", jobName, configXml);
	}
	
	/**
	 * 更新Jenkins Job，包含path，需要完整的配置XML，直接根据XML重新生成一个Job配置。
	 * @param jobPath
	 * @param jobName
	 * @param configXml
	 * @throws IOException
	 */
	public void updateJenkinsJob(String jobPath, String jobName, String configXml) throws IOException {
		jobName = encode(jobName);
		if (StringUtil.isEmpty(jobPath)) {
			server.updateJob(jobName, configXml);
		} else {
			jobPath = getValidJobPath(jobPath);
			jobPath = jobPath + "job/" + jobName + "/config.xml";
			writeLog(jobName, jobPath);
			JenkinsHttpConnection client = getJenkinsHttpConnection();
			client.post_xml(jobPath, configXml);
		}
	}
	
	/**
	 * 获取Job
	 * @param jobName
	 * @return
	 * @throws IOException
	 */
	public JobWithDetails getJenkinsJob(String jobPath, String jobName) throws Exception {
		jobName = encode(jobName);
		JobWithDetails job;
		if (StringUtil.isEmpty(jobPath)) {
			job = server.getJob(jobName);
		} else {
			jobPath = getValidJobPath(jobPath);
			jobPath = jobPath + "job/" + jobName;
			writeLog(jobName, jobPath);
			JenkinsHttpConnection client = getJenkinsHttpConnection();
			job = client.get(jobPath, JobWithDetails.class);
		}
		return job;
	}
	
	/**
	 * 查询是否存在名为jobName的job
	 * http://localhost:8087/job/itmp_maven/api/json
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	public boolean existJenkinsJob(String jobName) throws IOException {
		jobName = encode(jobName);
		JobWithDetails job = server.getJob(jobName);
		if (job != null) {
			return true;
		}
		return false;
	}

	/**
	 * 删除Jenkins Job
	 * @param jobName
	 * @throws Exception
	 */
	public void deleteJenkinsJob(String jobName) throws IOException {
		jobName = encode(jobName);
		server.deleteJob(jobName);
	}

	/**
	 * 获取服务器的config.xml配置文件内容，用来创建Jenkins任务
	 * @param configXmlPath
	 * @return
	 */
	public String getConfigContent(String configXmlPath) throws IOException {
		StringBuilder build = new StringBuilder();
		InputStream in = null;
		InputStreamReader read = null;
		try {
			in = JenkinsUtil.class.getResourceAsStream(configXmlPath);
			read = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null; 
			while ((lineTxt = bufferedReader.readLine()) != null) {
				build.append(lineTxt);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (read != null) {
					read.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}
		return build.toString();
	}
	
	/**
	 * 通过传入的JobName，返回Job的config.xml
	 * http://192.168.1.145:8087/job/parameter_pipeline/config.xml
	 * @param jobName
	 * @return
	 */
	public String getConfigXml(String jobName) throws IOException {
		jobName = encode(jobName);
		return getConfigXml("", jobName);
	}
	
	/**
	 * 通过传入的JobName，返回Job的config.xml
	 * 带相对任务路径如：
	 * ccic_int/trunk/trunk-multibranch-micro-service-build
	 * /job/ccic_int/job/trunk/job/trunk-multibranch-micro-service-build/
	 * @param jobName
	 * @return
	 */
	public String getConfigXml(String jobPath, String jobName) throws IOException {
		jobName = encode(jobName);
		jobPath = getValidJobPath(jobPath);
		String path = jobPath + "job/" + jobName + "/config.xml";
		writeLog(jobName, path);
		JenkinsHttpConnection client = getJenkinsHttpConnection();
		String configXml = client.get(path);
		return configXml;
	}
	
	/**
	 * 封装Jenkins的有效Path
	 * @param jobPath
	 * @return
	 */
	public String getValidJobPath(String jobPath) {
		if (StringUtil.isNotEmpty(jobPath) && jobPath.length() > 1) {
			if (jobPath.indexOf("/job/") != -1) {//如果包含job，刚path前后加上/，且把最后的job去除，便于连接url
				jobPath = addSlash(jobPath, "/", "/", true);
				if (jobPath.endsWith("/job/")) {
					jobPath = jobPath.substring(0, jobPath.length() - 4);
				}
			} else {//如果没有job时
				if (jobPath.startsWith("/")) {
					jobPath = jobPath.substring(1, jobPath.length());
				}
				if (jobPath.endsWith("/")) {
					jobPath = jobPath.substring(0, jobPath.length() - 1);
				}
				jobPath =  jobPath.replaceAll("/", "/job/");
				jobPath = "/job/" + jobPath + "/";
				
			}
		} else {
			jobPath = "/";
		}
		return jobPath;
	}
	
	/**
	 * 将str里面的splitSignRegex转换成slash，并且前后加上slash。
	 * 如：aaa/bbb > /aaa/bbb/
	 * "aaa":"bbb" > \"aaa\":\"bbb\"
	 * @param  str
	 * @param splitSign
	 * @param slash
	 * @param trimSlash 是:前后加入替换的slash字符。否：前后去除替换的slash字符
	 * @return
	 */
	public static String addSlash(String str, String splitSignRegex, String slash, boolean trimSlash) {
		if (StringUtil.isNotEmpty(str)) {
			str =  str.replaceAll(splitSignRegex, slash);
//			if (trimSlash && ! str.startsWith(slash)) {
//				str = slash +  str;
//			}
//			if (trimSlash && ! str.endsWith(slash)) {
//				str =  str + slash;
//			}
			
			if (trimSlash) {//前后加入替换的slash字符
				if (! str.startsWith(slash)) {
					str = slash +  str;
				}
				if (! str.endsWith(slash)) {
					str =  str + slash;
				}
			} else {
				if (str.startsWith(slash)) {
					str = str.substring(1, str.length());
				}
				if (str.endsWith(slash)) {
					str = str.substring(0, str.length() - 1);
				}
			}
		}
		return  str;
	}

	/**
	 * 终止Jenkins Job构建
	 * http://localhost:8087/job/itmp_maven/disable/api/json
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	public void disableJenkinsJob(String jobName) throws IOException {
		jobName = encode(jobName);
		server.disableJob(jobName);
	}
	
	/**
	 * 启用Jenkins Job构建
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	public void enableJenkinsJob(String jobName) throws IOException {
		jobName = encode(jobName);
		server.enableJob(jobName);
	}
	
	/**
	 *构建任务
	 *http://localhost:8087/job/itmp_maven/build/api/json
	 * @param job
	 * @throws IOException
	 */
	public void buildJenkinsJob(String jobPath, String jobName) throws IOException {
		jobName = encode(jobName);
		jobPath = getValidJobPath(jobPath);
		String path = jobPath + "job/" + jobName + "/build?delay=0sec";
		writeLog(jobName, path);
		JenkinsHttpConnection client = getJenkinsHttpConnection();
		client.post(path);
	}
	public void buildJenkinsJob(String jobPath, String jobName, Map<String, String> paraMap) throws IOException {
		jobName = encode(jobName);
		String paraStr = "";
		if (paraMap != null && paraMap.size() > 0) {//将分隔符##转换成&，##来自于手动构建中的多个参数
			for (Map.Entry<String, String> en : paraMap.entrySet()) {
				String key = en.getKey();
				String value = en.getValue() == null ? "" : en.getValue();
				if (value.indexOf("##") != -1) {
					String[] arr = value.split("##");
					for (String val : arr) {
						paraStr += "&" + key + "=" + val;
					}
				} else {
					paraStr += "&" + key + "=" + value;
				}
			}
//			paraStr = StringUtils.join(Collections2.transform(paraMap.entrySet(), new MapEntryToQueryStringPair()), "&");
		}
		jobPath = getValidJobPath(jobPath);
		String path = jobPath + "job/" + jobName + "/buildWithParameters?delay=0sec" + paraStr;
		writeLog(jobName, path);
		JenkinsHttpConnection client = getJenkinsHttpConnection();
		client.post(path);
	}
	
	/**
	 * 获取下一次构建生成的Jenkins任务编号
	 * @param jobPath
	 * @param jobName
	 * @return
	 * @throws IOException
	 */
	public int getNextBuildNumber(String jobPath, String jobName) throws Exception {
		JobWithDetails job = getJenkinsJob(jobPath, jobName);
		int nextBuildNumber = 1;
		if (job != null) {
			nextBuildNumber = job.getNextBuildNumber();
		}
		return nextBuildNumber;
	}
	
	/**
	 * 获取最后的构建编号 
	 * @param jobPath
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	public int getLastBuildNumber(String jobPath, String jobName) throws Exception {
		JobWithDetails job = getJenkinsJob(jobPath, jobName);
		int lastBuildNumber = 1;
		if (job != null) {
			Build build = job.getLastBuild();
			if (build != null) {
				lastBuildNumber = build.getNumber();
			}
		}
		return lastBuildNumber;
	}
	
	/**
	 * 通过jenkins任务编号获取日志
	 * @param jobPath
	 * @param jobName
	 * @param jobNumber
	 * @return
	 * @throws IOException
	 */
	public String getJenkinsLogByNumber(String jobPath, String jobName, int jobNumber) throws Exception {
		JobWithDetails job = getJenkinsJob(jobPath, jobName);
		String log = "";
		if (job != null) {
			Build build = job.getBuildByNumber(jobNumber);
			if (build != null) {
				BuildWithDetails detail = build.details();
				if (detail != null) {
					log = detail.getConsoleOutputHtml();
				}
			}
		}
		return log;
	}
	
	/**
	 * 获取最后一次构建Jenkins日志（全量）
	 * @param jobPath
	 * @param jobName
	 * @return
	 * @throws IOException
	 */
	public String getJenkinsLog(String jobPath, String jobName) throws Exception {
		JobWithDetails job = getJenkinsJob(jobPath, jobName);
		String log = "";
		if (job != null) {
			Build build = job.getLastBuild();
			if (build != null) {
				BuildWithDetails detail = build.details();
				if (detail != null) {
					log = detail.getConsoleOutputHtml();
				}
			}
		}
		return log;
	}
	/**
	 * 获取Jenkins日志（增量）
	 * @param jobPath
	 * @param jobName
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> getJenkinsBuildingLog(String jobPath, String jobName, String start, int jobNumber) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("start", "0");
		resultMap.put("log", "");
		jobName = encode(jobName);
		jobPath = getValidJobPath(jobPath);
		String path = jobPath + "job/" + jobName;
		JenkinsHttpConnection client = getJenkinsHttpConnection();
		
		if (jobNumber <= 0) {
			jobNumber = getLastBuildNumber(jobPath, jobName);//最后一次构建(可能正在构建)
		}
		path = path + "/" + jobNumber + "/logText/progressiveHtml?start=" + start;
		writeLog(jobName, path);
		HttpResponse response = null;
		try {
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("start", start));
			response = client.post_form_with_result(path, params, false);
		} catch (Exception e) {
			String errorMsg = "jobPath:" + jobPath + " jobName:" + jobName + " jobNumber:" + jobNumber + " ";
			logger.error(errorMsg + e.getMessage(),e);
		}
		if (response != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			String resultStr = "";
			if (statusCode == 200) {//成功时
				Header sizeHeader = response.getFirstHeader("X-Text-Size");
				if (sizeHeader != null) {
					resultMap.put("start", sizeHeader.getValue());
				}
				resultStr = EntityUtils.toString(response.getEntity());
				resultMap.put("resultFlag", "1");//成功
			}
			resultMap.put("log", resultStr);
		}
		return resultMap;
	}
	
	/**
	 * 部署暂停后手动操作继续执行调用
	 * job/zhoudu_itmp_1_45_packagedeploy/32/input/Interrupt1/proceedEmpty
	 * @param subUrl
	 * @return
	 * @throws IOException
	 */
	public void continuePipeline(String subUrl) throws IOException {
		if (!subUrl.startsWith("/")) {
			subUrl = "/" + subUrl;
		}
		JenkinsHttpConnection client = getJenkinsHttpConnection();
		client.post(subUrl);
	}
	
	/**
	 * 日志中出现调用其它job的链接
	 * /job/env-deploy/job/ccic-int-patch/4739/
	 * @param subUrl
	 * @return
	 * @throws IOException
	 */
	public String goOtherPageLog(String subUrl) throws Exception {
		String log = "";
		if (StringUtil.isNotEmpty(subUrl)) {
			subUrl = addSlash(subUrl, "/", "/", true);
			Pattern pattern = Pattern.compile("^\\d{1,}$"); //提取url中的数字：构建编号
			String[] urlArr = subUrl.split("/");
			Integer jobNumber = null;
			if (urlArr.length > 1) {
				for (int i = urlArr.length - 1; i >= 0; i--) {
					Matcher matcher = pattern.matcher(urlArr[i]);
					if (matcher.matches()) {
						jobNumber = Integer.parseInt(urlArr[i]);
						break;
					}
				}
			}
			if (jobNumber != null) {//如果有构建编号时
				String path = subUrl.substring(0, subUrl.indexOf("/" + jobNumber + "/"));
				String jobPath = path.substring(0, path.lastIndexOf("/"));
				String jobName = path.substring(path.lastIndexOf("/") + 1);
				writeLog(jobName, jobPath);
				log = getJenkinsLogByNumber(jobPath, jobName, jobNumber);
			} else {//没有编号，直接获取最后 一次构建编号 
				subUrl = subUrl.substring(0, subUrl.length() - 1);
				String jobPath = subUrl.substring(0, subUrl.lastIndexOf("/"));
				String jobName = subUrl.substring(subUrl.lastIndexOf("/") + 1);
				writeLog(jobName, jobPath);
				jobNumber = getLastBuildNumber(jobPath, jobName);
				log = getJenkinsLogByNumber(jobPath, jobName, jobNumber);
			}
		}
		return log;
	}
	
	/**
	 * 停止正在Build的Jenkins
	 * http://192.168.1.145:8087/job/zhoudu_wms_DEV_44_deploy/22/stop
	 * @param jobName
	 * @param jobNumber 
	 */
	public void stopJenkinsBuilding(String jobPath, String jobName, Integer jobNumber) throws Exception {
		jobName = encode(jobName);
		jobPath = getValidJobPath(jobPath);
		Boolean isBuilding = null;
		if (jobNumber != null && jobNumber != 0) {//有编号时，
			for (int i = 0; i < 3; i++) {
				isBuilding = isJenkinsBuilding(jobPath, jobName, jobNumber);
				if (isBuilding) {
					String path = jobPath + "job/" + jobName + "/" + jobNumber + "/stop";
					JenkinsHttpConnection client = getJenkinsHttpConnection();
					client.post(path);
					Thread.sleep(5000);
				} else {
					break;
				}
			}
		} else {
			for (int i = 0; i < 3; i++) {//尝试3次
				JobWithDetails job = getJenkinsJob(jobPath, jobName);
				if (job != null) {
					Build build = job.getLastBuild();
					if (build != null) {
						BuildWithDetails detail = build.details();
						if (detail != null) {
							isBuilding = detail.isBuilding();
							if (isBuilding) {//正在构建中
								int lastNumber = build.getNumber();//最后一次构建(可能正在构建)
								String path = jobPath + "job/" + jobName + "/" + lastNumber + "/stop";
								JenkinsHttpConnection client = getJenkinsHttpConnection();
								client.post(path);
								Thread.sleep(5000);
							} else {
								break;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 判断任务是不是正在构建
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	public boolean isJenkinsBuilding(String jobPath, String jobName, int jobNumber) throws Exception {
		boolean isBuilding = true;//默认任何情况都是正在构建中
		JobWithDetails job = getJenkinsJob(jobPath, jobName);
		if (job != null) {
			Build build = job.getBuildByNumber(jobNumber);
			if (build != null) {
				BuildWithDetails detail = build.details();
				if (detail != null) {
					isBuilding = detail.isBuilding();
				}
			} else {//Jenkins已经不存在此任务号时
				isBuilding = false;
			}
		}
		return isBuilding;
	}
	
	/**
	 * 验证Jenkins的Cron表达式
	 * http://192.168.1.145:8087/descriptorByName/hudson.triggers.TimerTrigger/checkSpec?value=H/15 * * * * 
	 * <div class=warning><img src='/static/a1a52d5a/images/none.gif' height=16 width=1>无计划因此不会运行</div>
	 * <div class=error><img src='/static/a1a52d5a/images/none.gif' height=16 width=1>Invalid input: &quot;H/5 * * *&quot;: line 1:10: expecting space, found &#039;null&#039;</div>
	 * <div class=ok><img src='/static/a1a52d5a/images/none.gif' height=16 width=1>上次运行的时间 2019年1月28日 星期一 下午04时20分23秒 CST; 下次运行的时间 2019年1月28日 星期一 下午04时25分23秒 CST.</div>
	 * @param cron
	 * @return
	 * @throws Exception
	 */
	public String validateJenkinsCron(String cron) throws Exception {
		String path = "/descriptorByName/hudson.triggers.TimerTrigger/checkSpec?value=" + cron;
		JenkinsHttpConnection client = getJenkinsHttpConnection();
		return client.get(path);
	}
	
	/**
	 * 获取构建开始时间
	 * @param jobPath
	 * @param jobName
	 * @param jobNumber
	 * @return
	 * @throws Exception
	 */
	public Timestamp getJenkinsJobStartDate(String jobPath, String jobName, int jobNumber) throws Exception {
		Timestamp time = null;
		String timestamp = getJenkinsJobData(jobPath, jobName, jobNumber, "timestamp");
		if (StringUtil.isNotEmpty(timestamp)) {
			time = new Timestamp(Long.parseLong(timestamp));
//			Date date = new Date(Long.parseLong(timestamp));
//			startDate = DateUtil.formatDate(date, DateUtil.fullFormat);
		} else {
			time = new Timestamp(System.currentTimeMillis());
		}
		return time;
	}
	/**
	 * 获取构建结束时间
	 * @param jobPath
	 * @param jobName
	 * @param jobNumber
	 * @return
	 * @throws Exception
	 */
	public Timestamp getJenkinsJobEndDate(String jobPath, String jobName, int jobNumber) throws Exception {
		Timestamp time = null;
		String timestamp = getJenkinsJobData(jobPath, jobName, jobNumber, "timestamp");
		String duration = getJenkinsJobData(jobPath, jobName, jobNumber, "duration");
		if (StringUtil.isNotEmpty(timestamp) && StringUtil.isNotEmpty(duration)) {
			long timestampValue = Long.parseLong(timestamp);
			long durationValue = Long.parseLong(duration);
			timestampValue = timestampValue + durationValue;
			time = new Timestamp(timestampValue);
//			Date date = new Date(timestampValue);
//			endDate = DateUtil.formatDate(date, DateUtil.fullFormat);
		} else {
			time = new Timestamp(System.currentTimeMillis());
		}
		return time;
	}
	
	public String getJenkinsJobData(String jobPath, String jobName, int jobNumber, String para) throws Exception {
		jobName = encode(jobName);
		String data = "";
		boolean exist = true;
		if (jobNumber <= 0) {//默认取最后一次编号 
			jobNumber = getLastBuildNumber(jobPath, jobName);//最后一次构建(可能正在构建)
		} else {
			JobWithDetails job = getJenkinsJob(jobPath, jobName);
			if (job != null) {
				Build build = job.getBuildByNumber(jobNumber);
				if (build == null) {
					exist = false;
				}
			} else {
				exist = false;
			}
		}
		if (exist) {//此任务存在才做处理
			jobPath = getValidJobPath(jobPath);
			jobPath = jobPath + "job/" + jobName + "/" + jobNumber;
			writeLog(jobName, jobPath);
			JenkinsHttpConnection client = getJenkinsHttpConnection();
			String buildJson = client.get(jobPath);
			if (StringUtil.isNotEmpty(buildJson)) {
				JSONObject obj = JSON.parseObject(buildJson);
				data = Objects.toString(obj.get(para), "");
			}
		}
		return data;
	}
	
	/**
	 * 获取StageView列表内容
	 * http://192.168.1.145:8087/job/zhoudu_itmp_git_2_175_deploy/67/wfapi/describe
	 * @param jobPath
	 * @param jobName
	 * @param jobNumber
	 * @return
	 */
	public String getStageViewDescribeJson(String jobPath, String jobName, int jobNumber) throws Exception {
		jobName = encode(jobName);
		if (jobNumber <= 0) {
			jobNumber = getLastBuildNumber(jobPath, jobName);//最后一次构建(可能正在构建)
		}
		jobPath = getValidJobPath(jobPath);
		jobPath = jobPath + "job/" + jobName + "/" + jobNumber + "/wfapi/describe";
		JenkinsHttpConnection client = getJenkinsHttpConnection();
		String dataJson = "";
		try {
			dataJson = client.get(jobPath);
		} catch (Exception e) {
			//logger.error("getStageViewDescribeJson:" + jobPath + "ERROR:" + e.getMessage(), e);
		}
		return dataJson;
	}
	
	/**
	 * 获取Jenkins的中断内容
	 * http://192.168.1.145:8087/job/zhoudu_itmp_1_45_deploy/523/wfapi/pendingInputActions
	 * @param jobPath
	 * @param jobName
	 * @param jobNumber
	 * @return
	 */
	public String getNextPendingInputAction(String jobPath, String jobName, int jobNumber) throws Exception {
		jobName = encode(jobName);
		if (jobNumber <= 0) {
			jobNumber = getLastBuildNumber(jobPath, jobName);//最后一次构建(可能正在构建)
		}
		jobPath = getValidJobPath(jobPath);
		jobPath = jobPath + "job/" + jobName + "/" + jobNumber + "/wfapi/nextPendingInputAction";
		JenkinsHttpConnection client = getJenkinsHttpConnection();
		String dataJson = "";
		try {
			dataJson = client.get(jobPath);
		} catch (Exception e) {
			logger.error("getNextPendingInputAction:" + jobPath + "ERROR:" + e.getMessage(), e);
		}
		return dataJson;
	}
	
	/**
	 * 执行中断继续或者停止
	 * /job/wms_git_20190806_2_176_deploy/10/input/Interrupt2/proceedEmpty
	 * @param jobPath
	 * @param jobName
	 * @param jobNumber
	 * @param interruptId
	 * @param flag
	 * @throws Exception
	 */
	public void getStageViewNextPending(String jobPath, String jobName, int jobNumber, String interruptId, Integer flag) throws Exception {
		jobName = encode(jobName);
		if (jobNumber <= 0) {
			jobNumber = getLastBuildNumber(jobPath, jobName);//最后一次构建(可能正在构建)
		}
		jobPath = getValidJobPath(jobPath);
		String methodStr = "abort";
		if (flag == 1) {//1为继续
			methodStr = "proceedEmpty";
		}
		jobPath = jobPath + "job/" + jobName + "/" + jobNumber + "/input/" + interruptId + "/" + methodStr;
		JenkinsHttpConnection client = getJenkinsHttpConnection();
		client.post(jobPath);
	}
	
	/**
	 * 获取StageView列表子页execution内容
	 * http://192.168.1.145:8087/job/zhoudu_itmp_git_2_175_deploy/67/execution/node/10/wfapi/describe
	 * @param jobPath
	 * @param jobName
	 * @param jobNumber
	 * @return
	 */
	public String getStageViewDescribeExecutionJson(String jobPath, String jobName, int jobNumber, int describeId) throws Exception {
		jobName = encode(jobName);
		if (jobNumber <= 0) {
			jobNumber = getLastBuildNumber(jobPath, jobName);//最后一次构建(可能正在构建)
		}
		jobPath = getValidJobPath(jobPath);
		jobPath = jobPath + "job/" + jobName + "/" + jobNumber + "/execution/node/" + describeId + "/wfapi/describe";
		JenkinsHttpConnection client = getJenkinsHttpConnection();
		String dataJson = "";
		try {
			dataJson = client.get(jobPath);
		} catch (Exception e) {
			logger.error("getStageViewDescribeExecutionJson:" + jobPath + "ERROR:" + e.getMessage(), e);
		}
		return dataJson;
	}
	
	/**
	 * 获取StageView列表子页execution的详细日志内容
	 * http://192.168.1.145:8087/job/zhoudu_itmp_git_2_175_deploy/67/execution/node/11/wfapi/log
	 * @param jobPath
	 * @param jobName
	 * @param jobNumber
	 * @param describeId
	 * @return
	 * @throws Exception
	 */
	public String getStageViewExecutionLogJson(String jobPath, String jobName, int jobNumber, int executionId) throws Exception {
		jobName = encode(jobName);
		if (jobNumber <= 0) {
			jobNumber = getLastBuildNumber(jobPath, jobName);//最后一次构建(可能正在构建)
		}
		jobPath = getValidJobPath(jobPath);
		jobPath = jobPath + "job/" + jobName + "/" + jobNumber + "/execution/node/" + executionId + "/wfapi/log";
		JenkinsHttpConnection client = getJenkinsHttpConnection();
		String dataJson = "";
		try {
			dataJson = client.get(jobPath);
		} catch (Exception e) {
			logger.error("getStageViewExecutionLogJson:" + jobPath + "ERROR:" + e.getMessage(), e);
		}
		return dataJson;
	}

	/**
	 * 获取流水线模板
	 * @param pipelineType 1为普通流水线模板，2 用于快照转releases的Jenkins模板 
	 * @return
	 */
	private String getJenkinsJobPipelineXml(int pipelineType) {
		String jenkinsJobPipelineXml = "";
		if (pipelineType == 1) {//标准流水线任务模板
			jenkinsJobPipelineXml = "<flow-definition plugin=\"workflow-job@2.25\">\r\n" + 
					"	<actions />\r\n" + 
					"	<description>自动构建</description>\r\n" + 
					"	<keepDependencies>false</keepDependencies>\r\n" + 
					"	<properties>\r\n" + 
					"		<org.jenkinsci.plugins.workflow.job.properties.DisableConcurrentBuildsJobProperty/>\r\n" + 
					"		<jenkins.model.BuildDiscarderProperty>\r\n" + 
					"			<strategy class=\"hudson.tasks.LogRotator\">\r\n" + 
					"				<daysToKeep>-1</daysToKeep>\r\n" + 
					"				<numToKeep>-1</numToKeep>\r\n" + 
					"				<artifactDaysToKeep>-1</artifactDaysToKeep>\r\n" + 
					"				<artifactNumToKeep>-1</artifactNumToKeep>\r\n" + 
					"			</strategy>\r\n" + 
					"		</jenkins.model.BuildDiscarderProperty>\r\n" + 
					"	</properties>\r\n" + 
					"	<definition\r\n" + 
					"		class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\"\r\n" + 
					"		plugin=\"workflow-cps@2.54\">\r\n" + 
					"		<script>\r\n" + 
					"		</script>\r\n" + 
					"		<sandbox>true</sandbox>\r\n" + 
					"	</definition>\r\n" + 
					"	<triggers />\r\n" + 
					"	<quietPeriod>0</quietPeriod>\r\n" + 
					"	<disabled>false</disabled>\r\n" + 
					"</flow-definition>";
		} else if (pipelineType == 2) {//用于快照转releases的Jenkins模板 
			jenkinsJobPipelineXml = "<project>\r\n" + 
					"	<description>包件管理打标签时执行构建任务：实现将SNAPSHOT快照转换成Release生产版本上传到Nexus\r\n" + 
					"	</description>\r\n" + 
					"	<keepDependencies>false</keepDependencies>\r\n" + 
					"	<properties>\r\n" + 
					"		<jenkins.model.BuildDiscarderProperty>\r\n" + 
					"			<strategy class=\"hudson.tasks.LogRotator\">\r\n" + 
					"				<daysToKeep>-1</daysToKeep>\r\n" + 
					"				<numToKeep>2</numToKeep>\r\n" + 
					"				<artifactDaysToKeep>-1</artifactDaysToKeep>\r\n" + 
					"				<artifactNumToKeep>-1</artifactNumToKeep>\r\n" + 
					"			</strategy>\r\n" + 
					"		</jenkins.model.BuildDiscarderProperty>\r\n" + 
					"	</properties>\r\n" + 
					"	<scm class=\"hudson.scm.NullSCM\" />\r\n" + 
					"	<canRoam>true</canRoam>\r\n" + 
					"	<disabled>false</disabled>\r\n" + 
					"	<blockBuildWhenDownstreamBuilding>false\r\n" + 
					"	</blockBuildWhenDownstreamBuilding>\r\n" + 
					"	<blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\r\n" + 
					"	<triggers />\r\n" + 
					"	<concurrentBuild>false</concurrentBuild>\r\n" + 
					"	<builders>\r\n" + 
					"		<hudson.tasks.Shell>\r\n" + 
					"			<command></command>\r\n" + 
					"		</hudson.tasks.Shell>\r\n" + 
					"	</builders>\r\n" + 
					"	<publishers />\r\n" + 
					"	<buildWrappers />\r\n" + 
					"</project>";
		}
		return jenkinsJobPipelineXml;
	}

}
