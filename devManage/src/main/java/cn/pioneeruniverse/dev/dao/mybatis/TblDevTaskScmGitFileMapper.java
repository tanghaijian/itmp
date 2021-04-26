package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblDevTaskScmGitFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 开发任务文件(SVN/git)提交
 * @Date: Created in 15:59 2019/9/2
 * @Modified By:
 */
public interface TblDevTaskScmGitFileMapper {

    /**
    *@author author
    *@Description   添加 工作任务代码 附件
    *@Date 2020/8/24
     * @param commitFiles
     * @param tblDevTaskScmGitFile
    *@return int
    **/
    int insertOrUpdateDevTaskScmGitFile(@Param("commitFiles") List<String> commitFiles, @Param("tblDevTaskScmGitFile") TblDevTaskScmGitFile tblDevTaskScmGitFile);

    /**
    *@author author
    *@Description 根据 工作任务代码id 获取文件
    *@Date 2020/8/24
     * @param devTaskScmId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScmGitFile>
    **/
    List<TblDevTaskScmGitFile> getReviewGitFilesByDevTaskScmId(Long devTaskScmId);

    /**
    *@author author
    *@Description  根据 工作任务id 获取文件
    *@Date 2020/8/24
     * @param devTaskId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScmGitFile>
    **/
    List<TblDevTaskScmGitFile> getGitFilesByDevTaskId(Long devTaskId);

    /**
    *@author author
    *@Description 根据 多个工作任务id 获取文件 
    *@Date 2020/8/24
     * @param param
    *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> getGitFilesByDevTaskIds(Map<String, Object> param);
}
