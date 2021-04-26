package cn.pioneeruniverse.dev.service.build.impl;

import java.util.Map;

import com.github.pagehelper.StringUtil;

import cn.pioneeruniverse.dev.entity.TblArtifactInfo;
import cn.pioneeruniverse.dev.entity.TblSystemModule;

/**
 * Jenkins构建部署公共Base类类
 * @author zhoudu
 *
 */
public class JenkinsBase {
	
	protected String BUILD_TYPE = "buildType";//构建类型
	protected String BUILD_TYPE_AUTO = "auto";//自动构建
	protected String BUILD_TYPE_AUTO_DEPLOY = "autoDeploy";//自动部署
	protected String BUILD_TYPE_PACKAGE_AUTO_DEPLOY = "packageAutoDeploy";//包件自动部署
	protected String BUILD_TYPE_MANUAL = "manual";//手动构建 
	protected String BUILD_TYPE_MANUAL_DEPLOY = "manualDeploy";//手动部署
	protected String BUILD_DEPLOY_SUCCESS = "BUILD_DEPLOY_SUCCESS";//部署成功标示，用于日志判断
	protected String BUILD_DEPLOY_FAILURE = "BUILD_DEPLOY_FAILURE";
	
	public final static String JENKINS_ERROR = "JenkinsError:";//错误关键字
	
	protected String callBackJenkinsLog = "callBackJenkinsLog";//自动构建回调
	protected String callBackManualJenkins = "callBackManualJenkins";//手动构建回调
	protected String callBackManualDepolyJenkins = "callBackManualDepolyJenkins";//手动部署回调
	protected String callBackAutoDepolyJenkins = "callBackAutoDepolyJenkins";//自动部署回调
	protected String callBackPackageDepolyJenkins = "callBackPackageDepolyJenkins";//包件自动部署回调
	
	protected String scriptInsertMsg = "自动同步信息";
	protected String serverUploadTemp = "ITMGRTemp";//部署缓存目录
	
	/*上传ftp目录结构*/
	protected String uploadTemp = "uploadTemp";//上传到PRO服务器暂存：config document sql
	protected String uploadConfigurationPath = "uploadTemp/configuration";
	protected String uploadDocumentPath = "uploadTemp/document";
	protected String uploadPackagePath = "uploadTemp/package";
	protected String uploadSqlPath = "uploadTemp/sql";
	protected String shellNoPrint = " > /dev/null 2>&1 & ";//shell输出空内容
//	protected String shellNoPrint = " ";
	
	protected String beforeStopGroovy = "BeforeStop";//部署sql固定命名
	protected String afterStopGroovy = "AfterStop";//部署sql固定命名
	protected String afterStartUpGroovy = "AfterStartUp";//部署sql固定命名
	
	
	protected String SUFFIX_TITLE_1 = "====================";
	protected String SUFFIX_TITLE_2 = "---------------";
	
	protected String CHECKOUT_TITLE = "源代码checkout";
	protected String BUILD_TITLE = "执行构建";
	protected String SONAR_TITLE = "执行Sonar扫描";
	protected String DEPLOY_TITLE = "执行部署";
	protected String DEPLOY_AUTO_TITLE = "执行自动化部署";
	protected String DOWNLOAD_PACKAGE_TITLE = "执行下载包件";
	protected String INIT_GROOVY_TITLE = "解压包件并且SQL封装";
	
	protected String DEPLOY_SQL_TITLE = "执行服务器部署脚本";
	protected String DEPLOY_SQL_SUB_TITLE_1 = "停止服务前执行SQL";
	protected String DEPLOY_SQL_SUB_TITLE_2 = "停止服务后执行SQL";
	protected String DEPLOY_SQL_SUB_TITLE_3 = "启动服务后执行SQL";
	
	protected String DEPLOY_SQL_IP_TITLE = "当前部署服务器";
	
	
	/**
	 * AOP基于pipeline的stage开始,脚本中，每个stage会添加一个开始及结束调用，用来修改执行结果状态
	 * @param tempSB
	 * @param blankCount
	 * @return
	 */
	protected int assembleAOPStart(Map<String, Object> paramMap, TblSystemModule tblSystemModule, StringBuilder tempSB, int blankCount) {
		
		if (tblSystemModule == null) {
			tempSB.append(getPreBlank(blankCount + 1)).append("stageStart(moduleMap,null)\n");
		} else {
			tempSB.append(getPreBlank(blankCount + 1)).append("stageStart(moduleMap,'").append(tblSystemModule.getModuleCode()).append("')\n");
		}
		return blankCount;
	}
	
	/**
	 * AOP基于pipeline的stage结束,脚本中，每个stage会添加一个开始及结束调用，用来修改执行结果状态
	 * @param tblSystemModule 
	 * @param tempSB
	 * @param blankCount
	 * @return
	 */
	protected int assembleAOPEnd(Map<String, Object> paramMap, TblSystemModule tblSystemModule, boolean stageLast, StringBuilder tempSB, int blankCount) {
		if (tblSystemModule == null) {
			tempSB.append(getPreBlank(blankCount + 1)).append("stageEnd(moduleMap,null,").append(stageLast).append(")\n");
		} else {
			tempSB.append(getPreBlank(blankCount + 1)).append("stageEnd(moduleMap,'").append(tblSystemModule.getModuleCode()).append("',").append(stageLast).append(")\n");
		}
		return blankCount;
	}
	
	protected String getPreBlank(int count) {
		String blank = "";
		for (int i = 0; i < count; i++) {
			blank += "  ";
		}
		return blank;
	}
	
	/**
	 * 将字符串通过\\封装，避免在Expect中被解析
	 * @param fileName
	 * @return
	 */
	protected String formatStringWithExcept(String fileName) {
		if (StringUtil.isNotEmpty(fileName)) {
			fileName = fileName.replaceAll("([\\$>\\[\\]])", "\\\\\\\\$1");
			fileName = fileName.replaceAll(" ", "\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ ");//特殊情况...
		}
		return fileName;
	}
	
	/**
	 * 将字符串通过//封装，避免在pipeline中被解析
	 * @param fileName
	 * @return
	 */
	protected String formatStringWithPipeline(String fileName) {
		if (StringUtil.isNotEmpty(fileName)) {
			fileName = fileName.replaceAll("([\\(\\)&<>\\$ ])", "\\\\\\\\$1");
		}
		return fileName;
	}
	
	/**
	 * 展示脚本中执行的标题，用于部署时，区分当前部署步骤
	 * @param title
	 * @param subTitle
	 * @param type
	 * @param suffixStr
	 * @param sb
	 * @param blankCount
	 * @return
	 */
	protected StringBuilder showScriptTitle(String title, String subTitle, int type, String suffixStr, StringBuilder sb, int blankCount) {
		if (type == 0) {//普通构建部署默认标题
			sb.append(getPreBlank(blankCount)).append("echo '").append(suffixStr).append("[[").append(title).append("]]").append(suffixStr).append("'\n");
		} else if (type == 1 || type == 2) {//带开始结束的标题
			String toggleStr = "";
			if (type == 1) {
				toggleStr = "START";
			} else if (type == 2) {
				toggleStr = "END";
			}
			String subTitleStr = ":" + subTitle;
			sb.append(getPreBlank(blankCount)).append("echo '").append(suffixStr).append("[[").append(title).append(subTitleStr)
			.append("  ").append(toggleStr).append("]]").append(suffixStr).append("'\n");
		}
		return sb;
	}
	protected StringBuilder showScriptTitle(String title, int type, String suffixStr, StringBuilder sb, int blankCount) {
		return showScriptTitle(title, "", type, suffixStr, sb, blankCount);
	}
	
	/**
	 * 将制品版本中，快照日期去除，用来实现制品部署。
	 * 如：2.0.1-20191217.060453-2
	 * @param version
	 * @return
	 */
	protected String getDeployArtifactVersion(TblArtifactInfo tblArtifactInfo) {
		String version = null;
		if (tblArtifactInfo != null) {
			version = tblArtifactInfo.getVersion();
			String path = tblArtifactInfo.getNexusPath();
			if (StringUtil.isNotEmpty(version) && path != null && path.toLowerCase().indexOf("snapshot") != -1) {//快照
				version = version.substring(0, version.lastIndexOf("."));
				version = version.substring(0, version.lastIndexOf("-"));
			}
		}
		return version;
	}
	
}
