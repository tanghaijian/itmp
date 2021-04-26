package cn.pioneeruniverse.report.controller.defectReport;


import cn.pioneeruniverse.report.dto.DefectInfoDTO;
import cn.pioneeruniverse.report.dto.TaskDeliverDTO;
import cn.pioneeruniverse.report.service.projectWeekly.IProjectWeeklyServer;
import cn.pioneeruniverse.report.vo.DefectResolvedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: ProjectWeeklyController
* @Description: 项目周报
* @author author
* @date 2020年8月3日 下午9:15:04
*
 */
@RestController
public class ProjectWeeklyController {


    @Autowired
    private IProjectWeeklyServer iProjectWeeklyServer;

    /**
     * 
    * @Title: getTaskDeliverHandler
    * @Description: 任务交付累计流图
    * @author author
    * @param taskDeliver 封装的查询条件
    * @return
    * @throws
     */
    @PostMapping("/taskDeliver")
    public Map<String, Object> getTaskDeliverHandler(@RequestBody TaskDeliverDTO taskDeliver){
        return iProjectWeeklyServer.getTaskDeliverList(taskDeliver);
    }

    /**
     * 
    * @Title: getDefectInfoCountHandler
    * @Description: 缺陷统计图
    * @author author
    * @param defectInfo 封装的查询条件
    * @return
    * @throws
     */
    @PostMapping("/defectInfo")
    public Map<String, Object> getDefectInfoCountHandler(@RequestBody DefectInfoDTO defectInfo){
        return iProjectWeeklyServer.getDefectInfoCountHandler(defectInfo);
    }

    /**
     * 
    * @Title: getDefectResolvedHandler
    * @Description: 缺陷待解决统计图
    * @author author
    * @param defectResolved 封装的查询条件
    * @return
    * @throws
     */
    @PostMapping("/defectResolved")
    public List<DefectResolvedVO> getDefectResolvedHandler(@RequestBody DefectInfoDTO defectResolved){
        return iProjectWeeklyServer.getDefectResolvedHandler(defectResolved);
    }

}
