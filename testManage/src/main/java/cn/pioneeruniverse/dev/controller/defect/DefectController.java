package cn.pioneeruniverse.dev.controller.defect;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.databus.DataBusRequestHead;
import cn.pioneeruniverse.common.databus.DataBusUtil;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.*;
import cn.pioneeruniverse.common.velocity.tag.VelocityDataDict;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.dto.AssetSystemTreeDTO;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.service.CustomFieldTemplate.ICustomFieldTemplateService;
import cn.pioneeruniverse.dev.service.defect.DefectService;
import cn.pioneeruniverse.dev.vo.DefectInfoVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;


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
    private TblRequirementFeatureMapper requirementFeatureMapper;
    @Autowired
    private TblDefectInfoMapper defectInfoMapper;
    @Autowired
    private S3Util s3Util;
    private Long currentUserId;
    @Autowired
    private ICustomFieldTemplateService iCustomFieldTemplateService;
    @Autowired
    private RedisUtils redisUtils;
    @Value("${databuscc.defectName}")
    private String databusccName;
    /**
     * 根据id查询当前日志信息
     * @param defectId
     * @return data 日志数据
     *         feature 工作任务
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
     * 根据id查询当前日志信息
     * @param defectId
     * @return  "data" defectLogs 日志
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
     * 移除附件
     * @param ids
     * @return
     */
    @RequestMapping(value = "removeAtts",method = RequestMethod.POST)
    public Map<String, Object> removeAtts(Long[] ids,Long defectId,Long logId,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            currentUserId = CommonUtil.getCurrentUserId(request);
            if (ids == null && logId != null ){
                return handleException(new Exception(), "删除附件失败");
            }
            TblDefectInfo defectInfo = defectService.getDefectById(defectId);
            if(defectInfo != null && defectInfo.getAssignUserId() != null && currentUserId != null && defectInfo.getAssignUserId().longValue() != currentUserId.longValue() && defectInfo.getSubmitUserId() != null && defectInfo.getSubmitUserId().longValue() != currentUserId.longValue()){
                result.put("status", "noPermission"); // 没有权限
                return result;
            }
            defectService.removeAtts(ids,defectId,logId,request);
        } catch (Exception e) {
            return handleException(e, "修改缺陷失败");
        }
        return result;
    }

    /**
     * 编辑查询附件
     * @param defectId
     * @return
     * result.put(field, extendedFields); // 自定义字段
     * result.put("feature",feature); // 开发任务
*             result.put("attList",defectAttachements);  附件
*             result.put("defectInfo",defectInfo); 缺陷
*             result.put("remarks", remarks); 备注
*             result.put("remarkAtts", remarkAtts); 备注附件
     */
    @RequestMapping(value = "findAttList",method = RequestMethod.POST)
    public Map<String, Object> findAttList(Long defectId){
        logger.info("defectId:"+defectId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (defectId == null){
                return handleException(new Exception(), "查询附件失败");
            }
            List<TblDefectAttachement> defectAttachements = defectService.findAttListByDefectId(defectId);
            TblDefectInfo defectInfo = defectService.getDefectEntity(defectId);
            // 根据工作任务，查询测试任务信息
            if(defectInfo.getTestTaskId() != null ){
                TblRequirementFeature feature = requirementFeatureMapper.getFeatureByTestTaskId(defectInfo.getTestTaskId());
                result.put("feature",feature);
            }
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
     * @return result.put("status", "noPermission"); // 无权限
     *  result.put("logId",logId); 日志id
     */
    @RequestMapping(value = "updateDefectStatus",method = RequestMethod.POST)
    public Map<String, Object> updateDefectStatus(Long defectId, TblDefectInfo tblDefectInfo, TblDefectRemark defectRemark, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            currentUserId = CommonUtil.getCurrentUserId(request);
            if(currentUserId != null && tblDefectInfo.getSubmitUserId() != null && tblDefectInfo.getSubmitUserId().longValue() != currentUserId.longValue() && tblDefectInfo.getFlag() != null && tblDefectInfo.getFlag() == "false"){
                result.put("status", "noPermission"); // 无权限
                return result;
            }

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
     * @return result.put("status", "noPermission"); // 无权限
     *  result.put("logId",logId); 日志id
     */
    @RequestMapping(value = "updateDefectwithTBC",method = RequestMethod.POST)
    public Map<String, Object> updateDefectwithTBC(Long defectId,TblDefectInfo tblDefectInfo, TblDefectRemark defectRemark, String oldStatus,String newStatus,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            currentUserId = CommonUtil.getCurrentUserId(request);
            if(currentUserId != null && tblDefectInfo.getSubmitUserId() != null && tblDefectInfo.getSubmitUserId().longValue() != currentUserId.longValue() && tblDefectInfo.getOldAssignUserId() != null && tblDefectInfo.getOldAssignUserId().longValue() != currentUserId.longValue() && tblDefectInfo.getFlag() != null && tblDefectInfo.getFlag() == "false"){
                result.put("status", "noPermission");
                return result;
            }

            if (defectId == null){
                return handleException(new Exception(), "修改缺陷失败");
            }
            tblDefectInfo.setId(defectId);
            Long logId = defectService.updateDefectwithTBC(tblDefectInfo,defectRemark,request);
            result.put("logId", logId);

            TblDefectInfo dblDefectInfo = defectInfoMapper.findDefectById(defectId);
            if(tblDefectInfo.getDefectStatus()==3&&dblDefectInfo.getCreateType() == 2){
                dblDefectInfo.setRejectReason(tblDefectInfo.getRejectReason());
                String resultInfo = putDefectInfo(dblDefectInfo);
                DataBusUtil.send(databusccName,dblDefectInfo.getDefectCode(),resultInfo);
            }
        } catch (Exception e) {
            return handleException(e, "修改缺陷失败");
        }
        return result;
    }

    /**
    *@author author
    *@Description 修改缺陷
    *@Date 2020/8/19
     * @param files 附件
 * @param defectInfo 缺陷
 * @param request
 * @param removeAttIds 需要移除的附件ids
    *@return java.util.Map<java.lang.String,java.lang.Object>
     *        * @return result.put("status", "noPermission"); // 无权限
     *    result.put("defectId",idMap.get("defectId")); // 缺陷id
 *                 result.put("logId",idMap.get("logId")); 日志id
 *                 result.put("mapDataSource",idMap); 缺陷信息
     *
 *                 result.put("projectIdList",projectIdList); 项目信息
 *                 result.put("systemId",tblDefectInfo.getSystemId()); 系统id
    **/
    @RequestMapping(value = "updateDefect",method = RequestMethod.POST)
    public Map<String, Object> updateDefect(@RequestParam("files") MultipartFile[] files,
                                            String defectInfo,
                                            HttpServletRequest request,
                                            String removeAttIds){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            currentUserId = CommonUtil.getCurrentUserId(request);
            TblDefectInfo tblDefectInfo = new TblDefectInfo();
            if(StringUtils.isNotBlank(defectInfo)){
               tblDefectInfo = JSONObject.parseObject(defectInfo, TblDefectInfo.class);
            }
            if(tblDefectInfo != null && currentUserId != null && tblDefectInfo.getSubmitUserId() != null && tblDefectInfo.getOldAssignUserId() != null && tblDefectInfo.getSubmitUserId().longValue() != currentUserId.longValue() && tblDefectInfo.getOldAssignUserId().longValue() != currentUserId.longValue() && tblDefectInfo.getFlag() != null && tblDefectInfo.getFlag() == "false"){
                result.put("status", "noPermission");
                return result;
            }
            if(tblDefectInfo != null ){
                Map projectIdList = defectService.getProjectIdList(tblDefectInfo.getSystemId());
                Map<String, Object> idMap = defectService.updateDefect(files,tblDefectInfo,request,removeAttIds, projectIdList);
                result.put("defectId",idMap.get("defectId"));
                result.put("logId",idMap.get("logId"));
                result.put("mapDataSource",idMap);
                result.put("projectIdList",projectIdList);
                result.put("systemId",tblDefectInfo.getSystemId());
            }
        } catch (Exception e) {
            return handleException(e, "修改缺陷失败");
        }

        return result;
    }

    /**
    *@author author
    *@Description 新增缺陷
    *@Date 2020/8/20
     * @param files 附件
     * @param defectInfo 缺陷信息
     * @param request
    *@return java.util.Map<java.lang.String,java.lang.Object>
     *      result.put("defectId",idMap.get("defectId")); 缺陷id
     *      result.put("defectData",idMap.get("defectData")); 缺陷数据
     *      result.put("logId",idMap.get("logId")); 日志id
    **/
    @RequestMapping(value = "insertDefect",method = RequestMethod.POST)
    public Map<String, Object> insertDefect(@RequestParam("files") MultipartFile[] files,
                                            String defectInfo,
                                            HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if(StringUtils.isNotBlank(defectInfo)){
                TblDefectInfo tblDefectInfo = JSONObject.parseObject(defectInfo, TblDefectInfo.class);
                Map<String, Object> idMap = defectService.insertDefect(tblDefectInfo,files,request);
                result.put("defectId",idMap.get("defectId"));
                result.put("defectData",idMap.get("defectData"));
                result.put("logId",idMap.get("logId"));
            }
        } catch (Exception e) {
            return handleException(e, "新建缺陷失败");
        }
        return result;
    }


    /**
     *  测试缺陷管理
     * @param request
     * @param response
     * @param defectInfoVo
     * @return 缺陷列表 Grid 格式
     */
    @RequestMapping(value="list",method = RequestMethod.POST)
    public JqGridPage<DefectInfoVo> list(HttpServletRequest request,HttpServletResponse response,DefectInfoVo defectInfoVo){
        JqGridPage<DefectInfoVo> jqGridPage = null;
        try {

            jqGridPage = defectService.findDefectListPage(new JqGridPage<>(request, response),defectInfoVo, request);
        }catch (Exception e){
            logger.error("message" + ":" + e.getMessage(), e);
            return null;
        }
        return jqGridPage;
    }

    /**
    *@author author
    *@Description 缺陷实体类信息
    *@Date 2020/8/20
     * @param defectId
    *@return java.util.Map<java.lang.String,java.lang.Object>
     *      result.put("field", extendedFields); 自定义字段
     *             result.put("defectInfo",defectInfo); 实体类
    **/
    @RequestMapping(value="getDefectEntity",method = RequestMethod.POST)
    public Map<String, Object> getDefectEntity(Long defectId){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (defectId == null){
                return handleException(new Exception(), "修改缺陷失败");
            }
            List<ExtendedField> extendedFields=iCustomFieldTemplateService.findFieldByDefect(defectId);
            TblDefectInfo defectInfo = defectService.getDefectEntity(defectId);
            result.put("field", extendedFields);
            result.put("defectInfo",defectInfo);
        } catch (Exception e) {
            return handleException(e, "新建缺陷失败");
        }
        return result;
    }


    /**
     * 逻辑删除缺陷
     * @param id
     * @return
     * result.put("status", "noPermission"); 无权限
     */
    @RequestMapping(value = "removeDefect", method = RequestMethod.POST)
    public Map<String,Object> removeDefect(Long id,Long submitUserId,String flag,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            currentUserId = CommonUtil.getCurrentUserId(request);
            if(submitUserId != null && currentUserId != null && submitUserId.longValue() != currentUserId.longValue() && flag != null && flag == "false"){
                result.put("status", "noPermission");
                return result;
            }
            defectService.removeDefect(id,request);
        } catch (Exception e) {
            return handleException(e, "删除缺陷信息失败");
        }
        return result;
    }

    /**
    *@author author
    *@Description 删除缺陷
    *@Date 2020/8/20
     * @param id
 * @param request
    *@return void
    **/
    @RequestMapping(value = "deleteDefect", method = RequestMethod.POST)
    public void deleteDefect(Long id,HttpServletRequest request){
        try {
            defectService.deleteDefect(id,request);
        } catch (Exception e) {
            handleException(e, "删除缺陷信息失败");
        }
    }

    /**
    *@author author
    *@Description 上传文件废弃
    *@Date 2020/8/20
     * @param files 附件
 * @param defectId 缺陷id
 * @param logId 日志id
 * @param request
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value="updateFiles")
    public Map<String, Object> updateFiles(@RequestParam("files") MultipartFile[] files,
                                           @RequestParam("id") Long defectId,
                                           @RequestParam("logId") Long logId,
                                           HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
           /* String str = defectService.updateFiles(files,defectId,logId,request);
            if (str.equals("NULL")){
                return handleException(new Exception(), "上传文件信息失败");
            }*/
        } catch (Exception e) {
            return handleException(e, "上传文件信息失败");
        }
        return result;
    }

    /**
    *@author author
    *@Description 上传备注文件
    *@Date 2020/8/20
     * @param files 附件
 * @param logId 日志id
 * @param request
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value="updateRemarkLogFiles")
    public Map<String, Object> updateRemarkLogFiles(@RequestParam("files") MultipartFile[] files,
                                                    @RequestParam("logId") Long logId,
                                                    HttpServletRequest request){
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
     * @param
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "export")
    public void export(DefectInfoVo defectInfoVo,HttpServletResponse response,HttpServletRequest request) throws Exception{
        //获取tbl_custom_field_template表中字段custom_form='tbl_defect_info'中的值
        TblCustomFieldTemplate tblCustomFieldTemplate = defectService.selectTblCustomFieldTemplateByTblDefectInfo();
        List<AssetSystemTreeDTO> assetSystemTreeDTOS = defectService.selectAssetSystemTreeAll();
        List<AssetSystemTreeDTO> systemVersion = defectService.selectSystemVersionAll();
        List<AssetSystemTreeDTO> dataDicAll = defectService.selectDataDicAll();
        defectService.export(new JqGridPage<>(request, response),defectInfoVo,response,request,tblCustomFieldTemplate,assetSystemTreeDTOS,systemVersion,dataDicAll) ;
    }
    
    /**
    *@author author
    *@Description 保存备注
    *@Date 2020/8/20
     * @param remark 备注
 * @param attachFiles 备注附件
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
			// TODO Auto-generated catch block
			 return handleException(e, "保存备注失败");
		}
		 return result;
	}

    /**
     *@Description 同步itsm生产问题信息
     *@Date 2020/7/21
     *@Param [defectData] 报文
     *@return
     **/
	@RequestMapping(value = "updateDefectData",method = RequestMethod.POST)
	public void synDefect(@RequestBody String defectData,HttpServletRequest request,HttpServletResponse response) {
		String ip = HttpUtil.getIPAddress(request);
		logger.info("来访IP:"+ip);
		try {
			logger.info("开始同步缺陷信息数据"+defectData);
			if (!StringUtils.isBlank(defectData)) {
                defectService.insertTmpDefect(defectData,request,response);

                Map<String,Object> head= new HashMap<>();
                Map<String,Object> map1= new HashMap<>();
                map1.put("consumerSeqNo","itmgr");
                map1.put("status",0);
                map1.put("seqNo","");
                map1.put("providerSeqNo","");
                map1.put("esbCode","");
                map1.put("esbMessage","");
                map1.put("appCode","0");
                map1.put("appMessage","同步需求信息成功");
                head.put("responseHead",map1);
                PrintWriter writer = response.getWriter();
                writer.write(new JSONObject(head).toJSONString());
			}
		} catch (Exception e) {
			handleException(e, "同步缺陷信息失败");
		}
	}

	/**
	*@author liushan
	*@Description 缺陷同步jira附件
	*@Date 2020/5/11
	*@Param [defectCode, request]
	*@return java.util.Map<java.lang.String,java.lang.Object>
	**/
    @RequestMapping(value = "jiraFileByCode",method = RequestMethod.POST)
    public Map<String, Object> jiraFileByCode(String defectJiraIds,@RequestParam(required = false) String key,HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            Set<String> code = defectService.jiraFileByCode(defectJiraIds,request);
            result.put("code", code);

            // 结果保存到redis
            Set<String> redisCode = new HashSet<>();
            redisCode.addAll(code);
            Long userId = CommonUtil.getCurrentUserId(request);
            if(StringUtils.isEmpty(key)){
                key = userId +"-"+ DateUtil.getDateString(new Timestamp(System.currentTimeMillis()),Constants.EXPORT_EXCEL_TIME_FORMAT);
            } else {
                Set<String> redis_Code = new Gson().fromJson(redisUtils.get(key).toString(),
                        new TypeToken<Set<String>>(){}.getType());
                redisCode.addAll(redis_Code);
            }
            redisUtils.set(key,new Gson().toJson(redisCode),Constants.ITMP_TOKEN_TIMEOUT);
            result.put("key", key);
        } catch (Exception e) {
            return handleException(e, "操作失败");
        }
        return result;
    }

    /**
    *@author liushan
    *@Description 导出测试缺陷jira附件导入结果
    *@Date 2020/7/21
    *@Param [key, name, response]
    *@return void
    **/
    @RequestMapping(value = "exportJiraFile")
    public void exportJiraFile(String key,String name,HttpServletResponse response) throws Exception{
        List<String> redisCode = new Gson().fromJson(redisUtils.get(key).toString(),
                new TypeToken<List<String>>(){}.getType());
        new ExportExcel("",new String[]{name+"同步jira附件导入结果"})
                .setDataObjectList(redisCode)
                .write(response, name+"_同步jira附件导入结果_"+DateUtil.getDateString(new Timestamp(System.currentTimeMillis()),Constants.EXPORT_EXCEL_TIME_FORMAT)+Constants.EXPORT_EXCEL_VERSION)
                .dispose();
    }


    /**
    *@author author
    *@Description 缺陷封装格式
    *@Date 2020/8/20
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
}