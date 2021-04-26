package cn.pioneeruniverse.dev.service.tool.impl;


import cn.pioneeruniverse.common.constants.DicConstants;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.TblToolInfoMapper;
import cn.pioneeruniverse.dev.entity.TblToolInfo;
import cn.pioneeruniverse.dev.service.tool.ToolService;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;


/**
 * Description:
 * Author:liushan
 * Date: 2018/10/29
 * Time: 下午 7:28
 */
@Transactional(readOnly = true)
@Service("toolService")
public class ToolServiceImpl extends ServiceImpl<TblToolInfoMapper,TblToolInfo> implements ToolService {



    @Autowired
    private TblToolInfoMapper toolInfoMapper;
    
    @Autowired
	RedisUtils redisUtils;

    /**
     * @deprecated 获取工具列表
     * @return Map<String, Object>
     */

    @Override
    public Map<String, Object> findList() {
        Map<String, Object> resultMap = new HashMap<>();
        Object functionType = redisUtils.get(DicConstants.DEV_FUNCTION_TYPE);
        Object toolType = redisUtils.get(DicConstants.DEV_TOOL_TYPE);
        Map<String, Object> functionMaps = new LinkedHashMap<>();
        functionMaps = JSON.parseObject(functionType.toString());
        Map<String, Object> typeMap = JSON.parseObject(toolType.toString());
        Map<String, Object> resultType = new HashMap<>();

        for (Map.Entry<String, Object> entry : functionMaps.entrySet()) {
            for(Map.Entry<String, Object> type : typeMap.entrySet()){
                Map<String,Object> param = new HashMap<>();
                param.put("function",entry.getKey());
                param.put("type",type.getKey());
                List<TblToolInfo> typelist =  toolInfoMapper.findType(param);
                if (typelist != null && typelist.size() != 0){
                    resultType.put(type.getValue().toString(),typelist);
                }
            }
            String functionEntry = entry.getValue().toString()+","+entry.getKey();
            resultMap.put(functionEntry,resultType);
            resultType = new HashMap<>();
        }
        return resultMap;
    }

    /**
     * @deprecated 更新工具
     * @param tblToolInfo 工具信息
     * @param request
     */

    @Transactional(readOnly = false)
	@Override
	public void updateTool(TblToolInfo tblToolInfo, HttpServletRequest request) {
		tblToolInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblToolInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		toolInfoMapper.updateTool(tblToolInfo);
	}

    /**
     * @deprecated 新增工具
     * @param toolInfo
     * @param request
     */
    @Transactional(readOnly = false)
    @Override
    public void insert(TblToolInfo toolInfo, HttpServletRequest request) {
        toolInfo = (TblToolInfo) CommonUtil.setBaseValue(toolInfo,request);
        toolInfo.setStatus(1);
        toolInfoMapper.insertTool(toolInfo);
    }

    /**
     * 逻辑删除工具配置信息
     * @param id
     * @param request
     */
    @Transactional(readOnly = false)
    @Override
    public void removeTool(Long id, HttpServletRequest request) {
        TblToolInfo toolInfo = new TblToolInfo();
        if(id != null){
            toolInfo.setId(id);
            toolInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
            toolInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
            toolInfo.setStatus(2);
            toolInfoMapper.updateTool(toolInfo);
        }
    }

    @Override
    public TblToolInfo getToolEntity(Long toolId) {
        return toolInfoMapper.getToolEntity(toolId);
    }

}
