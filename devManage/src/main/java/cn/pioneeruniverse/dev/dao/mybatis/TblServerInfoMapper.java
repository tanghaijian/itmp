package cn.pioneeruniverse.dev.dao.mybatis;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblServerInfo;
/**
 *
 * @ClassName: TblServerInfoMapper
 * @Description: 服务器mapper
 * @author author
 *
 */
public interface TblServerInfoMapper extends BaseMapper<TblServerInfo>{
    /**
     * 删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新增
     * @param record
     * @return
     */
    Integer insert(TblServerInfo record);

    /**
     * 查询
     * @param id
     * @return
     */
    TblServerInfo selectByPrimaryKey(Long id);
    
    List<TblServerInfo> selectByCon(@Param("tblServerInfo") TblServerInfo tblServerInfo,@Param("userId")Long userId,@Param("haveHost") List<Long> haveHost);

    int updateByPrimaryKeySelective(TblServerInfo record);
    List<String> selectByUserId(@Param("currentUserId")Long currentUserId);

	List<TblServerInfo> selectByIds(Map<String, Object> serverMap);

	List<TblServerInfo> selectByManyId(@Param("array")String[] array);

	List<TblServerInfo> selectAllServerInfo(@Param("tblServerInfo")TblServerInfo tblServerInfo, @Param("haveHost") List<Long> haveHost, @Param("currentUserId")Long currentUserId);
}