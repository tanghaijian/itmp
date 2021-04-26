package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryRole;

public interface TblSystemDirectoryRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblSystemDirectoryRole record);

    int insertSelective(TblSystemDirectoryRole record);

    TblSystemDirectoryRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryRole record);

    int updateByPrimaryKey(TblSystemDirectoryRole record);

	List<TblSystemDirectoryRole> getAllByProjectId(Long projectId);
}