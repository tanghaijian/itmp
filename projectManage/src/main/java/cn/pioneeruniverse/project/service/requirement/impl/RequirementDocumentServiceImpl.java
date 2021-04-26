package cn.pioneeruniverse.project.service.requirement.impl;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.databus.DataBusRequestHead;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.project.common.CsvUtils;
import cn.pioneeruniverse.project.common.MongoDB;
import cn.pioneeruniverse.project.dao.mybatis.RequirementMapper;
import cn.pioneeruniverse.project.dao.mybatis.UserMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDirectoryDocumentDao;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDirectoryDocumentHistoryDao;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.SystemDocumentType;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentRequirementMapper;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentRequirement;
import cn.pioneeruniverse.project.entity.TblSystemDocumentType;
import cn.pioneeruniverse.project.service.requirement.RequirementDocumentService;
import cn.pioneeruniverse.project.vo.ItcdAttrVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

@Transactional(readOnly = true)
@Service("requirementDocumentService")
public class RequirementDocumentServiceImpl implements RequirementDocumentService {

    @Autowired
    private S3Util s3Util;
    @Autowired
    private MongoDB mongoDB;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RequirementMapper requirementMapper;
    @Autowired
    private SystemDocumentType systemDocumentTypeDao;
    @Autowired
    private SystemDirectoryDocumentDao systemDirectoryDocumentDao;
    @Autowired
    private SystemDirectoryDocumentHistoryDao systemDirectoryDocumentHistoryDao;
    @Autowired
    private TblSystemDirectoryDocumentRequirementMapper documentRequirementMapper;


    private static Logger log = LoggerFactory.getLogger(RequirementServiceImpl.class);

    @Value("${esb.synitcdAttr.url}")
    private String synitcdAttr;

    /**
     * 
    * @Title: synItcdAttr
    * @Description: 同步IT全流程需求附件
    * @author author
    * @param ids 需要同步的需求id
    * @param request
    * @return Map<String, Object>  key:"status" value:"success"
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> synItcdAttr(Long[] ids, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        String [] reqCode = new String[ids.length];
        for(int i=0;i<ids.length;i++){
            TblRequirementInfo req = requirementMapper.findRequirementById(ids[i]);
            reqCode[i] = req.getRequirementCode();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Accept", "application/json");

        //封装请求参数
        Map<String, Object> mapAll = new LinkedHashMap<>();
        DataBusRequestHead.setProviderID1("");
        //封装报文头
        mapAll.put("requestHead", DataBusRequestHead.getRequestHead());
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("reqCode",reqCode);
        mapAll.put("requestBody",requestBody);

        //查看请求报文
        log.info("请求报文--------->>>>>>"+ JSON.toJSONString(mapAll,true));
        //发送请求
        HttpEntity<Map<String, Object>> sendParam = new HttpEntity<>(mapAll, headers);

        Map [] mapResult = send(sendParam);
        for(Map maps : mapResult){
            updataItcdAttr(maps);
        }
        map.put("status","success");
        return map;
    }

    /**
     * 
    * @Title: send
    * @Description: 发送报文
    * @author author
    * @param he 报文
    * @return Map[]
     */
    public Map [] send(HttpEntity<Map<String, Object>> he) {
        Map [] maps = null;
        try{
            //发送请求：esb服务名：demandfiletransferservicerest/Restful
            ResponseEntity<String> result = restTemplate.exchange
                    (synitcdAttr, HttpMethod.POST, he,String.class);
            if(result.getStatusCode().is2xxSuccessful()){
                log.info("响应报文--------->>>>>>"+result.getBody());
                String str = JSON.parseObject(result.getBody()).get("responseBody").toString();
                maps = JSONArray.parseObject(str,Map [].class);
            }else{
                log.info("调用失败，状态码："+result.getStatusCodeValue()+"。");
            }
        }catch (Exception ex){
            ex.getMessage();
        }
        return maps;
    }
    
    /**
     * 
    * @Title: updataItcdAttr
    * @Description: 同步需求附件
    * @author author
    * @param data 报文数据
    * @return int 1
     */
    @Override
    @Transactional(readOnly = false)
    public int updataItcdAttr (Map<String,Object> data){
    	//需求编码
        String reqCode = data.get("reqCode").toString();
        //文档在mongodb中的信息
        String reqDoc = data.get("reqDoc").toString();
        String taskDoc = data.get("taskDoc").toString();
        //响应报文封装
        List<ItcdAttrVO> reqdocList = JSONArray.parseArray(reqDoc,ItcdAttrVO.class);
        
        for(ItcdAttrVO itcdAttrVO : reqdocList){
            TblSystemDirectoryDocument tsdd = insertItcdAttr(itcdAttrVO);
            tsdd.setDocumentName(itcdAttrVO.getDocName());
            tsdd.setDocumentVersion(1);
            TblSystemDocumentType documentType = systemDocumentTypeDao.selectIdByType(itcdAttrVO.getDocType());
            tsdd.setDocumentType(documentType.getId());
            tsdd.setCheckoutStatus(2);
            tsdd.setCheckoutUserId(userMapper.findIdByUserAccount(itcdAttrVO.getUserAccount()));
            tsdd.setSaveType(1);
            tsdd.setStatus(1);
            tsdd.setCreateDate(new Timestamp(new Date().getTime()));
            tsdd.setCreateBy(Long.valueOf(1));
            tsdd.setLastUpdateBy(Long.valueOf(1));
            tsdd.setLastUpdateDate(new Timestamp(new Date().getTime()));
            systemDirectoryDocumentDao.insertDirectoryDocument(tsdd);
            TblSystemDirectoryDocumentRequirement documentRequirement = new TblSystemDirectoryDocumentRequirement();
            documentRequirement.setSystemDirectoryDocumentId(tsdd.getId());
            systemDirectoryDocumentHistoryDao.insertDirectoryDocumentHistory(tsdd);
            documentRequirement.setRequirementCode(reqCode);
            documentRequirement.setDocumentVersion(1);
            documentRequirement.setUpdateUserId(tsdd.getCheckoutUserId());
            documentRequirement.setUpdateTime(tsdd.getCreateDate());
            documentRequirementMapper.insertDocumentRequirement(documentRequirement);
        }

        List<ItcdAttrVO> taskDocList = JSONArray.parseArray(taskDoc,ItcdAttrVO.class);

        return  1;
    }

    /**
     * 
    * @Title: importExcel
    * @Description: 导入需求附件信息
    * @author author
    * @param file excel
    * @param request
    * @return Map<String, Object>
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> importExcel(MultipartFile file, HttpServletRequest request)  {
        Map<String, Object> map = new HashMap<>();
        try {
            String fileName = file.getOriginalFilename();
            boolean isExcel2003 = true;
            if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
                isExcel2003 = false;
            }
            InputStream is = null;

            try {
                is = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(file.getInputStream());
            }
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) {
                map.put("status", Constants.ITMP_RETURN_FAILURE);
                map.put("errorMessage", "标题未对应");
                return map;
            }
            Row titleRow = sheet.getRow(0);
            String[] title = {"需求ID", "需求编号","附件名称", "附件大小", "附件类型ID","附件类型", "MONGODB地址", "上传人工号"};//标题验证

            for (int i = 0; i < title.length; i++) {
                if (!titleRow.getCell(i).getStringCellValue().equals(title[i])) {
                    map.put("status", Constants.ITMP_RETURN_FAILURE);
                    map.put("errorMessage", "第" + (i + 1) + "列标题未对应");
                    return map;
                }
            }

            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {   //循环表格行
                Row row=sheet.getRow(i);
                if(row==null){
                    continue;
                }
                for(int z=0;z<8;z++){
                    if (row.getCell(z) != null) {
                        row.getCell(z).setCellType(Cell.CELL_TYPE_STRING);
                    }
                }
                ItcdAttrVO itcdAttrVO = new ItcdAttrVO();
                String reqCode=row.getCell(1).getStringCellValue();//需求编号
                itcdAttrVO.setDocName(row.getCell(2).getStringCellValue());//附件名称
                itcdAttrVO.setDocType(row.getCell(5).getStringCellValue());//附件类型
                itcdAttrVO.setDocKey(row.getCell(6).getStringCellValue());//mongoKey
                itcdAttrVO.setUserAccount(row.getCell(7).getStringCellValue());//上传人工号
                //上传文档到s3
                TblSystemDirectoryDocument tsdd = insertItcdAttr(itcdAttrVO);
                //组装文档信息，并保存进数据库
                CommonUtil.setBaseValue(tsdd,request);
                tsdd.setDocumentName(itcdAttrVO.getDocName());
                tsdd.setDocumentVersion(1);
                TblSystemDocumentType documentType = systemDocumentTypeDao.selectIdByType(itcdAttrVO.getDocType());
                tsdd.setDocumentType(documentType.getId());
                tsdd.setCheckoutStatus(2);
                tsdd.setCheckoutUserId(userMapper.findIdByUserAccount(itcdAttrVO.getUserAccount()));
                tsdd.setSaveType(1);
                systemDirectoryDocumentDao.insertDirectoryDocument(tsdd);
                //关联文档和需求关系以及保存文档历史
                TblSystemDirectoryDocumentRequirement documentRequirement = new TblSystemDirectoryDocumentRequirement();
                documentRequirement.setSystemDirectoryDocumentId(tsdd.getId());
                systemDirectoryDocumentHistoryDao.insertDirectoryDocumentHistory(tsdd);
                documentRequirement.setRequirementCode(reqCode);
                documentRequirement.setDocumentVersion(1);
                documentRequirement.setUpdateUserId(tsdd.getCheckoutUserId());
                documentRequirement.setUpdateTime(tsdd.getCreateDate());
                documentRequirementMapper.insertDocumentRequirement(documentRequirement);
            }
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (IOException e) {
            map.put("status", Constants.ITMP_RETURN_FAILURE);
            map.put("errorMessage", "上传文件失败");
            log.error(e.getMessage(), e);
            e.printStackTrace();
            return  map;
        }
        return map;
    }

    /**
     * 
    * @Title: importCsv
    * @Description: 导入csv格式需求附件
    * @author author
    * @param file csv文件
    * @param request
    * @return Map<String, Object>
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> importCsv(MultipartFile file, HttpServletRequest request){
        String[] title = {"id", "reqCode","docName", "docSize", "docType","docTypeName", "docKey", "userAccount"};//标题验证
        Map<String, Object> map = new HashMap<>();
        String[] fields = title;
        String read = null;
        try {
            InputStream is = file.getInputStream();
            read = CsvUtils.read(is, fields);
            List<ItcdAttrVO> itcdAttrList = JSONArray.parseArray(read,ItcdAttrVO.class);
            for(ItcdAttrVO itcdAttrVO : itcdAttrList){
            	//上传文档到s3
                TblSystemDirectoryDocument tsdd = insertItcdAttr(itcdAttrVO);
                //组装文档信息，并保存进数据库
                CommonUtil.setBaseValue(tsdd,request);
                tsdd.setDocumentName(itcdAttrVO.getDocName());
                tsdd.setDocumentVersion(1);
                TblSystemDocumentType documentType = systemDocumentTypeDao.selectIdByType(itcdAttrVO.getDocType());
                tsdd.setDocumentType(documentType.getId());
                tsdd.setCheckoutStatus(2);
                tsdd.setCheckoutUserId(userMapper.findIdByUserAccount(itcdAttrVO.getUserAccount()));
                tsdd.setSaveType(1);
                systemDirectoryDocumentDao.insertDirectoryDocument(tsdd);
              //关联文档和需求关系以及保存文档历史
                TblSystemDirectoryDocumentRequirement documentRequirement = new TblSystemDirectoryDocumentRequirement();
                documentRequirement.setSystemDirectoryDocumentId(tsdd.getId());
                systemDirectoryDocumentHistoryDao.insertDirectoryDocumentHistory(tsdd);
                documentRequirement.setRequirementCode(itcdAttrVO.getReqCode());
                documentRequirement.setDocumentVersion(1);
                documentRequirement.setUpdateUserId(tsdd.getCheckoutUserId());
                documentRequirement.setUpdateTime(tsdd.getCreateDate());
                documentRequirementMapper.insertDocumentRequirement(documentRequirement);
                map.put("status", Constants.ITMP_RETURN_SUCCESS);
            }
        } catch (IOException e) {
            map.put("status", Constants.ITMP_RETURN_FAILURE);
            map.put("errorMessage", "上传文件失败");
            log.error(e.getMessage(), e);
            e.printStackTrace();
            return  map;
        }
        return map;
    }

    private TblSystemDirectoryDocument insertItcdAttr(ItcdAttrVO itcdAttrVO){
        TblSystemDirectoryDocument tsdd = new TblSystemDirectoryDocument();
        String fileName = itcdAttrVO.getDocName();
        // 建立链接从请求中获取数据
        InputStream is = mongoDB.getInput(itcdAttrVO.getDocKey());
        //String fileName = "itcd.xlsx";
        //InputStream is= mongoDB.getInput("86d86227-c25e-480f-8f5d-bed17e197c23");
        int length = 0;
        try{
            is.read();
            length = is.available();
        }catch(Exception e) {
            e.printStackTrace();
        }
        String keyname = s3Util.putObject1(s3Util.getRequirementBucket(), fileName, is,Long.valueOf(length));
        tsdd.setDocumentS3Bucket(s3Util.getRequirementBucket());
        tsdd.setDocumentS3Key(keyname);
        tsdd.setDocumentMongoKey(itcdAttrVO.getDocKey());
        return tsdd;
    }

}
