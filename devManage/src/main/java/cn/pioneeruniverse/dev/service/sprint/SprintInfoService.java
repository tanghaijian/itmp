package cn.pioneeruniverse.dev.service.sprint;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.dev.entity.TblSprintInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import org.apache.ibatis.annotations.Param;

public interface SprintInfoService {
	/**
	 *@author liushan
	 *@Description 获取系统下拉框的数据
	 *@Date 2020/8/3
	 * @param
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.TblSystemInfo>
	 **/
	List<TblSystemInfo> getAllSystem();
	/**
	 *@author liushan
	 *@Description 冲刺管理列表显示
	 *@Date 2020/8/3
	 * @param sprintName 冲刺名称
	 * @param uid 用户id
	 * @param systemIds 系统ids
	 * @param validStatus 状态 1=有效；2=无效
	 * @param roleCodes 角色权限
	 * @param page 分页
	 * @param rows
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	 **/
	List<TblSprintInfo> getSprintInfos(String sprintName, Long uid, String systemIds, Integer validStatus, List<String> roleCodes, Integer page, Integer rows);
	/**
	 *@author liushan
	 *@Description 删除冲刺任务
	 *@Date 2020/8/3
	 * @param request
	 * @param sprintIdList 需要删除的冲刺ids
	 *@return void
	 **/
	void deleteSprintInfo(HttpServletRequest request, String sprintIdList);
	/**
	 *@author liushan
	 *@Description 新增冲刺任务
	 *@Date 2020/8/3
	 * @param sprintInfo 新增冲刺数据
	 * @param request
	 *@return void
	 **/
	void addSprintInfo(TblSprintInfo sprintInfo, HttpServletRequest request);
	/**
	 *@author liushan
	 *@Description 编辑数据回显
	 *@Date 2020/8/3
	 * @param id 冲刺id
	 *@return cn.pioneeruniverse.dev.entity.TblSprintInfo
	 **/
	TblSprintInfo getSprintInfoById(Long id);
	/**
	 *@author liushan
	 *@Description 编辑任务
	 *@Date 2020/8/3
	 * @param sprintInfo
	 * @param request
	 *@return void
	 **/
	void updateSprintInfo(TblSprintInfo sprintInfo, HttpServletRequest request);

	/**
	 *@author liushan
	 *@Description 分页总数
	 *@Date 2020/8/3  参数同上
	 * @param sprintName
	 * @param uid
	 * @param systemIds
	 * @param validStatus
	 * @param roleCodes
	 *@return java.lang.Integer
	 **/
	Integer getSprintInfosCount(String sprintName, Long uid, String systemIds, Integer validStatus,List<String> roleCodes);

	/**
	 *@author liushan
	 *@Description 关闭任务
	 *@Date 2020/8/3
	 * @param id 冲刺id
	 * @param request
	 *@return void
	 **/
	void closeSprint(String id, HttpServletRequest request);

	/**
	 *@author liushan
	 *@Description 冲刺列表数据
	 *@Date 2020/8/3
	 * @param sprintName 冲刺名称
	 * @param uid 用户id
	 * @param systemName 子系统名称
	 * @param roleCodes 角色权限
	 * @param pageNum 分页
	 * @param pageSize
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	 **/
	List<TblSprintInfo> getAllSprints(String sprintName, Long uid, String systemName, List<String> roleCodes,
			Integer pageNum, Integer pageSize);

	/**
	*@author liushan
	*@Description 冲刺列表数据 分页总数
	*@Date 2020/8/3
	 * @param sprintName
 * @param uid
 * @param systemName
 * @param roleCodes
	*@return java.lang.Integer
	**/
	Integer getAllSprinsCount(String sprintName, Long uid, String systemName, List<String> roleCodes);

	/**
	*@author liushan
	*@Description 开启冲刺
	*@Date 2020/8/3
	 * @param id 冲刺id
 * @param request
	*@return void
	**/
	void openSprint(String id, HttpServletRequest request);

	/**
	 *  根据冲刺id获取该冲刺相关信息
	 * @param sprintIdLists
	 * @return
	 */
	TblSprintInfo selectSprintInfoById(@Param("sprintIdLists") List<Object> sprintIdLists);

	/**
	*@author liushan
	*@Description 更新冲刺任务预估剩余时间
	*@Date 2020/8/3
	 * @param map
	*@return void
	**/
    void updateSprintWorkLoad(Map<String, Object> map);
}
