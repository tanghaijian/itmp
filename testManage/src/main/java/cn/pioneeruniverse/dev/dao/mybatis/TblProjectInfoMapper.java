package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblProjectInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;

import java.util.List;
import java.util.Map;

public interface TblProjectInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblProjectInfo record);

    int insertSelective(TblProjectInfo record);

    TblProjectInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblProjectInfo record);

    int updateByPrimaryKeyWithBLOBs(TblProjectInfo record);

    int updateByPrimaryKey(TblProjectInfo record);

    /**
    * @author author
    * @Description 获取系统管理员项目信息
    * @Date 2020/9/22
    * @param projectName
    * @return java.util.List<cn.pioneeruniverse.dev.entity.TblProjectInfo>
    **/
    List<TblProjectInfo> getProjectListBySystem(String projectName);

    /**
    * @author author
    * @Description 获取项目信息非管理员
    * @Date 2020/9/22
    * @param map
    * @return java.util.List<cn.pioneeruniverse.dev.entity.TblProjectInfo>
    **/
    List<TblProjectInfo> getProjectListByNoSystem(Map<String,Object> map);

    List<TblSystemInfo> getSystemByProjectId(Long projectId);
}