package cn.pioneeruniverse.project.service.program;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.project.entity.TblProgramInfo;

public interface ProgramService {
	/**
	 *
	 * @Title: getAllPrograms
	 * @Description: 获取所有项目群列表
	 * @author author
	 * @param programInfo：封装的查询条件
	 * @param uid 当前用户ID
	 * @param roleCodes
	 * @param page 第几页
	 * @param rows 每页数据量
	 * @return List<TblProgramInfo>
	 */
	List<TblProgramInfo> getAllPrograms(TblProgramInfo programInfo, Long uid, List<String> roleCodes, Integer page,
			Integer rows);
	/**
	 *
	 * @Title: getProgramById
	 * @Description: 根据ID获取项目群详情
	 * @author author
	 * @param id 项目群ID
	 * @return TblProgramInfo
	 */
	TblProgramInfo getProgramById(Long id);
	/**
	 *
	 * @Title: updateProgram
	 * @Description:更新项目群
	 * @author author
	 * @param tblProgramInfo 项目群信息
	 * @param request
	 */
	void updateProgram(TblProgramInfo tblProgramInfo, HttpServletRequest request);

}
