package cn.pioneeruniverse.project.controller;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.project.entity.*;
import cn.pioneeruniverse.project.service.personnelmanagement.PersonnelManagementService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PersonnelManagementController
 * @Description: (项目组人员管理)
 * @author author
 * @date Jul 28, 2020 11:33:07 AM
 *
 */
@RestController
@RequestMapping("personnelManagement")
public class PersonnelManagementController extends BaseController {
	@Autowired
	private PersonnelManagementService personnelManagementService;

    private static Logger log = LoggerFactory.getLogger(PersonnelManagementController.class);

	/**
	 *@Description 所属公司模糊查询
	 *@Date 2020/8/05
	 *@Param [companyName] 公司名称
	 *@return List<Company>
	 **/
	@RequestMapping(value = "selectCompanyVague",method= RequestMethod.POST)
	public List<Company> selectCompanyVague(String companyName) {
		return personnelManagementService.selectCompanyVague(companyName);
	}

	/**
	 *@Description 所属部门模糊查询
	 *@Date 2020/8/05
	 *@Param [deptName] 部门名称
	 *@return List<TblDeptInfo>
	 **/
	@RequestMapping(value = "selectDeptVague",method= RequestMethod.POST)
	public List<TblDeptInfo> selectDeptVague(String deptName) {
		return personnelManagementService.selectDeptVague(deptName);
	}

	/**
	 *@Description 项目组名称模糊查询
	 *@Date 2020/8/05
	 *@Param [projectName] 项目组名称
	 *@return List<TblProjectInfo>
	 **/
	@RequestMapping(value = "selectProjectNameVague",method= RequestMethod.POST)
	public List<TblProjectInfo> selectProjectNameVague(String projectName) {
		return personnelManagementService.selectProjectVague(projectName,null);
	}

	/**
	 *@Description 项目组编号模糊查询
	 *@Date 2020/8/05
	 *@Param [projectCode] 项目组编号
	 *@return List<TblProjectInfo>
	 **/
	@RequestMapping(value = "selectProjectCodeVague",method= RequestMethod.POST)
	public List<TblProjectInfo> selectProjectCodeVague(String projectCode) {
		return personnelManagementService.selectProjectVague(null,projectCode);
	}

	/**
	 *@Description 人员模糊查询
	 *@Date 2020/8/05
	 *@Param [userInfo] 人员表对象
	 *@return List<TblProjectInfo>
	 **/
	@RequestMapping(value = "selectUserInfoVague",method= RequestMethod.POST)
	public List<TblUserInfo> selectUserInfoVague(TblUserInfo userInfo) {
		return personnelManagementService.selectUserInfoVague(userInfo);
	}

	/**
	 *@Description 条件查询人员列表
	 *@Date 2020/8/05
	 *@Param [search] 条件参数
	 *@return String 用户列表的json字符串格式
	 **/
	@RequestMapping(value = "getAllUserProject",method= RequestMethod.POST)
	public String getAllUserProject(String search,Integer rows,Integer page) {
        JSONObject jsonObj = new JSONObject();
		try{
            Map<String,Object> mapObj = new HashMap<>();
            if (StringUtils.isNotBlank(search)) {
                mapObj = JSONObject.parseObject(search,Map.class);
            }
            //第几页
            mapObj.put("pageIndex", (page - 1) * rows);
            //每页大小
            mapObj.put("pageSize", rows);
            int count= personnelManagementService.getAllUserProjectCount(mapObj);
            List<TblUserInfo> list = personnelManagementService.getAllUserProject(mapObj);

            jsonObj.put("page", page); //当前页
            if(rows!=null) {
                jsonObj.put("total", (count+rows-1)/rows); //总页数
            }
            jsonObj.put("records", count); //总数量
            jsonObj.put("rows", list); //当前页数据
        } catch (Exception e) {
            e.printStackTrace();
            log.error("mes:" + e.getMessage(), e);
		}
		return jsonObj.toJSONString();
    }

	/**
	 *@Description 根据人员id查询用户所在项目组
	 *@Date 2020/8/05
	 *@Param userId 人员id
	 *@return java.util.Map<String,Object> key projectNames:项目组名称
	 **/
	@RequestMapping(value = "getProjectInfoByUser",method= RequestMethod.POST)
	public Map<String,Object> getProjectInfoByUser(Long userId) {
		Map<String,Object> map = new HashMap<>();
		try{
			String projectNames = personnelManagementService.getProjectInfoByUser(userId);
			//项目组名称
			map.put("projectNames",projectNames);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}

	/**
	 *@Description 获取所有项目组信息
	 *@Date 2020/8/05
	 *@return java.util.Map<String,Object> key:tblProjectInfo 项目组列表
	 **/
	@RequestMapping(value = "getAllProject",method= RequestMethod.POST)
	public Map<String,Object> getAllProject() {
		Map<String,Object> map = new HashMap<>();
		try{
			List<TblProjectInfo> tblProjectInfo = personnelManagementService.getAllProject();
			//项目组信息列表
			map.put("tblProjectInfo",tblProjectInfo);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}

	/**
	 *@Description 根据项目组id获取项目小组信息
	 *@Date 2020/8/05
	 *@Param [projectId] 项目组id
	 *@return java.util.Map<String,Object> Key：projectGroup 项目小组信息列表
	 **/
	@RequestMapping(value = "getProjectGroupByProjectId",method= RequestMethod.POST)
	public Map<String,Object> getProjectGroupByProjectId(Long projectId) {
		Map<String,Object> map = new HashMap<>();
		try{
			List<TblProjectGroup> projectGroup = personnelManagementService.
					getProjectGroupByProjectId(projectId);
			map.put("projectGroup",projectGroup);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}

	/**
	 *@Description 将用户添加到项目小组里面
	 *@Date 2020/8/05
	 *@Param [tblProjectGroupUser] 项目小组对象
	 *@return java.util.Map<String,Object>  key :status  1正常，2异常
	 **/
	@RequestMapping(value = "insertProjectGroupUser",method= RequestMethod.POST)
	public Map<String,Object> insertProjectGroupUser(String tblProjectGroupUser,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		try{
			List<TblProjectGroupUser> groupUserList = JSON.parseArray(tblProjectGroupUser,TblProjectGroupUser.class);
			personnelManagementService.insertItmpProjectGroupUser(groupUserList,request);
			map.put("status",1);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}

	/**
	 *@Description 查询小组
	 *@Date 2020/8/05
	 *@Param [projectId] 项目id
	 *@return java.util.Map<String,Object> key:groupList 项目小组信息
	 **/
	@RequestMapping(value = "previewProject",method= RequestMethod.POST)
	public Map<String,Object> previewProject(Long projectId) {
		Map<String,Object> map = new HashMap<>();
		try{
			List<TblProjectGroup> groupList = personnelManagementService.previewProject(projectId);
			map.put("groupList",groupList);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
}
