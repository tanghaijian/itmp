package cn.pioneeruniverse.project.dao.mybatis;

import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.project.entity.TblRiskAttachement;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 *
 * @ClassName:TblRiskAttachementMapper
 * @Description 风险附件mapper
 * @author author
 * @date 2020年8月24日
 *
 */
public interface TblRiskAttachementMapper extends BaseMapper<TblRiskAttachement> {
    /**
     * 删除
     * @param id
     * @return int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入
     * @param record
     * @return int
     */

    int insertSelective(TblRiskAttachement record);

    /**
     * 查询
     * @param id
     * @return TblRiskAttachement
     */

    TblRiskAttachement selectByPrimaryKey(Long id);

    /**
     * 更新
     * @param record
     * @return int
     */

    int updateByPrimaryKeySelective(TblRiskAttachement record);

    int updateByPrimaryKey(TblRiskAttachement record);

    void insertAttachement(TblAttachementInfoDTO attachementInfoDTO);

    void removeFile(@Param("ids") String removeFileIds,@Param("currentUserId")  Long currentUserId);

    /**
     * 查询
     * @param id
     * @return List<TblRiskAttachement>
     */

    List<TblRiskAttachement> getRiskAttachement(@Param("id") Long id);
}