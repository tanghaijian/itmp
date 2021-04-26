package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.*;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TblDefectInfoMapper extends BaseMapper<TblDefectInfo> {
    /**
    *@author liushan
    *@Description 删除缺陷
    *@Date 2020/7/31
     * @param id
    *@return int
    **/
    int deleteByPrimaryKey(Long id);

    /**
    *@author liushan
    *@Description 查询缺陷
    *@Date 2020/7/31
     * @param id
    *@return cn.pioneeruniverse.dev.entity.TblDefectInfo
    **/
    TblDefectInfo selectByPrimaryKey(Long id);

    /**
    *@author liushan
    *@Description 编辑缺陷
    *@Date 2020/7/31
     * @param record
    *@return int
    **/
    int updateByPrimaryKeySelective(TblDefectInfo record);

    /**
    *@author liushan
    *@Description 缺陷缺陷
    *@Date 2020/7/31
     * @param record
    *@return int
    **/
    int updateByPrimaryKey(TblDefectInfo record);

    /**
    *@author liushan
    *@Description 获取需求
    *@Date 2020/7/31
     * @param map
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementInfo>
    **/
    List<TblRequirementInfo> getAllRequirement(Map<String, Object> map);

    /**
    *@author liushan
    *@Description 添加缺陷
    *@Date 2020/7/31
     * @param tblDefectInfo
    *@return void
    **/
    void insertDefect(TblDefectInfo tblDefectInfo);

    /**
    *@author liushan
    *@Description 查询最大的缺陷编号
    *@Date 2020/7/31
     * @param 
    *@return java.lang.String
    **/
    String selectMaxDefectCode();

    /**
    *@author liushan
    *@Description 编辑缺陷
    *@Date 2020/7/31
     * @param tblDefectInfo
    *@return void
    **/
    void updateDefect(TblDefectInfo tblDefectInfo);

    /**
    *@author liushan
    *@Description 编辑缺陷提交人
    *@Date 2020/7/31
     * @param tblDefectInfo
    *@return void
    **/
    void updateDefectAssignUser(TblDefectInfo tblDefectInfo);

    /**
     *@author liushan
     *@Description 编辑缺陷驳回原因
     *@Date 2020/7/31
     * @param tblDefectInfo
     *@return void
     **/
    void updateDefectRejectReason(TblDefectInfo tblDefectInfo);

    /**
     *@author liushan
     *@Description 编辑缺陷状态
     *@Date 2020/7/31
     * @param tblDefectInfo
     *@return void
     **/
    void updateDefectStatus(TblDefectInfo tblDefectInfo);

    /**
     *@author liushan
     *@Description 编辑缺陷解决情况
     *@Date 2020/7/31
     * @param tblDefectInfo
     *@return void
     **/
    void updateDefectSolveStatus(TblDefectInfo tblDefectInfo);

    /**
    *@author liushan
    *@Description 获取用户名
    *@Date 2020/7/31
     * @param id
    *@return java.lang.String
    **/
    String getUserNameById(Long id);

    /**
    *@author liushan
    *@Description 获取缺陷数据
    *@Date 2020/7/31
     * @param id
    *@return cn.pioneeruniverse.dev.entity.TblDefectInfo
    **/
    TblDefectInfo getDefectById(Long id);

    /**
    *@author liushan
    *@Description 移除缺陷
    *@Date 2020/7/31
     * @param id
    *@return void
    **/
    void removeDefect(Long id);

    /**
    *@author liushan
    *@Description 查询缺陷列表
    *@Date 2020/7/31
     * @param tblDefectInfo
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectInfo>
    **/
    List<TblDefectInfo> findDefectList(DefectInfoVo tblDefectInfo);

    /**
    *@author liushan
    *@Description 编辑缺陷 修改开发管理平台的参数
    *@Date 2020/7/31
     * @param map
    *@return void
    **/
    void updateDevDefect(Map<String,Object> map);

    /**
    *@author liushan
    *@Description 删除缺陷
    *@Date 2020/7/31
     * @param id
    *@return void
    **/
    void deleteDefectById(Long id);

    /**
    *@author liushan
    *@Description 缺陷数量
    *@Date 2020/7/31
     * @param tblDefectInfo
    *@return int
    **/
    int countFindDefectList(DefectInfoVo tblDefectInfo);
    
    /**
    *@author liushan
    *@Description 查询缺陷根据转派人
    *@Date 2020/7/31
     * @param userId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectInfo>
    **/
    List<TblDefectInfo> findDefectByAssignUserId(Long userId);

    /**
    *@author liushan
    *@Description 查询缺陷
    *@Date 2020/7/31
     * @param code
    *@return cn.pioneeruniverse.dev.entity.TblDefectInfo
    **/
    TblDefectInfo getDefectByCode(String code);

    /**
    *@author liushan
    *@Description 查询缺陷
    *@Date 2020/7/31
     * @param defectId
    *@return cn.pioneeruniverse.dev.entity.TblDefectInfo
    **/
    TblDefectInfo getDefectEntity(Long defectId);

    /**
    *@author liushan
    *@Description 自定义字段
    *@Date 2020/7/31
     * @param id
 * @param fieldName
    *@return java.lang.String
    **/
    String getDafectFieldTemplateById(@Param("id")Long id, @Param("fieldName")String fieldName);

    /**
    *@author liushan
    *@Description 修改投产窗口
    *@Date 2020/7/31
     * @param requirementFeature
    *@return void
    **/
	void updateCommssioningWindowId(TblRequirementFeature requirementFeature);

	/**
	*@author liushan
	*@Description 修改投产窗口
	*@Date 2020/7/31
	 * @param id 缺陷id
 * @param commissioningWindowId 
	*@return void
	**/
    void updateCommssioningWindowId1(@Param("id")Long id,
                                     @Param("commissioningWindowId")Long commissioningWindowId);
    /**
    *@author liushan
    *@Description 关闭缺陷
    *@Date 2020/7/31
     * @param id
    *@return void
    **/
    void closeDefect(Long id);

    /**
    *@author liushan
    *@Description 查询缺陷
    *@Date 2020/7/31
     * @param id
    *@return cn.pioneeruniverse.dev.entity.TblDefectInfo
    **/
    TblDefectInfo findDefectById(Long id);

    /**
    *@author liushan
    *@Description 获取开发任务系统id
    *@Date 2020/7/31
     * @param id
    *@return cn.pioneeruniverse.dev.entity.TblDevTask
    **/
    TblDevTask selectRequirementFeatureById(Long id);

    /**
    *@author liushan
    *@Description 根据缺陷id获取项目id
    *@Date 2020/7/31
     * @param id
    *@return java.lang.Long
    **/
    Long getDefectInfoById(Long id);
}