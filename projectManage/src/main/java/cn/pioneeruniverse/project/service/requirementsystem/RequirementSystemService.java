package cn.pioneeruniverse.project.service.requirementsystem;
/**
 *
 * @ClassName:RequirementSystemService
 * @Description:需求关联service
 * @author author
 * @date 2020年8月19日
 *
 */

public interface RequirementSystemService {
	/**
	 *
	 * @Title: updateReqSystemData
	 * @Description: 更新需求和系统关联关系
	 * @author author
	 * @param reqSystem 需求关联系统列表
	 * @param reqId 需求id
	 */
	void updateReqSystemData(String reqSystem, String reqId);
	/**
	 *
	 * @Title: getFunctionCountByReqId
	 * @Description: 获取功能点数
	 * @author author
	 * @param requirementId 需求id
	 * @return
	 */
	int getFunctionCountByReqId(Long requirementId);
}
