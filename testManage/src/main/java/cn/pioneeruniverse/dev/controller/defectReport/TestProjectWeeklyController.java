package cn.pioneeruniverse.dev.controller.defectReport;



import cn.pioneeruniverse.dev.dto.TestDefectInfoDTO;
import cn.pioneeruniverse.dev.service.testProjectWeekly.ITestProjectWeeklyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 项目周报
 * @date 2020/4/15
 */
@RestController
public class TestProjectWeeklyController {


    @Autowired
    private ITestProjectWeeklyServer iProjectWeeklyServer;

//    /**
//     *  任务交付累计流图
//     * @param taskDeliver
//     * @return Map<String, Object>
//     */
//    @PostMapping("/taskDeliver")
//    public Map<String, Object> getTaskDeliverHandler(@RequestBody TaskDeliverDTO taskDeliver){
//        return iProjectWeeklyServer.getTaskDeliverList(taskDeliver);
//    }

    /**
     * 缺陷统计图
     * @param defectInfo
     * @return
     */
    @PostMapping("/testDefectInfo")
    public Map<String, Object> getDefectInfoCountHandler(@RequestBody TestDefectInfoDTO defectInfo){
        return iProjectWeeklyServer.getDefectInfoCountHandler(defectInfo);
    }

//    /**
//     * 缺陷待解决统计图
//     * @param defectResolved
//     * @return
//     */
//    @PostMapping("/defectResolved")
//    public List<DefectResolvedVO> getDefectResolvedHandler(@RequestBody DefectInfoDTO defectResolved){
//        return iProjectWeeklyServer.getDefectResolvedHandler(defectResolved);
//    }

}
