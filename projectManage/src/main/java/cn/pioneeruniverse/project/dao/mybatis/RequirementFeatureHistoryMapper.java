package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.project.entity.TblRequirementFeatureHistory;

/**
 *
 * @ClassName:RequirementFeatureHistoryMapper
 * @Description:开发任务历史mapper
 * @author author
 * @date 2020年8月19日
 *
 */
public interface RequirementFeatureHistoryMapper extends BaseMapper<TblRequirementFeatureHistory> {
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * 新增
	 * @param record
	 * @return Integer
	 */

	Integer insert(TblRequirementFeatureHistory record);

	/**
	 * 新增
	 * @param record
	 * @return int
	 */

	int insertSelective(TblRequirementFeatureHistory record);

	/**
	 * 查询
	 * @param id
	 * @return TblRequirementFeatureHistory
	 */

	TblRequirementFeatureHistory selectByPrimaryKey(Long id);

	/**
	 * 更新
	 * @param record
	 * @return int
	 */

	int updateByPrimaryKeySelective(TblRequirementFeatureHistory record);

	int updateByPrimaryKey(TblRequirementFeatureHistory record);

	List<TblRequirementFeatureHistory> getFeatureHistoryWorkloadList(Map<String, String> map);
}