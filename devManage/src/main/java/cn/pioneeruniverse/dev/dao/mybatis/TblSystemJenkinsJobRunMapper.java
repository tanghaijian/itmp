package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblDevTask;
import cn.pioneeruniverse.dev.entity.TblSystemJenkinsJobRun;
import cn.pioneeruniverse.dev.entity.TblSystemVersion;

public interface TblSystemJenkinsJobRunMapper extends BaseMapper<TblSystemJenkinsJobRun> {
	// Integer insert(TblSystemJenkinsJobRun record);
	/**
	 * @deprecated 删除TblSystemJenkinsJobRun
	 * @author weiji
	 *
	 */
	int deleteByPrimaryKey(Long id);
	/**
	 * @deprecated 插入
	 * @author weiji
	 *
	 */
	int insertSelective(TblSystemJenkinsJobRun record);
	/**
	 * @deprecated 删除根据id
	 * @author weiji
	 *
	 */

	TblSystemJenkinsJobRun selectByPrimaryKey(Long id);
	/**
	 * @deprecated 更新根据参数
	 * @author weiji
	 *
	 */
	int updateByPrimaryKeySelective(TblSystemJenkinsJobRun record);
	/**
	 * @deprecated 删除根据主键
	 * @author weiji
	 *
	 */
	int updateByPrimaryKey(TblSystemJenkinsJobRun record);
	/**
	 * @deprecated 查询次系统下最新一条记录部署记录
	 * @author weiji
	 *
	 */

	List<TblSystemJenkinsJobRun> selectLastTimeBySystemId(Long id);

	List<TblSystemJenkinsJobRun> selectNowTimeBySystemId(Long id);

	List<TblSystemJenkinsJobRun> selectLastTimeByJobName(String jobName);

	List<TblSystemJenkinsJobRun> selectByParam(Map<String, Object> map);
	/**
	 * @deprecated 获取成功或失败的jobrun信息
	 * @author weiji
	 *
	 */
	List<Map<String, Object>> selectMessageBySystemId(Map<String, Object> map);
	/**
	 * @deprecated 获取成功或失败的jobrun信息（分页）
	 * @author weiji
	 *
	 */
	List<Map<String, Object>> selectMessageBySystemIdAndPage(Map<String, Object> map);
	/**
	 * @deprecated 更新
	 * @author weiji
	 *
	 */
	void updateJobRun(TblSystemJenkinsJobRun record);
	/**
	 * @deprecated 查询信息
	 * @author weiji
	 *
	 */
	List<Map<String, Object>> selectModuleBuildMessage(Map<String, Object> map);
	/**
	 * @deprecated 获取最新的子模块构建或部落信息
	 * @author weiji
	 *
	 */
	List<Map<String, Object>> selectModuleBuildMessageNow(Map<String, Object> map);
	/**
	 * @deprecated 获取子模块最新20条记录
	 * @author weiji
	 *
	 */
	List<Map<String, Object>> selectModuleBuildMessagesNow(Map<String, Object> map);
	/**
	 * @deprecated 获取手动信息
	 * @author weiji
	 *
	 */
	List<TblSystemJenkinsJobRun> selectLastTimeBySystemIdManual(Long id);
	/**
	 * @deprecated 获取手动信息
	 * @author weiji
	 *
	 */
	List<TblSystemJenkinsJobRun> selectLastTimeBySystemIdManualDeploy(Long id);

	List<TblSystemJenkinsJobRun> getErrorStructure(Map<String, Object> paramMap);
	/**
	 * @deprecated
	 * @author weiji
	 *
	 */
	List<TblSystemJenkinsJobRun> getAutoErrorStructure(Map<String, Object> paramMap);
	/**
	 * @deprecated
	 * @author weiji
	 *
	 */
	List<TblSystemJenkinsJobRun> selectByMapLimit(Map<String, Object> runMap);
	
	// 根据用户查询所关联的构建日志
	List<TblSystemJenkinsJobRun> findJenkinsJobRunByUser(Long userId);
	// 查询系统最近14天每天的构建次数
	List<TblSystemJenkinsJobRun> find14DayJenkinsCountBySystemId(Long systemId);
	
	// 查询系统最近14天每天的构建时间
	List<TblSystemJenkinsJobRun> find14DayJenkinsMinuteBySystemId(Long systemId);
	// 查询系统最近5条构建记录
	List<Map<String, Object>> findTop5JenkinsBySystemId(Long systemId);
	// 查询系统最近N天的构建数量
	TblSystemJenkinsJobRun findJenkinsCountBySystemId(@Param(value="systemId")Long systemId,
			@Param(value="day")Long day);
	
	// 查询系统最近7天的构建情况
	List<TblSystemJenkinsJobRun> find7DayJenkinsBySysyemId(Long projectId);
	
	// 查询该条构建是否是该系统下的最新一条自动构建记录
	Long findTop1AutomaticJenkinsBySystemId(Map<String, Object> map);
	// 查询该条构建是否是该系统下的最新一条手动构建记录
	Long findTop1CustomJenkinsBySystemId(Map<String, Object> map);

	List<TblSystemJenkinsJobRun> selectAutoDeployLastTimeBySystemId(Long id);

	List<Map<String, Object>> selectModuleBuildMessagesNowAutoDeploy(Map<String, Object> moduleParam);

	List<Map<String, Object>> selectModuleBuildMessageAutoDeploy(Map<String, Object> moduleParam);

	List<Map<String, Object>> selectMessageBySystemIdAndPageAutoDeploy(Map<String, Object> mapParam);

	Integer insertNew(TblSystemJenkinsJobRun tblSystemJenkinsJobRun);

	List<TblSystemJenkinsJobRun> selectAutoDeployLastTimeBySystemIdArtifact(Long id);

	List<TblSystemJenkinsJobRun> getAutoBreakeErrorStructure(Map<String, Object> paramMap);

	List<TblSystemJenkinsJobRun> getErrorBreakeStructure(Map<String, Object> paramMap);

    List<Map<String, Object>> getEnvName(Map<String, Object> paramMap);

	List<Map<String, Object>> getDeleteEnvName(Map<String, Object> param);

    List<Map<String, Object>> getModuleInfoIng(Map<String, Object> moduleParam);

	List<Map<String, Object>> selectModuleRunInfo(Map<String, Object> moduleParam);

    List<Map<String, Object>> selectModuleBuildMessagesNowAutoDeployNew(Map<String, Object> moduleParam);

    List<Map<String, Object>> selectModuleBuildMessageAutoDeployNew(Map<String, Object> moduleParam);

    List<Map<String, Object>> selectModuleRunInfos(Map<String, Object> modulesParam);

	List<Map<String, Object>> getModuleInfoIngs(Map<String, Object> modulesParam);

	List<Map<String, Object>> selectModuleBuildMessagesNowAutoDeployNews(Map<String, Object> moduleParam);

	List<Map<String, Object>> selectModuleBuildMessageAutoDeployNews(Map<String, Object> moduleParam);

    List<Map<String, Object>> selectModuleBuildMessageAutoDeployNewsByModuleId(Map<String, Object> moduleParam);

	List<Map<String, Object>> selectModuleRunInfosByModuleId(Map<String, Object> modulesParam);
}