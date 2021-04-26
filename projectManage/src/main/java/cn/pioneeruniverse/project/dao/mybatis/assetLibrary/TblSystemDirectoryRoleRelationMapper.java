package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryRoleRelation;

public interface TblSystemDirectoryRoleRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblSystemDirectoryRoleRelation record);

    int insertSelective(TblSystemDirectoryRoleRelation record);

    TblSystemDirectoryRoleRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryRoleRelation record);

    int updateByPrimaryKey(TblSystemDirectoryRoleRelation record);

	List<TblSystemDirectoryRoleRelation> getSystemDirectoryRoleRelationByRoleId(Long roleId);

	void deleteByRoleId(Long roleId);

	void batchInsert(List<TblSystemDirectoryRoleRelation> systemDirectoryRoleRelations);

	List<TblSystemDirectoryRoleRelation> getCurentUserRoleRelations(@Param("projectId")Long projectId, @Param("systemDirectoryId")Long systemDirectoryId,@Param("userId") Long uid);
}