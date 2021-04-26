package cn.pioneeruniverse.dev.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblToolInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TblToolInfoMapper extends BaseMapper<TblToolInfo> {

    TblToolInfo selectByPrimaryKey(Long id);

    List<TblToolInfo> selectBytoolType(Long type);

    List<TblToolInfo> selectsSvnGitBytoolType();

    List<TblToolInfo> findType(Map<String, Object> key);

    void insertTool(TblToolInfo toolInfo);

    void updateTool(TblToolInfo tblToolInfo);

   /* List<TblToolInfo> findType(String key);*/

    List<Map<String, Object>> getMySvnRepositoryLocationInfo(Long systemId);

    List<Map<String, Object>> getSystemGitLabRepositoryLocationInfo(@Param("systemIds")String systemIds);

    List<Map<String, Object>> getMySvnToolInfo(@Param("userId")Long userId, @Param("toolType")Integer toolType);

    List<Map<String, Object>> getCodeBaseToolInfoByCodeBaseType(Integer codeBaseType);

    /**
     * @param scheme
     * @param host
     * @param port
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Description 通过协议、ip、端口获取svn服务器下的超级用户账号，密码
     * @MethodName getSvnToolSuperAdmin
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/4/25 10:43
     */
    Map<String, String> getSvnToolSuperAdmin(@Param("scheme") String scheme, @Param("host") String host, @Param("port") String port);

    /**
     * @param scheme
     * @param host
     * @param port
     * @return cn.pioneeruniverse.dev.entity.TblToolInfo
     * @Description 通过协议、ip、端口获取gitlab工具信息
     * @MethodName getGitLabToolInfo
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/7/17 11:51
     */
    TblToolInfo getGitLabToolInfo(@Param("scheme") String scheme, @Param("host") String host, @Param("port") String port);

    List<TblToolInfo> findNexusType();

    List<TblToolInfo> selectToolByEnv(Map<String, Object> sonarParam);

    TblToolInfo getToolEntity(Long toolId);
}