package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentHistory;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 16:18 2019/11/18
 * @Modified By:
 */
public interface SystemDirectoryDocumentHistoryDao extends BaseMapper<TblSystemDirectoryDocumentHistory> {

    List<TblSystemDirectoryDocumentHistory> getSystemDirectoryHistoryByDocumentId(@Param("documentId") Long documentId);

    int insertDirectoryDocumentHistory(TblSystemDirectoryDocument tblSystemDirectoryDocument);

}