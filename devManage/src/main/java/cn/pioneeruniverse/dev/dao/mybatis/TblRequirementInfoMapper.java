package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblRequirementInfo;

/**
 *
 * @ClassName: TblRequirementInfoMapper
 * @Description: 需求dao接口
 * @author author
 *
 */
public interface TblRequirementInfoMapper extends BaseMapper<TblRequirementInfo> {
	/**
	 * 删除
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Long id);

	/**
	 * 插入
	 * @param record
	 * @return
	 */

	int insertSelective(TblRequirementInfo record);

	/**
	 * 查询
	 * @param id
	 * @return
	 */

    TblRequirementInfo selectByPrimaryKey(Long id);

	/**
	 * 更新
	 * @param record
	 * @return
	 */

	int updateByPrimaryKeySelective(TblRequirementInfo record);

    int updateByPrimaryKey(TblRequirementInfo record);

	/**
	 * 获取所有的需求
	 * @return List<TblRequirementInfo>
	 */

	List<TblRequirementInfo> getAllRequirement();

	/**
	 * 获取用户所关联需求
	 * @param userId
	 * @return
	 */
	
	List<TblRequirementInfo> findRequirementByUser(Long userId);
	
	/**
	*@author author
	*@Description 条件获取需求
	*@Date 2020/8/18
	 * @param record
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementInfo>
	**/
	List<TblRequirementInfo> getRequirement(TblRequirementInfo record);

	/**
	 * 获取需求名称
	 * @param requirementId
	 * @return
	 */

	String getReqName(Long requirementId);
	
	/**
	*@author author
	*@Description 根据需求id 查询需求
	*@Date 2020/8/17
	 * @param reqId
	*@return cn.pioneeruniverse.dev.entity.TblRequirementInfo
	**/
	TblRequirementInfo findRequirementById(Long reqId);
	
	/**
	*@author author
	*@Description 根据需求编号查询需求
	*@Date 2020/8/17
	 * @param reqCode
	*@return cn.pioneeruniverse.dev.entity.TblRequirementInfo
	**/
	TblRequirementInfo findRequirementByCode(String reqCode);
	
	/**
	*@author author
	*@Description 根据 开发任务id 批量查询需求
	*@Date 2020/8/17
	 * @param featureIds
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementInfo>
	**/
	List<TblRequirementInfo> findRequirementByFeatureIds(Long []featureIds);
	
	/**
	*@author author
	*@Description 获取该需求所在系统所在项目的项目管理岗
	*@Date 2020/8/17
	 * @param requirementId
	*@return java.lang.String
	**/
	String getProManageUserIds(Long requirementId);

}