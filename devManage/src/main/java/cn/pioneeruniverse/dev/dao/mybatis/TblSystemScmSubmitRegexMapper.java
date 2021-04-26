package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblSystemScmSubmitRegex;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 16:18 2019/10/21
 * @Modified By:
 */
public interface TblSystemScmSubmitRegexMapper extends BaseMapper<TblSystemScmSubmitRegex> {

    int insertSystemScmSubmitRegex(TblSystemScmSubmitRegex tblSystemScmSubmitRegex);

    int updateSystemScmSubmitRegex(TblSystemScmSubmitRegex tblSystemScmSubmitRegex);

    int deleteSystemScmSubmitRegex(TblSystemScmSubmitRegex tblSystemScmSubmitRegex);

    List<TblSystemScmSubmitRegex> getSystemScmSubmitRegexes(@Param("systemId") Long systemId);

    List<TblSystemScmSubmitRegex> getSystemScmSubmitRegexesForCodeCommit(TblSystemScmSubmitRegex tblSystemScmSubmitRegex);
}
