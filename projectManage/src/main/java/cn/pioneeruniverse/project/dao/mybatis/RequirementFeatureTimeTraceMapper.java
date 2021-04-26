package cn.pioneeruniverse.project.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.pioneeruniverse.project.entity.TblRequirementFeatureTimeTrace;


public interface RequirementFeatureTimeTraceMapper extends BaseMapper<TblRequirementFeatureTimeTrace>{

    /**
    * @author author
    * @Description 增加时间追踪表数据
    * @Date 2020/9/16
    * @param record
    * @return java.lang.Integer
    **/
    Integer insertFeatureTimeTrace(TblRequirementFeatureTimeTrace record);
}