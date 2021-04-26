package cn.pioneeruniverse.dev.service.workTask;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.dev.entity.TblRequirementInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.entity.TblTestTask;
import cn.pioneeruniverse.dev.entity.TblUserInfo;
import cn.pioneeruniverse.dev.vo.TblTestWorkVo;
import cn.pioneeruniverse.dev.vo.TestTaskVo;


public interface WorkTaskService {
/**
 * 获取测试任务(jqgrid)  
 * @param TblTestTask
 * @param page
 * @param rows
 * @return
 */
Map getTestTask(String sidx, String sord,TblTestWorkVo tblTestTask,JqGridPage<TblTestWorkVo> jqGridPage,Integer page,Integer rows,HttpServletRequest request);
/**
 * 获取测试任务(bootstrap)  
 * @param TblTestTask
 * @param page
 * @param rows
 * @return
 */
Map<String,Object> getTestTask2(TblTestTask TblTestTask,Integer page,Integer rows);
/**
 * 获取所有关联任务
 * @param tblTestTask
 * @param pageNumber
 * @param pageSize
 * @return
 */
List<Map<String,Object>> getAllFeature(TblTestTask tblTestTask ,Long userdId,Integer pageNumber, Integer pageSize,HttpServletRequest request);
/**
 * 获取带状态（1，2）的关联任务
 * @param tblTestTask
 * @param pageNumber
 * @param pageSize
 * @return
 */
List<Map<String,Object>> getAllFeatureTask(TblTestTask tblTestTask ,Integer pageNumber, Integer pageSize);
/**
 * 获取人员
 * @param testUserId
 * @return
 */
List<TblTestTask> getAllTestUser(Long testUserId);
/**
 * 获取所有需求
 * @param RequirementInfo
 * @param page
 * @param rows
 * @return
 */
Map getAllRequirt(TblRequirementInfo RequirementInfo, Integer page, Integer rows);
/**
 * 获取所有系统
 * @param systemInfo
 * @param page
 * @param rows
 * @return
 */
Map getAllsystem(TblSystemInfo systemInfo,Integer page,Integer rows);

List<TblTestTask> getUserName();

Map<String,Object> getSeeDetail(String id);

//获取编辑信息
Map<String,Object> getEditTestTask(String id);
//添加
TblTestTask addtestTask(String obj,String attachFiles,Long Userid,HttpServletRequest request,String UserAccount);

void updateTestTask(String obj,String attachFiles,String deleteFiles,Long Userid,HttpServletRequest request) throws Exception;
//处理
void Handle(String handle,String HattachFiles,String deleteFiles,HttpServletRequest request);

//待处理
void DHandle(String handle,String DHattachFiles,String deleteFiles,HttpServletRequest request);

void examineHandle(String handle,String DHattachFiles,String deleteFiles,HttpServletRequest request);
//分派
void assigTest(String assig,String Remark,HttpServletRequest request);

String DevfindMaxCode(int length);
   
 /* 获取文件导出信息*/
List<TblTestTask> getExcelAllWork(TblTestWorkVo tblTestWorkVo,HttpServletRequest request);
Map ReqSystem();
Map ReqStatus();
/**
 * 获取弹窗用户信息
 * @param tblUserInfo
 * @param notWithUserID
 * @param devID
 * @param pageNumber
 * @param pageSize
 * @return
 */
List<Map<String, Object>> getAllTestUser(TblUserInfo tblUserInfo,Integer notWithUserID,Integer devID,Integer pageNumber, Integer pageSize);
 
TblTestTask getTestTaskById(Long id);

List<TblTestTask> selectTestTaskByRequirementFeatureId(Long requirementFeatureId,String nameOrNumber, String createBy, String testTaskId);
List<Long> selectUserIdsById(Long id);
List<TblTestTask> getTestTaskByCurrentUser(Long uid);
}
