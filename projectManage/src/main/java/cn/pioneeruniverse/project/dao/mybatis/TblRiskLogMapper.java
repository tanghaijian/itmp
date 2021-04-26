package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblRiskLog;
/**
 *
 * @ClassName:TblRiskLogMapper
 * @Description 风险日志mapper
 * @author author
 * @date 2020年8月24日
 *
 */
public interface TblRiskLogMapper {

	void insertLog(TblRiskLog log);

	/**
	 * 查询
	 * @param id
	 * @return List<TblRiskLog>
	 */

	List<TblRiskLog> getRiskLog(Long id);

}
