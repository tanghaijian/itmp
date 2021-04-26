package cn.pioneeruniverse.common.Rest;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.common.utils.UploadFileUtil;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectInfoMapper;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblDevTask;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttachement;
import cn.pioneeruniverse.dev.feignInterface.DevManageToTestManageInterface;
import cn.pioneeruniverse.dev.service.devtask.DevTaskService;
import cn.pioneeruniverse.dev.service.worktask.WorkTaskService;

/**
 * Description: 批量上传文件
 * Author:liushan
 * Date: 2019/2/21 下午 6:07
 */
@RestController
@RequestMapping(value = "UploadFiles")
public class UploadFiles extends BaseController {

    public final static Logger logger = LoggerFactory.getLogger(UploadFiles.class);
    @Autowired
    private DevTaskService devTaskService;
    @Autowired
    private WorkTaskService workTaskService;
    @Autowired
    private TblDefectInfoMapper defectInfoMapper;
    @Autowired
    private TblDefectAttachementMapper defectAttachementMapper;
    @Autowired
    private DevManageToTestManageInterface toTestManageInterface;

    @Autowired
    private S3Util s3Util;
    private static final String devTask = "开发任务";
    private static final String workTask = "工作任务";
    private static final String defect = "缺陷管理";

    @Value("${s3.devTaskBucket}")
    private String devTaskBucket;
    @Value("${s3.testTaskBucket}")
    private String testTaskBucket;
    @Value("${s3.defectBucket}")
    private String defectBucket;
    private List<String> codeList = new ArrayList<>();

//    public static void main(String[] args) throws Exception {
//        ConfigurableApplicationContext context = SpringApplication.run(DevManageApplication.class, args);
//        devTaskService = (DevTaskService) context.getBean("devTaskService");
//        workTaskService = (WorkTaskService) context.getBean("workTaskService");
//        s3Util = (S3Util) context.getBean("S3Util");
//        dev_work_Task_bucketName = s3Util.getDevTaskBucket();
//        new UploadFiles().readAllFile("C:\\Users\\珊珊\\Desktop\\测试管理平台\\716迁移数据\\CTF_attachments_EX",workTask);
//    }


    /**
     * 批量上传文件
     * @param selFunction
     * @param filePath
     * @return
     * @throws Exception
     */
    @GetMapping(value = "files/{selFunction}")
    @ResponseBody
    public Map<String,Object> uploadFiles(@PathVariable("selFunction") Integer selFunction,
                                          @RequestParam("filePath") String filePath){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        result.put("Message", "批量上传文件成功，实时更新时间："+DateUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        try {
            codeList = new ArrayList<>();
            readAllFile(selFunction,filePath);
            result.put("已上传文件的任务编号：",JSON.toJSONString(codeList));
        }catch (Exception e) {
            result =  handleException(e, "批量上传文件失败,请填写正确的路径!!!,实时更新时间："+DateUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        }
        return result;
    }


    /**
     * 进行文件查找
     * @param selFunction
     * @param filePath
     * @throws Exception
     */
    public void readAllFile(Integer selFunction,String filePath) throws Exception{
        String selFunction_value = "";
        switch (selFunction.intValue()){
            case 1:
                selFunction_value = devTask;
                break;
            case 2:
                selFunction_value = workTask;
                break;
            case 3:
                selFunction_value = defect;
                break;
        }
        logger.info(selFunction_value +"批量上传文件："+filePath+";");

        File file= new File(filePath);
        if(!file.isDirectory()){// 文件

            String code = file.getParentFile().getName();
            logger.info(selFunction_value +"批量上传文件："+file.getPath()+";"+selFunction_value+"编号："+code);
            selectByCode(file,code,selFunction);

        }else if(file.isDirectory()){ // 文件夹
            String[] filelist = file.list();
            for(int i = 0;i<filelist.length;i++){
                File readfile = new File(filePath);
                if (!readfile.isDirectory()) {// 文件

                    String readfile_code = file.getParentFile().getName();
                    logger.info(selFunction_value +"批量上传文件："+readfile.getPath()+";"+selFunction_value+"编号："+readfile_code);
                    selectByCode(readfile,readfile_code,selFunction);

                }else if (readfile.isDirectory()) {// 文件夹
                    // 递归
                    readAllFile(selFunction,filePath + File.separator + filelist[i]);
                }
            }
        }
    }

    /**
     * 根据code查询是否有此code
     * @param code 编号
     * @param selFunction 判断是开发任务 、 工作任务
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public void selectByCode(File file,String code,Integer selFunction) throws Exception{
        try{
            if(file != null){
                if(selFunction.intValue() == 1){// 开发任务
                    TblRequirementFeature feature = devTaskService.getFeatureByCode(code);
                    if(feature != null){ // 上传文件，保存到数据表中
                        TblAttachementInfoDTO attachementInfoDTO = UploadFileUtil.updateFile(file,s3Util,devTaskBucket);

                        TblRequirementFeatureAttachement featureAttachement = new TblRequirementFeatureAttachement();
                        featureAttachement.setCreateBy(1L);
                        featureAttachement.setCreateDate(new Timestamp(new Date().getTime()));
                        featureAttachement.setRequirementFeatureId(feature.getId());
                        featureAttachement.setFileNameOld(attachementInfoDTO.getFileNameOld());
                        featureAttachement.setFileType(attachementInfoDTO.getFileType());
                        featureAttachement.setFileS3Bucket(devTaskBucket);
                        featureAttachement.setFileS3Key(attachementInfoDTO.getFileS3Key());
                        devTaskService.insertAtt(featureAttachement);
                        codeList.add(code);
                    }

                } else if(selFunction.intValue() == 2 ){ // 工作任务
                   TblDevTask devTask = workTaskService.getDevTaskByCode(code);
                    if(devTask != null){
                        TblAttachementInfoDTO attachementInfoDTO = UploadFileUtil.updateFile(file,s3Util,devTaskBucket);
                        attachementInfoDTO.setAssociatedId(devTask.getId());
                        workTaskService.addworkTaskAttachement(attachementInfoDTO);
                        codeList.add(code);
                    }
                }else if(selFunction.intValue() == 3 ){// 缺陷
                    TblDefectInfo defectInfo = defectInfoMapper.getDefectByCode(code);
                    if(defectInfo != null ){
                        TblAttachementInfoDTO attachementInfoDTO = UploadFileUtil.updateFile(file,s3Util,defectBucket);
                        attachementInfoDTO.setAssociatedId(defectInfo.getId());
                        defectAttachementMapper.insertDefectDTOAttachement(attachementInfoDTO);
                        Map<String,Object> resultMap = toTestManageInterface.insertDefectAttachement(new Gson().toJson(attachementInfoDTO));
                        if (resultMap.get("status") != null && resultMap.get("status").equals("2")){
                            logger.error((String) resultMap.get("errorMessage"));
                            throw new Exception((String) resultMap.get("errorMessage"));
                        }
                        codeList.add(code);
                    }
                }
            }
        }catch (Exception e){
            logger.error(selFunction+"批量上传文件",e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
    }
}
