package cn.pioneeruniverse.report.dao.mybatis.report.projectWeeklyDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.report.dto.DefectInfoDTO;
import cn.pioneeruniverse.report.dto.TaskDeliverDTO;
import cn.pioneeruniverse.report.vo.DefectInfoVO;
import cn.pioneeruniverse.report.vo.DefectResolvedVO;
import cn.pioneeruniverse.report.vo.RequirementFeatureTimeTraceVO;

public interface ProjectWeeklyMapper {

	
/**
 * 
* @Title: selectBySystemId1
* @Description: 获取开发任务时间点追踪信息
* @author author
* @param taskDeliver 封装的查询条件
* @return
* @throws
 */
    List<RequirementFeatureTimeTraceVO> selectBySystemId1(TaskDeliverDTO taskDeliver);

    /**
     * 
    * @Title: getDefectInfoCountHandler
    * @Description: 从测试数据库中获取缺陷信息，按系统统计数目
    * @author author
    * @param defectInfo
    * @return
    * @throws
     */
    @DataSource(name = "tmpDataSource")
    List<DefectInfoVO> getDefectInfoCountHandler(DefectInfoDTO defectInfo);

    /**
     * 
    * @Title: getDefectResolvedHandler
    * @Description: 从测试库中获取缺陷待解决
    * @author author
    * @param defectInfo
    * @return
    * @throws
     */
    @DataSource(name = "tmpDataSource")
    List<DefectResolvedVO> getDefectResolvedHandler(DefectInfoDTO defectInfo);
   /**
    * 
   * @Title: getSystemInfoList
   * @Description: 获取系统列表
   * @author author
   * @param id
   * @return
   * @throws
    */
    List<DefectResolvedVO> getSystemInfoList(@Param("id") List<Long> id);
/**
 * 
* @Title: selectDefectInfoList
* @Description: 从测试库中获取缺陷列表
* @author author
* @param defectInfo
* @return
* @throws
 */
    @DataSource(name = "tmpDataSource")
    List<DefectResolvedVO>selectDefectInfoList(DefectInfoDTO defectInfo);

}
