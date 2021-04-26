package cn.pioneeruniverse.project.service.personnelmanagement.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.PinYinUtil;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.common.velocity.tag.VelocityDataDict;
import cn.pioneeruniverse.project.dao.mybatis.PersonnelManagementMapper;
import cn.pioneeruniverse.project.dao.mybatis.role.RoleDao;
import cn.pioneeruniverse.project.entity.Company;
import cn.pioneeruniverse.project.entity.TblDeptInfo;
import cn.pioneeruniverse.project.entity.TblProjectGroup;
import cn.pioneeruniverse.project.entity.TblProjectGroupUser;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
import cn.pioneeruniverse.project.entity.TblRoleInfo;
import cn.pioneeruniverse.project.entity.TblUserInfo;
import cn.pioneeruniverse.project.service.personnelmanagement.PersonnelManagementService;

@Service
public class PersonnelManagementServiceImpl implements PersonnelManagementService {

    @Autowired
    private PersonnelManagementMapper personnelManagementMapper;
    @Autowired
    private RoleDao roleDao;

    /**
     * 
    * @Title: selectCompanyVague
    * @Description: 获取公司列表
    * @author author
    * @param companyName
    * @return List<Company>
     */
    @Override
    public List<Company> selectCompanyVague(String companyName) {
        return personnelManagementMapper.selectCompanyVague(companyName);
    }

    /**
     * 
    * @Title: selectDeptVague
    * @Description: 获取部门列表
    * @author author
    * @param deptName
    * @return List<TblDeptInfo>
     */
    @Override
    public List<TblDeptInfo> selectDeptVague(String deptName) {
        return personnelManagementMapper.selectDeptVague(deptName);
    }

    /**
     * 
    * @Title: selectProjectVague
    * @Description: 获取项目列表
    * @author author
    * @param projectName
    * @param projectCode
    * @return List<TblProjectInfo>
     */
    @Override
    public List<TblProjectInfo> selectProjectVague(String projectName,String projectCode){
        return personnelManagementMapper.selectProjectVague(projectName,projectCode);
    }

    /**
     * 
    * @Title: selectUserInfoVague
    * @Description: 获取用户列表
    * @author author
    * @param userInfo
    * @return List<TblUserInfo>
     */
    @Override
    public List<TblUserInfo> selectUserInfoVague(TblUserInfo userInfo) {
        return personnelManagementMapper.selectUserInfoVague(userInfo.getUserAndAccount(),userInfo.getIds());
    }

    /**
     * 
    * @Title: getAllUserProjectCount
    * @Description: 获取有项目的人员总数，页面总数
    * @author author
    * @param map
    * @return Integer 总数
     */
    @Override
    public Integer getAllUserProjectCount(Map<String, Object> map) {
        return personnelManagementMapper.getAllUserProjectCount(map);
    }

    /**
     * 
    * @Title: getAllUserProject
    * @Description: 获取项目人员列表
    * @author author
    * @param map 查询条件
    * @return List<TblUserInfo>
     */
    @Override
    public List<TblUserInfo> getAllUserProject(Map<String, Object> map) {
        List<TblUserInfo> user = personnelManagementMapper.getAllUserProject(map);
        return user;
    }

    /**
     * 
    * @Title: getProjectInfoByUser
    * @Description: 获取人员的项目信息
    * @author author
    * @param userId 人员ID
    * @return String 项目名称，以,隔开
     */
    @Override
    public String getProjectInfoByUser(Long userId) {
        return personnelManagementMapper.getProjectInfoByUser(userId);
    }

    /**
     * 
    * @Title: getAllProject
    * @Description: 获取所有项目
    * @author author
    * @return List<TblProjectInfo>
     */
    @Override
    public List<TblProjectInfo> getAllProject() {
        return personnelManagementMapper.getAllProject();
    }

    /**
     * 
    * @Title: getProjectGroupByProjectId
    * @Description: 通过项目获取项目小组
    * @author author
    * @param projectId 项目ID
    * @return List<TblProjectGroup>
     */
    @Override
    public List<TblProjectGroup> getProjectGroupByProjectId(Long projectId){
        return personnelManagementMapper.getProjectGroupByProjectId(projectId);
    }

    /**
     * 
    * @Title: insertItmpProjectGroupUser
    * @Description: 保存人员项目信息，注意测试库中也存在相同信息，需要同步
    * @author author
    * @param groupUserList
    * @param request
    * @throws Exception
     */
    @Override
    @DataSource(name = "itmpDataSource")
    @Transactional(readOnly=false,rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
    public void insertItmpProjectGroupUser(List<TblProjectGroupUser> groupUserList, HttpServletRequest request) throws Exception  {
        List<TblProjectGroupUser> tmpProjectGroupUser = new ArrayList();
        for(TblProjectGroupUser groupUser : groupUserList) {
            if (groupUser.getProjectId() == 0) {
                List<TblProjectInfo> projectList = personnelManagementMapper.getAllProject();
                for(TblProjectInfo project : projectList){
                    insertAllProjectRole(project.getId(),groupUser,request);
                }
            } else {
                insertAllProjectRole(groupUser.getProjectId(),groupUser,request);
            }
            //循环所选人员
            for (Long userId : groupUser.getUserIds()) {
                groupUser.setUserId(userId);
                //获取项目组的所有小组信息
                List<TblProjectGroup> projectGroupList =
                        personnelManagementMapper.getProjectGroupByProjectId(groupUser.getProjectId());
                //循环项目小组信息
                for (TblProjectGroup projectGroup : projectGroupList) {
                    groupUser.setProjectGroupId(projectGroup.getId());
                    Integer count = personnelManagementMapper.getCountByGroupUser(groupUser);
                    if (count == 0) {
                    	//同步测试库信息
                        TblProjectGroupUser groupUser1 = new TblProjectGroupUser();
                        groupUser1.setUserId(userId);
                        groupUser1.setProjectGroupId(groupUser.getProjectGroupId());
                        groupUser1.setUserPost(groupUser.getUserPost());
                        CommonUtil.setBaseValue(groupUser1, request);
                        personnelManagementMapper.insertItmpProjectGroupUser(groupUser1);
                        tmpProjectGroupUser.add(groupUser1);
                    }
                }
            }
        }
        SpringContextHolder.getBean(PersonnelManagementService.class).insertTmpProjectGroupUser(tmpProjectGroupUser);
    }

    /**
     * 
    * @Title: insertTmpProjectGroupUser
    * @Description: 测试库
    * @author author
    * @param tblProjectGroupUsers
     */
    @Override
    @DataSource(name = "tmpDataSource")
    @Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
    public void insertTmpProjectGroupUser(List<TblProjectGroupUser> tblProjectGroupUsers){
        for (TblProjectGroupUser groupUser:tblProjectGroupUsers){
            personnelManagementMapper.insertTmpProjectGroupUser(groupUser);
        }
    }

    /**
     * 
    * @Title: previewProject
    * @Description: 项目组和项目小组同行显示，即将项目小组以，连接起来
    * @author author
    * @param projectId
    * @return List<TblProjectGroup>
     */
    @Override
    public List<TblProjectGroup> previewProject(Long projectId) {
        return personnelManagementMapper.previewProject(projectId);
    }

    /**
     * 
    * @Title: insertAllProjectRole
    * @Description: 项目岗位角色信息，同步到人员角色
    * @author author
    * @param projectId 项目ID
    * @param groupUser 项目组成员
    * @param request
     */
    private void insertAllProjectRole(Long projectId,TblProjectGroupUser groupUser,HttpServletRequest request){
        //查询角色表是否存在对应的项目组与岗位的记录
        List<TblRoleInfo> roleInfoList = roleDao.
                selectUserPostRole(projectId, groupUser.getUserPost());
        Long roleId;
        if (roleInfoList == null || roleInfoList.size() == 0) {
            //如果不存在就新增一条岗位记录
            TblRoleInfo roleInfo = new TblRoleInfo();
            roleInfo.setProjectId(projectId);
            roleInfo.setUserPost(groupUser.getUserPost());
            VelocityDataDict dict = new VelocityDataDict();
            Map<String, String> userPostMap = dict.getDictMap("TBL_PROJECT_GROUP_USER_USER_POST");
            //
            for (Map.Entry<String, String> entry : userPostMap.entrySet()) {
                if (entry.getKey().equals(groupUser.getUserPost().toString())) {
                    roleInfo.setRoleName(entry.getValue());
                    String empNo = projectId.toString() +
                            PinYinUtil.getPingYin(entry.getValue()).toUpperCase(Locale.ENGLISH);
                    roleInfo.setRoleCode(empNo);
                }
            }
            CommonUtil.setBaseValue(roleInfo, request);
            roleDao.insertUserPostRole(roleInfo);
            //获取新增岗位记录的id
            roleId = roleInfo.getId();
        } else {
            //如果存在就将数据的id赋值给roleId
            roleId = roleInfoList.get(0).getId();
        }
        for (Long userId : groupUser.getUserIds()) {
            //查询是否存在相对应的用户岗位
            Integer count = roleDao.selectUserRole(userId, roleId);
            if (count < 1) {
                Map<String, Object> userRole = new HashMap();
                //如果不存在相对应的用户岗位 就新增一条角色表记录
                userRole.put("roleId", roleId);
                userRole.put("useId", userId);
                userRole.put("status", 1);
                userRole.put("createBy", CommonUtil.getCurrentUserId(request));
                userRole.put("lastUpdateBy", CommonUtil.getCurrentUserId(request));
                roleDao.insertRoleUser(userRole);
            }
        }
    }
}
