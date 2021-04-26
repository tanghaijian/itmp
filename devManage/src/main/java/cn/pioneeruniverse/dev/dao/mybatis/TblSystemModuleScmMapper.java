package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemModuleScm;
import cn.pioneeruniverse.dev.entity.TblSystemScm;

/**
 * 模块源码配置接dao接口
 */
public interface TblSystemModuleScmMapper extends BaseMapper<TblSystemModuleScm> {
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

	int insertSelective(TblSystemModuleScm record);

    TblSystemModuleScm selectByPrimaryKey(Long id);

	/**
	 * 更新
	 * @param record
	 * @return
	 */

	int updateByPrimaryKeySelective(TblSystemModuleScm record);

    int updateByPrimaryKey(TblSystemModuleScm record);
    List<TblSystemModuleScm> getModuleScmByParam(Map<String,Object> map);

	/**
	 * 根据主键查询modulescm
	 * @param id
	 * @return
	 */

	List<TblSystemModuleScm> findModuleScm(Long id);

	void delete(Long systemModuleId);

	void insertModuleScm(TblSystemModuleScm moduleScm);

	/**
	 * 通过系统id删除
	 * @param id
	 */

	void deleteBySystemId(Long id);

	void updateNo(Long systemModuleId);

	void updateYes(TblSystemModuleScm moduleScm);

	List<TblSystemModuleScm> judge(Map<String, Object> map);

	void deleteBySystemScmId(TblSystemScm tblSystemScm);

	void updateModuleStatusBySystemId(Map<String, Object> map);

	void updateModuleScm(Map<String, Object> moduleScmid);


	/**
	 * 根据系统id获取modulescm信息
	 * @param systemId
	 * @return
	 */
	List<TblSystemModuleScm> getModuleScmBySystemId(Long systemId);
	void deleteModuleScmById(TblSystemModuleScm modelScm);
	void insertModuleScm1(TblSystemModuleScm modelScm);
	void updateModuleScm1(TblSystemModuleScm modelScm);

	/**
	 * 通过环境获取modulescm
	 * @param modelScm
	 * @return
	 */
    Integer findModuleScmByEnvironmentType(TblSystemModuleScm modelScm);

	/**
	 * 判断是否存在
	 * @param modelScm
	 * @return
	 */
	TblSystemModuleScm findModuleScmDoesItExist(TblSystemModuleScm modelScm);
	List<TblSystemModuleScm> findModuleScmBySystemId(Long systemId);

}