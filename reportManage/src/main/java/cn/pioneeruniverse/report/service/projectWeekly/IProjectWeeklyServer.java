package cn.pioneeruniverse.report.service.projectWeekly;

import cn.pioneeruniverse.report.dto.DefectInfoDTO;
import cn.pioneeruniverse.report.dto.TaskDeliverDTO;
import cn.pioneeruniverse.report.vo.DefectResolvedVO;

import java.util.List;
import java.util.Map;

public interface IProjectWeeklyServer {

    /**
     *
     * @Title: getTaskDeliverList
     * @Description: 任务交付累计流图
     * @author author
     * @param taskDeliver
     * @return map key:timeTraceList 返回的数据
     * @throws
     */
    Map<String, Object> getTaskDeliverList(TaskDeliverDTO taskDeliver);
    /**
     *
     * @Title: getDefectInfoCountHandler
     * @Description: 根据查询条件获取选择的时间范围内的缺陷数目，并且按照缺陷状态和紧急程度每天统计
     *               总体逻辑：比如时间范围为：2020-01-01 到 2020-03-01
     *               在这段时间提出的并且（当前还没有关闭的缺陷或者当前状态已经关闭，但是在统计当天以后关闭的）。
     *               比如统计：2020-02-02这一天，那么统计的条件就是在2020-01-01到2020-03-01提出的缺陷，到目前为止依然没有关闭的缺陷。
     *               或者当前已经关闭了，但是关闭日期大于2020-02-02这一天
     * @author author
     * @param defectInfo 封装的查询条件
     * @return map key:defectInfoList 返回的每天的缺陷数量
     * @throws
     */
    Map<String, Object> getDefectInfoCountHandler(DefectInfoDTO defectInfo);
    /**
     *
     * @Title: getDefectResolvedHandler
     * @Description: 获取未解决的缺陷数，并按系统分类返回
     *               限制条件：缺陷状态不是新建、撤销、关闭的缺陷，其他根据查询条件限制
     * @author author
     * @param defectResolved
     * @return
     * @throws
     */
    List<DefectResolvedVO> getDefectResolvedHandler(DefectInfoDTO defectResolved);


}
