package cn.pioneeruniverse.system.dao.mybatis.dept;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.common.dto.TblDeptInfoDTO;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.system.entity.TblDeptInfo;

/**
 *
 * @ClassName: DeptDao
 * @Description: 部门dao
 * @author author
 *
 */
public interface DeptDao extends BaseMapper<TblDeptInfo> {
	/**
	 * 查询部门
	 * @return List<TblDeptInfo>
	 */
	List<TblDeptInfo> getAllDept();

	/**
	 * 查询
	 * @param id
	 * @return TblDeptInfo
	 */
	
	TblDeptInfo selectDeptById(Long id);
	
	List<TblDeptInfo> selectDeptByParentId(Long id);
	
	void insertDept(TblDeptInfo dept);

	/**
	 * 更新
	 * @param dept
	 */
	void updateDept(TblDeptInfo dept);
	
	String selectMaxNum();
	
	String selectPPDept(Long id);
	
	void setChildrenDisable(TblDeptInfo dept);
	
	void updateChildrenDept(TblDeptInfo dept);
	
	List<TblDeptInfo> selectDeptByCompanyId(Long companyId);

    TblDeptInfo getDeptByCode(String depCode);

	/**
	 * 查询所有部门
	 * @return List<TblDeptInfoDTO>
	 */
    List<TblDeptInfoDTO> getAllDeptInfo();

	List<TblDeptInfo> getDeptByDeptName(TblDeptInfo deptInfo);

	List<TblDeptInfo> getAllDeptByPage(Map<String, Object> map);
}
