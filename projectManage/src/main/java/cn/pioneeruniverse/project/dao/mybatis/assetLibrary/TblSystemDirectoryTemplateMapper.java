package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryTemplate;

public interface TblSystemDirectoryTemplateMapper {

	List<TblSystemDirectoryTemplate> getAllSystemDirectoryTemplate(Integer projectType);

}
