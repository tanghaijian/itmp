package cn.pioneeruniverse.dev.service.build.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.StringUtil;

import cn.pioneeruniverse.common.jenkins.JenkinsUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.entity.FtpS3Vo;
import cn.pioneeruniverse.dev.entity.TblArtifactInfo;
import cn.pioneeruniverse.dev.entity.TblServerInfo;
import cn.pioneeruniverse.dev.entity.TblSystemDbConfig;
import cn.pioneeruniverse.dev.entity.TblSystemDeploy;
import cn.pioneeruniverse.dev.entity.TblSystemDeployScript;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.entity.TblSystemModule;
import cn.pioneeruniverse.dev.entity.TblToolInfo;

/**
 * 系统构建部署调用JenkinsAPI接口类:封装Expect实现部署逻辑脚本部分
 * @author zhoudu
 *
 */
@Service("jenkinsDeployExcept")
public class JenkinsDeployExcept extends JenkinsBase {
	
	@Value("${jenkins.upload.ip}")
	private String jenkinsUploadIp;//Jenkins回调Url，用于脚本回调
	@Value("${jenkins.upload.username}")
	private String jenkinsUploadUsername;//Jenkins推包FTP的用户名
	@Value("${jenkins.upload.password}")
	private String jenkinsUploadPassword;//jenkins推包FTP的密码

	@Autowired
	RedisUtils redisUtils;
	
	/*下面是Linux返回内容可能出现的错误信息，用于判断结果后续怎么处理*/
	private String permissionDenied = "Permission denied";
	private String permissionDeniedMsg = "处理失败，权限拒绝。";
	private String accessFailed = "Access failed";
	private String accessFailedMsg = "处理失败，Access failed。";
	private String noFileOrDirError = "没有那个文件";
	private String noFileOrDirErrorMsg = "处理失败，找不到文件或目录。";
	private String commandNotFound = "command not found";
	private String commandNotFoundMsg = "处理失败，未找到命令。";
	private String commandNotFoundCN = "未找到命令";
	private String commandNotFoundCNMsg = "处理失败，未找到命令。";
	private String writeError = "write error";
	private String writeErrorMsg = "处理失败，write error。";
	
	
	
	//单独的模块中，当有多个服务器时，解决停止前、停止后、启动后执行的顺序问题。
	private Map<String, Map<String, List<StringBuilder>>> serverOrderMap = new HashMap<String, Map<String, List<StringBuilder>>>();
	private String currentDeployStep;//用来记录当前处于部署哪个阶段:停止前、停止后、启动后
	
	
	/**
	 * 封装一个模块中Expect脚本 
	 * @param paramMap 
	 * @param tblSystemInfo
	 * @param jenkinsTool
	 * @param tblSystemModule
	 * @param tblArtifactInfo 
	 * @param tblServerInfoList
	 * @param tblSystemDeploy
	 * @param mapList
	 * @param currentCode
	 * @param scriptSB
	 * @param blankCount
	 */
	public void assembleExpect(Map<String, Object> paramMap, TblSystemInfo tblSystemInfo, TblToolInfo jenkinsTool, TblSystemModule tblSystemModule, 
			TblArtifactInfo tblArtifactInfo, List<TblServerInfo> tblServerInfoList, TblSystemDeploy tblSystemDeploy, 
			List<TblSystemDeployScript> tblSystemDeployScriptList, String currentCode, StringBuilder scriptSB, int blankCount) {
		String serverIds = "," + tblSystemDeploy.getServerIds() + ",";
		serverOrderMap.clear();
		//封装有效的服务器LIST，方便后面停止前、停止后、启动后逻辑判断。
		List<TblServerInfo> validServerInfoList = new ArrayList<TblServerInfo>();
		for (TblServerInfo tblServerInfo : tblServerInfoList) {
			if (serverIds.indexOf("," + tblServerInfo.getId().toString() + ",") != -1) {
				validServerInfoList.add(tblServerInfo);
			}
		}
		
		String serverIpStr = "";
		for (int serverIndex = 0; serverIndex < validServerInfoList.size(); serverIndex++) {//按一台一台服务器来封装脚本
			TblServerInfo tblServerInfo = validServerInfoList.get(serverIndex);
			serverIpStr += tblServerInfo.getIp() + " ";
			assembleExpectScript(paramMap, tblSystemInfo, jenkinsTool, tblSystemModule, tblArtifactInfo, validServerInfoList,
					tblServerInfo, tblSystemDeploy, tblSystemDeployScriptList, serverIndex, blankCount + 1);
		}
		scriptSB.append(getPreBlank(blankCount)).append("stage('deploy ").append(currentCode).append(" ").append(serverIpStr).append("'){\n");
		if (tblSystemModule == null) {
			showScriptTitle(tblSystemInfo.getSystemCode() + " " + DEPLOY_TITLE, 0, SUFFIX_TITLE_2, scriptSB, blankCount + 1);
		} else {
			showScriptTitle(tblSystemModule.getModuleCode() + " " + DEPLOY_TITLE, 0, SUFFIX_TITLE_2, scriptSB, blankCount + 1);
		}
		blankCount = this.assembleAOPStart(paramMap, tblSystemModule, scriptSB, blankCount);
		StringBuilder expectScript = getServerOrderScript(validServerInfoList);
		scriptSB.append(expectScript);
		scriptSB.append(getPreBlank(blankCount + 1)).append("echo '").append(currentCode).append(" 执行部署服务结束 ......'\n");
		blankCount = this.assembleAOPEnd(paramMap, tblSystemModule, false, scriptSB, blankCount);
		scriptSB.append(getPreBlank(blankCount)).append("}\n");
	}
	
	/**
	 * 根据排过顺序的Map内容封装出单独的模块中，当有多个服务器时，停止前、停止后、启动后执行脚本顺序。
	 * @param tblServerInfoList 
	 * @return
	 */
	private StringBuilder getServerOrderScript(List<TblServerInfo> tblServerInfoList) {
		StringBuilder expectSB = new StringBuilder();
		showScriptTitle(DEPLOY_SQL_TITLE, 1, SUFFIX_TITLE_1, expectSB, 0);
		currentDeployStep = beforeStopGroovy;
		getServerOrderScriptDetail(tblServerInfoList, currentDeployStep, expectSB);//停止前
//		showScriptTitle(DEPLOY_SQL_TITLE, DEPLOY_SQL_SUB_TITLE_1, 2, SUFFIX_TITLE_1, expectSB);
//		showScriptTitle(DEPLOY_SQL_TITLE, DEPLOY_SQL_SUB_TITLE_2, 1, SUFFIX_TITLE_1, expectSB);
		currentDeployStep = afterStopGroovy;
		getServerOrderScriptDetail(tblServerInfoList, currentDeployStep, expectSB);//停止后
//		showScriptTitle(DEPLOY_SQL_TITLE, DEPLOY_SQL_SUB_TITLE_2, 2, SUFFIX_TITLE_1, expectSB);
//		showScriptTitle(DEPLOY_SQL_TITLE, DEPLOY_SQL_SUB_TITLE_3, 1, SUFFIX_TITLE_1, expectSB);
		currentDeployStep = afterStartUpGroovy;
		getServerOrderScriptDetail(tblServerInfoList, currentDeployStep, expectSB);//启动后
		showScriptTitle(DEPLOY_SQL_TITLE, 2, SUFFIX_TITLE_1, expectSB, 0);
		return expectSB;
	}


	/**
	 * 添加sql的groovy脚本
	 * @param tblServerInfoList
	 * @param currentDeployStep
	 * @param expectSB
	 */
	private void getServerOrderScriptDetail(List<TblServerInfo> tblServerInfoList, String currentDeployStep, StringBuilder expectSB) {
		for (TblServerInfo tblServerInfo : tblServerInfoList) {
			Map<String, List<StringBuilder>> serverOrderScriptMap = serverOrderMap.get(tblServerInfo.getIp());
			if (serverOrderScriptMap != null) {
				List<StringBuilder> scriptList = serverOrderScriptMap.get(currentDeployStep);
				if (scriptList != null && scriptList.size() > 0) {
					showScriptTitle(DEPLOY_SQL_IP_TITLE, tblServerInfo.getIp(), 1, SUFFIX_TITLE_2, expectSB, 0);
					for (StringBuilder sbTemp : scriptList) {
						expectSB.append(sbTemp);
					}
					showScriptTitle(DEPLOY_SQL_IP_TITLE, tblServerInfo.getIp(), 2, SUFFIX_TITLE_2, expectSB, 0);
				}
			}
		}
	}

	/**
	 * 封装基于expect命令的Shell脚本，用于停止及更新服务。
	 * @param paramMap 
	 * @param jenkinsTool
	 * @param tblArtifactInfo 
	 * @param tblSystemModuleList
	 * @param tblServerInfoList
	 * @param tblSystemDeployList
	 * @param blankCount
	 * @return
	 */
	private void assembleExpectScript(Map<String, Object> paramMap, TblSystemInfo tblSystemInfo, TblToolInfo jenkinsTool, TblSystemModule tblSystemModule,
			TblArtifactInfo tblArtifactInfo, List<TblServerInfo> validServerInfoList, TblServerInfo tblServerInfo, TblSystemDeploy tblSystemDeploy, 
			List<TblSystemDeployScript> tblSystemDeployScriptList, int serverIndex, int blankCount) {
		List<TblSystemDbConfig> tblSystemDbConfigList = (List<TblSystemDbConfig>)paramMap.get("tblSystemDbConfigList");
//		StringBuilder detailSb = new StringBuilder();
		String sshAccountExpect = "*\\\\[#$>\\\\]";
		int sleeptime = 2;// 执行步骤等待时间S
		currentDeployStep = beforeStopGroovy;//默认停止前步骤
		
		int interruptCount = 0;
		TblSystemDeployScript changeUserBean = null;//切换过用户
		for (TblSystemDeployScript tblSystemDeployScript : tblSystemDeployScriptList) {
			if (tblSystemDeployScript.getSystemDeployId().equals(tblSystemDeploy.getId())) {
				
				if (tblSystemDeployScript.getOperateType() == 1) {//操作 1=执行脚本，2=切换用户，3=上传程序，4=暂停执行，5=执行断言
					StringBuilder detailSb = assembleExceptStart(tblSystemDeploy, 1, blankCount);
					/**************用户连接*************/
					changeUserData(tblSystemDeployScript, changeUserBean);
					assembleSpawnSSH(tblServerInfo, tblSystemDeployScript, blankCount + 1, detailSb);//spawn ssh root@10.1.12.38
					assembleLoginAnswer(tblServerInfo, tblSystemDeployScript,blankCount + 1, detailSb);
					
					//密码过期
				//	assemblePWDExpired(blankCount, detailSb, false);
					
					assembleLoopContinue(blankCount + 1, detailSb);
					/**************执行脚本*************/
					String script = tblSystemDeployScript.getScript();
					if (StringUtil.isNotEmpty(script)) {
						script = script.replaceAll("\"", "\\\\\\\\\"").replaceAll("\\$", "\\\\\\\\\\$").replaceAll("\\{", "\\\\\\\\\\{").replaceAll("\\}", "\\\\\\\\\\}");
					}
					detailSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n");
					detailSb.append(getPreBlank(blankCount + 2)).append("\"").append(sshAccountExpect).append("\" {send \"")
							.append(script).append(" \\r\"}").append("\n");
					assembleExecuteFail(permissionDenied, permissionDeniedMsg, blankCount + 2, detailSb);
					detailSb.append(getPreBlank(blankCount + 2)).append("timeout {set timeoutflag 1}\n");
					detailSb.append(getPreBlank(blankCount + 1)).append("}\n");
					assembleLoopContinue(blankCount + 1, detailSb);
					assembleWaitTime(tblSystemDeployScript, sleeptime, blankCount + 1, detailSb);
					/**************执行结果验证*************/
					detailSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n");
					assembleExecuteFail(noFileOrDirError, noFileOrDirErrorMsg, blankCount + 2, detailSb);
					expectConditionSuccess(sshAccountExpect, blankCount + 2, detailSb);
					detailSb.append(getPreBlank(blankCount + 2)).append("timeout {set timeoutflag 1}\n");
					detailSb.append(getPreBlank(blankCount + 1)).append("}\n");
					assembleLoopContinue(blankCount + 1, detailSb);
					assembleExceptEnd(tblServerInfo, tblSystemDeployScript, validServerInfoList, serverIndex, 1, blankCount, detailSb);
				} else if (tblSystemDeployScript.getOperateType() == 2) {//操作 1=执行脚本，2=切换用户，3=上传程序，4=暂停执行，5=执行断言
					changeUserBean = tblSystemDeployScript;
				} else if (tblSystemDeployScript.getOperateType() == 3) {//操作 1=执行脚本，2=切换用户，3=上传程序，4=暂停执行，5=执行断言
					StringBuilder detailSb = assembleExceptStart(tblSystemDeploy, 1, blankCount);
					/**************上传程序*************/
					changeUserData(tblSystemDeployScript, changeUserBean);//前一次为切换用户，上传用户名密码转换为所切换的用户。
					assembleSpawnSCP(tblSystemInfo, tblSystemModule, tblServerInfo, tblSystemDeploy, tblSystemDeployScript, blankCount + 1, detailSb);
					assembleLoginAnswer(tblServerInfo, tblSystemDeployScript, blankCount + 1, detailSb);
					assembleLoopContinue(blankCount + 1, detailSb);
					assembleWaitTime(tblSystemDeployScript, sleeptime, blankCount + 1, detailSb);
					
					/**************上传程序验证*************/
					detailSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n");
					//上传时，返回的数据特殊处理
					//assemblePWDExpired(blankCount + 2, detailSb,true);
					assembleExecuteFail(noFileOrDirError, noFileOrDirErrorMsg, blankCount + 2, detailSb);
					expectConditionSuccess(sshAccountExpect, blankCount + 2, detailSb);
					expectConditionSuccess("100%", blankCount + 2, detailSb);
					//上传循环显示进度
					detailSb.append(getPreBlank(blankCount + 2)).append("\"").append("* ETA").append("\" {send \"\\r\";sleep 5;exp_continue}").append("\n");
					assembleExecuteFail(permissionDenied, permissionDeniedMsg, blankCount +2, detailSb);
					detailSb.append(getPreBlank(blankCount + 2)).append("timeout {set timeoutflag 1}\n");
					detailSb.append(getPreBlank(blankCount + 1)).append("}\n");
					assembleLoopContinue(blankCount + 1, detailSb);
					assembleWaitTime(tblSystemDeployScript, 5, blankCount + 1, detailSb);//上传有概率还未真正结束，所以等待
					
					if (tblSystemInfo.getDeployType() == 2) {//制品部署,目标服务器package包转移、configuration环境编码处理
						/**************用户连接*************/
						changeUserData(tblSystemDeployScript, changeUserBean);//前一次不是切换用户,添加切换用户操作。
						assembleSpawnSSH(tblServerInfo, tblSystemDeployScript, blankCount + 1, detailSb);//spawn ssh root@10.1.12.38
						assembleLoginAnswer(tblServerInfo, tblSystemDeployScript,blankCount + 1, detailSb);
						//密码过期
						//assemblePWDExpired(blankCount, detailSb, false);
						assembleLoopContinue(blankCount + 1, detailSb);
						/**************执行CD-SQL中的configuration package转移逻辑*************/
						String script = getConfigPackageScript(paramMap, tblSystemInfo, tblSystemModule, tblArtifactInfo, tblSystemDeploy);
						if (StringUtil.isNotEmpty(script)) {
							script = script.replaceAll("\"", "\\\\\\\\\"").replaceAll("\\$", "\\\\\\\\\\$").replaceAll("\\{", "\\\\\\\\\\{").replaceAll("\\}", "\\\\\\\\\\}");
						}
						detailSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n");
						detailSb.append(getPreBlank(blankCount + 2)).append("\"").append(sshAccountExpect).append("\" {send \"")
								.append(script).append(" \\r\"}").append("\n");
						assembleExecuteFail(permissionDenied, permissionDeniedMsg, blankCount + 2, detailSb);
						detailSb.append(getPreBlank(blankCount + 2)).append("timeout {set timeoutflag 1}\n");
						detailSb.append(getPreBlank(blankCount + 1)).append("}\n");
						assembleLoopContinue(blankCount + 1, detailSb);
						assembleWaitTime(tblSystemDeployScript, 20, blankCount + 1, detailSb);//except执行send有大量信息时，用了\r会直接进入except拦截，用sleep等待send内容全部结束才往下执行
						/**************执行结果验证*************/
						detailSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n");
						assembleExecuteFail(noFileOrDirError, noFileOrDirErrorMsg, blankCount + 2, detailSb);
						assembleExecuteFail(commandNotFound, commandNotFoundMsg, blankCount + 2, detailSb);
						assembleExecuteFail(commandNotFoundCN, commandNotFoundCNMsg, blankCount + 2, detailSb);
						assembleExecuteFail(writeError, writeErrorMsg, blankCount + 2, detailSb);
						expectConditionSuccess(sshAccountExpect, blankCount + 2, detailSb);
						detailSb.append(getPreBlank(blankCount + 2)).append("timeout {set timeoutflag 1}\n");
						detailSb.append(getPreBlank(blankCount + 1)).append("}\n");
						assembleLoopContinue(blankCount + 1, detailSb);
					}
					assembleExceptEnd(tblServerInfo, tblSystemDeployScript, validServerInfoList, serverIndex, 1, blankCount, detailSb);
				} else if (tblSystemDeployScript.getOperateType() == 4) {//操作 1=执行脚本，2=切换用户，3=上传程序，4=暂停执行，5=执行断言
					StringBuilder detailSb = assembleExceptStart(tblSystemDeploy, 0, blankCount);
					interruptCount++;
					String interruptStr = "input id: 'interrupt" + interruptCount + "', message: '" + tblSystemDeployScript.getScript() + "', ok: 'Continue'";
					detailSb.append(getPreBlank(blankCount + 1)).append(interruptStr).append("\n");
					assembleExceptEnd(tblServerInfo, tblSystemDeployScript, validServerInfoList, serverIndex, 0, blankCount, detailSb);
				} else if (tblSystemDeployScript.getOperateType() == 5) {//操作 1=执行脚本，2=切换用户，3=上传程序，4=暂停执行，5=执行断言
					StringBuilder detailSb = assembleExceptStart(tblSystemDeploy, 1, blankCount);
					/**************用户连接*************/
					changeUserData(tblSystemDeployScript, changeUserBean);
					assembleSpawnSSH(tblServerInfo, tblSystemDeployScript, blankCount + 1, detailSb);//spawn ssh root@10.1.12.38
					assembleLoginAnswer(tblServerInfo, tblSystemDeployScript,blankCount + 1, detailSb);
					//密码过期
					//assemblePWDExpired(blankCount, detailSb, false);
					assembleLoopContinue(blankCount + 1, detailSb);
					/**************执行脚本*************/
					String script = tblSystemDeployScript.getScript();
					if (StringUtil.isNotEmpty(script)) {
						script = script.replaceAll("\"", "\\\\\\\\\"").replaceAll("\\$", "\\\\\\\\\\$").replaceAll("\\{", "\\\\\\\\\\{").replaceAll("\\}", "\\\\\\\\\\}");
					}
					String[] checkArr = script.split("#ASSERT#");
					if (checkArr.length == 2) {
						detailSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n");
						detailSb.append(getPreBlank(blankCount + 2)).append("\"").append(sshAccountExpect).append("\" {send \"")
						.append(checkArr[0]).append(" \\r\"}").append("\n");
						assembleExecuteFail(permissionDenied, permissionDeniedMsg, blankCount + 2, detailSb);
						detailSb.append(getPreBlank(blankCount + 2)).append("timeout {set timeoutflag 1}\n");
						detailSb.append(getPreBlank(blankCount + 1)).append("}\n");
						assembleLoopContinue(blankCount + 1, detailSb);
						
						script = checkArr[1].trim();
						String assertMsg = "\n执行断言内容：" + script + "\n执行断言结果：TRUE";
						detailSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n");
						detailSb.append(getPreBlank(blankCount + 2)).append("\"").append("*").append(script).append("*").append("\" {").append("\n");
						detailSb.append(getPreBlank(blankCount + 3)).append("set i [expr {\\\\$maxloop}]").append("\n");
						detailSb.append(getPreBlank(blankCount + 3)).append("puts \"").append(assertMsg).append("\"").append("\n");
						detailSb.append(getPreBlank(blankCount + 2)).append("}\n");
						assertMsg = "\n执行断言内容：" + script + "\n执行断言结果：FALSE";
						assembleExecuteFail(sshAccountExpect, assertMsg, blankCount + 2, detailSb);
						detailSb.append(getPreBlank(blankCount + 2)).append("timeout {set timeoutflag 1}\n");
						detailSb.append(getPreBlank(blankCount + 1)).append("}\n");
					}
					assembleLoopContinue(blankCount + 1, detailSb);
					assembleWaitTime(tblSystemDeployScript, sleeptime, blankCount + 1, detailSb);
					assembleExceptEnd(tblServerInfo, tblSystemDeployScript, validServerInfoList, serverIndex, 1, blankCount, detailSb);
				} else if (tblSystemDeployScript.getOperateType() == 6) {//操作 6=停止前执行SQL，7=停止后执行SQL，8=启动后执行SQL
					StringBuilder detailSb = assembleExceptStart(tblSystemDeploy, 2, blankCount);
					assembleCDSQL(tblSystemInfo, tblSystemModule, tblSystemDeploy, tblSystemDbConfigList, beforeStopGroovy, blankCount + 1, detailSb);
					assembleWaitTime(tblSystemDeployScript, sleeptime, blankCount + 1, detailSb);
					assembleExceptEnd(tblServerInfo, tblSystemDeployScript, validServerInfoList, serverIndex, 2, blankCount, detailSb);
				} else if (tblSystemDeployScript.getOperateType() == 7) {//操作 6=停止前执行SQL，7=停止后执行SQL，8=启动后执行SQL
					StringBuilder detailSb = assembleExceptStart(tblSystemDeploy, 2, blankCount);
					assembleCDSQL(tblSystemInfo, tblSystemModule, tblSystemDeploy, tblSystemDbConfigList, afterStopGroovy, blankCount + 1, detailSb);
					assembleWaitTime(tblSystemDeployScript, sleeptime, blankCount + 1, detailSb);
					assembleExceptEnd(tblServerInfo, tblSystemDeployScript, validServerInfoList, serverIndex, 2, blankCount, detailSb);
				} else if (tblSystemDeployScript.getOperateType() == 8) {//操作 6=停止前执行SQL，7=停止后执行SQL，8=启动后执行SQL
					StringBuilder detailSb = assembleExceptStart(tblSystemDeploy, 2, blankCount);
					assembleCDSQL(tblSystemInfo, tblSystemModule, tblSystemDeploy, tblSystemDbConfigList, afterStartUpGroovy, blankCount + 1, detailSb);
					assembleWaitTime(tblSystemDeployScript, sleeptime, blankCount + 1, detailSb);
					assembleExceptEnd(tblServerInfo, tblSystemDeployScript, validServerInfoList, serverIndex, 2, blankCount, detailSb);
				}
				
			}
		}
		
	}

	/**
	 * Except脚本开始部分
	 * @param tblSystemDeploy
	 * @param blankCount
	 * @param type 0：不需要追加脚本，1：追加标准开始脚本，2：追加少量脚本
	 * @param detailSb
	 */
	private StringBuilder assembleExceptStart(TblSystemDeploy tblSystemDeploy, int type, int blankCount) {
		StringBuilder detailSb = new StringBuilder();
		if (type == 1) {//type 0：不需要追加脚本，1：追加标准开始脚本，2：追加少量脚本
			detailSb.append(getPreBlank(blankCount + 1)).append("sh '''\n");//三个'表示连续的字符串。
			detailSb.append("cat>${WORKSPACE}/expect.sh<<eof\n");
			detailSb.append(getPreBlank(blankCount + 1)).append("#!/usr/bin/expect\n");
			
			if (tblSystemDeploy.getTimeOut() != null && tblSystemDeploy.getTimeOut() != 0) {//设置超时秒
				detailSb.append(getPreBlank(blankCount)).append("set timeout ").append(tblSystemDeploy.getTimeOut()).append("\n");
			} else {//默认10秒
				detailSb.append(getPreBlank(blankCount)).append("set timeout 10\n");
			}
			if (tblSystemDeploy.getRetryNumber() != null && tblSystemDeploy.getRetryNumber() != 0) {//设置重试次数
				detailSb.append(getPreBlank(blankCount)).append("set maxloop ").append(tblSystemDeploy.getRetryNumber()).append("\n");
			} else {//默认3次
				detailSb.append(getPreBlank(blankCount)).append("set maxloop 3\n");
			}
			detailSb.append(getPreBlank(blankCount)).append("set i 0\n");
			detailSb.append(getPreBlank(blankCount)).append("while {\\\\$i < \\\\$maxloop} {\n");//while start
			detailSb.append(getPreBlank(blankCount + 1)).append("set timeoutflag 0\n");//是否超时标志,1为超时
		} else if (type == 2) {//type 0：不需要追加脚本，1：追加标准开始脚本，2：追加少量脚本
			detailSb.append(getPreBlank(blankCount + 1)).append("sh '''\n");//三个'表示连续的字符串。
			detailSb.append("cat>${WORKSPACE}/expect.sh<<eof\n");
			detailSb.append(getPreBlank(blankCount + 1)).append("#!/usr/bin/expect\n");
			
			if (tblSystemDeploy.getTimeOut() != null && tblSystemDeploy.getTimeOut() != 0) {//设置超时秒
				detailSb.append(getPreBlank(blankCount)).append("set timeout ").append(tblSystemDeploy.getTimeOut()).append("\n");
			} else {//默认10秒
				detailSb.append(getPreBlank(blankCount)).append("set timeout 10\n");
			}
		}
		return detailSb;
	}
	
	/**
	 * Except脚本结束部分
	 * @param blankCount
	 * @param type 0：不需要追加脚本，1：追加标准开始脚本，2：追加少量脚本
	 * @param detailSb
	 */
	private void assembleExceptEnd(TblServerInfo tblServerInfo, TblSystemDeployScript tblSystemDeployScript, List<TblServerInfo> validServerInfoList,
			int serverIndex, int type, int blankCount, StringBuilder detailSb) {
		if (type == 1) {//type 0：不需要追加脚本，1：追加标准开始脚本，2：追加少量脚本
			detailSb.append(getPreBlank(blankCount + 1)).append("incr i\n");
			detailSb.append(getPreBlank(blankCount)).append("}\n");//where end
			
			detailSb.append("eof\n");//前面不能有空格，否则会出错
			detailSb.append("expect ${WORKSPACE}/expect.sh\n");
			detailSb.append(getPreBlank(blankCount + 1)).append(" '''\n");
			
		} else if (type == 2) {//type 0：不需要追加脚本，1：追加标准开始脚本，2：追加少量脚本
			detailSb.append("eof\n");//前面不能有空格，否则会出错
			detailSb.append("expect ${WORKSPACE}/expect.sh\n");
			detailSb.append(getPreBlank(blankCount + 1)).append(" '''\n");
		}
		
		if (tblSystemDeployScript.getOperateType() == 6) {//操作 6=停止前执行SQL，7=停止后执行SQL，8=启动后执行SQL
			currentDeployStep = beforeStopGroovy;
			if (serverIndex == 0) {//停止前执行的script，只在开始的第一个服务器执行
				putServerOrderMap(tblServerInfo, detailSb);
			}
		} else if (tblSystemDeployScript.getOperateType() == 7) {//7=停止后执行SQL，同一模块下，需要所有节点都停止才执行。
			currentDeployStep = afterStopGroovy;
			if (serverIndex == 0) {
				putServerOrderMap(tblServerInfo, detailSb);
			}
		} else if (tblSystemDeployScript.getOperateType() == 8) {//8=启动后执行SQL，同一模块下，需要所有节点都启动才执行。
			currentDeployStep = afterStartUpGroovy;
			if (serverIndex == 0) {
				putServerOrderMap(tblServerInfo, detailSb);
			}
		} else {
			putServerOrderMap(tblServerInfo, detailSb);
		}
			
	}

	/**
	 * 将封装好的对象Map按sql执行顺序存放：停止前、停止后、启动后执行的顺序问题
	 * @param tblServerInfo
	 * @param detailSb
	 */
	private void putServerOrderMap(TblServerInfo tblServerInfo, StringBuilder detailSb) {
		Map<String, List<StringBuilder>> serverOrderScriptMap = serverOrderMap.get(tblServerInfo.getIp());
		if (serverOrderScriptMap == null) {
			serverOrderScriptMap = new HashMap<String, List<StringBuilder>>();
			List<StringBuilder> scriptList = new ArrayList<StringBuilder>();
			scriptList.add(detailSb);
			serverOrderScriptMap.put(currentDeployStep, scriptList);
			serverOrderMap.put(tblServerInfo.getIp(), serverOrderScriptMap);
		} else {
			List<StringBuilder> scriptList = serverOrderScriptMap.get(currentDeployStep);
			if (scriptList == null) {
				scriptList = new ArrayList<StringBuilder>();
				scriptList.add(detailSb);
				serverOrderScriptMap.put(currentDeployStep, scriptList);
			} else {
				scriptList.add(detailSb);
			}
		}
	}

	/**
	 * 默认用户服务器表里的用户信息。
	 * 如果有变更用户步骤，则之后流程过会采用变更后的用户信息。
	 * @param tblSystemDeployScript
	 * @param changeUserBean
	 */
	private void changeUserData(TblSystemDeployScript tblSystemDeployScript, TblSystemDeployScript changeUserBean) {
		if (changeUserBean != null) {//切换过用户,默认用切换后的用户信息。
			tblSystemDeployScript.setUserName(changeUserBean.getUserName());
			tblSystemDeployScript.setPassword(changeUserBean.getPassword());
		} else {
			tblSystemDeployScript.setUserName(null);
			tblSystemDeployScript.setPassword(null);
		}
	}

	/**
	 * 6=停止前执行SQL，7=停止后执行SQL，8=启动后执行SQL
	 * @param tblSystemInfo
	 * @param tblSystemModule
	 * @param tblSystemDeploy
	 * @param tblSystemDbConfigList 
	 * @param title
	 * @param blankCount
	 * @param detailSb
	 */
	private void assembleCDSQL(TblSystemInfo tblSystemInfo, TblSystemModule tblSystemModule, TblSystemDeploy tblSystemDeploy, 
			List<TblSystemDbConfig> tblSystemDbConfigList, String title, int blankCount, StringBuilder detailSb) {
		String localAddr = "";//Jenkins的工作目录地址
		if (tblSystemModule != null) {
			if (tblSystemModule.getId().equals(tblSystemDeploy.getSystemModuleId())) {//微服务
				localAddr += JenkinsUtil.addSlash(tblSystemModule.getRelativePath(), "/|\\\\", "/", false) + "/";
			}
		}
		localAddr = "cd " + localAddr + "target/\\r";
		detailSb.append(getPreBlank(blankCount)).append("puts \"============").append(title).append(" SQL Start=============\"\n");
		if (tblSystemDbConfigList != null && tblSystemDbConfigList.size() > 0) {//当页面配置得有sql基本信息时才需要处理。
			if (tblSystemInfo.getArchitectureType() == 2) { //1=微服务架构；2=传统架构
				detailSb.append(getPreBlank(blankCount)).append("set sqlResult [exec sh -c {").append(localAddr).append("groovy ").append(title).append(".groovy}]\n");
				detailSb.append(getPreBlank(blankCount)).append("puts \\\\$sqlResult\n");
			} else {
				boolean existDbConfig = false;
				for (TblSystemDbConfig tblSystemDbConfig : tblSystemDbConfigList) {
					if (tblSystemModule.getId().equals(tblSystemDbConfig.getSystemModuleId())) {
						existDbConfig = true;
						break;
					}
				}
				if (existDbConfig) {//存在sql配置信息时
					detailSb.append(getPreBlank(blankCount)).append("set sqlResult [exec sh -c {").append(localAddr).append("groovy ").append(title).append(".groovy}]\n");
					detailSb.append(getPreBlank(blankCount)).append("puts \\\\$sqlResult\n");
				}
			}
		}
		detailSb.append(getPreBlank(blankCount)).append("puts \"============").append(title).append(" SQL End=============\"\n");
		
	}

	/**
	 * package下的工程包移到运行目录。
	 * configuration根据环境编码将对应环境编码下的配置文件移到运行目录。
	 * @param paramMap 
	 * @param tblSystemModule 
	 * @param tblSystemInfo 
	 * @param tblArtifactInfo 
	 * @param tblSystemDeploy 
	 * @return
	 */
	private String getConfigPackageScript(Map<String, Object> paramMap, TblSystemInfo tblSystemInfo, TblSystemModule tblSystemModule, 
			TblArtifactInfo tblArtifactInfo, TblSystemDeploy tblSystemDeploy) {
		String artifactId = tblSystemInfo.getArtifactId();
		String groupId = tblSystemInfo.getGroupId();
		String packageSuffix = tblSystemInfo.getPackageSuffix();
		String packageCode = tblSystemInfo.getSystemCode();
		if (tblSystemModule != null) {
			if (StringUtil.isNotEmpty(tblSystemModule.getArtifactId())) {
				artifactId = tblSystemModule.getArtifactId();
			}
			if (StringUtil.isNotEmpty(tblSystemModule.getGroupId())) {
				groupId = tblSystemModule.getGroupId();
			}
			if (StringUtil.isNotEmpty(tblSystemModule.getPackageSuffix())) {
				packageSuffix = tblSystemModule.getPackageSuffix();
			}
			if (StringUtil.isNotEmpty(tblSystemModule.getModuleCode())) {
				packageCode = tblSystemModule.getModuleCode();
			}
		}
		String remoteAddr = JenkinsUtil.addSlash(tblSystemDeploy.getSystemDeployPath(), "/|\\\\", "/", true);
		String zipPackage = artifactId + "-" + tblArtifactInfo.getVersion() + ".zip";
		String mvnPackage = "*." + packageSuffix;
		String envName = paramMap.get("envName").toString();
		packageCode = packageCode + serverUploadTemp;
		
		StringBuilder msgSb = new StringBuilder();
		/**package下的工程包移到运行目录。*/
		msgSb.append("cd ").append(remoteAddr).append("\n");
		msgSb.append("ls -lt").append("\n");
		msgSb.append("rm -rf ").append(packageCode).append("\n");
		msgSb.append("mkdir ").append(packageCode).append("\n");
		msgSb.append("mv -f ").append(zipPackage).append(" ").append(packageCode).append("\n");
		msgSb.append("unzip -oq ").append(packageCode).append("/").append(zipPackage).append(" -d ").append(packageCode).append("\n");
		msgSb.append("cp -rf ").append(packageCode).append("/package/").append(mvnPackage).append(" .").append("\n");
		/**configuration根据环境编码将对应环境编码下的配置文件移到运行目录。*/
//		String envPath = packageCode + "/configuration/" + envName;
//		msgSb.append("if [ -d \"").append(envPath).append("/\" ] && [ \"`ls -A ").append(envPath).append("/`\" != \"\" ];then ")
//		.append("cp -rf ").append(envPath).append("/*").append(" . ").append("; else echo \"\"; fi").append("\n");
		msgSb.append("cp -rf ").append(packageCode).append("/configuration/").append(envName).append("/*").append(" . ").append(shellNoPrint).append("\n");
		msgSb.append("sleep 5").append("\n");
		msgSb.append("rm -rf ").append(packageCode).append("\n");
		msgSb.append("ls -lt").append("\n");
		msgSb.append("sleep 2");
		return msgSb.toString();
	}

	/**
	 * 追加执行失败条件判断
	 * @param blankCount
	 * @param detailSb
	 */
	private void assembleExecuteFail(String condition, String msg, int blankCount, StringBuilder detailSb) {
		detailSb.append(getPreBlank(blankCount)).append("\"").append(condition).append("\" {").append("\n");
		detailSb.append(getPreBlank(blankCount + 1)).append("set i [expr {\\\\$maxloop}]").append("\n");
		detailSb.append(getPreBlank(blankCount + 1)).append("puts \"").append(msg).append("\"").append("\n");
		detailSb.append(getPreBlank(blankCount + 1)).append("exit 1").append("\n");
		detailSb.append(getPreBlank(blankCount)).append("}\n");
	}

	
	/**
	 * 执行下一步程序等待时间
	 * @param tblSystemDeployScript
	 * @param sleeptime
	 * @param blankCount
	 * @param detailSb
	 */
	public void assembleWaitTime(TblSystemDeployScript tblSystemDeployScript, int sleeptime, int blankCount, StringBuilder detailSb) {
		if (tblSystemDeployScript != null && tblSystemDeployScript.getWaitTime() != null && tblSystemDeployScript.getWaitTime() > 0) {
			if (tblSystemDeployScript.getOperateType() == 1) {
				sleeptime = tblSystemDeployScript.getWaitTime();
			}
		}
		detailSb.append(getPreBlank(blankCount)).append("sleep " + sleeptime + "\n");
	}

	/**
	 * 重复性的expecct接收条件
	 * @param blankCount
	 * @param tblServerInfo
	 * @param msgSb
	 * @param sshAccountExpect
	 */
	private void expectConditionSuccess(String condition, int blankCount, StringBuilder msgSb) {
		msgSb.append(getPreBlank(blankCount)).append("\"").append(condition).append("\" {\n");
		msgSb.append(getPreBlank(blankCount + 1)).append("if { \\\\$timeoutflag == 0 } {\n");//if Start
		msgSb.append(getPreBlank(blankCount + 2)).append("set i [expr {\\\\$maxloop}]").append("\n");
		msgSb.append(getPreBlank(blankCount + 2)).append("puts \"").append(BUILD_DEPLOY_SUCCESS).append("\"").append("\n");
		msgSb.append(getPreBlank(blankCount + 2)).append("set successStatus 1").append("\n");
		msgSb.append(getPreBlank(blankCount + 1)).append("}\n");//if end
		msgSb.append(getPreBlank(blankCount)).append("}\n");
	}
	
	/**
	  if { "$timeoutflag" == "1" } {
	        incr i
		continue
	 }
	 * @param i
	 * @param detailSb
	 */
	private void assembleLoopContinue(int blankCount, StringBuilder msgSb) {
		msgSb.append(getPreBlank(blankCount)).append("if { \\\\$timeoutflag == 1 } {\n");
		msgSb.append(getPreBlank(blankCount + 1)).append("incr i").append("\n");
		msgSb.append(getPreBlank(blankCount + 1)).append("continue").append("\n");
		msgSb.append(getPreBlank(blankCount)).append("}").append("\n");
	}
	
	/**
	 * 用于返回需要登录验证的信息
	 * expect {
	 * "*password:" {send "password\r";}
	 * "yes/no" {send "yes\r";exp_continue}
	 * }
	 * @param blankCount 
	 * @param msgSb 
	 * @return
	 */
	private void assembleLoginAnswer(TblServerInfo tblServerInfo, TblSystemDeployScript tblSystemDeployScript, int blankCount, StringBuilder msgSb) {
		String password = tblServerInfo.getSshUserPassword();
		if (tblSystemDeployScript != null && StringUtil.isNotEmpty(tblSystemDeployScript.getPassword())) {
			password = tblSystemDeployScript.getPassword();
		}
		msgSb.append(getPreBlank(blankCount)).append("expect {\n");
		msgSb.append(getPreBlank(blankCount + 1)).append("\"yes/no\" {send \"yes\\r\";exp_continue}").append("\n");
		msgSb.append(getPreBlank(blankCount + 1)).append("\"*password:\" {send \"").append(password).append("\\r\";}").append("\n");
		msgSb.append(getPreBlank(blankCount + 1)).append("\"*:\" {send \"").append(password).append("\\r\";}").append("\n");
		msgSb.append(getPreBlank(blankCount + 1)).append("timeout {set timeoutflag 1}").append("\n");
		msgSb.append(getPreBlank(blankCount)).append("}").append("\n");
	}
	
	
	/**
	 * 
	* @Title: assemblePWDExpired
	* @Description: 密码过期
	* @author author
	* @param blankCount
	* @param msgSb
	* @param isSCP
	* @throws
	 */
	/*
	 * public void assemblePWDExpired(int blankCount, StringBuilder msgSb,boolean
	 * isSCP) { //非SCP单独一个expect,SCP与其他异常一起处理 if(!isSCP) {
	 * msgSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n"); }
	 * msgSb.append(getPreBlank(blankCount +
	 * 1)).append("\"*Your password has expired*\" {\n" );
	 * msgSb.append(getPreBlank(blankCount +
	 * 1)).append(" set i [expr {\\\\$maxloop}] \n" );
	 * msgSb.append(getPreBlank(blankCount + 1)).append(" puts \"您的密码已过期\"\n" );
	 * msgSb.append(getPreBlank(blankCount + 1)).append(" exit 1\n" );
	 * msgSb.append(getPreBlank(blankCount)).append(" }\n"); if(!isSCP) {
	 * msgSb.append(getPreBlank(blankCount +
	 * 1)).append("\"*\\\\[#$>\\\\]\" {send \"\\r\"}\n");
	 * msgSb.append(getPreBlank(blankCount)).append("}").append("\n"); } }
	 */
	
	/**
	 *    连接SSH脚本
	 * spawn ssh root@10.1.12.38
	 * @param tblServerInfo
	 * @param blankCount
	 * @param msgSb
	 */
	private void assembleSpawnSSH(TblServerInfo tblServerInfo, TblSystemDeployScript tblSystemDeployScript, int blankCount, StringBuilder msgSb) {
		String username = tblServerInfo.getSshUserAccount();
		if (StringUtil.isNotEmpty(tblSystemDeployScript.getUserName()) && StringUtil.isNotEmpty(tblSystemDeployScript.getPassword())) {
			username = tblSystemDeployScript.getUserName();
		}
		String userIP = username + "@" + tblServerInfo.getIp();
		msgSb.append(getPreBlank(blankCount)).append("spawn ssh ").append("-p ").append(tblServerInfo.getSshPort()).append(" ").append(userIP).append("\n");
	}
	
	
	/**
	 *   上传SCP脚本
	 * spawn scp ${WORKSPACE}/target/*.{war,jar} root@10.1.12.38:/home/
	 * @param tblServerInfo
	 * @param tblSystemDeploy
	 * @param tblArtifactInfo 
	 * @param type
	 * @param blankCount
	 * @param msgSb
	 */
	private void assembleSpawnSCP(TblSystemInfo tblSystemInfo, TblSystemModule tblSystemModule, TblServerInfo tblServerInfo, TblSystemDeploy tblSystemDeploy,
			TblSystemDeployScript tblSystemDeployScript, int blankCount, StringBuilder msgSb) {
		String localAddr = "";//Jenkins的工作目录地址
		String packageSuffix = tblSystemInfo.getPackageSuffix();
		if (tblSystemModule != null) {
			if (tblSystemModule.getId().equals(tblSystemDeploy.getSystemModuleId())) {//微服务
				localAddr += JenkinsUtil.addSlash(tblSystemModule.getRelativePath(), "/|\\\\", "/", false) + "/";
				if (StringUtil.isNotEmpty(tblSystemModule.getPackageSuffix())) {
					packageSuffix = tblSystemModule.getPackageSuffix();
				}
			}
		}
		if (tblSystemInfo.getDeployType() == 2) {//制品部署
			packageSuffix = "zip";
		}
		localAddr += "target/*." + packageSuffix;
		String userIP = tblServerInfo.getSshUserAccount() + "@" + tblServerInfo.getIp();
		if (StringUtil.isNotEmpty(tblSystemDeployScript.getUserName())) {
			userIP = tblSystemDeployScript.getUserName() + "@" + tblServerInfo.getIp();
		}
		String remoteAddr = JenkinsUtil.addSlash(tblSystemDeploy.getSystemDeployPath(), "/|\\\\", "/", true);
		msgSb.append(getPreBlank(blankCount)).append("spawn bash -c \"scp -P ").append(tblServerInfo.getSshPort()).append(" ")
		.append(localAddr).append(" ").append(userIP).append(":").append(remoteAddr).append("\"\n");
	}
	
	/**
	 * 封装PRO部署服务逻辑Except脚本
	 * @param paramMap
	 * @param tblSystemInfo
	 * @param tblSystemModuleList
	 * @param checkModuleList
	 * @param blankCount
	 * @return
	 */
	public String assemblePROExpectScript(Map paramMap, TblSystemInfo tblSystemInfo, List<TblSystemModule> tblSystemModuleList, 
			List<String> checkModuleList, int blankCount) {
		TblServerInfo tblServerInfo = new TblServerInfo();
		tblServerInfo.setIp(jenkinsUploadIp);
		tblServerInfo.setSshUserAccount(jenkinsUploadUsername);
		tblServerInfo.setSshUserPassword(jenkinsUploadPassword);
		
		StringBuilder sshSB = new StringBuilder();
		sshSB.append(getPreBlank(blankCount)).append("#!/usr/bin/expect\n");
		sshSB.append(getPreBlank(blankCount)).append("puts \"=============Deploy Start=============\"\n");
			
		StringBuilder detailSb = new StringBuilder();
//				String sshAccountExpect = "*" + tblServerInfo.getSshUserAccount() + "@*";
		String sshAccountExpect = "*\\\\[#$>\\\\]";
		int sleeptime = 2;// 执行步骤等待时间S
		int moduleCount = Integer.parseInt(paramMap.get("moduleCount").toString());
		detailSb.append(getPreBlank(blankCount)).append("set timeout ").append(moduleCount * 600).append(" \n");
		detailSb.append(getPreBlank(blankCount)).append("set maxloop 3\n");
		detailSb.append(getPreBlank(blankCount)).append("set successStatus 0\n");//是否成功完成
		detailSb.append(getPreBlank(blankCount)).append("set i 0\n");
		detailSb.append(getPreBlank(blankCount)).append("while {\\\\$i < \\\\$maxloop} {\n");//while start
		detailSb.append(getPreBlank(blankCount + 1)).append("set timeoutflag 0\n");//是否超时标志,1为超时
		
//		List<FtpS3Vo> configFtpList = (List<FtpS3Vo>)paramMap.get("configResult");
		List<FtpS3Vo> operFtpList = (List<FtpS3Vo>)paramMap.get("operFileResult");
//		List<FtpS3Vo> sqlFtpList = (List<FtpS3Vo>)paramMap.get("sqlFileResult");
		String ftpPath = Objects.toString(paramMap.get("ftpPath"), "");
		
		List<FtpS3Vo> ftpList = new ArrayList<FtpS3Vo>();
		if (operFtpList != null) {
			ftpList.addAll(operFtpList);
		}
		if (ftpList.size() > 0 || StringUtil.isNotEmpty(ftpPath)) {
			assembleWaitTime(null, sleeptime, blankCount + 1, detailSb);
			assembleSpawnSFTP(tblServerInfo, blankCount + 1, detailSb);//spawn sftp ftpuser@10.1.11.190
			assembleLoginAnswer(tblServerInfo, null,blankCount + 1, detailSb);
			assembleLoopContinue(blankCount + 1, detailSb);
			
			String configFolder = "configuration";
			String documentFolder = "document";
			String sqlFolder = "sql";
			String packageFolder = "package";
			String[] folderArr = {configFolder, documentFolder, sqlFolder, packageFolder};
			String[] uploadArr = {uploadConfigurationPath, uploadDocumentPath, uploadSqlPath, uploadPackagePath};
			
			//mkdir
			assembleWaitTime(null, sleeptime, blankCount + 1, detailSb);
			detailSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n");
			detailSb.append(getPreBlank(blankCount + 2)).append("\"").append(sshAccountExpect).append("\" {\n");
			assembleSpawnMKDIR(ftpPath, folderArr, blankCount + 3, detailSb);
			detailSb.append(getPreBlank(blankCount + 2)).append("}").append("\n");
			assembleExecuteFail(permissionDenied, permissionDeniedMsg, blankCount + 2, detailSb);
			detailSb.append(getPreBlank(blankCount + 2)).append("timeout {set timeoutflag 1}\n");
			detailSb.append(getPreBlank(blankCount + 1)).append("}\n");
			assembleLoopContinue(blankCount + 1, detailSb);
			
			/**上传document文件*/
			for (int i = 0; i < folderArr.length; i++) {
				assembleWaitTime(null, sleeptime, blankCount + 1, detailSb);
				detailSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n");
				if (i == 0) {//expect第一个请求封装
					detailSb.append(getPreBlank(blankCount + 2)).append("\"").append(sshAccountExpect).append("\" {\n");
					assembleSpawnPUT(uploadArr[i], folderArr[i], blankCount + 3, detailSb);
					detailSb.append(getPreBlank(blankCount + 2)).append("}").append("\n");
				} else {//非第一个请求封装
					detailSb.append(getPreBlank(blankCount + 2)).append("\"").append("*Total:*").append("\" {\n");
					assembleSpawnPUT(uploadArr[i], folderArr[i], blankCount + 3, detailSb);
					detailSb.append(getPreBlank(blankCount + 2)).append("}").append("\n");
					//上传循环显示进度
					detailSb.append(getPreBlank(blankCount + 2)).append("\"").append("* eta*").append("\" {send \"\\r\";sleep 5;exp_continue}").append("\n");
				}
				assembleExecuteFail(permissionDenied, permissionDeniedMsg, blankCount + 2, detailSb);
				detailSb.append(getPreBlank(blankCount + 2)).append("timeout {set timeoutflag 1}\n");
				detailSb.append(getPreBlank(blankCount + 1)).append("}\n");
				assembleLoopContinue(blankCount + 1, detailSb);
			}
			
//			detailSb.append(getPreBlank(blankCount + 1)).append("sleep ").append(moduleCount * 60).append(" \n");
			/**上传完成后比较包完整性脚本*/
			String packagePaths = "";
			if (tblSystemModuleList == null || tblSystemModuleList.size() == 0) {
				packagePaths = folderArr[3] + "/" + tblSystemInfo.getSystemCode();
			} else {
				for (TblSystemModule bean : tblSystemModuleList) {
					for (String checkModuleId : checkModuleList) {
						if (checkModuleId.equals(bean.getId().toString())) {
							String relativePath = JenkinsUtil.addSlash(bean.getRelativePath(), "/|\\\\", "/", false);
							packagePaths += folderArr[3] + "/" + relativePath + " ";
							break;
						}
					}
				}
			}
			assembleWaitTime(null, sleeptime, blankCount + 1, detailSb);
			detailSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n");
			detailSb.append(getPreBlank(blankCount + 2)).append("\"").append("*Total:*").append("\" {\n");
			detailSb.append(getPreBlank(blankCount + 3)).append("puts \"").append("\n================自动化运维包件上传前================").append("\"").append("\n");
			detailSb.append(getPreBlank(blankCount + 3)).append("puts \"").append("${checkPROFile}").append("\"").append("\n");
			detailSb.append(getPreBlank(blankCount + 3)).append("puts \"").append("================自动化运维包上传后================").append("\"").append("\n");
			detailSb.append(getPreBlank(blankCount + 3)).append("send \"").append("\\rls ").append(packagePaths).append("\\r\"\n");
			detailSb.append(getPreBlank(blankCount + 2)).append("}\n");
			//上传循环显示进度
			detailSb.append(getPreBlank(blankCount + 2)).append("\"").append("* eta").append("\" {send \"\\r\";sleep 5;exp_continue}").append("\n");
			assembleExecuteFail(permissionDenied, permissionDeniedMsg, blankCount + 2, detailSb);
			assembleExecuteFail(accessFailed, accessFailedMsg, blankCount + 2, detailSb);
			detailSb.append(getPreBlank(blankCount + 2)).append("timeout {set timeoutflag 1}\n");
			detailSb.append(getPreBlank(blankCount + 1)).append("}\n");
			assembleLoopContinue(blankCount + 1, detailSb);
			
			//**判断循环是否结束脚本*/
			assembleWaitTime(null, sleeptime, blankCount + 1, detailSb);
			detailSb.append(getPreBlank(blankCount + 1)).append("expect {").append("\n");
			expectConditionSuccess(sshAccountExpect, blankCount + 2, detailSb);
//			expectConditionSuccess("100%", blankCount + 2, detailSb);
			assembleExecuteFail(permissionDenied, permissionDeniedMsg, blankCount + 2, detailSb);
			assembleExecuteFail(accessFailed, accessFailedMsg, blankCount + 2, detailSb);
			detailSb.append(getPreBlank(blankCount + 2)).append("timeout {set timeoutflag 1}\n");
			detailSb.append(getPreBlank(blankCount + 1)).append("}\n");
		}
		
		detailSb.append(getPreBlank(blankCount + 1)).append("incr i\n");
		detailSb.append(getPreBlank(blankCount)).append("}\n");//where end
		if (detailSb.length() > 0) {//只有明细操作有的情况下脚本才有效
			sshSB.append(detailSb);
		}
		/**停止expect脚本*/
		sshSB.append(getPreBlank(blankCount)).append("puts \"=============Deploy End=============\"\n");
//		sshSB.append(getPreBlank(blankCount)).append("expect eof").append("\n");
		expectSuccessStatus(blankCount, sshSB);
		return sshSB.toString();
	}
	
	/**
	 * 添加预先创建目录脚本内容
	 * @param path
	 * @param folderArr
	 * @param blankCount
	 * @param msgSb
	 */
	private void assembleSpawnMKDIR(String path, String[] folderArr, int blankCount, StringBuilder msgSb) {
		String remotePath = JenkinsUtil.addSlash(path, "/|\\\\", "/", false);
		StringBuilder mkdirSB = new StringBuilder();
		if (StringUtil.isNotEmpty(remotePath)) {
			mkdirSB.append("mkdir -p ").append(remotePath).append("\\r");
			mkdirSB.append("cd ").append(remotePath).append("\\r");
			String folderStr = "";
			for (String folder : folderArr) {
				folderStr += folder + " ";
			}
			mkdirSB.append("mkdir ").append(folderStr).append("\\r");
			msgSb.append(getPreBlank(blankCount)).append("send \"").append(mkdirSB).append("\"\n");
		}
	}
	
	/**
	 *   上传configuration/document/package/sql脚本
	 * mirror -R test/file aaa/bbb 批量文件夹上传
	 * put  /home/test.war /agent_package/test/
	 * rsync -P --rsh=ssh /uploadTemp/test.tar root@192.168.1.190:/home/test.tar
	 * 
	 * @param tblServerInfo
	 * @param tblSystemDeploy
	 * @param type
	 * @param blankCount
	 * @param msgSb
	 */
	private void assembleSpawnPUT(String uploadPath, String folderStr, int blankCount, StringBuilder msgSb) {
//		String remoteAddr = addSlash(vo.getFtpPath(), "/|\\\\", "/", true);
		String filePath = "mirror -R " + uploadPath + " " + folderStr;
		msgSb.append(getPreBlank(blankCount)).append("send \"").append(filePath).append("\\r\"\n");
	}
	
	/**
	 * 脚本处理结果成功或者失败返回判断
	 * @param blankCount
	 * @param msgSb
	 */
	private void expectSuccessStatus(int blankCount, StringBuilder msgSb) {
		msgSb.append(getPreBlank(blankCount)).append("if { \\\\$successStatus == 0 } {\n");
		msgSb.append(getPreBlank(blankCount + 1)).append("puts \"").append(BUILD_DEPLOY_FAILURE).append("\"").append("\n");
		msgSb.append(getPreBlank(blankCount + 1)).append("exit 1").append("\n");
		msgSb.append(getPreBlank(blankCount)).append("}").append("\n");
		msgSb.append(getPreBlank(blankCount)).append("exit").append("\n");
	}
	
	/**
	 * 连接SFTP
	 * sftp ftpuser@10.1.11.190
	 * lftp -u ftpuser sftp://10.1.11.190
	 * @param tblServerInfo
	 * @param tblSystemDeployScript
	 * @param blankCount
	 * @param msgSb
	 */
	private void assembleSpawnSFTP(TblServerInfo tblServerInfo, int blankCount, StringBuilder msgSb) {
		String username = tblServerInfo.getSshUserAccount();
//		String userIP = username + "@" + tblServerInfo.getIp();
		String userIP = "sftp://" + tblServerInfo.getIp();
//		msgSb.append(getPreBlank(blankCount)).append("spawn sftp ").append(userIP).append("\n");
		msgSb.append(getPreBlank(blankCount)).append("spawn lftp -u ").append(username).append(" ").append(userIP).append("\n");
	}
	
}
