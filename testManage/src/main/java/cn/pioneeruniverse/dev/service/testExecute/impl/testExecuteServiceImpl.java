package cn.pioneeruniverse.dev.service.testExecute.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.common.constants.DicConstants;
import cn.pioneeruniverse.common.utils.*;
import cn.pioneeruniverse.dev.common.ExportExcel;
import cn.pioneeruniverse.dev.dao.mybatis.*;
import cn.pioneeruniverse.dev.service.defect.DefectService;
import cn.pioneeruniverse.dev.vo.ExportParameters;
import cn.pioneeruniverse.dev.yiranUtil.PoiExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.service.testExecute.testExecuteService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Transactional(readOnly = true)
@Service("testExecuteService")
public class testExecuteServiceImpl implements testExecuteService {
    @Autowired
    private TblTestSetCaseMapper tblTestSetCaseMapper;
    @Autowired
    private TblTestSetCaseStepMapper tblTestSetCaseStepMapper;
    @Autowired
    private TblTestSetExecuteRoundCaseResultMapper tblTestSetExecuteRoundCaseResult;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TblTestSetCaseExecuteMapper tblTestSetCaseExecuteMapper;
    @Autowired
    private TblTestSetCaseStepExecuteMapper tblTestSetCaseStepExecuteMapper;
    @Autowired
    private TblTestSetCaseExecuteAttachementMapper tblTestSetCaseExecuteAttachementMapper;
    @Autowired
    private TblDefectInfoMapper tblDefectInfoMapper;
    @Autowired
    private TblUserInfoMapper tblUserInfoMapper;
    @Autowired
    private TblRequirementFeatureMapper featureMapper;
    @Autowired
    private DefectService defectService;

    /**
     * ??????????????????
     * @param testSetMap
     */
    @Override
    @Transactional(readOnly = true)
    public PageInfo<List<Map<String, Object>>> selectByPrimaryKey(String testSetMap) {
        Map<String, Object> params = (Map) JSON.parse(testSetMap);
        int pageNumber = Integer.parseInt(params.get("pageNumber").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        PageHelper.startPage((pageNumber - 1) * pageSize,pageSize);
        List<Map<String, Object>> list = tblTestSetCaseMapper.selectBatchPass(params);
        return new PageInfo(list);
    }

    @Override
    public int countSelectByPrimaryKey( Map<String, Object> params) {
        return tblTestSetCaseMapper.countSelectBatchPass(params);
    }

    /**
    *@author liushan
    *@Description ??????????????????
    *@Date 2020/4/9
    *@Param [testSetMap]
    *@return cn.pioneeruniverse.dev.entity.TblRequirementFeature
    **/
    @Override
    public TblRequirementFeature selectRequirementFeatureByTestSet(String testSetMap) {
        Map<String, Object> params = (Map) JSON.parse(testSetMap);
        return featureMapper.selectRequirementFeatureByTestSet(params);
    }

    //????????????????????????
    @Override
    @Transactional(readOnly = true)
    public List<TblTestSetCaseStep> selectTestCaseRun(Long id) {
        List<TblTestSetCaseStep> list = tblTestSetCaseStepMapper.selectByPrimaryKey(id);
        return list;
    }

    //?????????????????? ???????????????????????????,???????????????updateTestSetCaseAndInsertExecute
    @Override
    @Transactional(readOnly = false)
    public String insertSelective(String rows, String type, String enforcementResults, String excuteRemark, int excuteRound, Long testSetId, Long testUserId, String files) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> list = (List<Map<String, String>>) JSONArray.parse(enforcementResults);
        result = (Map) JSON.parse(rows);
        result.put("createBy", testUserId);
        //result.put("list", list);
        result.put("caseExecuteResult", type);
        result.put("excuteRemark", excuteRemark);
        result.put("excuteRound", excuteRound);
        result.put("testSetId", testSetId);
        int number = tblTestSetExecuteRoundCaseResult.updateSelective(result);//??????????????????
        tblTestSetCaseExecuteMapper.insertSelective(result);//??????????????????
        if (!files.equals("") && !files.equals(null) && !files.equals("[]")) {
            List<TblTestSetCaseExecuteAttachement> fileTask = JsonUtil.fromJson(files, JsonUtil.createCollectionType(ArrayList.class, TblTestSetCaseExecuteAttachement.class));
            for (int i = 0; i < fileTask.size(); i++) {
                Long execeteId = Long.parseLong(result.get("id").toString());
                fileTask.get(i).setTestSetCaseExecuteId(execeteId);
                fileTask.get(i).setCreateBy(testUserId);
            }
            tblTestSetCaseExecuteAttachementMapper.addAttachement(fileTask);
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i).put("id", result.get("id").toString());
        }
        if (list.size() > 0) {
            tblTestSetCaseStepExecuteMapper.insertSelective(list);//??????????????????
        }

        if (number < 1) {
            tblTestSetExecuteRoundCaseResult.insertSelective(result);//?????????????????????
        }
        tblTestSetCaseMapper.updateExecuteResult(result);//??????????????????????????????
        return result.get("id").toString();
    }

    /**
     * @return java.lang.String
     * @author liushan
     * @Description ??????????????????
     * @Date 2020/3/25
     * @Param [testSetCase, enforcementResults, files, excuteRemark, request]
     **/
    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public String updateTestSetCaseAndInsertExecute(TblTestSetCase testSetCase, String enforcementResults, String files, String excuteRemark, HttpServletRequest request) throws Exception {
        Map<String, Object> param = new HashMap<>();
        Long currentUserId = CommonUtil.getCurrentUserId(request);
        testSetCase.setLastUpdateBy(currentUserId);
        param.put("testSetCase", testSetCase);
        param.put("currentUserId", currentUserId);
        param.put("excuteRemark", excuteRemark);

        // 1??????????????????????????????????????????
        tblTestSetCaseMapper.updateExecuteResultById(param);

        // 2?????????????????????????????????
        int number = tblTestSetExecuteRoundCaseResult.updateSelective(param);
        //?????????????????????
        if (number < 1) {
            tblTestSetExecuteRoundCaseResult.insertSelective(param);
        }

        // 3.????????????????????????
        tblTestSetCaseExecuteMapper.insertByTestSetCase(param);
        param.put("executeCaseId", param.get("id"));

        // 4?????????????????????????????????
        if (enforcementResults != null) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) JSONArray.parse(enforcementResults);
            if (list.size() > 0) {
                param.put("list", list);
                tblTestSetCaseStepExecuteMapper.insertSelectiveMap(param);
            }
        }

        // 5?????????????????????
        if (!files.equals("") && !files.equals(null) && !files.equals("[]")) {
            List<TblTestSetCaseExecuteAttachement> fileTask = JsonUtil.fromJson(files, JsonUtil.createCollectionType(ArrayList.class, TblTestSetCaseExecuteAttachement.class));
            param.put("fileTask", fileTask);
            tblTestSetCaseExecuteAttachementMapper.addAttachementMap(param);
        }
        return param.get("id").toString();
    }

    /**
     * @return void
     * @author liushan
     * @Description ?????????????????????????????????
     * @Date 2020/3/30
     * @Param [testSetCase, enforcementResults, testSetCaseFiles, excuteRemark, defectFiles, defectInfo, request]
     **/
    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public void insertDefectWithxecution(String testSetCase, String enforcementResults, String testSetCaseFiles, String excuteRemark, MultipartFile[] defectFiles, String defectInfo, HttpServletRequest request) throws Exception {
        TblTestSetCase setCase = JSONObject.parseObject(testSetCase, TblTestSetCase.class);
        TblDefectInfo tblDefectInfo = JSONObject.parseObject(defectInfo, TblDefectInfo.class);
        String testCaseExecuteId = updateTestSetCaseAndInsertExecute(setCase, enforcementResults, testSetCaseFiles, excuteRemark, request);
        tblDefectInfo.setTestSetCaseExecuteId(Long.valueOf(testCaseExecuteId));
        defectService.insertDefect(tblDefectInfo, defectFiles, request);
    }


    //??????????????????????????????
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> selectTestExecute(Long testCaseExecuteId, Long testSetCaseId) {
        Map<String, Object> result = new HashMap<>();
        //TblTestSetCaseExecute tblTestSetCaseExecute=new TblTestSetCaseExecute();
        //tblTestSetCaseExecute =tblTestSetCaseExecuteMapper.findeSetCase(testCaseExecuteId);
        //result.put("tblTestSetCaseExecute", tblTestSetCaseExecute);
        List<TblTestSetCaseStepExecute> list = tblTestSetCaseStepExecuteMapper.selectByPrimaryKey(testCaseExecuteId);
        TblTestSetCaseExecute listCase = tblTestSetCaseExecuteMapper.selectByID(testCaseExecuteId);
        String userName = tblUserInfoMapper.getUserNameById(listCase.getExecuteUserId());//???????????????
        List<TblTestSetCaseExecuteAttachement> listFile = tblTestSetCaseExecuteAttachementMapper.selectAttchement(testCaseExecuteId);
        List<TblDefectInfo> listDefect = tblDefectInfoMapper.selectBytestCaseId(testCaseExecuteId);
        result.put("listCase", listCase);
        result.put("userName", userName);
        result.put("listStep", list);
        result.put("listFile", listFile);
        result.put("listDefect", listDefect);
        return result;
    }


    @Override
    public Map<String, Object> selectTestCaseExecute(Long testSetId, String caseNumber, Integer pageNumber, Integer pageSize) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("rows", tblTestSetCaseExecuteMapper.findeSetCase(testSetId, caseNumber, (pageNumber - 1) * pageSize, pageSize));
        result.put("total", tblTestSetCaseExecuteMapper.countfindeSetCase(testSetId, caseNumber));
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public void delecteFile(Long id, Long userId) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("lastUpdateBy", userId);
        tblTestSetCaseExecuteAttachementMapper.deleteFile(result);
    }

    /**
     * ??????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> selectUpdateCase(Long testSetCaseId) {
        Map<String, Object> result = new HashMap<>();
        List<TblTestSetCaseStep> list = tblTestSetCaseStepMapper.selectByPrimaryKey(testSetCaseId);
        result = tblTestSetCaseMapper.getTestSetCaseId(testSetCaseId);
        result.put("caseStep", list);
        return result;
    }

    /**
     * ????????????
     *
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false)
    public void updateCaseStep(String testSetCase, String testCaseStep, Long testUserId) throws Exception {
        // ??????????????????????????????????????????
        // testCaseManageService.updateCaseStep(testSetCase, testCaseStep, testUserId);

        Map<String, Object> result = (Map) JSON.parse(testSetCase);
        result.put("testUserId", testUserId);
        tblTestSetCaseMapper.updatetestSetCase(result);

        List<TblTestSetCaseStep> tblCaseStep = JsonUtil.fromJson(testCaseStep, JsonUtil.createCollectionType(ArrayList.class, TblTestSetCaseStep.class));
        if(tblCaseStep != null && tblCaseStep.size() > 0){

            Long caseId = Long.parseLong(result.get("id").toString());
            List<TblTestSetCaseStep> beforeList = tblTestSetCaseStepMapper.selectByPrimaryKey(caseId);
            List<Long> beforeIds = CollectionUtil.extractToList(beforeList, "id");

            List<Long> afterIds = CollectionUtil.extractToList(tblCaseStep, "id");
            List<Long> deleteIds = (List<Long>) CollectionUtil.getDiffent(beforeIds, afterIds);

            if (deleteIds.size() > 0) {
                tblTestSetCaseStepMapper.updateStatus(deleteIds);
            }

            for (TblTestSetCaseStep i : tblCaseStep) {
                if (i.getId() != null) {
                    i.setLastUpdateBy(testUserId);
                    tblTestSetCaseStepMapper.updateCaseStep(i);
                } else {
                    i.setCreateBy(testUserId);
                    i.setTestSetCaseId(caseId);
                    i.setStatus(1);
                    tblTestSetCaseStepMapper.insert(i);
                }
            }
        }
    }

    /**
     * ????????????
     */
    @Override
    @Transactional(readOnly = false)
    public void uodateTestCaseExecute(String allExecuteDate, Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> tblCaseStep = JsonUtil.fromJson(allExecuteDate,
                JsonUtil.createCollectionType(ArrayList.class, Map.class));
        result.put("createBy", userId);
        result.put("list", tblCaseStep);
        for (int i = 0; i < tblCaseStep.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map = tblCaseStep.get(i);
            map.put("createBy", userId);
            tblTestSetExecuteRoundCaseResult.updateRoundResult(map);
            tblTestSetCaseExecuteMapper.insertCodeBatch(map);
            if (tblCaseStep.get(i).get("resultId") == null || tblCaseStep.get(i).get("resultId") == "") {
                Map<String, Object> roundResult = new HashMap<>();//???????????????
                roundResult.put("tblCaseStep", tblCaseStep.get(i));
                roundResult.put("createBy", userId);
                tblTestSetExecuteRoundCaseResult.insertResltPass(roundResult);
            }
            Long id = Long.parseLong(tblCaseStep.get(i).get("setCaseId").toString());
            List<TblTestSetCaseStep> listStep = tblTestSetCaseStepMapper.selectByPrimaryKey(id);


            for (int j = 0; j < listStep.size(); j++) {
                listStep.get(j).setId(Long.parseLong(map.get("id").toString()));
            }
            if (listStep.size() > 0) {
                tblTestSetCaseStepExecuteMapper.insertPass(listStep);
            }

        }

        tblTestSetCaseMapper.updateResult(result);

    }

    @Override
    @Transactional(readOnly = true)
    public List<TblTestSetCaseVo> findExeTestSetByexcuteRound(Long testSetId, int excuteRound) {
        List<TblTestSetCaseVo> list = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        result.put("testSetId", testSetId);
        result.put("excuteRound", excuteRound);
        list = tblTestSetCaseMapper.selectByTestSetId(result);
        Object object = redisUtils.get("TBL_TEST_SET_CASE_CASE_EXECUTE_RESULT");
        Map<String, Object> mapsource = new HashMap<>();
        if (object != null && !"".equals(object)) {//redis????????????redis??????
            mapsource = JSON.parseObject(object.toString());
        }
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setExcuteRound("???" + excuteRound + "???");
                for (String key : mapsource.keySet()) {
                    if (list.get(i).getCaseExecuteResult() == null) {
                        list.get(i).setExecuteResult("?????????");
                    } else {
                        if (key.equals(list.get(i).getCaseExecuteResult().toString())) {
                            list.get(i).setExecuteResult(mapsource.get(key).toString());
                        }
                    }
                }
                List<CaseStepVo> tscs = tblTestSetCaseStepMapper.findByPrimaryKey(list.get(i).getId());
                list.get(i).setCaseStep(tscs);
            }
        }
        return list;
    }

    /**
    *@author liushan
    *@Description ?????????????????????????????????
    *@Date 2020/3/31
    *@Param [testSetMap, response]
    *@return void
    **/
    @Override
    public void exportTestSetCase(String testSetMap, HttpServletResponse response) throws Exception {
        Map<String, Object> params = (Map) JSON.parse(testSetMap);
        if(params.containsKey("caseIds")){
            params.put("caseIds",JSON.parseArray(params.get("caseIds").toString(),Long.class));
        }
        List<TblTestSetCaseVo>  list = tblTestSetCaseMapper.selectBatchPassWithStep(params);
        for (int i = 0; i < list.size(); i++){
            list.get(i).setCaseNumber(String.valueOf(i+1));
        }
        ExportParameters ep = tblTestSetCaseMapper.getTestSetInfo(Integer.parseInt(params.get("testSetId").toString()));
//        String excelFileName = PoiExcelUtil.getExcelFileName(ep);
        String excelFileName = ep.getTestTaskName()+".xlsx";
        new ExportExcel()
                .setWorkHead(TblTestSetCaseVo.class, CaseStepVo.class, ep.getTestTaskCode(), 7, 9, "????????????")
                .setDataListWithList(list).write(response,
                excelFileName);
//                excelFileName).dispose();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TblTestSetCaseVo> findExeTestSet(Long testSetId, int excuteRound[]) {
        List<TblTestSetCaseVo> list = new ArrayList<TblTestSetCaseVo>();

        Object object = redisUtils.get(DicConstants.TEST_SET_CASE_EXECUTE_RESULT);
        Map<String, Object> mapsource = new HashMap<>();
        if (object != null && !"".equals(object)) {//redis????????????redis??????
            mapsource = JSON.parseObject(object.toString());
        }
        for (int i = 0; i < excuteRound.length; i++) {
            Map<String, Object> result = new HashMap<>();
            result.put("testSetId", testSetId);
            result.put("excuteRound", excuteRound[i]);

            List<TblTestSetCaseVo> list2 = tblTestSetCaseMapper.selectByTestSetId(result);
            if (list2.size() > 0) {
                for (int j = 0; j < list2.size(); j++) {
                    list2.get(j).setExcuteRound("???" + excuteRound[i] + "???");
                    for (String key : mapsource.keySet()) {
                        if (list2.get(j).getCaseExecuteResult() == null) {
                            list2.get(j).setExecuteResult("?????????");
                        } else {
                            if (key.equals(list2.get(j).getCaseExecuteResult().toString())) {
                                list2.get(j).setExecuteResult(mapsource.get(key).toString());
                            }
                        }
                    }
                }
                list.addAll(list2);
            }
        }
        for (TblTestSetCaseVo caseVo : list) {
            List<CaseStepVo> tscs = tblTestSetCaseStepMapper.findByPrimaryKey(caseVo.getId());
            caseVo.setCaseStep(tscs);
        }
        return list;
    }

    @Override
    public List<TblTestSetCaseExecute> selectByTestSet(Long testSetId) {

        return tblTestSetCaseExecuteMapper.selectByTestSet(testSetId);
    }

    //??????????????????
    @Override
    public Long getCaseExecuteResult(Long testSetId, String caseNumber) {
        // TODO Auto-generated method stub
        HashMap<String, Object> map = new HashMap<>();
        map.put("testSetId", testSetId);
        map.put("caseNumber", caseNumber);
        return tblTestSetCaseMapper.getCaseExecuteResult(map);
    }

    //????????????????????????????????????
    @Override
    public List<TblTestSetCaseExecute> getCaseExecute(Long testSetId, String caseNumber) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<>();
        map.put("testSetId", testSetId);
        map.put("caseNumber", caseNumber);
        List<TblTestSetCaseExecute> list = tblTestSetCaseExecuteMapper.getCaseExecute(map);
        for (TblTestSetCaseExecute tblTestSetCaseExecute : list) {
            Integer integer = tblTestSetCaseExecute.getCaseExecuteResult();
            if (integer != null) {
                String key = integer.toString();
                String string = redisUtils.get(DicConstants.TEST_SET_CASE_EXECUTE_RESULT).toString();
                JSONObject jsonObject = JSON.parseObject(string);
                String result = jsonObject.get(key).toString();
                tblTestSetCaseExecute.setExecuteResult(result);
            }
        }
        return list;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @author liushan
     * @Description ??????id??????????????????????????? ????????????
     * @Date 2020/3/20
     * @Param [testCaseId]
     **/
    @Override
    public Map<String, Object> getTestCaseWithStepAndRemarkById(Long testCaseId, Long testSetId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> testSetCase = tblTestSetCaseMapper.getTestCaseById(testCaseId);
        result.put("testSetCase", testSetCase);
        result.put("testSetCaseStep", tblTestSetCaseStepMapper.selectByPrimaryKey(testCaseId));
        result.put("checkExcuteRemark", getCaseExecute(testSetId, testSetCase.get("caseNumber").toString()));
        return result;
    }


}
