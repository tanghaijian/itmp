package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRelation;
import cn.pioneeruniverse.project.vo.ZtreeVo;

public interface TblSystemDirectoryDocumentChaptersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblSystemDirectoryDocumentChapters record);

    int insertSelective(TblSystemDirectoryDocumentChapters record);

    TblSystemDirectoryDocumentChapters selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryDocumentChapters record);

    int updateByPrimaryKey(TblSystemDirectoryDocumentChapters record);
    /**
     * 获取章节树
     * @param systemDirectoryDocumentId
     * @return
     */
    List<ZtreeVo> getChaptersTreeByDocumentId(@Param("systemDirectoryDocumentId")Long systemDirectoryDocumentId);
    /**
     * 获取章节树(含选中的)
     * @param systemDirectoryDocumentId
     * @return
     */
    List<ZtreeVo> getChaptersCheckedTreeByDocumentId(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation);
    /**
     * 删除章节关联关系
     * @param tblSystemDirectoryDocumentChaptersRelation
     */
    void deleteRelation(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation);
    /**
     * 添加章节关联关系
     * @param tblSystemDirectoryDocumentChaptersRelation
     * @return
     */
    int insertDocumentRelation(@Param("list")List<TblSystemDirectoryDocumentChaptersRelation> list);
    /**
     * 获取关联章节
     * @param systemDirectoryDocumentId1
     * @return
     */
    List<TblSystemDirectoryDocumentChapters> getRelationChapters(@Param("systemDirectoryDocumentId1")Long systemDirectoryDocumentId1);
    /**
     * 获取被关联章节
     * @param systemDirectoryDocumentId1
     * @param systemDirectoryDocumentChapterId1
     * @return
     */
    List<TblSystemDirectoryDocumentChapters> getRelationedChapters(@Param("systemDirectoryDocumentId1")Long systemDirectoryDocumentId1,
    																	@Param("systemDirectoryDocumentChapterId1")Long systemDirectoryDocumentChapterId1);
    /**
     * 根据文档id查询章节
     * @param systemDirectoryDocumentId
     * @return
     */
    List<TblSystemDirectoryDocumentChapters> getChaptersByDocumentId(@Param("systemDirectoryDocumentId")Long systemDirectoryDocumentId);
    /**
     * 根据父级id查询章节
     * @param parentId
     * @return
     */
    List<TblSystemDirectoryDocumentChapters> getChaptersByPrentId(@Param("parentId")Long parentId);
    
	List<TblSystemDirectoryDocumentChapters> getAllBySystemDirectoryDocumentId(Long systemDirectoryDocumentId);
	//查询该章节下的所有子集
	List<TblSystemDirectoryDocumentChapters> getChildrenChapters(@Param("id")Long id);
	//查询该章节下的子集
	List<TblSystemDirectoryDocumentChapters> getChildChapters(@Param("id")Long id);

	void updateChildrenChapters(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters);

	void updateParentIdsById(TblSystemDirectoryDocumentChapters tblSystemDirectoryDocumentChapters);

	void updateStatusById(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters);

	/**
	*@author liushan
	*@Description 查询次章节关联的附件、开发任务、需求
	*@Date 2020/1/14
	*@Param [id]
	*@return cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters
	**/
	TblSystemDirectoryDocumentChapters selectChaptersWithRelatedData(Long id);

	void updateChaptersOrder(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters);

	Integer getChildrenMaxOrder(Long parentId);

	void updateChaptersLevel(@Param("id")Long id,@Param("chaptersLevel") Integer chaptersLevel);

	void updateParent(TblSystemDirectoryDocumentChapters systemDirectoryDocumentChapters);
	
	List<TblSystemDirectoryDocumentChapters> selectChapterByIds(@Param("list") List<String> list);

	/**
	 *@author liushan
	 *@Description 查询需求关联的章节ids
	 *@Date 2020/2/17
	 *@Param [chaptersId]
	 *@return java.util.List<cn.pioneeruniverse.project.entity.TblRequirementInfo>
	 **/
	List<TblSystemDirectoryDocumentChapters> getChaptersByRequirementId(@Param("systemDirectoryDocumentId") Long systemDirectoryDocumentId, @Param("requirementCode") String requirementCode);

	/**
	 *@author liushan
	 *@Description 章节最大版本状态
	 *@Date 2020/2/18
	 *@Param [chaptersId]
	 *@return java.util.List<cn.pioneeruniverse.project.entity.TblRequirementInfo>
	 **/
	TblSystemDirectoryDocumentChapters getMaxVersionStatusById(@Param("id") Long id,@Param("requirementCode") String requirementCode);
	/**
	 * 根据系统 项目查询章节
	 * @param systemId
	 * @param projectId
	 * @return
	 */
	List<TblSystemDirectoryDocumentChapters> selectWikiTree(@Param("systemId") Long systemId,@Param("projectId") Long projectId);
}