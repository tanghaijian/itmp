package cn.pioneeruniverse.dev.service.dashboard;

import java.util.Map;

public interface DashBoardService {
	
	//个人工作台
	Map<String, Object> getUserDesk(Long userId);
	//获取所有项目
	Map<String, Object> getAllProject(Long id);
	//获取投产窗口关联开发任务状态
	Map<String, Object> getFratureStatus(Long windowId,Long systemId);
	//DashBoard
	Map<String, Object> getDashBoard(Long systemId,String startDate,String endDate);
	
	Map<String, Object> getTheCumulative(Long systemId,String startDate,String endDate);

	Map<String, Object> valueStreamMapping(Long timeTraceId);

	Map<String, Object> getSonarByModuleId(Long systemModuleId);
}
