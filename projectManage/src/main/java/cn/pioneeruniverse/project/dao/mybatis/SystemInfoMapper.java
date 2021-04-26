package cn.pioneeruniverse.project.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.project.entity.TblSystemInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
/**
 *
 * @ClassName: SystemInfoMapper
 * @Description: 系统dao接口
 * @author author
 *
 */

public interface SystemInfoMapper extends BaseMapper<TblSystemInfo> {

	List<TblSystemInfo> selectSystemInfo(HashMap<String, Object> map);

	Integer selectCount();

	/**
	 * 更新
	 * @param map
	 */

	void updateSystemInfo(HashMap<String, Object> map);

	/**
	 * 查询系统名称
	 * @param id
	 * @return
	 */

	List<String> selectSystemName(Long id);

	void updateSystemInfoBySystemName(String systemName);

	void untyingSystem(Long id);

	List<TblSystemInfo> selectSystems(Long id);

	String selectSystemNameById(Long systemId);

	/**
	 * 更新
	 * @param map
	 */

	void updateSystem(HashMap<String, Object> map);

	/**
	 * 根据系统编号获取系统信息
	 * @param systemCode
	 * @return
	 */

	Long findSystemIdBySystemCode(String systemCode);

	List<TblSystemInfo> getSystemsByProjectId(Long id);

	/**
	 * 根据投产窗口获取系统
	 * @param windowId
	 * @return
	 */

	List<TblSystemInfo> getSystemsByWindowId(Long windowId);


}
