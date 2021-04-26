package cn.pioneeruniverse.dev.service.testArchivedCase;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import cn.pioneeruniverse.dev.entity.TblArchivedCase;
import cn.pioneeruniverse.dev.entity.TblCaseInfo;

public interface TestArchivedCaseService {

	/**
	 * @author author
	 * @Description 获取归档案例
	 * @Date 2020/9/8
	 * @param tblArchivedCase
	 * @param filters
	 * @param page
	 * @param rows
	 * @param request
	 * @return java.util.List<cn.pioneeruniverse.dev.entity.TblArchivedCase>
	 **/
	List<TblArchivedCase> getArchivedCases(TblArchivedCase tblArchivedCase, String filters, Integer page, Integer rows, HttpServletRequest request);
	/**
	 * @author author
	 * @Description 案例详情
	 * @Date 2020/9/8
	 * @param id
	 * @return cn.pioneeruniverse.dev.entity.TblArchivedCase
	 **/
	TblArchivedCase getArchivedCaseById(Long id);
	/**
	 * @author author
	 * @Description 新增测试案例
	 * @Date 2020/9/8
	 * @param tblCaseInfo
	 * @param request
	 * @return void
	 **/
	void insertTestCase(TblCaseInfo tblCaseInfo, HttpServletRequest request);
	/**
	 * @author author
	 * @Description 移除归档案例(直接删除归档案例，
	 * 同时把测试案例的归档状态改为未归档，
	 * 之所以删除是因为移除之后还可以再归档回来不删除就不能实现这个操作)
	 * @Date 2020/9/8
	 * @param ids
	 * @param request
	 * @return void
	 **/
	void removeArchivedTest(List<Long> ids, HttpServletRequest request);
	/**
	 * @author author
	 * @Description 下载模板
	 * @Date 2020/9/8
	 * @param request
	 * @param response
	 * @return void
	 **/
	void exportTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception;
	/**
	 * 归档案例导入 2020-06-28
	 * @param file
	 * @param caseCatalogId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> importExcel(MultipartFile file,Integer caseCatalogId, HttpServletRequest request) throws Exception;
	/**
	 * @author author
	 * @Description 查询出要导出的归档案例和步骤
	 * @Date 2020/9/8
	 * @param tblArchivedCase
	 * @param filters
	 * @return java.util.List<cn.pioneeruniverse.dev.entity.TblArchivedCase>
	 **/
	List<TblArchivedCase> getCaseAndSteps(TblArchivedCase tblArchivedCase, String f);
	/**
	 * 归档案例导出  2020 - 06 - 28
	 * @param list
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	void exportExcel(List<TblArchivedCase> list, HttpServletRequest request, HttpServletResponse response) throws Exception;
	/**
	 * @author author
	 * @Description 编辑案例
	 * @Date 2020/9/8
	 * @param caseInfo
	 * @param request
	 * @return void
	 **/
	void editCaseInfo(TblCaseInfo caseInfo, HttpServletRequest request);
	/**
	 * @author author
	 * @Description 编辑案例的同时也更新归档案例
	 * @Date 2020/9/8
	 * @param caseInfo
	 * @param request
	 * @return void
	 **/
	void editArchivedCase(TblCaseInfo caseInfo, HttpServletRequest request);

}
