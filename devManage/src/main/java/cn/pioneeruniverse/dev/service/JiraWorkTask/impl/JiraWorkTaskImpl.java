package cn.pioneeruniverse.dev.service.JiraWorkTask.impl;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.common.utils.UploadFileUtil;
import cn.pioneeruniverse.dev.dao.mybatis.*;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttachement;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.service.JiraWorkTask.IJiraWorkTaskService;
import cn.pioneeruniverse.common.entity.JiraDevelopmentVO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JiraWorkTaskImpl implements IJiraWorkTaskService{

    @Autowired
    private S3Util s3Util;
    @Autowired
    private JiraWorkTaskMapper jiraWorkTaskMapper;
    @Autowired
    private TblUserInfoMapper userMapper;
    @Autowired
    private TblSystemInfoMapper systemInfoMapper;
    @Autowired
    private TblRequirementFeatureMapper featureMapper;
    @Autowired
    private TblRequirementFeatureAttachementMapper attachementMapper;



    public final static Logger logger = LoggerFactory.getLogger(JiraWorkTaskImpl.class);
    /**
     *  jira数据增加
     * @param jiraDevelopmentVO
     * @return
     */
    @Override
    public void synJiraWorkTask(JiraDevelopmentVO jiraDevelopmentVO,HttpServletRequest request) {
        getJiraDevelopmentVO(jiraDevelopmentVO);
        CommonUtil.setBaseValue(jiraDevelopmentVO,request);
        TblRequirementFeature feature = featureMapper.getFeatureByCode(jiraDevelopmentVO.getFeatureCode());
        if(feature!=null){
            jiraDevelopmentVO.setId(feature.getId());
            jiraWorkTaskMapper.updateJiraWorkTask(jiraDevelopmentVO);
        }else{
            jiraWorkTaskMapper.insertJiraWorkTask(jiraDevelopmentVO);
        }
        insertAttr(jiraDevelopmentVO);
    }

    //组装jire数据
    private JiraDevelopmentVO getJiraDevelopmentVO(JiraDevelopmentVO jiraDevelopmentVO){
        Map<String,Object>  fieldTemplate = new HashMap<>();
        Map [] extendedFields = new Map[1];
        Map<String,String> extendedField = new HashMap<>();
        extendedField.put("required","true");
        extendedField.put("fieldName","systemReqID");
        extendedField.put("labelName","systemReqID");
        extendedField.put("valueName",jiraDevelopmentVO.getSystemReqID());
        extendedFields[0] = extendedField;
        fieldTemplate.put("field",extendedFields);
        jiraDevelopmentVO.setFieldTemplate(JSON.toJSONString(fieldTemplate));
        jiraDevelopmentVO.setManageUserId(getUserId(jiraDevelopmentVO.getUserCode()));
        jiraDevelopmentVO.setExecuteUserId(getUserId(jiraDevelopmentVO.getUserCode()));
        jiraDevelopmentVO.setSystemId(getSystemId(jiraDevelopmentVO.getSystemCode()));
        jiraDevelopmentVO.setAssetSystemTreeId(getAssetSystemTreeId(jiraDevelopmentVO.getSystemCode(),
                jiraDevelopmentVO.getModuleName(),jiraDevelopmentVO.getModuleName1()));
        jiraDevelopmentVO.setExecuteProjectGroupId(getExecuteProjectGroupId(jiraDevelopmentVO.getProjectGroupName()));
        jiraDevelopmentVO.setRepairSystemVersionId(getSystemVersion(jiraDevelopmentVO.getSystemId(),
                jiraDevelopmentVO.getRepairSystemVersioName()));
        jiraDevelopmentVO.setCreateType(1);
        jiraDevelopmentVO.setRequirementFeatureStatus("01");
        return jiraDevelopmentVO;
    }

    private Long getUserId(String userCode) {
        Long userId = userMapper.findIdByUserAccount(userCode);
        if(userId!=null&&userId.longValue()>0) {
            return userId;
        }else {
            return null;
        }
    }

    private Long getSystemId(String systemCode) {
        TblSystemInfo systeInfo = systemInfoMapper.getSystemByCode(systemCode);
        if(systeInfo!=null) {
            return systeInfo.getId();
        }else {
            return null;
        }
    }

    private Long getAssetSystemTreeId(String systemCode,String module,String module1){
        Long id = jiraWorkTaskMapper.findAssetSystemTreeID(systemCode,module);
        if(module1!=null&&!"".equals(module1)){
            id = jiraWorkTaskMapper.findAssetSystemTreeID1(id.toString(),module1);
        }
        return  id;
    }

    private Long getExecuteProjectGroupId(String projectGroupName){
        Long id = jiraWorkTaskMapper.findProjectGroupID(projectGroupName);
        if(id!=null&&id.longValue()>0){
            return id;
        }else {
            return null;
        }
    }
    private Long getSystemVersion(Long systemId, String versionAndFlag) {
        Long userId = null;
        String [] version = versionAndFlag.split("/");
        if(version.length==2){
            userId = jiraWorkTaskMapper.getSystemVersionID(systemId,version[0],version[1]);
        }
        return userId;
    }

    /**
     * 
    * @Title: insertAttr
    * @Description:保存附件
    * @author author
    * @param jiraDevelopmentVO
     */
    public void insertAttr(JiraDevelopmentVO jiraDevelopmentVO) {
        try {
            for(Map<String,Object> map:jiraDevelopmentVO.getMaps()){
                String fileName = map.get("fileName").toString();
                TblRequirementFeatureAttachement atta = new TblRequirementFeatureAttachement();
                String extension = fileName
                        .substring(fileName.lastIndexOf(".") + 1);// 后缀名

                HttpURLConnection httpURLConnection = UploadFileUtil.downloadFile(map.get("accessory").toString());
                // 文件大小
                Long fileLengthLong = httpURLConnection.getContentLengthLong();
                // 控制台打印文件大小
                logger.info("您要下载的文件大小为:" + fileLengthLong / (1024) + "KB");
                // 建立链接从请求中获取数据
                InputStream is = httpURLConnection.getInputStream();
                String keyname = s3Util.putObject1(s3Util.getDevTaskBucket(), fileName, is,fileLengthLong);

                atta.setRequirementFeatureId(jiraDevelopmentVO.getId());
                atta.setFileS3Bucket(s3Util.getDevTaskBucket());
                atta.setFileS3Key(keyname);
                atta.setFileNameOld(fileName);
                atta.setFileType(extension);
                TblRequirementFeatureAttachement fAtta = attachementMapper.
                        getReqFeatureAttByfileNameOld(atta.getRequirementFeatureId(),fileName);
                if (fAtta == null) {
                    attachementMapper.insertAtt(atta);
                }else{
                    attachementMapper.updateByPrimaryKey(atta);
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("文件下载失败！");
        }
    }
}
