package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemDeployScript;
/**
 *
 * @ClassName: TblSystemDeployScriptMapper
 * @Description: 系统部署脚本mapper
 * @author author
 * @date 2020年8月12日 下午16:20
 *
 */
public interface TblSystemDeployScriptMapper extends BaseMapper<TblSystemDeployScript>{
    int deleteByPrimaryKey(Long id);
    
    void deleteByBatchIds(@Param("list") List<Long> list);

    Integer insertScript(TblSystemDeployScript record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TblSystemDeployScript record);
    
    List<TblSystemDeployScript> selectByDeployId(@Param("systemDeployId") Long systemDeployId);

    /**
     * 查询步骤
     * @param systemDeployId
     * @return
     */
	List<TblSystemDeployScript> selectScriptOrder(Long systemDeployId);

}