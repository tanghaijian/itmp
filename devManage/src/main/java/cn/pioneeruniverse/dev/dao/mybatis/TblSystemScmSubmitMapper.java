package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblSystemScmSubmit;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmitLog;
import cn.pioneeruniverse.dev.entity.TblToolInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:02 2019/6/24
 * @Modified By:
 */
public interface TblSystemScmSubmitMapper extends BaseMapper<TblSystemScmSubmit> {

    int insertSystemScmSubmit(TblSystemScmSubmit tblSystemScmSubmit);

    int insertOperationLog(Long id, String now, String subStatus, Long operation, Long systemId);

    int updateSystemScmSubmit(TblSystemScmSubmit tblSystemScmSubmit);

    int updateSubmitUser(Long id, Integer commitFlag, String userIds);

    int deleteSystemScmSubmit(TblSystemScmSubmit tblSystemScmSubmit);

    List<TblSystemScmSubmit> getSystemScmSubmits(@Param("systemId") Long systemId, @Param("scmType") Integer scmType);

    List<TblSystemScmSubmitLog> findOperationLog(Long systemId, Integer start);

    Long findOperationLogCount(Long systemId);

    TblSystemScmSubmit getSystemScmSubmitByScmUrl(String scmUrl);

    TblSystemScmSubmit getGitLabSystemScmSubmit(@Param("tool") TblToolInfo tool, @Param("gitRepositoryId") Long gitRepositoryId, @Param("scmBranch") String scmBranch);

}
