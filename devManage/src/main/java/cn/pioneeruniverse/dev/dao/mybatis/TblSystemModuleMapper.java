package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemModule;

/**
 * 模块表dao接口
 */

public interface TblSystemModuleMapper extends BaseMapper<TblSystemModule> {
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

    int insertSelective(TblSystemModule record);

    TblSystemModule selectByPrimaryKey(Long id);

    /**
     * 更新
     * @param record
     * @return
     */

    int updateByPrimaryKeySelective(TblSystemModule record);

    int updateByPrimaryKey(TblSystemModule record);

	List<Map<String, Object>> getSystemModuleBySystemId(Long id);

    /**
     * 根据系统id查询其子系统
     * @param systemid
     * @return
     */
	
	List<TblSystemModule> selectSystemModule(Long systemid);

	Map<String, Object> getOneSystemModule(Long id);

	void insertModule(TblSystemModule systemModule);

    /**
     * 根据规则排序module
     * @param result
     * @return
     */

	List<TblSystemModule> sortModule(Map<String, Object> result);


}