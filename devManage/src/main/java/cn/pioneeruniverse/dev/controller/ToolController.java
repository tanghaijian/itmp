package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.entity.TblToolInfo;
import cn.pioneeruniverse.dev.service.tool.ToolService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: tool controller
 * Author:liushan
 * Date: 2018/10/29
 * Time: 下午 7:27
 */
@RestController
@RequestMapping(value = "tool")
public class ToolController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(ToolController.class);

    @Autowired
    private ToolService toolService;
    
    @Autowired
	RedisUtils redisUtils;


    /**
    *@author liushan
    *@Description 获取工具配置实体类信息
    *@Date 2020/7/21
    *@Param [toolId]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value = "getToolEntity",method = RequestMethod.POST)
    public Map<String,Object> getToolEntity(Long toolId){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try{
            TblToolInfo toolInfo = toolService.getToolEntity(toolId);
            result.put("toolInfo",toolInfo);
        }catch(Exception e){
            return handleException(e,"操作失败");
        }
        return result;
    }

    /**
     *  获取工具列表
     * @return Map<String, Object> status=1正常返回，2异常返回
     */
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public Map<String,Object> list(){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try{
            result  = toolService.findList();
        }catch(Exception e){
            return handleException(e,"获取工具信息失败");
        }
        return result;
    }

    /**
     * 修改操作
     * @param tblToolInfo 工具实体类
     * @param request token信息
     * @return Map<String, Object> status=1正常返回，2异常返回
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Map<String,Object> updateTool(@RequestBody TblToolInfo tblToolInfo,HttpServletRequest request){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try{
            toolService.updateTool(tblToolInfo,request);
            tblToolInfo = toolService.getToolEntity(tblToolInfo.getId());
            Integer toolType = tblToolInfo.getToolType();
            if (toolType != null && toolType == 4) {//Jenkins
            	redisUtils.removePattern(Constants.Jenkins.STAGE_VIEW + "*");//修改工具配置，移除日志频繁查询redis
            }
        }catch(Exception e){
            return handleException(e,"修改工具信息失败");
        }
        return result;
    }

    /**
     * 添加工具配置信息
     * @param toolInfo 接收实体类参数
     * @param request token信息
     * @return Map<String, Object> status=1正常返回，2异常返回
     */
    @RequestMapping(value = "insert",method = RequestMethod.POST)
    public Map<String,Object> insert(@RequestBody TblToolInfo toolInfo,HttpServletRequest request){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try{
            toolService.insert(toolInfo,request);
        }catch(Exception e){
            return handleException(e,"新增工具信息失败");
        }
        return result;
    }

    /**
     * 逻辑删除配置信息
     * @param id 工具id
     * @param request
     * @return Map<String, Object> status=1正常返回，2异常返回
     */
    @RequestMapping(value = "removeTool",method = RequestMethod.POST)
    public Map<String,Object> removeTool(Long id,HttpServletRequest request){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try{
            toolService.removeTool(id,request);
        }catch(Exception e){
            return handleException(e,"新增工具信息失败");
        }
        return result;
    }


}
