package cn.pioneeruniverse.dev.service.tool;

import cn.pioneeruniverse.dev.entity.TblToolInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2018/10/29
 * Time: 下午 7:28
 */
public interface ToolService {

    Map<String,Object> findList();

	void updateTool(TblToolInfo tblToolInfo, HttpServletRequest request);

    void insert(TblToolInfo toolInfo, HttpServletRequest request);

    void removeTool(Long id, HttpServletRequest request);

    TblToolInfo getToolEntity(Long toolId);
}
