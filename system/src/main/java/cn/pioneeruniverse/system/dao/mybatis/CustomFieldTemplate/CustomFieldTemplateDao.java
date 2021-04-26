package cn.pioneeruniverse.system.dao.mybatis.CustomFieldTemplate;

import cn.pioneeruniverse.system.entity.TblCustomFieldTemplate;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface CustomFieldTemplateDao extends BaseMapper<TblCustomFieldTemplate> {

    /**
    * @author author
    * @Description 获取测试数据库中的表的基本信息 tableName:表名，tmp_db:对应测试库
    * @Date 2020/9/3
    * @param tableName
    * @return java.util.List<cn.pioneeruniverse.system.entity.TblCustomFieldTemplate>
    **/
    List<TblCustomFieldTemplate> selectTableStructureByTableName(String tableName);

    /**
    * @author author
    * @Description 通过表明获取自定义的属性  tableName:表名
    * @Date 2020/9/3
    * @param tableName
    * @return java.util.List<cn.pioneeruniverse.system.entity.TblCustomFieldTemplate>
    **/
    List<TblCustomFieldTemplate> selectFieldByCustomForm(String tableName);

    /**
    * @author author
    * @Description 添加一条扩展字段属性
    * @Date 2020/9/3
    * @param blCustomFieldTemplate
    * @return java.lang.Integer
    **/
    Integer addCustomFieldTemplate(TblCustomFieldTemplate blCustomFieldTemplate);

    /**
    * @author author
    * @Description 更新扩展字段属性记录
    * @Date 2020/9/3
    * @param blCustomFieldTemplate
    * @return java.lang.Integer
    **/
    Integer updCustomFieldTemplate(TblCustomFieldTemplate blCustomFieldTemplate);

    /**
    * @author author
    * @Description  获取开发数据库中的表的基本信息 tableName：表名，itmp_db：对应开发库
    * @Date 2020/9/3
    * @param tableName
    * @return java.util.List<cn.pioneeruniverse.system.entity.TblCustomFieldTemplate>
    **/
    List<TblCustomFieldTemplate> selectTableStructureByTableNameItmp(String tableName);
}
