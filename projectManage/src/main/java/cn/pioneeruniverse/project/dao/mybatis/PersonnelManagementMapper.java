package cn.pioneeruniverse.project.dao.mybatis;


import cn.pioneeruniverse.project.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PersonnelManagementMapper {

    /**
    * @author author
    * @Description 获取公司列表
    * @Date 2020/9/15
    * @param companyName
    * @return java.util.List<cn.pioneeruniverse.project.entity.Company>
    **/
    List<Company> selectCompanyVague(String companyName);

    /**
    * @author author
    * @Description 获取部门列表
    * @Date 2020/9/15
    * @param deptName
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblDeptInfo>
    **/
    List<TblDeptInfo> selectDeptVague(String deptName);

    /**
    * @author author
    * @Description 获取项目列表
    * @Date 2020/9/15
    * @param projectName
    * @param projectCode
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblProjectInfo>
    **/
    List<TblProjectInfo> selectProjectVague(@Param("projectName")String projectName,
                                            @Param("projectCode")String projectCode);

    /**
    * @author author
    * @Description 获取用户列表
    * @Date 2020/9/15
    * @param userAndAccount
    * @param ids
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblUserInfo>
    **/
    List<TblUserInfo> selectUserInfoVague(@Param("userAndAccount")String userAndAccount,
                                          @Param("ids")Long [] ids);

    /**
    * @author author
    * @Description 前端查询结果，列表总数 
    * @Date 2020/9/15
    * @param map
    * @return java.lang.Integer
    **/
    Integer getAllUserProjectCount(Map<String,Object> map);

    /**
    * @author author
    * @Description 前端列表信息
    * @Date 2020/9/15
    * @param map
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblUserInfo>
    **/
    List<TblUserInfo> getAllUserProject(Map<String,Object> map);

    /**
    * @author author
    * @Description 通过用户获取项目信息
    * @Date 2020/9/15
    * @param userId
    * @return java.lang.String
    **/
    String getProjectInfoByUser(Long userId);

    /**
    * @author author
    * @Description 获取所有项目
    * @Date 2020/9/15
    * @param 
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblProjectInfo>
    **/
    List<TblProjectInfo> getAllProject();

    /**
    * @author author
    * @Description 通过项目ID获取小组
    * @Date 2020/9/15
    * @param projectId
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblProjectGroup>
    **/
    List<TblProjectGroup> getProjectGroupByProjectId(Long projectId);

    /**
    * @author author
    * @Description 获取关联过的小组人员总数
    * @Date 2020/9/15
    * @param tblProjectGroupUser
    * @return java.lang.Integer
    **/
    Integer getCountByGroupUser(TblProjectGroupUser tblProjectGroupUser);

    /**
    * @author author
    * @Description 插入开发库项目组人员
    * @Date 2020/9/15
    * @param tblProjectGroupUser
    * @return void
    **/
    void insertItmpProjectGroupUser(TblProjectGroupUser tblProjectGroupUser);

    /**
    * @author author
    * @Description 插入测试库项目组成员
    * @Date 2020/9/15
    * @param tblProjectGroupUser
    * @return void
    **/
    void insertTmpProjectGroupUser(TblProjectGroupUser tblProjectGroupUser);

    /**
    * @author author
    * @Description 项目和项目小组同行显示，将小组信息合并到一行
    * @Date 2020/9/15
    * @param projectId
    * @return java.util.List<cn.pioneeruniverse.project.entity.TblProjectGroup>
    **/
    List<TblProjectGroup> previewProject(Long projectId);

}
