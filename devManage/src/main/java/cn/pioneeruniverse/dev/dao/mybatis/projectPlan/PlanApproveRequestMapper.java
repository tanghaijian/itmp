package cn.pioneeruniverse.dev.dao.mybatis.projectPlan;

import cn.pioneeruniverse.dev.entity.projectPlan.PlanApproveRequest;

import java.util.List;
/**
 *
 * @ClassName:PlanApproveRequestMapper
 * @Description mapper
 * @author author
 * @date 2020年8月25日
 *
 */
public interface PlanApproveRequestMapper {

    List<PlanApproveRequest> getApproveByUserId(Long userId);

    List<PlanApproveRequest> getRequestUserIdByUserId(Long userId);
}


