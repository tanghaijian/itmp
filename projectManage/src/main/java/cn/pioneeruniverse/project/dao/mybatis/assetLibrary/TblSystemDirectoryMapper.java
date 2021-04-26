package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;


import cn.pioneeruniverse.project.entity.TblSystemDirectory;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
/**
 *
 * @ClassName: TblSystemDirectoryMapper
 * @Description: 系统目录mapper
 * @author author
 *
 */
public interface TblSystemDirectoryMapper extends BaseMapper<TblSystemDirectory> {

	/**
	 * 查询
	 * @param projectId
	 * @return
	 */
	List<TblSystemDirectory> getSystemDirectoryBySystemId(Long projectId);

	void insertSystemDir(TblSystemDirectory systemDirectory);

	/**
	 * 更新
	 * @param map
	 */
	void updateSystemDIr(Map<String, Object> map);

	Long getId(Map<String, Object> map);

	void updateSystemDIr2(Map<String, Object> map);

}