package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblDevTaskScmFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 17:48 2019/3/11
 * @Modified By:
 */
public interface TblDevTaskScmFileMapper {

    int insertOrUpdateDevTaskScmFile(@Param("commitFiles") List<Map<String, String>> commitFiles, @Param("tblDevTaskScmFile") TblDevTaskScmFile tblDevTaskScmFile);

    List<TblDevTaskScmFile> getReviewFilesByDevTaskScmId(Long devTaskScmId);

    List<TblDevTaskScmFile> getSvnFilesByDevTaskId(Long devTaskId);

    List<Map<String, Object>> getSvnFilesByDevTaskIds(Map<String, Object> param);
}