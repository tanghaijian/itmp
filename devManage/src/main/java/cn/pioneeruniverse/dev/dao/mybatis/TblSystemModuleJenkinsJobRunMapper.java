package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemModuleJenkinsJobRun;
import org.apache.ibatis.annotations.Param;

public interface TblSystemModuleJenkinsJobRunMapper extends BaseMapper<TblSystemModuleJenkinsJobRun> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblSystemModuleJenkinsJobRun record);

    TblSystemModuleJenkinsJobRun selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemModuleJenkinsJobRun record);

    int updateByPrimaryKey(TblSystemModuleJenkinsJobRun record);
    List<TblSystemModuleJenkinsJobRun> selectLastTimeByModuleId(Long id);
    List<TblSystemModuleJenkinsJobRun> selectSonarBySystemidAndDate(Map<String, Object> map);

	List<TblSystemModuleJenkinsJobRun> selectSonarBySystemidAndDateMincro(Map<String, Object> map);

	List<Map<String, Object>> selectModuleJobRunByjobRunId(long jobRunId);

	List<TblSystemModuleJenkinsJobRun> selectSonarBySystemidAndDateManual(Map<String, Object> mapParam);

	List<Map<String, Object>> selectModuleJobRunByjobRunIdManual(long id);

	void updateErrorInfo(TblSystemModuleJenkinsJobRun tblSystemModuleJenkinsJobRun);
	
	// 查询系统最近一次代码扫描问题总览
	List<TblSystemModuleJenkinsJobRun> findNewModuleCountBySystemId(@Param(value="systemId")Long systemId,
          @Param(value="systemModuleId")Long systemModuleId);
	// 查询系统最近7日代码扫描问题趋势
	List<TblSystemModuleJenkinsJobRun> find7DayModuleTrendBySystemId(@Param(value="systemId")Long systemId,
         @Param(value="systemModuleId")Long systemModuleId);

	List<TblSystemModuleJenkinsJobRun> selectSonarBySystemidAndDateMincroDeploy(Map<String, Object> mapParam);

	List<TblSystemModuleJenkinsJobRun> selectSonarBySystemidAndDateDeploy(Map<String, Object> mapParam);

	Integer insertNew(TblSystemModuleJenkinsJobRun tblSystemModuleJenkinsJobRun);

    String selectModulesNamesByRunId(Long jobRunId);
}