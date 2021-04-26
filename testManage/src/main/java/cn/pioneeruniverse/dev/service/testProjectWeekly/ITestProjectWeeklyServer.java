package cn.pioneeruniverse.dev.service.testProjectWeekly;


import cn.pioneeruniverse.dev.dto.TestDefectInfoDTO;
import cn.pioneeruniverse.dev.dto.TestTaskDeliverDTO;
import cn.pioneeruniverse.dev.vo.TestDefectResolvedVO;

import java.util.List;
import java.util.Map;

public interface ITestProjectWeeklyServer {


    Map<String, Object> getTaskDeliverList(TestTaskDeliverDTO taskDeliver);

    Map<String, Object> getDefectInfoCountHandler(TestDefectInfoDTO defectInfo);

    List<TestDefectResolvedVO> getDefectResolvedHandler(TestDefectInfoDTO defectResolved);


}
