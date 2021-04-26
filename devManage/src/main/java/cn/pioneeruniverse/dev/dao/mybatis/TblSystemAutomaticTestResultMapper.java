package cn.pioneeruniverse.dev.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemAutomaticTestResult;
/**
 *
 * @ClassName: TblSystemAutomaticTestResultMapper
 * @Description: 自动化测试结果mapper
 * @author author
 *
 */
public interface TblSystemAutomaticTestResultMapper extends BaseMapper<TblSystemAutomaticTestResult> {
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Long id);

	Integer insert(TblSystemAutomaticTestResult record);

	int insertSelective(TblSystemAutomaticTestResult record);

	/**
	 * 查询
	 * @param id
	 * @return TblSystemAutomaticTestResult
	 */
	TblSystemAutomaticTestResult selectByPrimaryKey(Long id);

	/**
	 * 更新
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(TblSystemAutomaticTestResult record);

	int updateByPrimaryKey(TblSystemAutomaticTestResult record);

    void insertNew(TblSystemAutomaticTestResult tblSystemAutomaticTestResult);
}