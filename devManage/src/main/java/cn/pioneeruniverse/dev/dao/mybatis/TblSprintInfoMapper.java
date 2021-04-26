package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSprintInfo;
import org.apache.ibatis.annotations.Param;

public interface TblSprintInfoMapper extends BaseMapper<TblSprintInfo>{
    int deleteByPrimaryKey(Long id);

    Integer insert(TblSprintInfo record);

    int insertSelective(TblSprintInfo record);


    TblSprintInfo selectByPrimaryKey(Long id);

	/**
	 *  根据冲刺id获取该冲刺相关信息
	 * @param sprintIdLists
	 * @return
	 */
	TblSprintInfo selectSprintInfoById(@Param("sprintIdLists") List<Object> sprintIdLists);
	/**
	*@author liushan
	*@Description 修改数据
	*@Date 2020/8/3
	 * @param record
	*@return int
	**/
	int updateByPrimaryKeySelective(TblSprintInfo record);

	/**
	*@author liushan
	*@Description 修改数据
	*@Date 2020/8/3
	 * @param record
	*@return int
	**/
    int updateByPrimaryKey(TblSprintInfo record);

    /**
    *@author liushan
    *@Description 获取冲刺列表数据
    *@Date 2020/8/3
     * @param 
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
    **/
	List<TblSprintInfo> getAllSprint();

	/**
	*@author liushan
	*@Description 根据条件获取冲刺数据
	*@Date 2020/8/3
	 * @param map
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	**/
	List<TblSprintInfo> getSprintInfos(HashMap<String, Object> map);

	/**
	*@author liushan
	*@Description 根据条件删除冲刺数据
	*@Date 2020/8/3
	 * @param map
	*@return void
	**/
	void deleteSprintInfo(HashMap<String, Object> map);

	/**
	*@author liushan
	*@Description 添加冲刺数据
	*@Date 2020/8/3
	 * @param sprintInfo
	*@return void
	**/
	void addSprintInfo(TblSprintInfo sprintInfo);

	/**
	*@author liushan
	*@Description 冲刺数据分页总数
	*@Date 2020/8/3
	 * @param map
	*@return java.lang.Integer
	**/
	Integer getSprintInfosCount(HashMap<String, Object> map);

	/**
	*@author liushan
	*@Description 根据子系统获取冲刺数据
	*@Date 2020/8/3
	 * @param systemId
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	**/
	List<TblSprintInfo> getSprintBySystemId(Long systemId);

	/**
	*@author liushan
	*@Description 关闭冲刺
	*@Date 2020/8/3
	 * @param map
	*@return void
	**/
	void closeSprint(HashMap<String, Object> map);

	/**
	*@author liushan
	*@Description 根据条件获取冲刺数据
	*@Date 2020/8/3
	 * @param map
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	**/
	List<TblSprintInfo> getSprintInfoCondition(HashMap<String, Object> map);

	/**
	*@author liushan
	*@Description 分页总数 上述方法
	*@Date 2020/8/3
	 * @param map
	*@return java.lang.Integer
	**/
	Integer getSprintInfosCountCondition(HashMap<String, Object> map);

	/**
	*@author liushan
	*@Description 关闭冲刺
	*@Date 2020/8/3
	 * @param map
	*@return void
	**/
	void openSprint(HashMap<String, Object> map);

	/**
	*@author liushan
	*@Description 根据子系统条件获取冲刺
	*@Date 2020/8/3
	 * @param systemId
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	**/
	List<TblSprintInfo> findSprintBySystemIdDate(Long systemId);
	
	/**
	*@author liushan
	*@Description 当前日期之前的 
	*@Date 2020/8/3
	 * @param systemId
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	**/
    List<TblSprintInfo> getSprintBySystemIdBefor(Long systemId);

    /**
    *@author liushan
    *@Description 当前日期之后的
    *@Date 2020/8/3
     * @param systemId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
    **/
	List<TblSprintInfo> getSprintBySystemIdAfter(Long systemId);

	/**
	*@author liushan
	*@Description 子系统id 、名称 查询
	*@Date 2020/8/3
	 * @param tblSprintInfo
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	**/
    List<TblSprintInfo> getSprintBySystemIdAndName(TblSprintInfo tblSprintInfo);

    /**
    *@author liushan
    *@Description 根据系统id和项目id获取冲刺信息
    *@Date 2020/8/3
     * @param systemId 系统id
 * @param projectId 项目id
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
    **/
	List<TblSprintInfo> getSprintInfoListBySystemIdAndProjectId(@Param("systemId") Long systemId, @Param("projectId") Long projectId);

}