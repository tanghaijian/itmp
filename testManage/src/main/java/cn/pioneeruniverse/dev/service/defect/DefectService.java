package cn.pioneeruniverse.dev.service.defect;


import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.dev.dto.AssetSystemTreeDTO;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.vo.DefectInfoVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 * Author:liushan
 * Date: 2018/12/10 下午 1:44
 */
public interface DefectService {
    /**
     *@author liushan
     *@Description 缺陷列表
     *@Date 2020/7/29
     *@Param [page, defectInfo, request]
     *@return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.dev.vo.DefectInfoVo>
     **/
    JqGridPage<DefectInfoVo> findDefectListPage(JqGridPage<DefectInfoVo> page, DefectInfoVo tblDefectInfo, HttpServletRequest request) throws Exception;

    /**
    *@author author
    *@Description 获取需求
    *@Date 2020/8/19
     * @param requirementInfo 查询条件
 * @param pageNumber
 * @param pageSize
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String, Object> getAllRequirement(TblRequirementInfo requirementInfo, Integer pageNumber, Integer pageSize) throws Exception;

    /**
    *@author author
    *@Description  新增缺陷
    *@Date 2020/8/19
     * @param tblDefectInfo
 * @param files
 * @param request
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String, Object> insertDefect(TblDefectInfo tblDefectInfo,MultipartFile[] files, HttpServletRequest request)throws Exception;

    Map<String, Object>  updateDefect(MultipartFile[] files,TblDefectInfo tblDefectInfo, HttpServletRequest request, String removeAttIds, Map projectIdMap)throws Exception;

    Long updateDefectwithTBC(TblDefectInfo tblDefectInfo, TblDefectRemark defectRemark, HttpServletRequest request)throws Exception;

    Long updateDefectStatus(TblDefectInfo tblDefectInfo, TblDefectRemark defectRemark, HttpServletRequest request)throws Exception;

    Map<String, Object> getAllSystemInfo(TblSystemInfo systemInfo, Integer pageNumber, Integer pageSize)throws Exception;

    void removeDefect(Long id,HttpServletRequest request)throws Exception;

    void removeDefectById(Long id)throws Exception;

    //String updateFiles(MultipartFile[] files, Long defectId, Long logId, HttpServletRequest request) throws Exception;

    Map<String, Object> updateFiles(MultipartFile[] files, Long defectId, Long logId, HttpServletRequest request) throws Exception;

    TblDefectAttachement getDefectAttByUrl(String path)throws Exception;

    void updateItmpDefectByWorkId(TblDefectInfo tblDefectInfo)  throws Exception;

    void updateTmpDefectByWorkId(TblDefectInfo tblDefectInfo)  throws Exception;

    List<TblDefectAttachement> findAttListByDefectId(Long defectId)throws Exception;

    void removeAtts(Long[] ids, Long defectId,Long logId, HttpServletRequest request)throws Exception;

    String updateRemarkLogFiles(MultipartFile[] files, Long logId, HttpServletRequest request) throws Exception;

    List<TblDefectLog> getDefectLogsById(Long id)throws Exception;

    Map<String, Object> getDefectRecentLogById(Long defectId)throws Exception;

    Map<String, Object> getAllTestTask(TblTestTask testTask, Integer pageNumber, Integer pageSize,HttpServletRequest request)throws Exception;

    Map<String, Object> getAllComWindow(TblCommissioningWindow window, Integer pageNumber, Integer pageSize)throws Exception;

    TblDefectInfo getDefectById(Long defectId);

    void deleteDefect(Long id, HttpServletRequest request) throws Exception;

    TblDefectInfo getDefectEntity(Long defectId);

    Map<String,Object> getAllProject(HttpServletRequest request);

    List<TblProjectInfo> getProject(Long systemId);

    void export(JqGridPage<DefectInfoVo> tJqGridPage, DefectInfoVo defectInfoVo, HttpServletResponse response, HttpServletRequest request,TblCustomFieldTemplate tblCustomFieldTemplate,List<AssetSystemTreeDTO> assetSystemTreeDTO, List<AssetSystemTreeDTO> assetSystemTreeDTOList,List<AssetSystemTreeDTO> dataDicAll) throws Exception;

    Map<String,Object> getDicKey(String key);

	void addDefectRemark(TblDefectRemark tblDefectRemark, List<TblDefectRemarkAttachement> files, HttpServletRequest request);

	List<TblDefectRemark> getRemarkByDefectId(Long defectId);

	List<TblDefectRemarkAttachement> getRemarkAttsByDefectId(Long defectId);

	void addItmpDefectRemark(TblDefectRemark tblDefectRemark, List<TblDefectRemarkAttachement> files,
			HttpServletRequest request);


	void insertTmpDefect(String defectData, HttpServletRequest request, HttpServletResponse response) throws Exception;

    void insertItmpDefect(TblDefectInfo tblDefectInfo,Integer type) throws Exception;

    //查询tbl_custom_field_template表中字段custom_form='tbl_defect_info'中的值
    TblCustomFieldTemplate selectTblCustomFieldTemplateByTblDefectInfo();

    /**
     * 查询资产系统树表中的数据
     */
    List<AssetSystemTreeDTO> selectAssetSystemTreeAll();

    /**
     * 查询系统版本表中的数据
     * @return
     */
    List<AssetSystemTreeDTO> selectSystemVersionAll();

    /**
     *  查询数据字典表中的数据
     * @return
     */
    List<AssetSystemTreeDTO> selectDataDicAll();

	List<TblDefectInfo> getDefectByCurrentUser(Long uid);


    /**
     *  获取系统下的所有项目
     * @param systemId
     * @return
     */
    Map getProjectIdList(Long systemId);

    void updateOrInsertItmpDefectFile(TblDefectAttachement defectAttachement,TblAttachementInfoDTO attachement) throws Exception;

    /**
    *@author liushan
    *@Description 缺陷同步jira附件
    *@Date 2020/5/11
    *@Param [defectCode, request]
    *@return java.util.List<java.lang.String>
    **/
    Set<String> jiraFileByCode(String defectJiraIds, HttpServletRequest request) throws Exception;
}
