package cn.pioneeruniverse.project.service.requirement;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import cn.pioneeruniverse.project.entity.ExtendedField;
import cn.pioneeruniverse.project.entity.TblRequirementAttachement;
import cn.pioneeruniverse.project.entity.TblRequirementAttention;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;

public interface RequirementService {

	List<TblRequirementInfo> getAllRequirement(TblRequirementInfo requirement, Integer pageIndex, Integer pageSize,List<String> roleCodes);
	
	int getCountRequirement(TblRequirementInfo record,List<String> roleCodes);
	
	Map<String, Object> findRequirementById(Long id,Long parentId);
	
	List<TblRequirementInfo> findRequirementByName(TblRequirementInfo requirementInfo);
	
	Map<String, Object> toEditRequirementById(Long id);
	
	String selectMaxRqeuirementCode();
	//新增需求至itmp_db
	Map<String, Object> insertRequirementItmp(TblRequirementInfo record,HttpServletRequest request);
	//新增需求至tmp_db
	void insertRequirementTmp(TblRequirementInfo requirementInfo);
	
	//编辑需求至itmp_db
	Map<String, Object> updateRequirementItmp(TblRequirementInfo record,HttpServletRequest request);
	///编辑需求至tmp_db
	void updateRequirementTmp(TblRequirementInfo requirementInfo);
	
	//查询需求关注
	List<TblRequirementAttention> getAttentionList(TblRequirementAttention attention);
	
	void changeAttention(Long id, Integer attentionStatus, HttpServletRequest request);
	
	//上传需求附件至itmp_db
	void uploadFileItmp(MultipartFile[] files, Long reqId, HttpServletRequest request) throws Exception;
	//上传需求附件至tmp_db
	void uploadFileTmp(List<TblRequirementAttachement> listAtta) throws Exception;
	
	//删除itmp_db需求附件
	void removeAttItmp(TblRequirementAttachement atta, HttpServletRequest request);
	//删除tmp_db需求附件
	void removeAttTmp(TblRequirementAttachement atta);
		
	//同步需求至itmp_db	
	Map<String, Object> updateRequirementDataItmp(String requirementData,String reqSystemList);
	//同步需求至tmp_db
	void updateRequirementDataTmp(TblRequirementInfo tblRequirementInfo);
		
    int deleteRequirement(Long id);
    
    List<TblRequirementAttachement> getRequirementAttachement(Long id);

	List<Map<String, Object>> getAllRequirement2(TblRequirementInfo requirement, int i, int maxValue);

	int getAllRequirementCount(TblRequirementInfo requirement);

	List<ExtendedField> findRequirementField(Long id);

	void sendAddMessage(HttpServletRequest request, TblRequirementInfo requirementInfo);

	void sendEditMessage(HttpServletRequest request, TblRequirementInfo requirementInfo, List<TblRequirementAttention> attentionList);

	List<String> getRequirementsByIds(String reqIds);

	List<String> getsystems(Long id);
	
}
