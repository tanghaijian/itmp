package cn.pioneeruniverse.system.service.dept;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.common.dto.TblDeptInfoDTO;
import cn.pioneeruniverse.system.entity.TblDeptInfo;
/**
 *
 * @ClassName: IDeptService
 * @Description: 部门service
 * @author author
 * @date 2020年8月16日
 *
 */
public interface IDeptService {

    /**
     * 查询所有部门
     * @return  List<TblDeptInfo>
     */
    List<TblDeptInfo> getAllDept();

    /**
     * 查询
     * @param id
     * @return TblDeptInfo
     */

    TblDeptInfo selectDeptById(Long id);

    List<TblDeptInfo> selectDeptByParentId(Long id);

    /**
     * 新增
     * @param dept
     */

    void insertDept(TblDeptInfo dept);

    /**
     * 更新
     * @param dept
     */

    void updateDept(TblDeptInfo dept);

    List<TblDeptInfo> selectDeptByCompanyId(Long companyId);

    List<Map<String, Object>> getDeptData(String deptData);

    List<TblDeptInfoDTO> getAllDeptInfo();

	int tmpDeptData(TblDeptInfo deptInfo);

    /**
     * 查询
     * @param deptInfo
     * @return List<TblDeptInfo>
     */

	List<TblDeptInfo> getDeptByDeptName(TblDeptInfo deptInfo);
}
