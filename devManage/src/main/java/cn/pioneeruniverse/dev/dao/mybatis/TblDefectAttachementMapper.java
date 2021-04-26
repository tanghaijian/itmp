package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.dev.entity.TblDefectAttachement;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface TblDefectAttachementMapper  extends BaseMapper<TblDefectAttachement> {
    /**
    * @author author
    * @Description 删除缺陷附件
    * @Date 2020/9/21
    * @param id
    * @return int
    **/
    int deleteByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 插入缺陷附件
    * @Date 2020/9/21
    * @param defectAttachement
    * @return void
    **/
    void insertDefectAttachement(TblDefectAttachement defectAttachement);

    /**
    * @author author
    * @Description 根据id查询
    * @Date 2020/9/21
    * @param id
    * @return cn.pioneeruniverse.dev.entity.TblDefectAttachement
    **/
    TblDefectAttachement selectByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 选择性更新字段
    * @Date 2020/9/21
    * @param record
    * @return int
    **/
    int updateByPrimaryKeySelective(TblDefectAttachement record);

    /**
    * @author author
    * @Description 根据id更新缺陷
    * @Date 2020/9/21
    * @param record
    * @return int
    **/
    int updateByPrimaryKey(TblDefectAttachement record);

    /**
    * @author author
    * @Description 查询文件信息
    * @Date 2020/9/21
    * @param path
    * @return cn.pioneeruniverse.dev.entity.TblDefectAttachement
    **/
    TblDefectAttachement getDefectAttByUrl(String path);

    /**
    * @author author
    * @Description 根据缺陷主键查询附件
    * @Date 2020/9/21
    * @param defectId
    * @return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectAttachement>
    **/
    List<TblDefectAttachement> findAttListByDefectId(Long defectId);

    /**
    * @author author
    * @Description 逻辑删除附件
    * @Date 2020/9/21
    * @param defectAttachement
    * @return void
    **/
    void removeAttachement(TblDefectAttachement defectAttachement);

    /**
    * @author author
    * @Description 逻辑删除缺陷附件
    * @Date 2020/9/21
    * @param defectId
    * @return void
    **/
    void removeAttachements(Long defectId);

    /**
    * @author author
    * @Description 根据id获取缺陷附件
    * @Date 2020/9/21
    * @param id
    * @return cn.pioneeruniverse.dev.entity.TblDefectAttachement
    **/
    TblDefectAttachement getAttById(Long id);

    /**
    * @author author
    * @Description 插入缺陷附件
    * @Date 2020/9/21
    * @param attachementInfoDTO
    * @return void
    **/
    void insertDefectDTOAttachement(TblAttachementInfoDTO attachementInfoDTO);
}