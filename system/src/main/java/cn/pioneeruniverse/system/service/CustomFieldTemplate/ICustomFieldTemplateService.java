package cn.pioneeruniverse.system.service.CustomFieldTemplate;

import cn.pioneeruniverse.system.entity.TblCustomFieldTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ICustomFieldTemplateService {
    /**
     *
     * @Title: selectTableStructureByTableName
     * @Description: 获取测试数据库中对应tablename的属性信息，包括字段，备注
     * @author author
     * @param tableName 表名
     * @return List<TblCustomFieldTemplate>  自定义字段列表
     * @throws
     */
    List<TblCustomFieldTemplate> selectTableStructureByTableName(String tableName);
    /**
     *
     * @Title: selectFieldByCustomForm
     * @Description: 通过表明获取自定义的属性字段
     * @author author
     * @param tableName 表名
     * @return map :自定义字段的map格式（自定义字段在数据库中以json格式存储，此处转化为map）
     * @throws
     */
    Map<String,Object> selectFieldByCustomForm(String tableName);
    /**
     *
     * @Title: findFieldByFieldName
     * @Description: 通过表明和字段名获取字段是否存在，1不存在，2存在
     * @author author
     * @param customForm 表名
     * @param fieldName 字段名
     * @return Integer 1不存在，2存在
     */
    Integer findFieldByFieldName(String customForm,String fieldName);
    /**
     *
     * @Title: saveFieldTemplate
     * @Description: 保存自定义属性字段
     * @author author
     * @param fieldTemplate 自定义属性配置字段信息
     * @param request
     */
    void saveFieldTemplate(TblCustomFieldTemplate fieldTemplate,HttpServletRequest request);
    /**
     *
     * @Title: selectTableStructureByTableNameItmp
     * @Description: 获取开发数据库中对应tablename的属性信息，包括字段，备注
     * @author author
     * @param tableName 表名
     * @return List<TblCustomFieldTemplate>自定义字段信息
     * @throws
     */
    List<TblCustomFieldTemplate> selectTableStructureByTableNameItmp(String tableName);
}
