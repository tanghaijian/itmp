package cn.pioneeruniverse.dev.dao.mybatis;


import cn.pioneeruniverse.dev.entity.TblSystemDeployScriptTemplate;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 *
 * @ClassName: TblSystemDeployScriptTemplateMapper
 * @Description: 部署脚本模板mapper
 * @author author
 * @date 2020年8月12日 下午16:20
 *
 */
public interface TblSystemDeployScriptTemplateMapper extends BaseMapper<TblSystemDeployScriptTemplate>{
    int deleteByPrimaryKey(Long id);

    void deleteByBatchIds(@Param("list") List<Long> list);

    Integer insertScriptTemplate(TblSystemDeployScriptTemplate record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TblSystemDeployScriptTemplate record);



}