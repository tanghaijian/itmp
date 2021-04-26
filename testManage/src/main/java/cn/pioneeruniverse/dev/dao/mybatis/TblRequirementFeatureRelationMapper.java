package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureRelation;
import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface TblRequirementFeatureRelationMapper extends BaseMapper<TblRequirementFeatureRelation> {
    int deleteByPrimaryKey(Long id);

    int insertNew(TblRequirementFeatureRelation record);

    int insertSelective(TblRequirementFeatureRelation record);

    TblRequirementFeatureRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblRequirementFeatureRelation record);

    int updateByPrimaryKey(TblRequirementFeatureRelation record);
}