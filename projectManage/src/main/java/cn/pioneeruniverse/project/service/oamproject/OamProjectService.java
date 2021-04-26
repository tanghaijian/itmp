package cn.pioneeruniverse.project.service.oamproject;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.project.entity.TblProjectGroup;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
import cn.pioneeruniverse.project.entity.TblSystemInfo;
import cn.pioneeruniverse.project.entity.TblUserInfo;
import cn.pioneeruniverse.project.entity.User;

public interface OamProjectService {
	/**
	 *
	 * @Title: selectOamProject
	 * @Description: 查询运维项目
	 * @author author
	 * @param projectName 项目名
	 * @param projectStatusName 项目状态
	 * @param projectManageName 项目管理岗
	 * @param developManageName 项目开发岗
	 * @param uid 当前用户ID
	 * @param roleCodes 角色编码
	 * @param page 第几页
	 * @param rows 每页数据量
	 * @return
	 * @throws Exception
	 * @throws
	 */
	List<TblProjectInfo> selectOamProject(String projectName, String projectStatusName, String projectManageName,
			String developManageName, Long uid, List<String> roleCodes, Integer page, Integer rows) throws Exception;

	/**
	 *
	 * @Title: selectProject
	 * @Description: 运维类项目详情
	 * @author author
	 * @param id
	 * @return
	 * @throws Exception
	 * @throws
	 */
	TblProjectInfo selectProject(Long id) throws Exception;

	/**
	 *
	 * @Title: insertOamProject
	 * @Description: 开发库新增运维类项目
	 * @author author
	 * @param projectInfo
	 * @param request
	 * @return
	 * @throws Exception
	 * @throws
	 */
	Long insertOamProject(TblProjectInfo projectInfo,HttpServletRequest request) throws Exception;

	/**
	 *
	 * @Title: selectUser
	 * @Description: 编辑页面人员弹框
	 * @author author
	 * @param userName
	 * @param employeeNumber
	 * @param deptName
	 * @param companyName
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 * @throws Exception
	 * @throws
	 */
	List<TblUserInfo> selectUser(String userName, String employeeNumber, String deptName, String companyName,
			Integer pageNumber, Integer pageSize) throws Exception;

	/**
	 *
	 * @Title: selectDeptName
	 * @Description: 人员弹框部门下拉框内容
	 * @author author
	 * @return
	 * @throws Exception
	 * @throws
	 */
	List<String> selectDeptName() throws Exception;

	/**
	 *
	 * @Title: selectCompanyName
	 * @Description: 人员弹框公司内容
	 * @author author
	 * @return
	 * @throws Exception
	 * @throws
	 */
	List<String> selectCompanyName() throws Exception;

	/**
	 * @author author
	 * @Description 编辑项目
	 * @Date 2020/9/14
	 * @param tblProjectInfo
	 * @param request
	 * @return void
	 **/
	void editProject(TblProjectInfo tblProjectInfo, HttpServletRequest request) throws Exception;

	/**
	 * @author author
	 * @Description 获取项目下的所有项目组
	 * @Date 2020/9/14
	 * @param id
	 * @return java.util.List<cn.pioneeruniverse.project.entity.TblProjectGroup>
	 **/
	List<TblProjectGroup> selectProjectGroup(Long id) throws Exception;

	/**
	 * @author author
	 * @Description 选中人员显示在编辑页面
	 * @Date 2020/9/14
	 * @param ids
	 * @return java.util.List<cn.pioneeruniverse.project.entity.User>
	 **/
	List<User> selectUsers(Long[] ids) throws Exception;

	/**
	 * @author author
	 * @Description 结束项目
	 * @Date 2020/9/14
	 * @param id
	 * @param request
	 * @return void
	 **/
	void endProject(Long id, HttpServletRequest request) throws Exception;

	/**
	 * @author author
	 * @Description 关联系统的弹窗
	 * @Date 2020/9/14
	 * @param tblSystemInfo
	 * @param pageNumber
	 * @param pageSize
	 * @param systemIds
	 * @return java.util.List<cn.pioneeruniverse.project.entity.TblSystemInfo>
	 **/
	List<TblSystemInfo> selectSystemInfo(TblSystemInfo tblSystemInfo, Integer pageNumber, Integer pageSize, Long[] systemIds) throws Exception;

	/**
	 * @author author
	 * @Description 删除项目组
	 * @Date 2020/9/14
	 * @param projectGroupId
	 * @param request
	 * @return void
	 **/
	void deletePeojectGroup(Long projectGroupId, HttpServletRequest request) throws Exception;

	/**
	 * @author author
	 * @Description 编辑项目组
	 * @Date 2020/9/14
	 * @param tblProjectGroup
	 * @param request
	 * @return void
	 **/
	void editProjectGroup(TblProjectGroup tblProjectGroup, HttpServletRequest request) throws Exception;

	/**
	 * @author author
	 * @Description 新增项目组
	 * @Date 2020/9/14
	 * @param tblProjectGroup
	 * @param request
	 * @return void
	 **/
	void saveProjectGroup(TblProjectGroup tblProjectGroup, HttpServletRequest request) throws Exception;

	/**
	 *
	 * @Title: insertTmpProject
	 * @Description: 测试库新增运维类项目
	 * @author author
	 * @param projectInfo
	 * @param request
	 * @param id
	 * @throws
	 */
	void insertTmpProject(TblProjectInfo projectInfo, HttpServletRequest request, Long id) throws Exception;

	/**
	 * @author author
	 * @Description 编辑项目同步测管平台数据
	 * @Date 2020/9/14
	 * @param projectInfo
	 * @param request
	 * @return void
	 **/
	void editTmpProject(TblProjectInfo projectInfo, HttpServletRequest request) throws Exception;

	/**
	 * @author author
	 * @Description 结束项目同步测管数据
	 * @Date 2020/9/14
	 * @param id
	 * @param request
	 * @return void
	 **/
	void endTmpProject(Long id, HttpServletRequest request) throws Exception;

	/**
	 * @author author
	 * @Description 删除项目组同步测管数据
	 * @Date 2020/9/14
	 * @param projectGroupId
	 * @param request
	 * @return void
	 **/
	void deleteTmpPeojectGroup(Long projectGroupId, HttpServletRequest request) throws Exception;

	/**
	 * @author author
	 * @Description 编辑项目组同步测管数据
	 * @Date 2020/9/14
	 * @param tblProjectGroup
	 * @param request
	 * @return void
	 **/
	void editTmpProjectGroup(TblProjectGroup tblProjectGroup, HttpServletRequest request) throws Exception;

	/**
	 * @author author
	 * @Description 新增项目组同步测管数据
	 * @Date 2020/9/14
	 * @param tblProjectGroup
	 * @param request
	 * @param id
	 * @return void
	 **/
	void saveTmpProjectGroup(TblProjectGroup tblProjectGroup, HttpServletRequest request, Long id) throws Exception;


	List<Map<String, Object>> getProjectUserPost(Long projectId, String userPostIds);




}
