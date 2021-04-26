package cn.pioneeruniverse.common.utils;

import cn.pioneeruniverse.common.entity.JiraDevelopmentVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component("JiraUtil")
@Lazy
public class JiraUtil {

    private static String jiraUrl;
    private static String jiraUsername;
    private static String jiraPassword;

    public final static Logger logger = LoggerFactory.getLogger(JiraUtil.class);

    @Value("${jira.url}")
    public void setJiraUrl(String param) {
        jiraUrl = param;
    }

    @Value("${jira.username}")
    public void setJiraUsername(String param) {
        jiraUsername = param;
    }

    @Value("${jira.password}")
    public void setJiraPassword(String param) {
        jiraPassword = param;
    }

    public static String getJiraUrl() {
        return jiraUrl;
    }

    public static String getJiraUsername() {
        return jiraUsername;
    }

    public static String getJiraPassword() {
        return jiraPassword;
    }

    /**
     *  自定义url语句
     * @param url
     * @return
     */
    public static Object getCustomUrl(String url){
        try {
            HttpResponse<JsonNode> response = Unirest.get(url)
                .basicAuth(jiraUsername,jiraPassword)
                .header("Accept", "application/json")
                .asJson();
            return  response.getBody();
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 获取仪表板
     * @param url
     * @return
     */
    public static String getDashboard(String url){
        try {
            HttpResponse<JsonNode> response = Unirest.get(url+"/rest/api/2/dashboard")
                .basicAuth(jiraUsername,jiraPassword)
                .header("Accept", "application/json")
                .asJson();
            return  response.getBody().toString();
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    /**
     *  取得问题
     * @param url
     * @param issueIdOrKey
     * @return
     */
    public static String getIssue(String url,String issueIdOrKey){
        try {
            HttpResponse<JsonNode> response = Unirest.get(url+issueIdOrKey)   //  /rest/api/2/issue/{issueIdOrKey}
                    .basicAuth(jiraUsername,jiraPassword)
                    .header("Accept", "application/json")
                    .asJson();
            return  response.getBody().toString();
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 获取附件文件
     * @param id
     * @return
     */
    public static String getAccessory(String id){
        try {
            HttpResponse<JsonNode> response = Unirest.get(jiraUrl+"/rest/api/2/attachment/"+id)
                .basicAuth(jiraUsername,jiraPassword)
                .header("Accept", "application/json")
                .asJson();
            JSONObject accessoryJSON = JSON.parseObject(response.getBody().toString());
            return  accessoryJSON.get("content").toString();
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    /**
     *  根据问题url获取附件
     * @param fields
     */
    public static Map[] getAll(String fields){
        JSONObject jsonObject = JSON.parseObject(fields);
        Object field = jsonObject.get("attachment");
        System.err.println("fields"+field);
        JSONArray objects = JSON.parseArray(field.toString());
        System.err.println("fields"+objects);
        Map[] maps = new Map[objects.size()];
        for (int i =0;i<objects.size();i++){
            Map<String,Object> map = new HashMap<>();
            String id = objects.getJSONObject(i).get("id").toString();
            String fileName = objects.getJSONObject(i).get("filename").toString();
            if (id != null ){
                String accessory = JiraUtil.getAccessory(id);
                logger.info("id:"+id+"的附件content为："+accessory);
                map.put("fileName",fileName);
                map.put("accessory",accessory);
                maps[i] = map;
            }
        }
        return maps;
    }

    public static JiraDevelopmentVO jiraDemo(String id){
        Object customUrl = JiraUtil.getCustomUrl(jiraUrl + "/rest/api/2/issue/"+ id);//获得问题
        logger.info("DataSource："+customUrl);
        Object fields = JSON.parseObject(customUrl.toString()).get("fields");
        JSONObject jsonObject = JSON.parseObject(fields.toString());

        JiraDevelopmentVO jiraDevelopmentVO = new JiraDevelopmentVO();
        jiraDevelopmentVO.setFeatureCode("jira"+id);
        //任务名称
        if (jsonObject.get("summary") != null){
            String summary = jsonObject.get("summary").toString();
            jiraDevelopmentVO.setFeatureName(summary);
        }
        //任务描述
        if (jsonObject.get("description") != null){
            String description = jsonObject.get("description").toString();
            jiraDevelopmentVO.setFeatureOverview(description);
        }
        //任务优先级
        if (jsonObject.get("priority") != null){
            JSONObject priorityJson = JSON.parseObject(jsonObject.get("priority").toString());
            String priority = priorityJson.get("id").toString();
            jiraDevelopmentVO.setRequirementFeaturePriority(Integer.valueOf(priority));
        }
        //systemReqID
        if (jsonObject.get("customfield_10326") != null){
            String systemReqID = jsonObject.get("customfield_10326").toString();
            jiraDevelopmentVO.setSystemReqID(systemReqID);
        }
        //经办人
        if(jsonObject.get("assignee") != null){
            JSONObject userJson = JSON.parseObject(jsonObject.get("assignee").toString());
            String userCode = userJson.get("key").toString();
            jiraDevelopmentVO.setUserCode(userCode);
        }
        //模块
        if (jsonObject.get("customfield_10329") != null){
            JSONObject moduleJson = JSON.parseObject(jsonObject.get("customfield_10329").toString());
            String moduleName = moduleJson.get("value").toString();
            jiraDevelopmentVO.setModuleName(moduleName);
            if(moduleJson.get("child") != null){
                Object child = moduleJson.get("child");
                JSONObject childJson = JSON.parseObject(child.toString());
                String moduleName1 = childJson.get("value").toString();
                jiraDevelopmentVO.setModuleName1(moduleName1);
            }
        }
        //项目小组
        if(jsonObject.get("customfield_10328") != null){
            JSONObject projectGroupId = JSON.parseObject(jsonObject.get("customfield_10328").toString());
            String projectGroupIdName = projectGroupId.get("value").toString();
            jiraDevelopmentVO.setProjectGroupName(projectGroupIdName);
        }
        //预计开始日期
        if (jsonObject.get("customfield_10324") != null){
            String expectStartedDate = jsonObject.get("customfield_10324").toString();
            jiraDevelopmentVO.setPlanStartDate(DateUtil.getDate(expectStartedDate,DateUtil.format));
        }
        //预计结束日期
        if (jsonObject.get("customfield_10323") != null){
            String expectFinishDate = jsonObject.get("customfield_10323").toString();
            jiraDevelopmentVO.setPlanEndDate(DateUtil.getDate(expectFinishDate,DateUtil.format));
        }
        //修复的发布版本
        if(jsonObject.get("fixVersions") != null){
            JSONArray fixVersionsArray = JSON.parseArray(jsonObject.get("fixVersions").toString());
            if(fixVersionsArray != null&&fixVersionsArray.size()>0 ){
                String fixVersions = fixVersionsArray.getJSONObject(0).get("name").toString();
                jiraDevelopmentVO.setRepairSystemVersioName(fixVersions);
            }
        }
        //工作量
        if(jsonObject.get("timetracking") != null){
            JSONObject timetrac = JSON.parseObject(jsonObject.get("timetracking").toString());
            String estimateWorkload = timetrac.get("originalEstimateSeconds").toString();       //预计工作量
            String estimateRemainWorkload = timetrac.get("remainingEstimateSeconds").toString(); //剩余工作量
            jiraDevelopmentVO.setEstimateWorkload(JiraUtil.getEstimate(estimateWorkload));
            jiraDevelopmentVO.setEstimateRemainWorkload(JiraUtil.getEstimate(estimateRemainWorkload));
        }
        //附件
        Map[] maps = JiraUtil.getAll(fields.toString());  //获取附件
        jiraDevelopmentVO.setMaps(maps);
        return jiraDevelopmentVO;
    }

    /**
    *@author liushan
    *@Description 问题id获取所有附件
    *@Date 2020/5/11
    *@Param [id]
    *@return java.util.Map[]
    **/
    public static Map<String,Map[]> jiraFileDemo(String id){
        Map<String,Map[]>  map = new HashMap<>();
        Object customUrl = JiraUtil.getCustomUrl(jiraUrl + "/rest/api/2/issue/"+ id);//获得问题
        logger.info("DataSource："+customUrl);
        JSONObject jsonObject = JSON.parseObject(customUrl.toString());
        Object fields = jsonObject.get("fields");
        System.err.println("fields"+fields);

        Map[] maps = new Map[2];
        maps[0] = new HashMap(){{
            put("key",jsonObject.get("key").toString());
        }};
        maps[1] = new HashMap(){{
            put("customUrl",jsonObject.toJSONString());
        }};
        map.put("customUrl",maps);

        //附件
        map.put("files",JiraUtil.getAll(fields.toString()));
        return map;  //获取附件
    }

    public static Double getEstimate(String estimateString){
        Integer dt = 8 * 60 * 60;
        Integer estimate = Integer.valueOf(estimateString);
        double day = new BigDecimal((float)estimate/dt).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return day;
    }

    public static void main(String[] args){
       // Map<String,Map[]> customUrl = JiraUtil.jiraFileDemo("CCIC-16383");//获得问题
        //JSONObject jsonObject = JSON.parseObject(fields.toString());

        /*if (jsonObject.get("customfield_10329") != null){
            JSONObject moduleJson = JSON.parseObject(jsonObject.get("customfield_10329").toString());
            String moduleName = moduleJson.get("value").toString();
            Object child = moduleJson.get("child");
            JSONObject childJson = JSON.parseObject(child.toString());
            String moduleName1 = childJson.get("value").toString();
            System.out.println(moduleName);
            System.out.println(moduleName1);
        }
        String [] version = "pkg/1.3.1B01".split("/");
        for(String s:version){
            System.out.println(s);
        }*/
    }

}
