package cn.pioneeruniverse.project.service.systeminfo;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblSystemInfo;
/**
 *
 * @ClassName: SystemInfoService
 * @Description: 系统信息service
 * @author author
 *
 */
public interface SystemInfoService {
	/**
	 * 查询
	 * @param tblSystemInfo
	 * @param pageNumber
	 * @param pageSize
	 * @return List<TblSystemInfo>
	 */

	List<TblSystemInfo> selectSystemInfo(TblSystemInfo tblSystemInfo, Integer pageNumber, Integer pageSize);


	/**
	 * 更新
	 * @param projectId
	 * @param systemNames
	 */
	void updateSystemInfo(Long projectId, List<String> systemNames);


	List<String> selectSystemName(Long id);


	void updateSystemInfoBySystemName(String systemName);

}
