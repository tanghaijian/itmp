package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblSystemDeployeUserConfig;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 *
 * @ClassName: TblSystemDeployeUserConfigMapper
 * @Description: 部署mapper
 * @author author
 * @date 2020年8月12日 下午16:20
 *
 */
public interface TblSystemDeployeUserConfigMapper  extends BaseMapper<TblSystemDeployeUserConfig> {
    int deleteByPrimaryKey(Long id);

    /**
     * 插入
     * @param record
     * @return
     */

    int insertSelective(TblSystemDeployeUserConfig record);

    TblSystemDeployeUserConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDeployeUserConfig record);

    /**
     * 查询
     * @param record
     * @return
     */
    int updateByPrimaryKey(TblSystemDeployeUserConfig record);
}