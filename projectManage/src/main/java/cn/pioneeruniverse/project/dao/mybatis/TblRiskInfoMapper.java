package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblRiskInfo;
import org.apache.ibatis.annotations.Param;

public interface TblRiskInfoMapper {

	List<TblRiskInfo> getRiskInfo(Long projectId);

	void insertRiskInfo(TblRiskInfo tblRiskInfo);

	void deleteRiskInfo(TblRiskInfo riskInfo);

	TblRiskInfo getRiskInfoById(Long id);

	/**
	*@author liushan
	*@Description 运维类项目查询风险数据
	*@Date 2020/4/29
	*@Param [id]
	*@return cn.pioneeruniverse.project.entity.TblRiskInfo
	**/
	TblRiskInfo getRiskById(Long id);

	void updateRisk(TblRiskInfo tblRiskInfo);

	/**
	*@author author
	*@Description 新建类：根据项目获取
	*@Date 2020/8/18
	 * @param programId
	*@return java.util.List<cn.pioneeruniverse.project.entity.TblRiskInfo>
	**/
	List<TblRiskInfo> getRiskInfoByProgram(Long programId);

	/**
	*@author liushan
	*@Description 运维类项目查询的列表
	*@Date 2020/4/27
	*@Param [projectId]
	*@return java.util.List<cn.pioneeruniverse.project.entity.TblRiskInfo>
	**/
    List<TblRiskInfo> getAllRisk(@Param("riskInfo") TblRiskInfo riskInfo,@Param("start")  Integer start,@Param("pageSize")  Integer pageSzie);

    /**
    *@author author
    *@Description 运维类项目查询的列表 总数
    *@Date 2020/8/18
     * @param riskInfo
    *@return int
    **/
	int getAllRiskCount(@Param("riskInfo") TblRiskInfo riskInfo);
}
