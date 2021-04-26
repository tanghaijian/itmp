package cn.pioneeruniverse.dev.service.testExecute;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.dev.entity.*;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 测试案例执行
 *
 * @author:xukai
 * @version:2018年12月5日 下午3:13:08
 */
public interface testExecuteService {

    /**
     * 查询测试案例批量执行列表
     *
     * @param testSetMap
     * @return
     */

    PageInfo<List<Map<String, Object>>> selectByPrimaryKey(String testSetMap);

    int countSelectByPrimaryKey(Map<String, Object> params);

    /**
    *@author liushan
    *@Description 查询测试任务
    *@Date 2020/4/9
    *@Param [testSetMap]
    *@return cn.pioneeruniverse.dev.entity.TblRequirementFeature
    **/
    TblRequirementFeature selectRequirementFeatureByTestSet(String testSetMap);

    /**
     * 批量执行
     *
     * @param allExecuteDate
     * @param userId
     */
    void uodateTestCaseExecute(String allExecuteDate, Long userId);

    /**
     * 测试案例执行弹窗
     *
     * @param id
     * @return
     */
    List<TblTestSetCaseStep> selectTestCaseRun(Long id);

    /**
     * 添加测试执行 旧的，怕影响没删除,需要请调用updateTestSetCaseAndInsertExecute
     *
     * @param rows
     * @param type
     * @param enforcementResults
     * @param testUserId
     */
    String insertSelective(String rows, String type, String enforcementResults, String excuteRemark, int excuteRound, Long testUserId, Long testSetId, String files);

    /**
     * @return java.lang.String
     * @author liushan
     * @Description 测试执行操作
     * @Date 2020/3/25
     * @Param [testSetCase, enforcementResults, files, excuteRemark, request]
     **/
    String updateTestSetCaseAndInsertExecute(TblTestSetCase testSetCase, String enforcementResults, String files, String excuteRemark, HttpServletRequest request) throws Exception;

    /**
     * @return void
     * @author liushan
     * @Description 测试执行失败并提交缺陷
     * @Date 2020/3/30
     * @Param [testSetCase, enforcementResults, testSetCaseFiles, excuteRemark, defectFiles, defectInfo, request]
     **/
    void insertDefectWithxecution(String testSetCase, String enforcementResults, String testSetCaseFiles, String excuteRemark, MultipartFile[] defectFiles, String defectInfo, HttpServletRequest request) throws Exception;

    /**
     * 查询执行详情信息
     *
     * @param testCaseExecuteId
     * @return
     */
    Map<String, Object> selectTestExecute(Long testCaseExecuteId, Long testSetCaseId);

    /**
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author liushan
     * @Description 查询测试执行列表信息
     * @Date 2020/3/20
     * @Param [testSetId, caseNumber, pageNumber, pageSize]
     **/
    Map<String, Object> selectTestCaseExecute(Long testSetId, String caseNumber, Integer pageNumber, Integer pageSize) throws Exception;

    /**
     * 删除附件
     *
     * @param id
     * @param userId
     */
    void delecteFile(Long id, Long userId);

    Map<String, Object> selectUpdateCase(Long testSetCaseId);

    void updateCaseStep(String testSetCase, String testCaseStep, Long testUserId) throws Exception;


    List<TblTestSetCaseVo> findExeTestSetByexcuteRound(Long testSetId, int excuteRound);

    /**
     * @return void
     * @author liushan
     * @Description 导出测试集下的测试案例
     * @Date 2020/3/31
     * @Param [testSetMap, response]
     **/
    void exportTestSetCase(String testSetMap, HttpServletResponse response) throws Exception;

    List<TblTestSetCaseVo> findExeTestSet(Long testSetId, int excuteRound[]);

    List<TblTestSetCaseExecute> selectByTestSet(Long testSetId);

    Long getCaseExecuteResult(Long testSetId, String caseNumber);

    List<TblTestSetCaseExecute> getCaseExecute(Long testSetId, String caseNumber);

    /**
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author liushan
     * @Description 根据id获取测试案例、步骤 备注数据
     * @Date 2020/3/20
     * @Param [testCaseId]
     **/
    Map<String, Object> getTestCaseWithStepAndRemarkById(Long testCaseId, Long testSetId);
}
