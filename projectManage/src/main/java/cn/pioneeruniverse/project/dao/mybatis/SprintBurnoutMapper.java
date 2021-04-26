package cn.pioneeruniverse.project.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.project.entity.TblSprintBurnout;
/**
 *
 * @ClassName:SprintBurnoutMapper
 * @Description 冲刺燃尽mapper
 * @author author
 * @date 2020年8月25日
 *
 */
public interface SprintBurnoutMapper extends BaseMapper<TblSprintBurnout> {
	/**
	 * 删除
	 * @param id
	 * @return int
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * 新增
	 * @param record
	 * @return
	 */

	Integer insert(TblSprintBurnout record);

	int insertSelective(TblSprintBurnout record);

	/**
	 * 查询
	 * @param id
	 * @return TblSprintBurnout
	 */

	TblSprintBurnout selectByPrimaryKey(Long id);

	/**
	 * 更新
	 * @param record
	 * @return int
	 */

	int updateByPrimaryKeySelective(TblSprintBurnout record);

	int updateByPrimaryKey(TblSprintBurnout record);
}