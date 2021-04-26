package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.dev.dto.AssetSystemTreeDTO;
import cn.pioneeruniverse.dev.entity.TblCustomFieldTemplate;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblProjectInfo;
import cn.pioneeruniverse.dev.entity.TblReportMonthlySystem;
import cn.pioneeruniverse.dev.entity.TblRequirementInfo;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlySystemData;
import cn.pioneeruniverse.dev.vo.DefectInfoVo;
import cn.pioneeruniverse.dev.vo.DefectInputInfoVo;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TblDefectInfoMapper extends BaseMapper<TblDefectInfo> {

    /**
    *@author author
    *@Description 删除缺陷
    *@Date 2020/8/19
     * @param id 缺陷id
    *@return int
    **/
    int deleteByPrimaryKey(Long id);

    /**
    *@author author
    *@Description 根据id查询
   *@Date 2020/8/19
     * @param id
    *@return cn.pioneeruniverse.dev.entity.TblDefectInfo
    **/
    TblDefectInfo selectByPrimaryKey(Long id);

    /**
    *@author author
    *@Description 修改缺陷
    *@Date 2020/8/19
     * @param record
    *@return int
    **/
    int updateByPrimaryKeySelective(TblDefectInfo record);

    /**
    *@author author
    *@Description 判断修改缺陷
    *@Date 2020/8/19
     * @param record
    *@return int
    **/
    int updateByPrimaryKey(TblDefectInfo record);

    /**
    *@author author
    *@Description 获取需求
    *@Date 2020/8/20
     * @param map
    *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> getAllRequirement(Map<String, Object> map);

    /**
    *@author author
    *@Description 新增缺陷
    *@Date 2020/8/20
     * @param tblDefectInfo
    *@return void
    **/
    void insertDefect(TblDefectInfo tblDefectInfo);

    /**
    *@author author
    *@Description 查询最大的缺陷编号
    *@Date 2020/8/20
     * @param 
    *@return java.lang.String
    **/
    String selectMaxDefectCode();

    /**
    *@author author
    *@Description 修改缺陷
    *@Date 2020/8/20
     * @param tblDefectInfo
    *@return void
    **/
    void updateDefect(TblDefectInfo tblDefectInfo);

    /**
    *@author author
    *@Description 待确认状态:转交操作，只是修改指派人
    *@Date 2020/8/20
     * @param tblDefectInfo
    *@return void
    **/
    void updateDefectAssignUser(TblDefectInfo tblDefectInfo);

    /**
    *@author author
    *@Description 待确认状态：驳回操作，状态变为拒绝 
    *@Date 2020/8/20
     * @param tblDefectInfo
    *@return void
    **/
    void updateDefectRejectReason(TblDefectInfo tblDefectInfo);

    /**
    *@author author
    *@Description 修改缺陷状态
    *@Date 2020/8/20
     * @param tblDefectInfo
    *@return void
    **/
    void updateDefectStatus(TblDefectInfo tblDefectInfo);

    /**
    *@author author
    *@Description 保存解决信息
    *@Date 2020/8/20
     * @param tblDefectInfo
    *@return void
    **/
    void updateDefectSolveStatus(TblDefectInfo tblDefectInfo);

    /**
    *@author author
    *@Description 获取用户名
    *@Date 2020/8/20
     * @param id
    *@return java.lang.String
    **/
    String getUserNameById(Long id);

    /**
    *@author author
    *@Description 根据id获取缺陷
    *@Date 2020/8/20
     * @param id
    *@return cn.pioneeruniverse.dev.entity.TblDefectInfo
    **/
    TblDefectInfo getDefectById(Long id);

    /**
    *@author author
    *@Description 移除缺陷
    *@Date 2020/8/20
     * @param id
    *@return void
    **/
    void removeDefect(Long id);

    /**
    *@author author
    *@Description 查询缺陷列表
    *@Date 2020/8/20
     * @param tblDefectInfo
 * @param pageNum
 * @param rows
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectInfo>
    **/
    List<TblDefectInfo> findDefectList( @Param("defect") DefectInfoVo tblDefectInfo, @Param("page") Object pageNum,  @Param("rows") Object rows);
    
    /**
    *@author author
    *@Description 查询缺陷列表
    *@Date 2020/8/20
     * @param defectInputInfoVo
 * @param pageNum
 * @param rows
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectInfo>
    **/
    List<TblDefectInfo> findDefectListBySql ( @Param("defect") DefectInputInfoVo defectInputInfoVo, @Param("page") Object pageNum,  @Param("rows") Object rows);

    /**
    *@author author
    *@Description 非管理员查询缺陷列表
    *@Date 2020/8/20
     * @param tblDefectInfo
 * @param pageNum
 * @param rows
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectInfo>
    **/
    List<TblDefectInfo> findDefectListCondition( @Param("defect") DefectInfoVo tblDefectInfo, @Param("page") Object pageNum,  @Param("rows") Object rows);

    /**
    *@author author
    *@Description 非管理员搜索(拼接sql)
     *@Date 2020/8/20
     * @param defectInputInfoVo
 * @param pageNum
 * @param rows
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectInfo>
    **/
    List<TblDefectInfo> findDefectListConditionBySql ( @Param("defect") DefectInputInfoVo defectInputInfoVo, @Param("page") Object pageNum,  @Param("rows") Object rows);
    
    /**
    *@author author
    *@Description xml 没有该方法
    *@Date 2020/8/20
     * @param tblDefectInfo
    *@return void
    **/
    void insertDevDefect(TblDefectInfo tblDefectInfo);

    /**
    *@author author
    *@Description 删除缺陷
    *@Date 2020/8/20
     * @param id
    *@return java.lang.Integer
    **/
    Integer deleteDefectById(Long id);
    
    /**
    *@author author
    *@Description 根据测试案例ID查询
    *@Date 2020/8/20
     * @param testCaseExecuteId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectInfo>
    **/
    List<TblDefectInfo> selectBytestCaseId(Long testCaseExecuteId);

    /**
    *@author author
    *@Description 缺陷列表总数
    *@Date 2020/8/20
     * @param tblDefectInfo
    *@return int
    **/
    int countFindDefectList( @Param("defect") DefectInfoVo tblDefectInfo);
    
    /**
    *@author author
    *@Description 缺陷列表总数  listWhereBySql
    *@Date 2020/8/20
     * @param defectInputInfoVo
    *@return int
    **/
    int countFindDefectListBySql(@Param("defect") DefectInputInfoVo defectInputInfoVo);

    /**
    *@author author
    *@Description 非管理员统计缺陷列表
    *@Date 2020/8/20
     * @param tblDefectInfo
    *@return int
    **/
    int countFindDefectListCondition( @Param("defect") DefectInfoVo tblDefectInfo);
    
    /**
    *@author author
    *@Description  非管理员搜索(拼接sql)
     *@Date 2020/8/20
     * @param defectInputInfoVo
    *@return int
    **/
    int countFindDefectListConditionBySql(@Param("defect") DefectInputInfoVo defectInputInfoVo);

    /**
    *@author author
    *@Description 统计条件查询需求
    *@Date 2020/8/20
     * @param requirementInfo
    *@return int
    **/
    int countGetAllRequirement(TblRequirementInfo requirementInfo);

    /**
    *@author author
    *@Description 根据测试工作任务查询所关联的缺陷
    *@Date 2020/8/20
     * @param testTaskId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectInfo>
    **/
    List<TblDefectInfo> findDefectsByTestTaskId(Long testTaskId);

    /**
    *@author author
    *@Description 缺陷所有关联数据
    *@Date 2020/8/20
     * @param defectId
    *@return cn.pioneeruniverse.dev.entity.TblDefectInfo
    **/
    TblDefectInfo getDefectEntity(Long defectId);

    /**
    *@author author
    *@Description 根据当前人查询项目
    *@Date 2020/8/20
     * @param currentUserId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblProjectInfo>
    **/
    List<TblProjectInfo>  getAllProjectByCurrentUserId(Long currentUserId);

    /**
    *@author author
    *@Description  获取roject.PROJECT_STATUS != 4 的项目 
    *@Date 2020/8/20
     * @param 
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblProjectInfo>
    **/
    List<TblProjectInfo> getAllProject();

    /**
    *@author author
    *@Description 根据id 查询自定义字段 json形式 
    *@Date 2020/8/20
     * @param id
 * @param fieldName
    *@return java.lang.String
    **/
    String getDafectFieldTemplateById(@Param("id")Long id, @Param("fieldName")String fieldName);

    /**
    *@author author
    *@Description 统计 DEFECT_STATUS NOT IN (1,6)  和 测试工作任务 缺陷数
    *@Date 2020/8/20
     * @param id
    *@return java.lang.Long
    **/
	Long findDefectCount(Long id);

	/**
	*@author author
	*@Description 查询项目
	*@Date 2020/8/20
	 * @param systemId
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblProjectInfo>
	**/
	List<TblProjectInfo> getProject(Long systemId);

	/**
	*@author author
	*@Description 修改缺陷的投产窗口
	*@Date 2020/8/20
	 * @param testTaskIds
 * @param windowId
	*@return void
	**/
	void updateCommssioningWindowId(@Param("testTaskIds")String testTaskIds,@Param("windowId") Long windowId);

    void updateRequirementCode(@Param("testTaskIds")String testTaskIds,@Param("requirementCode") String requirementCode);
	/**
	*@author author
	*@Description 根据工作任务修改缺陷
	*@Date 2020/8/20
	 * @param tblDefectInfo
	*@return void
	**/
    void updateDefectByWorkId(TblDefectInfo tblDefectInfo);

    /**
    *@author author
    *@Description 根据系统查询 项目小组人员 
    *@Date 2020/8/20
     * @param systemId
    *@return java.util.List<java.lang.Long>
    **/
	List<Long> findUserIdBySystemId(Long systemId);

	/**
	*@author author
	*@Description 根据缺陷id 查询开发任务的 管理岗
	*@Date 2020/8/20
	 * @param defectId
	*@return java.lang.Long
	**/
	Long findUserIdByDefectId(Long defectId);

	/**
	*@author author
	*@Description 根据缺陷id 查询系统id
	*@Date 2020/8/20
	 * @param defectId
	*@return java.lang.Long
	**/
	Long findSystemIdByDefectId(Long defectId);
	
	/**
	*@author author
	*@Description 查询缺陷率(表格)
	*@Date 2020/8/20
	 * @param startDate 开始时间
 * @param endDate 结束时间
 * @param systemCode 系统编号
	*@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	**/
	List<Map<String, Object>> selectDefectPro(@Param("startDate")String startDate,@Param("endDate") String endDate,@Param("systemCode")String systemCode);
	
	/**
	*@author author
	*@Description 查询月缺陷率(按系统)
     *@Date 2020/8/20
	 * @param startDate 开始时间
    * @param endDate 结束时间
 * @param taskCount 工作任务数
 * @param systemCode 系统编号
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblReportMonthlySystem>
	**/
	List<TblReportMonthlySystem> selectDefectProBySystem(@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("taskCount") Integer taskCount,@Param("systemCode")String systemCode);

	/**
	*@author author
	*@Description 根据项目查询缺陷
	*@Date 2020/8/20
	 * @param startDate 开始时间
     * @param endDate 结束时间
 * @param systemIds 系统ids
 * @param taskCount
	*@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	**/
	List<Map<String, Object>> selectDefectProByProject(@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("systemIds")List<String> systemIds,@Param("taskCount") Integer taskCount);
	
	List<Map<String, Object>> selectDefectLevel(@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("systemCode")String systemCode);
	
	List<Map<String, Object>> selectWorseProject(@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("systemCode")String systemCode);
	
	List<Map<String, Object>> selectRemainDefect(@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("systemCode")String systemCode);
	
    TblDefectInfo getDefectByDefectCode(String defectCode);

    TblDefectInfo getDefectByCode(String defectCode);

    void insertSynDefect(TblDefectInfo tblDefectInfo);
    void updateSynDefect(TblDefectInfo tblDefectInfo);

    TblDefectInfo findDefectById(Long id);

    //查询tbl_custom_field_template表中字段custom_form='tbl_defect_info'中的值
    TblCustomFieldTemplate selectTblCustomFieldTemplateByTblDefectInfo();

    /**
     * 查询出所需日志字段
     **/
    TblDefectInfo getDefectByIdForLog(@Param("defectId") Long defectId);

    /**
     * 查询资产系统树表中的数据
     */
    List<AssetSystemTreeDTO> selectAssetSystemTreeAll();

    /**
     * 查询系统版本表中的数据
     * @return
     */
    List<AssetSystemTreeDTO> selectSystemVersionAll();

    /**
     *  查询数据字典表中的数据
     * @return
     */
    List<AssetSystemTreeDTO> selectDataDicAll();

	String getDevTaskFieldTemplateById(@Param("id")Long id, @Param("fieldName")String fieldName);

    /**
     *  获取系统下的所有项目
     * @param systemId
     * @return
     */
    Map getProjectIdList(Long systemId);

	List<TblDefectInfo> getDefectByCurrentUser(@Param("uid")Long uid);
	
	/**
	 * 根据时间获取每个系统的缺陷
	 * @param startDate
	 * @param endDate
	 * @param systemCode
	 * @return
	 */
	List<Map<String, Object>> getWorseDefectByTime(@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("systemCode")String systemCode);
	
	List<Map<String, Object>> getDefectTotalByTime(@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("systemCode")String systemCode);

    List<TblReportMonthlySystemData> selectMonthlyDefectProBySystem(@Param("startDate")String startDate, @Param("endDate")String endDate,
                                                                    @Param("taskCount") Integer taskCount, @Param("systemCode")String systemCode,
                                                                    @Param("userId") Long userId);

}