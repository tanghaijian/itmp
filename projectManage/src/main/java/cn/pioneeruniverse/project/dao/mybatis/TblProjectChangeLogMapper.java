package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblProjectChangeLog;
/**
 *
 * @ClassName:TblProjectChangeLogMapper
 * @Description
 * @author author
 * @date 2020年8月27日
 *
 */
public interface TblProjectChangeLogMapper {
	/**
	 * 新增
	 * @param log
	 */

	void insertLog(TblProjectChangeLog log);

	/**
	 * 根据id查询
	 * @param id
	 * @return List<TblProjectChangeLog>
	 */

	List<TblProjectChangeLog> getChangeLog(Long id);

}
