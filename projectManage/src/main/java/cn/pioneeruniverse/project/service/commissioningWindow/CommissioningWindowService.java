package cn.pioneeruniverse.project.service.commissioningWindow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.project.entity.TblCommissioningWindow;
import cn.pioneeruniverse.project.entity.TblRequirementFeature;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.entity.TblSystemInfo;

public interface CommissioningWindowService {

	List<TblCommissioningWindow> selectCommissioningWindows(TblCommissioningWindow tblCommissioningWindow, String year, Integer page, Integer rows) throws Exception;

	List<String> selectWindowType() throws Exception;

	//新增窗口至itmp_db
	void insertCommissioningWindowItmp(TblCommissioningWindow commissioningWindow, HttpServletRequest request) throws Exception;
	//新增窗口至tmp_db
	void insertCommissioningWindowTmp(TblCommissioningWindow commissioningWindow) throws Exception;
	
	//修改itmp_db窗口状态
	HashMap<String, Object> delectCommissioningWindowItmp(Long id,Long status, HttpServletRequest request) throws Exception;
	//修改tmp_db窗口状态
	void delectCommissioningWindowTmp(HashMap<String, Object> map) throws Exception;

	TblCommissioningWindow selectCommissioningWindowById(Long id) throws Exception;
	
	//编辑投产窗口至itmp_db
	TblCommissioningWindow editCommissioningWindowItmp(TblCommissioningWindow commissioningWindow, HttpServletRequest request) throws Exception;
	//编辑投产窗口至itmp_db
	void editCommissioningWindowTmp(TblCommissioningWindow commissioningWindow) throws Exception;

	Map<String, Object> selectFeatureType() throws Exception;

	List<TblRequirementFeature> selectRequirement(TblRequirementFeature tblRequirementFeature, Integer pageNumber,
			Integer pageSize) throws Exception;

	void relationRequirement(Long windowId, Long[] ids, HttpServletRequest request) throws Exception;
	
	List<TblCommissioningWindow> findCommissioningByWindowDate(TblCommissioningWindow commissioningWindow);

	List<TblCommissioningWindow> selectAllWindows(TblCommissioningWindow tblCommissioningWindow, Integer pageNumber,
			Integer pageSize);

	List<TblSystemInfo> getSystemsByWindowId(Long windowId);

//	List<TblRequirementInfo> getExportRequirements(Long windowId, String systemIds);

	void export(List<TblRequirementInfo> list, List<TblRequirementFeature> feaList, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
