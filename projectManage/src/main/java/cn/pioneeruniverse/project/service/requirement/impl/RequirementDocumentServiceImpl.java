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
    * @Description: ??????IT?????????????????????
    * @author author
    * @param ids ?????????????????????id
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

        //??????????????????
        Map<String, Object> mapAll = new LinkedHashMap<>();
        DataBusRequestHead.setProviderID1("");
        //???????????????
        mapAll.put("requestHead", DataBusRequestHead.getRequestHead());
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("reqCode",reqCode);
        mapAll.put("requestBody",requestBody);

        //??????????????????
        log.info("????????????--------->>>>>>"+ JSON.toJSONString(mapAll,true));
        //????????????
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
    * @Description: ????????????
    * @author author
    * @param he ??????
    * @return Map[]
     */
    public Map [] send(HttpEntity<Map<String, Object>> he) {
        Map [] maps = null;
        try{
            //???????????????esb????????????demandfiletransferservicerest/Restful
            ResponseEntity<String> result = restTemplate.exchange
                    (synitcdAttr, HttpMethod.POST, he,String.class);
            if(result.getStatusCode().is2xxSuccessful()){
                log.info("????????????--------->>>>>>"+result.getBody());
                String str = JSON.parseObject(result.getBody()).get("responseBody").toString();
                maps = JSONArray.parseObject(str,Map [].class);
            }else{
                log.info("???????????????????????????"+result.getStatusCodeValue()+"???");
            }
        }catch (Exception ex){
            ex.getMessage();
        }
        return maps;
    }
    
    /**
     * 
    * @Title: updataItcdAttr
    * @Description: ??????????????????
    * @author author
    * @param data ????????????
    * @return int 1
     */
    @Override
    @Transactional(readOnly = false)
    public int updataItcdAttr (Map<String,Object> data){
    	//????????????
        String reqCode = data.get("reqCode").toString();
        //?????????mongodb????????????
        String reqDoc = data.get("reqDoc").toString();
        String taskDoc = data.get("taskDoc").toString();
        //??????????????????
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
    * @Description: ????????????????????????
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
                map.put("errorMessage", "???????????????");
                return map;
            }
            Row titleRow = sheet.getRow(0);
            String[] title = {"??????ID", "????????????","????????????", "????????????", "????????????ID","????????????", "MONGODB??????", "???????????????"};//????????????

            for (int i = 0; i < title.length; i++) {
                if (!titleRow.getCell(i).getStringCellValue().equals(title[i])) {
                    map.put("status", Constants.ITMP_RETURN_FAILURE);
                    map.put("errorMessage", "???" + (i + 1) + "??????????????????");
                    return map;
                }
            }

            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {   //???????????????
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
                String reqCode=row.getCell(1).getStringCellValue();//????????????
                itcdAttrVO.setDocName(row.getCell(2).getStringCellValue());//????????????
                itcdAttrVO.setDocType(row.getCell(5).getStringCellValue());//????????????
                itcdAttrVO.setDocKey(row.getCell(6).getStringCellValue());//mongoKey
                itcdAttrVO.setUserAccount(row.getCell(7).getStringCellValue());//???????????????
                //???????????????s3
                TblSystemDirectoryDocument tsdd = insertItcdAttr(itcdAttrVO);
                //??????????????????????????????????????????
                CommonUtil.setBaseValue(tsdd,request);
                tsdd.setDocumentName(itcdAttrVO.getDocName());
                tsdd.setDocumentVersion(1);
                TblSystemDocumentType documentType = systemDocumentTypeDao.selectIdByType(itcdAttrVO.getDocType());
                tsdd.setDocumentType(documentType.getId());
                tsdd.setCheckoutStatus(2);
                tsdd.setCheckoutUserId(userMapper.findIdByUserAccount(itcdAttrVO.getUserAccount()));
                tsdd.setSaveType(1);
                systemDirectoryDocumentDao.insertDirectoryDocument(tsdd);
                //???????????????????????????????????????????????????
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
            map.put("errorMessage", "??????????????????");
            log.error(e.getMessage(), e);
            e.printStackTrace();
            return  map;
        }
        return map;
    }

    /**
     * 
    * @Title: importCsv
    * @Description: ??????csv??????????????????
    * @author author
    * @param file csv??????
    * @param request
    * @return Map<String, Object>
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> importCsv(MultipartFile file, HttpServletRequest request){
        String[] title = {"id", "reqCode","docName", "docSize", "docType","docTypeName", "docKey", "userAccount"};//????????????
        Map<String, Object> map = new HashMap<>();
        String[] fields = title;
        String read = null;
        try {
            InputStream is = file.getInputStream();
            read = CsvUtils.read(is, fields);
            List<ItcdAttrVO> itcdAttrList = JSONArray.parseArray(read,ItcdAttrVO.class);
            for(ItcdAttrVO itcdAttrVO : itcdAttrList){
            	//???????????????s3
                TblSystemDirectoryDocument tsdd = insertItcdAttr(itcdAttrVO);
                //??????????????????????????????????????????
                CommonUtil.setBaseValue(tsdd,request);
                tsdd.setDocumentName(itcdAttrVO.getDocName());
                tsdd.setDocumentVersion(1);
                TblSystemDocumentType documentType = systemDocumentTypeDao.selectIdByType(itcdAttrVO.getDocType());
                tsdd.setDocumentType(documentType.getId());
                tsdd.setCheckoutStatus(2);
                tsdd.setCheckoutUserId(userMapper.findIdByUserAccount(itcdAttrVO.getUserAccount()));
                tsdd.setSaveType(1);
                systemDirectoryDocumentDao.insertDirectoryDocument(tsdd);
              //???????????????????????????????????????????????????
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
            map.put("errorMessage", "??????????????????");
            log.error(e.getMessage(), e);
            e.printStackTrace();
            return  map;
        }
        return map;
    }

    private TblSystemDirectoryDocument insertItcdAttr(ItcdAttrVO itcdAttrVO){
        TblSystemDirectoryDocument tsdd = new TblSystemDirectoryDocument();
        String fileName = itcdAttrVO.getDocName();
        // ????????????????????????????????????
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
