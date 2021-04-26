package cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentHistory;
import org.apache.ibatis.annotations.Param;
public interface ReSystemDirectoryDocumentHistoryDao extends BaseMapper<TblSystemDirectoryDocumentHistory> {

	/**
	* @author author
	* @Description 获取某个文档的历史信息
	* @Date 2020/9/16
	* @param documentId
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentHistory>
	**/
	List<TblSystemDirectoryDocumentHistory> getSystemDirectoryHistoryByDocumentId(@Param("documentId") Long documentId);
	
	/**
	* @author author
	* @Description  获取某个文档的所有历史信息，包括无效的
	* @Date 2020/9/16
	* @param documentId
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentHistory>
	**/
	List<TblSystemDirectoryDocumentHistory> getReHistoryByDocumentId(@Param("documentId") Long documentId);
	
	/**
	* @author author
	* @Description 获取某个文档关联的开发任务信息
	* @Date 2020/9/16
	* @param documentId
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentHistory>
	**/
	List<TblSystemDirectoryDocumentHistory> getFeHistoryByDocumentId(@Param("documentId") Long documentId);

	/**
	* @author author
	* @Description 保存文档历史
	* @Date 2020/9/16
	* @param tblSystemDirectoryDocument
	* @return int
	**/
    int insertDirectoryDocumentHistory(TblSystemDirectoryDocument tblSystemDirectoryDocument);

}