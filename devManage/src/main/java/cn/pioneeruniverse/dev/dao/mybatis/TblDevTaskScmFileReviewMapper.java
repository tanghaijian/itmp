package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblDevTaskScmFileReview;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 18:27 2019/3/19
 * @Modified By:
 */
public interface TblDevTaskScmFileReviewMapper {

    int getScmFileCommentsCountByFileId(@Param("devTaskScmFileId") Long devTaskScmFileId, @Param("scmFileType") Integer scmFileType);

    List<TblDevTaskScmFileReview> getFileComments(@Param("devTaskScmFileId") Long devTaskScmFileId, @Param("scmFileType") Integer scmFileType);

    int insertOneDevTaskScmFileReview(TblDevTaskScmFileReview tblDevTaskScmFileReview);
}
