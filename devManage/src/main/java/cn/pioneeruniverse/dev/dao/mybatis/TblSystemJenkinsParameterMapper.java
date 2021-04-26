package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemJenkinsParameter;
import feign.Param;


public interface TblSystemJenkinsParameterMapper extends BaseMapper<TblSystemJenkinsParameter>{

	List<TblSystemJenkinsParameter> getSystemJenkinsParameterList(TblSystemJenkinsParameter systemJenkinsParameter);
	
	void addSystemJenkinsParameter(TblSystemJenkinsParameter systemJenkinsParameter);
	
	TblSystemJenkinsParameter getSystemJenkinsParameterById(@Param("id") Long systemJenkinsParameterId);

	void updateSystemJenkinsParameter(TblSystemJenkinsParameter systemJenkinsParameter);
	
	List<Map<String, Object>>  systemSystemName(Map<String, Object> map);
}


