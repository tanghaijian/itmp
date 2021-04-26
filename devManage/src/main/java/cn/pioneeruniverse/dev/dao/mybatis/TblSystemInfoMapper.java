package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.vo.TaskProjectVO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface TblSystemInfoMapper extends BaseMapper<TblSystemInfo> {

    //获取项目信息
    List<TaskProjectVO> getProjectListByProjectName(Map<String, Object> map);
    //获取冲刺下的项目信息
    List<TaskProjectVO> getProjectListBySprint(Map<String, Object> map);

    //获取项目信息非管理员
    List<TaskProjectVO> getProjectListByNoSystem(Map<String, Object> map);


    int deleteByPrimaryKey(Long id);

    int insertSelective(TblSystemInfo record);

    List<TblSystemInfo> selectAll();

    TblSystemInfo selectByPrimaryKey(Long id);

    TblSystemInfo selectByPage(Long pageSize);

    int updateByPrimaryKeySelective(TblSystemInfo record);

    int updateByPrimaryKey(TblSystemInfo record);

    List<Map<String, Object>> getAllSystemInfo(Map<String, Object> map);

    TblSystemInfo getOneSystemInfo(Long id);

    List<TblDataDic> findEnvironmentType(Map<String, Object> map);

    void updateSystemInfo(TblSystemInfo systemInfo);

    TblSystemInfo findById(Long id);

    TblSystemInfo getSystemByCode(String systemCode);

    void insertSystem(TblSystemInfo systemInfo);

    void updateSystemById(TblSystemInfo systemInfo);

    List<Map<String, Object>> getAllSystemInfoByBuild(Map<String, Object> map);

    List<TblSystemInfo> getAllsystem(TblSystemInfo system);

    String getSystemName(Long systemId);

    List<Map<String, Object>> FindSystemPackage(Map<String, Object> map);

    List<Map<String, Object>> getAllSystemInfoByBuilding(Map<String, Object> systeminfoMap);

    String getUserName(String userId);

    String getBatchUserName(@Param("list") List<String> list);

    List<Long> findSystemIdByUserId(Long id);

    int getAllSystemInfoTotal(Map<String, Object> systemInfo);

    List<TblSystemInfo> findSystemWithProjectByUserId(Map<String, Object> map);

    int CountFindSystemWithProjectByUserId(Map<String, Object> map);

    /**
    *@author liushan
    *@Description 根据项目id 查询system
    *@Date 2020/8/4
     * @param projectId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblSystemInfo>
    **/
    List<TblSystemInfo> getSystemByPId(Long projectId);

    List<Map<String, Object>> getAllSystemInfoCondition(Map<String, Object> map);

    Boolean judgeSystemNeedCodeReview(@Param("systemId") Long systemId);

    void configEnvironment(Map<String, Object> map);

    String getEnvBySystemId(Long systemId);

    List<TblSystemInfo> findSystemByProject(Long userId);

    //获取用户所在项目组对应的系统
    List<Map<String, Object>> getMySystemList(Long userId);

    List<TblSystemInfo> getSyetemByNameOrCode(@Param("systemName") String systemName,
                                              @Param("systemCode") String systemCode);
    void insertItmpSystem(TblSystemInfo systemInfo);

    void insertTmpSystem(TblSystemInfo systemInfo);

    void updateTmpSystem(TblSystemInfo systemInfo);

	String getEnvTypeById(Long id);

	List<Map<String, Object>> getSystems();

	List<String> getUserRoles(Long currentUserId);

    List<TblSystemInfo> getSystemsByUserId(long userId);

    /**
    *@author liushan
    *@Description 根据系统id查询系统信息
    *@Date 2019/11/20
    *@Param [systemId]
    *@return cn.pioneeruniverse.dev.entity.TblSystemInfo
    **/
    TblSystemInfo getTblSystemInfoById(Long systemId);
    
    Set<String> getGruupId(Long systemId);
    
    List<String> getModuleGruupId(Long systemId); 
    
    Set<String> getArtifactId(Long systemId);
    
    List<String> getModuleArtifactId(Long systemId);

    List<String> getAllSystemInfoTopPage(Map<String, Object> map);

    List<String> getAllSystemInfoConditionTopPage(Map<String, Object> map);
}