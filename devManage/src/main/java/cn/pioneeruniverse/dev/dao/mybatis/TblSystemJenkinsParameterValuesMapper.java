package cn.pioneeruniverse.dev.dao.mybatis;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemJenkinsParameterValues;
import feign.Param;

public interface TblSystemJenkinsParameterValuesMapper extends BaseMapper<TblSystemJenkinsParameterValues>{
	
	void insertParameterValues(Map<String, Object> map);
	
	List<TblSystemJenkinsParameterValues> selectParameterValuesById(@Param("systemJenkinsParameterId")Long systemJenkinsParameterId );

	void insertParameterValuesOne(TblSystemJenkinsParameterValues  tblSystemJenkinsParameterValues);

	void updateParameterValues(TblSystemJenkinsParameterValues  tblSystemJenkinsParameterValues);

	void deleteParameterValues(@Param("list") List<Long> list);

    List<TblSystemJenkinsParameterValues> selectParameterValuesByMap(Map<String, Object> paramValut);
}
