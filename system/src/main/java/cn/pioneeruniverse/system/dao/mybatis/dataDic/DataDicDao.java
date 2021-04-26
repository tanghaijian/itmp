package cn.pioneeruniverse.system.dao.mybatis.dataDic;

import java.util.List;

import cn.pioneeruniverse.common.dto.TblDataDicDTO;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.system.entity.TblDataDic;

public interface DataDicDao extends BaseMapper<TblDataDic> {

    /**
    * @author author
    * @Description 根据编号获取数据字典
    * @Date 2020/9/3
    * @param termCode
    * @return java.util.List<cn.pioneeruniverse.system.entity.TblDataDic>
    **/
    List<TblDataDic> getDataDicByTerm(String termCode);

    /**
    * @author author
    * @Description 分页查询数据字典
    * @Date 2020/9/3
    * @param tblDataDicDTO
    * @return java.util.List<cn.pioneeruniverse.common.dto.TblDataDicDTO>
    **/
    List<TblDataDicDTO> selectDataDicPage(TblDataDicDTO tblDataDicDTO);

    /**
    * @author author
    * @Description 添加一条数据字典
    * @Date 2020/9/3
    * @param tblDataDic
    * @return java.lang.Integer
    **/
    Integer addDataDict(TblDataDic tblDataDic);

    /**
    * @author author
    * @Description 更新数据字典
    * @Date 2020/9/3
    * @param tblDataDic
    * @return java.lang.Integer
    **/
    Integer updateDataDict(TblDataDic tblDataDic);

    /**
    * @author author
    * @Description  更新数据字典：逻辑删除
    * @Date 2020/9/3
    * @param tblDataDic
    * @return java.lang.Integer
    **/
    Integer delDataDict(TblDataDic tblDataDic);

    /**
    * @author author
    * @Description 更新数据一个编码下的所有数据字典
    * @Date 2020/9/3
    * @param tblDataDic
    * @return java.lang.Integer
    **/
    Integer updateStatusByTermCode(TblDataDic tblDataDic);

}
