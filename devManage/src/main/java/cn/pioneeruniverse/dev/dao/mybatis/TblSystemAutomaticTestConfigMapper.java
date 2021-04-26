package cn.pioneeruniverse.dev.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemAutomaticTestConfig;
/**
 *
 * @ClassName: TblSystemAutomaticTestConfigMapper
 * @Description: 自动化测试配置mapper
 * @author author
 *
 */
public interface TblSystemAutomaticTestConfigMapper extends BaseMapper<TblSystemAutomaticTestConfig> {
	int deleteByPrimaryKey(Long id);

	/**
	 * 新增
	 * @param record
	 * @return
	 */
	Integer insertAutoTest(TblSystemAutomaticTestConfig record);

	int insertSelective(TblSystemAutomaticTestConfig record);

	/**
	 * 查询
	 * @param id
	 * @return TblSystemAutomaticTestConfig
	 */
	TblSystemAutomaticTestConfig selectByPrimaryKey(Long id);

	/**
	 * 更新
	 * @param record
	 * @return
	 */

	int updateByPrimaryKeySelective(TblSystemAutomaticTestConfig record);

	int updateByPrimaryKey(TblSystemAutomaticTestConfig record);
}