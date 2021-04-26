package cn.pioneeruniverse.project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.project.entity.TblProjectGroup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
import cn.pioneeruniverse.project.entity.TblSystemInfo;
import cn.pioneeruniverse.project.service.dept.DeptInfoService;
import cn.pioneeruniverse.project.service.projectgroup.ProjectGroupService;
import cn.pioneeruniverse.project.service.projectgroupuser.ProjectGroupUserService;
import cn.pioneeruniverse.project.service.projectinfo.ProjectInfoService;
import cn.pioneeruniverse.project.service.systeminfo.SystemInfoService;
import cn.pioneeruniverse.project.service.user.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * 
* @ClassName: ProjectInfoController
* @Description: 项目管理
* @author author
* @date 2020年8月11日 下午9:57:47
*
 */
@RestController
@RequestMapping("project")
public class ProjectInfoController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(ProjectInfoController.class);

    @Autowired
    private ProjectInfoService projectInfoService;

    @Autowired
    private DeptInfoService deptInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectGroupUserService projectGroupUserService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SystemInfoService systemInfoService;

    @Autowired
    private ProjectGroupService projectGroupService;

    /**
     * 根据当前用户查询所处的项目
     *
     * @param request
     * @return map  key projects:项目信息
     */
    @RequestMapping(value = "getAllProject", method = RequestMethod.POST)
    public Map<String, Object> getAllProject(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = projectInfoService.getAllProject(request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return handleException(e, "获取系统信息失败");
        }
        return result;
    }

    /**
     * 条件分页查询and所有项目展示
     *
     * @param tblProjectInfo 封装的查询条件
     * @param userName 姓名
     * @param currentPage 第几页
     * @param pageSize 每页大小
     * @return Map<String, Object> key:status=1正常，2异常
     */
    @RequestMapping(value = "selectProjects", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> selectProjects(
            TblProjectInfo tblProjectInfo, @RequestParam(value = "userName", required = false) String userName,
            Integer page, Integer rows
    ) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            //根据用户名获取项目id
            if (StringUtils.isNotEmpty(userName)) {
                Long userId = userService.findIdByUserName(userName);
                List<Long> ids = projectGroupUserService.findProjectGroupIdsByUserId(userId);
                List<Long> ids1 = projectGroupService.findProjectIdsByProjectGroupIds(ids);
                tblProjectInfo.setIds(ids1);
            }
            //根据输入的状态和类型，从redis中获取对应的code
            //TBL_PROJECT_INFO_PROJECT_TYPE   TBL_PROJECT_INFO_PROJECT_STATUS
            String typeName = tblProjectInfo.getProjectTypeName();
            String statusName = tblProjectInfo.getProjectStatusName();
            if (StringUtils.isNotEmpty(typeName)) {
            	//项目类型
                String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_TYPE").toString();
                JSONObject jsonObj = JSON.parseObject(redisStr);

                for (String key : jsonObj.keySet()) {
                    String value = jsonObj.get(key).toString();
                    if (value.equals(typeName)) {
                        tblProjectInfo.setProjectType(Integer.valueOf(key));
                    }
                }
            }
            if (StringUtils.isNotEmpty(statusName)) {
            	//项目状态
                String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_STATUS").toString();
                JSONObject jsonObj = JSON.parseObject(redisStr);
                for (String key : jsonObj.keySet()) {
                    String value = jsonObj.get(key).toString();
                    if (value.equals(statusName)) {
                        tblProjectInfo.setProjectStatus(Integer.valueOf(key));
                    }
                }
            }
            //查询当前页数据
            List<TblProjectInfo> list = projectInfoService.selectProjects(tblProjectInfo, page, rows);
            //获取总数据：总数
            List<TblProjectInfo> list2 = projectInfoService.selectProjects(tblProjectInfo, 1, Integer.MAX_VALUE);
            for (TblProjectInfo tpi : list) {
                Long deptId = tpi.getDeptId();
                String deptName = deptInfoService.selectDeptName(deptId);
                tpi.setDeptName(deptName);
                //再根据查询出来的结果中获取code，从redis中获取对应的名称。
                Integer t = tpi.getProjectType();
                if (t != null) {
                    String type = t.toString();
                    String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_TYPE").toString();
                    JSONObject jsonObj = JSON.parseObject(redisStr);
                    String projectTypeName = jsonObj.get(type).toString();
                    tpi.setProjectTypeName(projectTypeName);
                }
                Integer s = tpi.getProjectStatus();
                if (s != null) {
                    String status = s.toString();
                    String redisStr2 = redisUtils.get("TBL_PROJECT_INFO_PROJECT_STATUS").toString();
                    JSONObject jsonObj2 = JSON.parseObject(redisStr2);
                    String projectStatusName = jsonObj2.get(status).toString();
                    tpi.setProjectStatusName(projectStatusName);
                }
            }
//				Integer records = projectInfoService.selectCount();    //总条目数
            Integer total = list2.size() / rows;                      //总页数=总条目数/每页显示条目数(查出来的)
//				Integer page = page;                            //当前页
           //当前页
            map.put("page", page);
            //总页数
            map.put("total", total);
            //总条数
            map.put("records", list2.size());
            //当前页数据
            map.put("data", list);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            this.handleException(e, "查询项目失败");
        }
        return map;
    }

    /**
     * 从redis中获取项目类型和状态栏的下拉列表
     *
     * @param termCode 数据字典编码
     * @return Map<String, Object> key:status=1正常，2异常
     */
    @ResponseBody
    @RequestMapping(value = "selectTypeAndStatusList", method = RequestMethod.POST)
    public Map<String, Object> selectTypeList(@RequestParam("termCode") String termCode) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            ArrayList<String> list = new ArrayList<>();
            String redisStr = redisUtils.get(termCode).toString();
            JSONObject jsonObj = JSON.parseObject(redisStr);
            for (String key : jsonObj.keySet()) {
                String ValueName = jsonObj.get(key).toString();
                list.add(ValueName);
            }
//			System.out.println(list);
            map.put("data", list);
        } catch (Exception e) {
            this.handleException(e, "获取下拉目录失败");
        }
        return map;
    }

    /**
     * 新增项目
     *
     * @param tblProjectInfo 项目信息
     */
    @RequestMapping(value = "insertProject", method = RequestMethod.POST)
    public void insertProject(TblProjectInfo tblProjectInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        String deptName = tblProjectInfo.getDeptName();
        Long deptId = deptInfoService.selectDeptId(deptName);
        tblProjectInfo.setDeptId(deptId);
        //根据输入的类型从redis中获取类型状态码
        String typeName = tblProjectInfo.getProjectTypeName();
        String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_TYPE").toString();
        JSONObject jsonObj = JSON.parseObject(redisStr);
        for (String key : jsonObj.keySet()) {
            String value = jsonObj.get(key).toString();
            if (value.equals(typeName)) {
                tblProjectInfo.setProjectType(Integer.valueOf(key));
            }
        }
        try {
            projectInfoService.insertProject(tblProjectInfo);
            //拿到插入这条数据的主键id
            Long projectId = tblProjectInfo.getId();
            //获取接收到的系统名称
            List<String> systemNames = tblProjectInfo.getSystemName();
            //更新系统表的数据
            systemInfoService.updateSystemInfo(projectId, systemNames);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            this.handleException(e, "新增项目失败");
        }
    }

    /**
     * 废弃项目
     * @param id 项目ID
     */
    @RequestMapping(value = "deleteProject", method = RequestMethod.POST)
    public void deleteProjectById(@RequestParam("id") Long id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            projectInfoService.deleteProjectById(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            this.handleException(e, "废弃项目失败");
        }
    }

    /**
     * 根据项目id查询项目(编辑项目的数据回显或项目详情)
     *
     * @param id 项目ID
     * @return Map<String, Object> key:status=1正常，2异常
     */
    @ResponseBody
    @RequestMapping(value = "selectProjectById", method = RequestMethod.POST)
    public Map<String, Object> selectProjectById(@RequestParam("id") Long id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            TblProjectInfo tpi = projectInfoService.selectProjectById(id);
            Long deptId = tpi.getDeptId();
            String type = tpi.getProjectType().toString();
            String status = tpi.getProjectStatus().toString();
            String deptName = deptInfoService.selectDeptName(deptId);
            tpi.setDeptName(deptName);
            //从redis中获取类型名称
            String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_TYPE").toString();
            JSONObject jsonObj = JSON.parseObject(redisStr);
            String typeName = jsonObj.get(type).toString();
            //获取状态名
            String redisStr2 = redisUtils.get("TBL_PROJECT_INFO_PROJECT_STATUS").toString();
            JSONObject jsonObj2 = JSON.parseObject(redisStr2);
            String statusName = jsonObj2.get(status).toString();
            tpi.setProjectTypeName(typeName);
            tpi.setProjectStatusName(statusName);
            //查询关联系统
            List<String> systemNames = systemInfoService.selectSystemName(id);
            tpi.setSystemName(systemNames);
//			map.put("systemNames", systemNames);
            //项目信息
            map.put("data", tpi);
        } catch (Exception e) {
            this.handleException(e, "数据显示失败");
        }
        return map;
    }

    /**
     * 
    * @Title: updateProject
    * @Description: 更新项目
    * @author author
    * @param tblProjectInfo 项目信息
    * @param systemNames 系统名称
    * @return Map<String, Object> key:status=1正常，2异常 
     */
    @RequestMapping(value = "updateProjectById", method = RequestMethod.POST)
    public Map<String, Object> updateProject(TblProjectInfo tblProjectInfo, List<String> systemNames) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
//		String deptName = tblProjectInfo.getDeptName();
//		Long deptId = deptInfoService.selectDeptId(deptName);
//		tblProjectInfo.setDeptId(deptId);
        //获取当前编辑项目的id
        Long projectId = tblProjectInfo.getId();
        try {
            projectInfoService.updateProject(tblProjectInfo);
            systemInfoService.updateSystemInfo(projectId, systemNames);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            this.handleException(e, "编辑项目失败");
        }
        return map;
    }

    /**
     * 编辑项目时删除关联系统的解绑(点叉叉时发送的请求)
     *
     * @param systemName 系统名
     */
    public void updateSystemInfoBySystemName(String systemName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            systemInfoService.updateSystemInfoBySystemName(systemName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            this.handleException(e, "系统与项目的解绑失败");
        }
    }

    /**
     * 
    * @Title: selectSystemInfo
    * @Description: 新建项目弹框，关联系统查询
    * @author author
    * @param tblSystemInfo 封装的查询条件
    * @param pageNumber 第几页
    * @param pageSize 每页数量
    * @return Map<String, Object> status=1正常，2异常
     */
    @ResponseBody
    @RequestMapping(value = "selectSystemInfo", method = RequestMethod.POST)
    public Map<String, Object> selectSystemInfo(TblSystemInfo tblSystemInfo,
                                                @RequestParam(value = "pageNumber", defaultValue = "1", required = true) Integer pageNumber,                             //
                                                @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize
    ) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            List<TblSystemInfo> list = systemInfoService.selectSystemInfo(tblSystemInfo, pageNumber, pageSize);
            List<TblSystemInfo> list2 = systemInfoService.selectSystemInfo(tblSystemInfo, 1, Integer.MAX_VALUE);
            map.put("total", list2.size());//总数量
            map.put("rows", list);//系统信息列表
        } catch (Exception e) {
            this.handleException(e, "获取系统信息失败");
        }
        return map;
    }


    /**
     * 查询部门表  查询出所有部门名称并显示在新建弹框的部门下拉列表里边
     *
     * @return Map<String, Object> status=1正常，2异常
     */
    @ResponseBody
    @RequestMapping(value = "selectAllDeptName", method = RequestMethod.POST)
    public Map<String, Object> selectAllDeptName() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            List<String> list = deptInfoService.selectAllDeptName();
            map.put("data", list);
        } catch (Exception e) {
            this.handleException(e, "获取部门列表失败");
        }
        return map;
    }


    /**
     * @deprecated
    * @Title: getProjectGroupTree
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @author author
    * @param projectId 项目Id
    * @return List<Map<String,Object>>
     */
    @RequestMapping(value = "getProjectGroupTree", method = RequestMethod.POST)
    public List<Map<String, Object>> getProjectGroupTree(Long projectId) {
        return projectGroupService.getProjectGroupTree(projectId);
    }

    /**
     * @deprecated
    * @Title: getProjectGroupTreeBySystemId
    * @Description: 编辑测试缺陷中选择项目小组
    * @author author
    * @param systemId 系统ID
    * @return List<Map<String,Object>>
     */
    @RequestMapping(value = "getProjectGroupTreeBySystemId", method = RequestMethod.POST)
    public List<Map<String, Object>> getProjectGroupTreeBySystemId(Long systemId) {
        return projectGroupService.getProjectGroupTreeBySystemId(systemId);
    }

    /**
     * @deprecated
    * @Title: getProjectGroupByProjectGroupId
    * @Description: 通过项目小组ID获取项目小组信息，在测试缺陷和开发缺陷详情展示项目小组
    * @author author
    * @param projectGroupId 项目组小组ID
    * @return TblProjectGroup
     */
    @RequestMapping(value = "getProjectGroupByProjectGroupId", method = RequestMethod.POST)
    public TblProjectGroup getProjectGroupByProjectGroupId(Long projectGroupId) {
        return projectGroupService.getProjectGroupByProjectGroupId(projectGroupId);
    }

    /**
     * 
    * @Title: getAllProjectInfo
    * @Description: 获取所有项目组
    * @author author
    * @return List<TblProjectInfo>
     */
    @RequestMapping(value = "getAllProjectInfo", method = RequestMethod.POST)
    public List<TblProjectInfo> getAllProjectInfo() {
        return projectInfoService.getAllProjectInfo();
    }


    /**
     * 
    * @Title: handleException
    * @Description: 统一捕获异常
    * @author author
    * @param e
    * @param message
    * @return Map<String, Object> key:status=2异常返回
     */
    public Map<String, Object> handleException(Exception e, String message) {
        e.printStackTrace();
        logger.error(message + ":" + e.getMessage(), e);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", Constants.ITMP_RETURN_FAILURE);
        map.put("errorMessage", message);
        return map;
    }
}
