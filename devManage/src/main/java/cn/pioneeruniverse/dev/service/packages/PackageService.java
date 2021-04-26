package cn.pioneeruniverse.dev.service.packages;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.common.nexus.NexusSearchVO;
import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;
import cn.pioneeruniverse.dev.entity.TblArtifactInfo;
import cn.pioneeruniverse.dev.entity.TblArtifactQualityDetail;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblSystemModuleJenkinsJobRun;

public interface PackageService {
	/**
	 *
	 * @Title: findFrature
	 * @Description: 查询开发任务
	 * @author author
	 * @param tblRequirementFeature 封装的查询条件
	 * @param page 第几页
	 * @param rows 每页条数
	 * @return map key rows:返回的数据，total：返回的总数目
	 * @throws
	 */
	Map  findFrature(TblRequirementFeature tblRequirementFeature, Integer rows, Integer page);
	/**
	 *
	 * @Title: addArtifactFrature
	 * @Description: 包件标记
	 * @author author
	 * @param artifactInfoStr 包件信息
	 * @param tagStr 标记
	 * @param request
	 * @throws
	 */
	void addArtifactFrature(String artifactInfoStr,String tagStr,HttpServletRequest request);
	/**
	 * @author author
	 * @Description 查询包件信息
	 * @Date 2020/9/15
	 * @param nexusSearchVO
	 * @param systemId
	 * @param projectId
	 * @param tagStr
	 * @param rows
	 * @param page
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	Map<String, Object> findNexusType(NexusSearchVO nexusSearchVO,Long systemId,Long projectId,String tagStr,Integer rows,Integer page)  throws Exception;
	/**
	 * @author author
	 * @Description 获取Artifact信息
	 * @Date 2020/9/15
	 * @param id
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	Map<String, Object> getArtifactInfo(Long id);
	/**
	 * @author author
	 * @Description 通过ids获取包件List
	 * @Date 2020/9/15
	 * @param artifactIds
	 * @return java.util.List<cn.pioneeruniverse.dev.entity.TblArtifactInfo>
	 **/
	List<TblArtifactInfo> getPackageListById(String artifactIds);
	/**
	 * @author author
	 * @Description 根据制品表IDS查询制品包质量明细表
	 * @Date 2020/9/15
	 * @param artifactIds
	 * @return java.util.List<cn.pioneeruniverse.dev.entity.TblArtifactQualityDetail>
	 **/
	List<TblArtifactQualityDetail> getArtifactQualityDetailList(String artifactIds);
	/**
	 * @author author
	 * @Description 关联任务
	 * @Date 2020/9/15
	 * @param requirementFeatureId
	 * @param artifactInfoId
	 * @param request
	 * @return void
	 **/
	void relateFeature(Long requirementFeatureId,Long artifactInfoId,HttpServletRequest request);
	/**
	 * @author author
	 * @Description 去除关联
	 * @Date 2020/9/15
	 * @param id
	 * @param request
	 * @return void
	 **/
	void removeFeature(Long id,HttpServletRequest request);
	/**
	 * @author author
	 * @Description 批量去除关联
	 * @Date 2020/9/15
	 * @param ids
	 * @param request
	 * @return void
	 **/
	void removeManyFeature(String ids,HttpServletRequest request);
	/**
	 * @author author
	 * @Description 根据ArtifactInfoId删除tag
	 * @Date 2020/9/15
	 * @param artifactInfoId
	 * @return void
	 **/
	void deleteTagByArtifactInfoId(Long artifactInfoId);
	/**
	 * @author author
	 * @Description 查找最新的jar包
	 * @Date 2020/9/15
	 * @param systemId
	 * @param systemModuleId
	 * @param env
	 * @return java.util.List<cn.pioneeruniverse.dev.entity.TblArtifactInfo>
	 **/
	List<TblArtifactInfo> findNewPackage(Long systemId,Long systemModuleId,Integer env);
	/**
	 * @author author
	 * @Description 查找jar包d对应的开发任务
	 * @Date 2020/9/15
	 * @param artifactids
	 * @return java.util.List<java.lang.String>
	 **/
	List<String> findRequidsByartId(String artifactids);

	void downPackage(String nexusPath, String repositoryName, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void addArtifactFrature(Map<String, Object> backMap, TblArtifactInfo tblArtifactInfo, List<TblSystemModuleJenkinsJobRun> moduleJobRunList, String tagStr,long userId,String artType) throws SonarQubeException;

	Map<String, Object> getEnvType(Long id);

	Map<String, Object> getGroupAndArtifactID(Long systemId);

	String deletePackage(List<TblArtifactInfo> artifactList);
}
