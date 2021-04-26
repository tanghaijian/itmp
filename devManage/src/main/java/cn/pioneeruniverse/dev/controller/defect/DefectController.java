package cn.pioneeruniverse.dev.controller.defect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.databus.DataBusRequestHead;
import cn.pioneeruniverse.common.databus.DataBusUtil;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.common.velocity.tag.VelocityDataDict;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectInfoMapper;
import cn.pioneeruniverse.dev.entity.DefectInfoVo;
import cn.pioneeruniverse.dev.entity.ExtendedField;
import cn.pioneeruniverse.dev.entity.TblDefectAttachement;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblDefectLog;
import cn.pioneeruniverse.dev.entity.TblDefectRemark;
import cn.pioneeruniverse.dev.entity.TblDefectRemarkAttachement;
import cn.pioneeruniverse.dev.service.CustomFieldTemplate.ICustomFieldTemplateService;
import cn.pioneeruniverse.dev.service.defect.test.DefectService;


/**
 * Description: 缺陷管理后端controller
 * Author:liushan
 * Date: 2018/12/10 下午 1:41
 */
@RestController
@RequestMapping(value = "defect")
public class DefectController extends BaseController {
    @Autowired
    private DefectService defectService;
    @Autowired
    private TblDefectInfoMapper defectInfoMapper;
    @Autowired
    private S3Util s3Util;
    private Long currentUserId;
    @Autowired
    private ICustomFieldTemplateService iCustomFieldTemplateService;

    // 回调databus：ITSM_UpdatePBMData
    @Value("${databuscc.defectName}")
    private String databusccName;

    
    /**
     * 
    * @Title: getDefectRecentLogById
    * @Description: 根据缺陷ID获取缺陷最近处理日志
    * @author author
    * @param defectId 缺陷Id
    * @return map key - devTask:工作任务
    *                   feature：开发任务
    *                   data: 缺陷日志
    *                   status :1正常返回，2异常返回
     */
    @RequestMapping(value = "getDefectRecentLogById",method = RequestMethod.POST)
    public Map<String, Object> getDefectRecentLogById(Long defectId){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (defectId == null){
                return handleException(new Exception(), "查询当前信息失败");
            }
            result = defectService.getDefectRecentLogById(defectId);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return handleException(e, "查询当前信息失败");
        }
        return result;
    }

    /**
     * 
    * @Title: getDefectLogById
    * @Description: 获取所有日志
    * @author author
    * @param defectId 缺陷ID
    * @return map key - data 日志
    *                   status 1正常返回，2异常返回
     */
    @RequestMapping(value = "getDefectLogById",method = RequestMethod.POST)
    public Map<String, Object> getDefectLogById(Long defectId){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (defectId == null){
                return handleException(new Exception(), "查询当前信息失败");
            }
            List<TblDefectLog> defectLogs = defectService.getDefectLogsById(defectId);
            result.put("data",defectLogs);
        } catch (Exception e) {
            return handleException(e, "查询当前信息失败");
        }
        return result;
    }

    /**
     * 
    * @Title: removeAtts
    * @Description: 移出缺陷附件
    * @author author
    * @param ids 附件ID
    * @param defectId 缺陷ID
    * @param logId 日志ID
    * @param request
    * @return key status 1正常，2异常
     */
    @RequestMapping(value = "removeAtts",method = RequestMethod.POST)
    public Map<String, Object> removeAtts(Long[] ids,Long defectId,Long logId,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (ids == null && logId != null ){
                return handleException(new Exception(), "删除附件失败");
            }
            defectService.removeAtts(ids,defectId,logId,request);
        } catch (Exception e) {
            return handleException(e, "修改缺陷失败");
        }
        return result;
    }

    /**
     * 
    * @Title: findAttList
    * @Description: 获取所有附件
    * @author author
    * @param defectId 缺陷ID
    * @return map key - field:扩展字段
    *                   attList:缺陷附件
    *                   defectInfo 缺陷
    *                   remarkAtts 备注附件
    *                   remarks 备注
     */
    @RequestMapping(value = "findAttList",method = RequestMethod.POST)
    public Map<String, Object> findAttList(Long defectId){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (defectId == null){
                return handleException(new Exception(), "查询附件失败");
            }
            List<TblDefectAttachement> defectAttachements = defectService.findAttListByDefectId(defectId);
            TblDefectInfo defectInfo = defectService.getDefectEntity(defectId);
            List<ExtendedField> extendedFields=iCustomFieldTemplateService.findFieldByDefect(defectId);
            List<TblDefectRemark> remarks = defectService.getRemarkByDefectId(defectId);
            List<TblDefectRemarkAttachement> remarkAtts = defectService.getRemarkAttsByDefectId(defectId);
            result.put("field", extendedFields);
            result.put("attList",defectAttachements);
            result.put("defectInfo",defectInfo);
            result.put("remarks", remarks);
            result.put("remarkAtts", remarkAtts);
        } catch (Exception e) {
            return handleException(e, "查询附件失败");
        }
        return result;
    }

    /**
     * 修改的操作
     * @param tblDefectInfo
     * @param defectRemark
     * @param request
     * @return logId 日志id
     */
    @RequestMapping(value = "updateDefectStatus",method = RequestMethod.POST)
    public Map<String, Object> updateDefectStatus(Long defectId, TblDefectInfo tblDefectInfo, TblDefectRemark defectRemark, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (defectId == null){
                return handleException(new Exception(), "修改缺陷失败");
            }
            tblDefectInfo.setId(defectId);
            Long logId = defectService.updateDefectStatus(tblDefectInfo,defectRemark,request);
            result.put("logId",logId);
        } catch (Exception e) {
            return handleException(e, "修改缺陷失败");
        }
        return result;
    }

    /**
     * 待确认的操作
     * @param tblDefectInfo
     * @param defectRemark
     * @param request
     * @return logId 日志id
     */
    @RequestMapping(value = "updateDefectwithTBC",method = RequestMethod.POST)
    public Map<String, Object> updateDefectwithTBC(Long defectId,Long oldAssignUserId,TblDefectInfo tblDefectInfo, TblDefectRemark defectRemark,Double actualWorkload,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        currentUserId = CommonUtil.getCurrentUserId(request);
        try {
			/*
			 * if(currentUserId != null && oldAssignUserId != null &&
			 * oldAssignUserId.longValue() != currentUserId.longValue()){
			 * result.put("status", "noPermission"); return result; }
			 */
            if (defectId == null){
                return handleException(new Exception(), "修改缺陷失败");
            }
            tblDefectInfo.setId(defectId);
            Long logId = defectService.updateDefectwithTBC(tblDefectInfo,defectRemark,actualWorkload,request);
            result.put("logId", logId);

            TblDefectInfo dblDefectInfo = defectInfoMapper.findDefectById(defectId);
            if(tblDefectInfo.getDefectStatus()==3&&dblDefectInfo.getCreateType() == 2){
                dblDefectInfo.setRejectReason(tblDefectInfo.getRejectReason());
                //组装databus报文
                String resultInfo = putDefectInfo(dblDefectInfo);
                DataBusUtil.send(databusccName,dblDefectInfo.getDefectCode(),resultInfo);
            }
        } catch (Exception e) {
            return handleException(e, "修改缺陷失败");
        }
        return result;
    }

    /**
    *@author liushan
    *@Description 修改缺陷
    *@Date 2020/7/29
    *@Param [tblDefectInfo, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    *     defectId  缺陷id
    *     logId   日志id
    *     mapSource  缺陷修改返回结果
    **/
    @RequestMapping(value = "updateDefect",method = RequestMethod.POST)
    public Map<String, Object> updateDefect(TblDefectInfo tblDefectInfo, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        currentUserId = CommonUtil.getCurrentUserId(request);
        try {
            Map<String, Object> idMap = defectService.updateDefect(tblDefectInfo,request);
            if(idMap.containsKey("defectId") && idMap.containsKey("logId")){
                result.put("defectId",idMap.get("defectId"));
                result.put("logId",idMap.get("logId"));//日志id
                result.put("mapSource",idMap);// 缺陷修改返回结果
            }
        } catch (Exception e) {
            return handleException(e, "修改缺陷失败");
        }
        return result;
    }

    /**
    *@author liushan
    *@Description 添加缺陷
    *@Date 2020/7/29
    *@Param [tblDefectInfo, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    *     result.put("defectId",idMap.get("defectId"));  缺陷id
    *     result.put("logId",idMap.get("logId"));// 日志id
    **/
    @RequestMapping(value = "insertDefect",method = RequestMethod.POST)
    public Map<String, Object> insertDefect(TblDefectInfo tblDefectInfo ,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            Map<String, Object> idMap = defectService.insertDefect(tblDefectInfo,request);
            result.put("defectId",idMap.get("defectId"));
            result.put("logId",idMap.get("logId"));
        } catch (Exception e) {
            return handleException(e, "新建缺陷失败");
        }
        return result;
    }

    /**
    *@author liushan
    *@Description 缺陷列表信息
    *@Date 2020/7/29
    *@Param [request, response, tblDefectInfo]
    *@return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.dev.entity.DefectInfoVo>
    **/
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public JqGridPage<DefectInfoVo> list(HttpServletRequest request,HttpServletResponse response,
                                          DefectInfoVo tblDefectInfo) {
        JqGridPage<DefectInfoVo> list = null;
        currentUserId = CommonUtil.getCurrentUserId(request);
        try {
             list = defectService.findDefectListPage(new JqGridPage<>(request, response),tblDefectInfo,currentUserId,request);
        } catch (Exception e) {
            logger.error("获取缺陷信息失败" + ":" + e.getMessage(), e);
            return null;
        }
        return list;
    }

    /**
    *@author liushan
    *@Description 缺陷实体类数据信息
    *@Date 2020/7/29
    *@Param [defectId]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    *      field:扩展字段
    *      defectInfo 缺陷
    **/
    @RequestMapping(value="getDefectEntity",method = RequestMethod.POST)
    public Map<String, Object> getDefectEntity(Long defectId){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (defectId == null){
                return handleException(new Exception(), "修改缺陷失败");
            }
            // 扩展字段
            List<ExtendedField> extendedFields=iCustomFieldTemplateService.findFieldByDefect(defectId);
            TblDefectInfo defectInfo = defectService.getDefectEntity(defectId);
            result.put("defectInfo",defectInfo);
            result.put("field", extendedFields);
        } catch (Exception e) {
            return handleException(e, "新建缺陷失败");
        }
        return result;
    }

    /**
     * 逻辑删除缺陷
     * @param id 缺陷id
     * @return Map<String,Object>
     */
    @RequestMapping(value = "removeDefect", method = RequestMethod.POST)
    public Map<String,Object> removeDefect(Long id,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            defectService.removeDefect(id,request);
        } catch (Exception e) {
            return handleException(e, "删除缺陷信息失败");
        }
        return result;
    }


    /**
     * 
    * @Title: updateFiles
    * @Description: 上传文件
    * @author author
    * @param files 附件
    * @param defectId 缺陷id
    * @param logId 日志id
    * @param request
    * @return Map<String,Object>
     */
    @RequestMapping(value="updateFiles")
    public Map<String, Object> updateFiles(@RequestParam("files") MultipartFile[] files,@RequestParam("id") Long defectId,@RequestParam("logId") Long logId, HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            String str = defectService.updateFiles(files,defectId,logId,request);
            if (str.equals("NULL")){
                return handleException(new Exception(), "上传文件信息失败");
            }
        } catch (Exception e) {
            return handleException(e, "上传文件信息失败");
        }
        return result;
    }

    /**
     * 上传备注文件
     * @param files
     * @param request
     * @return Map<String, Object>
     */
    @RequestMapping(value="updateRemarkLogFiles")
    public Map<String, Object> updateRemarkLogFiles(@RequestParam("files") MultipartFile[] files,@RequestParam("logId") Long logId,HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            String str = defectService.updateRemarkLogFiles(files,logId,request);
            if (str.equals("NULL")){
                return handleException(new Exception(), "上传文件信息失败");
            }
        } catch (Exception e) {
            return handleException(e, "上传文件信息失败");
        }
        return result;
    }

    /**
     * 下载文件
     */
    @RequestMapping(value= "downloadFile")
    public void downloadFile(TblDefectAttachement defectAttachement,HttpServletResponse response) {
        s3Util.downObject(defectAttachement.getFileS3Bucket(), defectAttachement.getFileS3Key(), defectAttachement.getFileNameOld(), response);
    }

    /**
     * 导出当前页面查询出的结果集
     * @param defectInfoVo
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "export")
    public void export(DefectInfoVo defectInfoVo,HttpServletResponse response,HttpServletRequest request) throws Exception{
       defectService.export(new JqGridPage<DefectInfoVo>(request, response),defectInfoVo,response,request) ;
   }
    
    /**
    *@author liushan
    *@Description 保存备注
    *@Date 2020/7/31
    * @param remark
    * @param attachFiles
    * @param request
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @PostMapping(value = "addDefectRemark")
	public Map<String, Object> addTaskRemark(String remark, String attachFiles, HttpServletRequest request) {
    	 Map<String, Object> result = new HashMap<>();
         result.put("status", Constants.ITMP_RETURN_SUCCESS);
         List<TblDefectRemarkAttachement> files = JsonUtil.fromJson(attachFiles,
 				JsonUtil.createCollectionType(ArrayList.class, TblDefectRemarkAttachement.class));
		try {
			TblDefectRemark tblDefectRemark = JSON.parseObject(remark, TblDefectRemark.class);
			defectService.addDefectRemark(tblDefectRemark,files, request);
		} catch (Exception e) {
			 return handleException(e, "保存备注失败");
		}
		 return result;
	}

	/**
	*@author author
	*@Description 组装databus报文
	*@Date 2020/7/31
	 * @param tblDefectInfo
	*@return java.lang.String
	**/
    private String putDefectInfo(TblDefectInfo tblDefectInfo){
        Map<String, Object> mapAll = new LinkedHashMap<>();
        Map<String,Object> requestBody = new HashMap<>();
        VelocityDataDict dict= new VelocityDataDict();

        String comment = "";
        requestBody.put("problemNumber",tblDefectInfo.getDefectCode());
        requestBody.put("operationType",1);
        Map<String,String> rejectReason = dict.getDictMap("TBL_DEFECT_INFO_REJECT_REASON");
        for(Map.Entry<String, String> entry : rejectReason.entrySet()){
            if(entry.getKey().equals(tblDefectInfo.getRejectReason().toString())){
                comment = entry.getValue();
            }
        }
        requestBody.put("comment",comment);
        requestBody.put("releaseTime","");
        mapAll.put("requestHead",DataBusRequestHead.getRequestHead());
        mapAll.put("requestBody",requestBody);
        String result = JSON.toJSONString(mapAll, SerializerFeature.WriteDateUseDateFormat);
        return result;
    }

    /**
    *@author liushan
    *@Description 修改缺陷投产窗口
    *@Date 2020/7/31
    * @param defectId
    * @param commissioningWindowId
    * @param request
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @PostMapping(value = "updateCommissioningWindowId")
    public Map<String, Object> updateCommissioningWindowId(Long defectId,Long commissioningWindowId, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            defectService.updateCommssioningWindowId(defectId, commissioningWindowId);
            result.put("status",  Constants.ITMP_RETURN_SUCCESS);
        }catch (Exception e){
            return handleException(e, "修改投产窗口失败");
        }
        return result;
    }

    /**
    *@author liushan
    *@Description 关闭缺陷
    *@Date 2020/7/31
    * @param defectId 缺陷id
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @PostMapping(value = "closeDefect")
    public Map<String, Object> closeDefect(Long defectId) {
        Map<String, Object> result = new HashMap<>();
        try {
            defectService.closeDefect(defectId);
            result.put("status",  Constants.ITMP_RETURN_SUCCESS);
        }catch (Exception e){
            return handleException(e, "关闭缺陷失败");
        }
        return result;
    }
}


