package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 17:18 2019/11/16
 * @Modified By:
 */
public interface SystemDirectoryDocumentAttachmentDao extends BaseMapper<TblSystemDirectoryDocumentAttachment> {

    List<TblSystemDirectoryDocumentAttachment> getNumOfAttachmentsByDocumentId(@Param("documentId") Long documentId, @Param("number") Integer number);


}
