package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.entity.TblRequirementSystem;
/**
 *
 * @ClassName: RequirmentSystemMapper
 * @Description: 需求系统mapper
 * @author author
 *
 */
public interface RequirmentSystemMapper {
	/**
	 * 插入
	 * @param requirementSystem
	 */

	void insertReqSystem(TblRequirementSystem requirementSystem);

	/**
	 * 更新
	 * @param requirementSystem
	 */

	void updateReqSystem(TblRequirementSystem requirementSystem);

	TblRequirementSystem selectReqSystemByReqSystem(TblRequirementSystem requirementSystem);

	void deleteReqSystem(Long requirementId);

	int getFunctionCountByReqId(Long requirementId);

	List<Long> getSystemByrequirementId(Long rId);

	List<String> getSystemNameByIds(List<Long> sysIds);

	Long getAssetSystemIdByRequirementId(Long rId);

	String getAssetSystemNameByAssetSystemId(Long assetSystemId);

	void updateReqAssSystemTree(TblRequirementInfo requirementInfo);

	List<TblRequirementSystem> getReqSystemByReqId(Long id);

	String getSystemNameBySystemId(Long systemId);

	String getSystemCodeBySystemId(Long systemId);

	List<String> getsystems(Long id);
}
