package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblDevTaskScmGitFile;

public interface TblDevTaskScmGitFileMapper {

	/**
	* @author author
	* @Description 根据开发工作任务获取git  提交文件 和 操作类型
	* @Date 2020/9/22
	* @param id
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblDevTaskScmGitFile>
	**/
	List<TblDevTaskScmGitFile> getGitFile(Long id);

}
