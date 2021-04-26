package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblProjectInfo;

public interface TblProjectInfoMapper extends BaseMapper<TblProjectInfo> {




    int deleteByPrimaryKey(Long id);

    int insertSelective(TblProjectInfo record);

    TblProjectInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblProjectInfo record);

    int updateByPrimaryKey(TblProjectInfo record);
    
    List<TblProjectInfo> selectAllProject();
    //获取用户所参与的所有项目
    List<TblProjectInfo> findProjectByUser(Long userId);
    //获取项目管理岗所属项目
    //List<TblProjectInfo> findProjectByManagerUser(Long userId);

	List<TblProjectInfo> selectAllProjectByUser(Long currentUserId);
	
	/**
	*@author liushan
	*@Description 看板  根据当前登录用户获取所在项目
	*@Date 2020/8/4
	 * @param uid
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblProjectInfo>
	**/ 
	List<TblProjectInfo> getAllProjectByUid(Long uid);
    List<Map<String, Object>>  getProjectGroupByUid(Map<String, Object> map) ;

    List<Map<String, Object>> getProjectGroupBySystemId(Map<String, Object> mapParam);

    /**
    *@author liushan
    *@Description 根据项目获取项目小组
    *@Date 2020/8/4
     * @param mapParam
    *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String,Object>> getProjectGroupByProjectId(Map<String, Object> mapParam);
    List<Map<String,Object>> getUserIdByProjectId(Map<String, Object> mapParam);


    List<Map<String, Object>> getProjectGroupByName(Map<String, Object> projectLiteparam);

    List<TblProjectInfo> findProjectByName(String projectPart);

	List<Map<String, Object>> getProjectGroupByProjectIds(Long projectIds);
	
	List<TblProjectInfo> getProjectListBySystemId(Long systemId);

    TblProjectInfo getProjectBySystemId(Long systemId);

    List<TblProjectInfo> getProjectListBySystem(String projectName);

    List<TblProjectInfo> getProjectListByNoSystem(Map<String,Object> map);

    List<TblSystemInfo> getSystemByProjectId(Long projectId);

    List<Long> findProjectGroupIdsByProjectId(long id);

    List<Long> findUserId(HashMap<String, Object> map1);
}