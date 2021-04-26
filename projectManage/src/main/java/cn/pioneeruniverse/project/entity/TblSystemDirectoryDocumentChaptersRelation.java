package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblSystemDirectoryDocumentChaptersRelation
* @Description: 文档-章节关联bean
* @author author
* @date 2020年8月31日 上午11:25:27
*
 */
public class TblSystemDirectoryDocumentChaptersRelation extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3934644801040341067L;

	private Long systemDirectoryDocumentChapterId1; //被关联的章节
	
	private Long systemDirectoryDocumentId1;//被关联的文档
	
	private Long systemDirectoryDocumentId2;//关联的文档
	
	private Long systemDirectoryDocumentChapterId2;//关联的章节

	public Long getSystemDirectoryDocumentChapterId1() {
		return systemDirectoryDocumentChapterId1;
	}

	public void setSystemDirectoryDocumentChapterId1(Long systemDirectoryDocumentChapterId1) {
		this.systemDirectoryDocumentChapterId1 = systemDirectoryDocumentChapterId1;
	}

	public Long getSystemDirectoryDocumentId1() {
		return systemDirectoryDocumentId1;
	}

	public void setSystemDirectoryDocumentId1(Long systemDirectoryDocumentId1) {
		this.systemDirectoryDocumentId1 = systemDirectoryDocumentId1;
	}

	public Long getSystemDirectoryDocumentId2() {
		return systemDirectoryDocumentId2;
	}

	public void setSystemDirectoryDocumentId2(Long systemDirectoryDocumentId2) {
		this.systemDirectoryDocumentId2 = systemDirectoryDocumentId2;
	}

	public Long getSystemDirectoryDocumentChapterId2() {
		return systemDirectoryDocumentChapterId2;
	}

	public void setSystemDirectoryDocumentChapterId2(Long systemDirectoryDocumentChapterId2) {
		this.systemDirectoryDocumentChapterId2 = systemDirectoryDocumentChapterId2;
	}

	
}
