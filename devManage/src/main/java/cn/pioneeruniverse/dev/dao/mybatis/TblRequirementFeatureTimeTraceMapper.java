package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.Date;
import java.util.List;

import cn.pioneeruniverse.dev.entity.TblRequirementFeatureTimeTraceVo;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblRequirementFeatureTimeTrace;

public interface TblRequirementFeatureTimeTraceMapper  extends BaseMapper<TblRequirementFeatureTimeTrace>{
    int deleteByPrimaryKey(Long id);

    Integer insertFeatureTimeTrace(TblRequirementFeatureTimeTrace record);

    int insertSelective(TblRequirementFeatureTimeTrace record);

    TblRequirementFeatureTimeTrace selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblRequirementFeatureTimeTrace record);

    int updateByPrimaryKey(TblRequirementFeatureTimeTrace record);

	void updateDevTaskCreateTime(TblRequirementFeatureTimeTrace timeTrace);

	void updategetCodeFirstCommitTime(TblRequirementFeatureTimeTrace timeTrace);

	void updateReqFeatureDevCompleteTime(TblRequirementFeatureTimeTrace timeTrace);

	void updateReqFeatureFirstTestDeployTime(TblRequirementFeatureTimeTrace timeTrace);

	void updateReqFeatureTestingTime(TblRequirementFeatureTimeTrace timeTrace);

	void updateReqFeatureTestCompleteTime(TblRequirementFeatureTimeTrace timeTrace);

	void updateReqFeatureLastProdTime(TblRequirementFeatureTimeTrace timeTrace);

	int updateReqFeatureProdCompleteTime(TblRequirementFeatureTimeTrace timeTrace);

	TblRequirementFeatureTimeTrace selectByReqFeatureId(Long requirementFeatureId);

	List<Date> findDateById(Long id);
	
	List <TblRequirementFeatureTimeTraceVo> selectBySystemId1(Long systemId);

	List <TblRequirementFeatureTimeTrace> selectBySystemId2(Long systemId);

	/*List <TblRequirementFeatureTimeTrace> selectBySystemId(@Param(value="day")Long day,
			@Param(value="systemId")Long systemId);*/
}