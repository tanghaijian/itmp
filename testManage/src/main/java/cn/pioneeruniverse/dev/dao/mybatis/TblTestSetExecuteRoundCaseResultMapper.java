package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.tblTestSetExecuteRoundCaseResult;
public interface TblTestSetExecuteRoundCaseResultMapper extends BaseMapper<tblTestSetExecuteRoundCaseResult>{
	 List<Map<String, Object>> selectByPrimaryKey(Map<String, Object> map);
	 
	 int updateSelective(Map<String, Object> map);
	 
	 int insertSelective(Map<String, Object> map);
	 //批量
	 int updateRoundResult(Map<String, Object> map);
	 
	 int insertResltPass(Map<String, Object> map); 
}
