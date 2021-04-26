package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.entity.JiraDevelopmentVO;
import cn.pioneeruniverse.common.utils.JiraUtil;
import cn.pioneeruniverse.dev.service.JiraWorkTask.IJiraWorkTaskService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 
* @ClassName: JiraWorkTaskController
* @Description: jira操作controller
* @author author
* @date 2020年8月16日 下午7:12:23
*
 */
@RestController
@RequestMapping("jiraWorkTask")
public class JiraWorkTaskController extends BaseController {

    @Autowired
    private IJiraWorkTaskService jiraWorkTaskService;
    @Autowired
    private JiraUtil jiraUtil;

   
    /**
     * 
    * @Title: insertJiraWorkTask
    * @Description: 开发任务同步jira数据
    * @author author
    * @param request
    * @return Map<String,Object>
     */
    @RequestMapping("jiraWorkTaskMethod")
    public Map<String,Object> insertJiraWorkTask(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try{
            String customUrlTotal = jiraUtil.getCustomUrl(jiraUtil.getJiraUrl() + "/rest/api/2/search?maxResults=1&startAt=0").toString();//获得所有问题
            logger.info("total:"+JSON.parseObject(customUrlTotal).get("total"));  //总数
            Integer total = Integer.valueOf(JSON.parseObject(customUrlTotal).get("total").toString());
            JiraDevelopmentVO jiraDevelopmentVO = jiraUtil.jiraDemo("29257");
            jiraDevelopmentVO.setSystemCode("JARASYN");
            jiraWorkTaskService.synJiraWorkTask(jiraDevelopmentVO,request);
            /*for(int j=0;j<total;j = j + 1000){
                String customUr = JiraUtil.getCustomUrl(JiraUtil.JIRA_URL + "/rest/api/2/search?maxResults=1000&startAt="+j).toString();//获得所有问题
                JSONArray issues = JSON.parseArray(JSON.parseObject(customUr).get("issues").toString());
                for (int i =0 ;i<issues.size();i++){
                    String id = issues.getJSONObject(i).get("id").toString();   //当前问题id
                    String fields = issues.getJSONObject(i).get("fields").toString();//获取当前问题
                    JSONObject jsonObject = JSON.parseObject(JSON.parseObject(fields).get("issuetype").toString());
                    String type = jsonObject.get("name").toString();   //获取问题中的类型
                    if (type.equals("Development")){   //如果类型是Development则存储该数据
                        JiraDevelopmentVO jiraDevelopmentVO = JiraWorkTaskUtil.jiraDemo(id);
                        jiraDevelopmentVO.setSystemCode("JARASYN");
                        jiraWorkTaskService.synJiraWorkTask(jiraDevelopmentVO,request);
                    }
                }
            }*/
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        }catch (Exception e){
            return super.handleException(e, "获取数据失败！");
        }
        return map;
    }

}
