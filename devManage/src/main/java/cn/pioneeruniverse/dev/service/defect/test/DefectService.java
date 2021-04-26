package cn.pioneeruniverse.dev.service.defect.test;

import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.common.entity.BootStrapTablePage;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.dev.entity.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2018/12/10 下午 1:44
 */
public interface DefectService {
 /**
  *@author liushan
  *@Description 条件查询所有的需求
  *@Date 2020/7/31
  * @param requirementInfo 条件查询数据
  * @param pageNumber
  * @param pageSize
  *@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementInfo>
  **/
 List<TblRequirementInfo> getAllRequirement(TblRequirementInfo requirementInfo, Integer pageNumber, Integer pageSize)throws Exception;
 /**
  * 新增缺陷的 暂存操作（状态：新建 1）,提交操作（状态：待确认 2）
  *
  * @param tblDefectInfo
  * @param request
  */
 Map<String, Object> insertDefect(TblDefectInfo tblDefectInfo, HttpServletRequest request)throws Exception;

 /**
  *
  * @Title: updateDefect
  * @Description: 更新缺陷信息
  * @author author
  * @param tblDefectInfo 缺陷信息
  * @param request
  * @return
  * @throws Exception
  * @throws
  */
 Map<String, Object>  updateDefect(TblDefectInfo tblDefectInfo, HttpServletRequest request)throws Exception;
 /**
  * 根据数据字典编号获取数据字典
  *
  * @param key
  * @return
  */
 Map<String,Object> getDicKey(String key);
 /**
  * @author author
  * @Description 条件查询所有的系统
  * @Date 2020/9/18
  * @param systemInfo 条件查询
  * @param pageNumber 页码
  * @param pageSize 页数
  * @param request
  * @return java.util.Map<java.lang.String,java.lang.Object>
  *  result.put("rows", list); 分页查询的数据
  *  result.put("total", total); 总数据
  **/
 Map<String, Object> getAllSystemInfo(TblSystemInfo systemInfo, Integer pageNumber, Integer pageSize,HttpServletRequest request)throws Exception;
 /**
  * @author author
  * @Description 逻辑删除缺陷
  * @Date 2020/9/18
  * @param id
  * @param request
  * @return void
  **/
 void removeDefect(Long id,HttpServletRequest request)throws Exception;
 /**
  * @author author
  * @Description 逻辑删除缺陷 的方法
  * @Date 2020/9/18
  * @param id
  * @return void
  **/
 void removeDefectById(Long id)throws Exception;
 /**
  * 上传文件
  *
  * @param files
  * @param defectId
  * @param logId
  * @param request
  */
 String updateFiles(MultipartFile[] files, Long defectId, Long logId, HttpServletRequest request) throws Exception;
 /**
  * @author author
  * @Description 根据路径获取文件信息
  * @Date 2020/9/18
  * @param path 文件路径
  * @return cn.pioneeruniverse.dev.entity.TblDefectAttachement
  **/
 TblDefectAttachement getDefectAttByUrl(String path)throws Exception;
 /**
  * @author author
  * @Description 根据缺陷主键查询，所属的附件
  * @Date 2020/9/18
  * @param defectId 缺陷主键
  * @return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectAttachement>
  **/
 List<TblDefectAttachement> findAttListByDefectId(Long defectId)throws Exception;
 /**
  *@author liushan
  *@Description 删除附件
  *@Date 2020/7/31
  * @param ids 需要删除附件的ids
  * @param defectId 缺陷id
  * @param logId 日志id
  * @param request
  *@return void
  **/
 void removeAtts(Long[] ids, Long defectId, Long logId,HttpServletRequest request) throws Exception;
 /**
  * 备注日志上传文件
  *
  * @param files
  * @param logId
  * @param request
  * @return
  */
 String updateRemarkLogFiles(MultipartFile[] files, Long logId, HttpServletRequest request) throws Exception;
 /**
  * 待确认的三个操作
  * 根据操作修改缺陷的状态保存缺陷日志内容
  *
  * @param tblDefectInfo
  * @param defectRemark
  * @param request
  */
 Long updateDefectwithTBC(TblDefectInfo tblDefectInfo, TblDefectRemark defectRemark, Double actualWorkload, HttpServletRequest request)throws Exception;

 /**
  * 修改的操作
  *
  * @param tblDefectInfo
  * @param defectRemark
  * @param request
  */
 Long updateDefectStatus(TblDefectInfo tblDefectInfo, TblDefectRemark defectRemark,  HttpServletRequest request)throws Exception;
 /**
  * 根据id查询日志信息
  *
  * @param id
  * @return
  */
 List<TblDefectLog> getDefectLogsById(Long id)throws Exception;
 /**
  *@author liushan
  *@Description 查询最近的一次日志
  *@Date 2020/7/31
  * @param defectId 缺陷id
  *@return java.util.Map<java.lang.String,java.lang.Object> 看方法注释
  **/
 Map<String, Object> getDefectRecentLogById(Long defectId)throws Exception;
 /**
  * 条件查询所有的投产窗口
  *
  * @param window  条件查询数据
  * @param pageNumber
  * @param pageSize
  * @return
  */
 Map<String,Object> getAllComWindow(TblCommissioningWindow window, Integer pageNumber, Integer pageSize)throws Exception;
 /**
  * 导出当前页面查询出的结果集
  * 当前页面查询出的结果
  *
  * @param response
  * @param request
  * @throws Exception
  */
 void export(JqGridPage<DefectInfoVo> defectInfoVoJqGridPage, DefectInfoVo defectInfoVo,HttpServletResponse response, HttpServletRequest request) throws Exception;
 /**
  *@author liushan
  *@Description 获取缺陷管理列表数据
  *@Date 2020/7/21
  *@Param [page, defectInfo, currentUserId, request]
  *@return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.dev.entity.DefectInfoVo>
  **/
 JqGridPage<DefectInfoVo> findDefectListPage(JqGridPage<DefectInfoVo> page,DefectInfoVo tblDefectInfo,Long currentUserId,HttpServletRequest request) throws Exception;
 /**
  * @author author
  * @Description 根据id查询缺陷数据
  * @Date 2020/9/18
  * @param defectId
  * @return cn.pioneeruniverse.dev.entity.TblDefectInfo
  **/
 TblDefectInfo getDefectById(Long defectId);
 /**
  *
  * @Title: getDefectEntity
  * @Description: 缺陷详情 以及关联数据
  * @author author
  * @param defectId
  * @return
  * @throws
  */
 TblDefectInfo getDefectEntity(Long defectId);
 /**
  *@author liushan
  *@Description 查询备注
  *@Date 2020/7/31
  * @param defectId
  *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectRemark>
  **/
 List<TblDefectRemark> getRemarkByDefectId(Long defectId);
 /**
  *@author liushan
  *@Description 查询备注附件
  *@Date 2020/7/31
  * @param defectId
  *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectRemarkAttachement>
  **/
 List<TblDefectRemarkAttachement> getRemarkAttsByDefectId(Long defectId);
 /**
  *@author liushan
  *@Description 保存备注
  *@Date 2020/7/31
  * @param tblDefectRemark 缺陷备注
  * @param files 附件
  * @param request
  *@return void
  **/
 void addDefectRemark(TblDefectRemark tblDefectRemark, List<TblDefectRemarkAttachement> files,
         HttpServletRequest request);
 /**
  *@author liushan
  *@Description 同步到测管数据 保存备注
  *@Date 2020/7/31
  * @param tblDefectRemark
  * @param files
  * @param request
  *@return void
  **/
 void addTmpDefectRemark(TblDefectRemark tblDefectRemark, List<TblDefectRemarkAttachement> files,
         HttpServletRequest request);
 /**
  *@author liushan
  *@Description 缺陷修改投产窗口数据 开管
  *@Date 2020/7/31
  * @param id 缺陷id
  * @param commissioningWindowId  投产窗口id
  *@return void
  **/
 void updateCommssioningWindowId(Long id,Long commissioningWindowId);
 /**
  *@author liushan
  *@Description 缺陷修改投产窗口数据 测管
  *@Date 2020/7/31
  * @param id 缺陷id
  * @param commissioningWindowId  投产窗口id
  *@return void
  **/
 void updateTmpCommssioningWindowId(Long id,Long commissioningWindowId);
 /**
  *@author liushan
  *@Description 开发库关闭缺陷
  *@Date 2020/7/31
  * @param id
  *@return void
  **/
 void closeDefect(Long id);
 /**
  *@author liushan
  *@Description 测试库关闭缺陷
  *@Date 2020/7/31
  * @param id
  *@return void
  **/
 void closeTmpDefect (Long id);
}
