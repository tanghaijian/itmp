package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.dev.vo.common.request.JenkinsJobBuildStateQuery;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemJenkins;

public interface TblSystemJenkinsMapper extends BaseMapper<TblSystemJenkins> {
//	int deleteByPrimaryKey(Long id);
//
//	int insertSelective(TblSystemJenkins record);

	TblSystemJenkins selectByPrimaryKey(Long id);
	List<TblSystemJenkins> selectBySystemId(Long id);
	List<TblSystemJenkins> selectByParam(Map<String, Object> map);
	List<TblSystemJenkins> selectByParamManual(Map<String, Object> columnMap);
	List<TblSystemJenkins> selectCornJenkinsByMap(Map<String, Object> systemJenkinsParam);
	Integer insertNew(TblSystemJenkins tblSystemJenkins);

    List<TblSystemJenkins> selectBreakName(Map<String, Object> param);
	List<TblSystemJenkins> selectSortJobByMap(Map<String, Object> param);

	/**
	 * 根据条件查询 TblSystemJenkins
	 * @param query
	 * @return
	 */
	List<TblSystemJenkins> findByBuildStatusQuery(JenkinsJobBuildStateQuery query);
}