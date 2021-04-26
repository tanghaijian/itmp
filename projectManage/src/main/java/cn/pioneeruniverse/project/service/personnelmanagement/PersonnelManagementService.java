package cn.pioneeruniverse.project.service.personnelmanagement;

import cn.pioneeruniverse.project.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface PersonnelManagementService {
    /**
     *
     * @Title: selectCompanyVague
     * @Description: 获取公司列表
     * @author author
     * @param companyName
     * @return List<Company>
     */
    List<Company> selectCompanyVague(String companyName);
    /**
     *
     * @Title: selectDeptVague
     * @Description: 获取部门列表
     * @author author
     * @param deptName
     * @return List<TblDeptInfo>
     */
    List<TblDeptInfo> selectDeptVague(String deptName);
    /**
     *
     * @Title: selectProjectVague
     * @Description: 获取项目列表
     * @author author
     * @param projectName
     * @param projectCode
     * @return List<TblProjectInfo>
     */
    List<TblProjectInfo> selectProjectVague(String projectName,String projectCode);
    /**
     *
     * @Title: selectUserInfoVague
     * @Description: 获取用户列表
     * @author author
     * @param userInfo
     * @return List<TblUserInfo>
     */
    List<TblUserInfo> selectUserInfoVague(TblUserInfo userInfo);
    /**
     *
     * @Title: getAllUserProjectCount
     * @Description: 获取有项目的人员总数，页面总数
     * @author author
     * @param map
     * @return Integer 总数
     */
    Integer getAllUserProjectCount(Map<String,Object> map);
    /**
     *
     * @Title: getAllUserProject
     * @Description: 获取项目人员列表
     * @author author
     * @param map 查询条件
     * @return List<TblUserInfo>
     */
    List<TblUserInfo> getAllUserProject(Map<String,Object> map);
    /**
     *
     * @Title: getProjectInfoByUser
     * @Description: 获取人员的项目信息
     * @author author
     * @param userId 人员ID
     * @return String 项目名称，以,隔开
     */
    String getProjectInfoByUser(Long userId);
    /**
     *
     * @Title: getAllProject
     * @Description: 获取所有项目
     * @author author
     * @return List<TblProjectInfo>
     */
    List<TblProjectInfo> getAllProject();
    /**
     *
     * @Title: getProjectGroupByProjectId
     * @Description: 通过项目获取项目小组
     * @author author
     * @param projectId 项目ID
     * @return List<TblProjectGroup>
     */
    List<TblProjectGroup> getProjectGroupByProjectId(Long projectId);
    /**
     *
     * @Title: insertItmpProjectGroupUser
     * @Description: 保存人员项目信息，注意测试库中也存在相同信息，需要同步
     * @author author
     * @param groupUserList
     * @param request
     * @throws Exception
     */
    void insertItmpProjectGroupUser(List<TblProjectGroupUser> groupUserList, HttpServletRequest request)  throws Exception ;
    /**
     *
     * @Title: insertTmpProjectGroupUser
     * @Description: 测试库
     * @author author
     * @param tblProjectGroupUsers
     */
    void insertTmpProjectGroupUser(List<TblProjectGroupUser> tblProjectGroupUsers);
    /**
     *
     * @Title: previewProject
     * @Description: 项目组和项目小组同行显示，即将项目小组以，连接起来
     * @author author
     * @param projectId
     * @return List<TblProjectGroup>
     */
    List<TblProjectGroup> previewProject(Long projectId);

}
