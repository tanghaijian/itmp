package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblSystemScmRepository;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 17:12 2019/5/29
 * @Modified By:
 */
public interface TblSystemScmRepositoryMapper extends BaseMapper<TblSystemScmRepository> {

    int insertSystemScmRepository(TblSystemScmRepository tblSystemScmRepository);

    List<Map<String, Object>> getSystemScmRepositories(@Param("systemId") Long systemId, @Param("scmType") Integer scmType);

    List<TblSystemScmRepository> findScmRepositoryBySystemId(Long systemId);

    TblSystemScmRepository findScmRepositoryById(Long id);
}
