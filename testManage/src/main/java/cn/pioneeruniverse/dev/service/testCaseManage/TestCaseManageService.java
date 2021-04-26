package cn.pioneeruniverse.dev.service.testCaseManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import cn.pioneeruniverse.dev.entity.TblCaseCatalog;
import cn.pioneeruniverse.dev.entity.TblCaseInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.entity.TblTestSetCase;
import cn.pioneeruniverse.dev.entity.TblUserInfo;

public interface TestCaseManageService {

	List<TblCaseInfo> getCaselist(TblCaseInfo tblCaseInfo, String filters, Integer page, Integer rows, HttpServletRequest request);

	List<TblSystemInfo> getAllSystem(String systemName);

	TblCaseInfo insertCaseInfo(TblCaseInfo tblCaseInfo, HttpServletRequest request);

	void deleteCaseInfo(List<Long> ids, HttpServletRequest request);

	TblCaseInfo selectCaseInfoById(Long id);

	void exportExcel(List<TblCaseInfo> list, HttpServletRequest request, HttpServletResponse response) throws Exception;

	void editCaseInfo(TblCaseInfo caseInfo, HttpServletRequest request);

	HashMap<String, Object> getCaseInfo(Long testSetId, TblCaseInfo caseInfo,Integer page, Integer rows);

//	List<TblCaseInfo> importExcel(String xlsPath, HttpServletRequest request, HttpServletResponse response) throws Exception;

//	void saveCaseInfo(List<TblCaseInfo> list);

	void archivingCase(List<String> ids, HttpServletRequest request);

	List<String> getArchivedCaseIds();

	void deleteArchivedCase(List<Long> ids, HttpServletRequest request);

//	void editArchivedCase(TblCaseInfo caseInfo, HttpServletRequest request);

	Long getIdByCaseNumber(String caseNumber);

	Map<String, Object> importExcel(MultipartFile file,Integer caseCatalogId, HttpServletRequest request) throws Exception;

	void exportTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception;

	void updateCaseStep(String testSetCase, String testCaseStep, Long testUserId) throws Exception;

	List<TblCaseInfo> getCaseAndSteps(TblCaseInfo tblCaseInfo, String f);

	/**
	 * 获取案例树
	 * @param systemId
	 * @return
	 */
	Map<String, Object> getCaseTreeBySystemId(Long systemId,HttpServletRequest request);

	/**
	 * 新增或修改案例目录
	 * @param tblCaseCatalog
	 */
	Map<String, Object> addOrUpdateCaseCatalog(TblCaseCatalog tblCaseCatalog,HttpServletRequest request);

	/**
	 * 根据系统id获取案例目录
	 * @return
	 */
	List<TblCaseCatalog> getCatalogBySystemId(Long systemId);
}
