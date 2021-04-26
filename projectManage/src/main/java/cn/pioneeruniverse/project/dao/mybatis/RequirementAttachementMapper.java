package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.pioneeruniverse.project.entity.TblRequirementAttachement;

public interface RequirementAttachementMapper extends BaseMapper<TblRequirementAttachement>{
	   
    /**
    * @author author
    * @Description 根据需求id查询附件
    * @Date 2020/9/15
    * @param id
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblRequirementAttachement>
    **/
    List<TblRequirementAttachement> getRequirementAttachement(Long id);
    
    /**
    * @author author
    * @Description 新增
    * @Date 2020/9/15
    * @param record
    * @return int
    **/
    int insertSelective(TblRequirementAttachement record);
    
    /**
    * @author author
    * @Description 逻辑删除附件
    * @Date 2020/9/15
    * @param record
    * @return void
    **/
    void removeAttachement(TblRequirementAttachement record);
    
}