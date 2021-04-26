package cn.pioneeruniverse.system.service.CustomFieldTemplate.impl;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.system.dao.mybatis.CustomFieldTemplate.CustomFieldTemplateDao;
import cn.pioneeruniverse.system.entity.ExtendedField;
import cn.pioneeruniverse.system.entity.TblCustomFieldTemplate;
import cn.pioneeruniverse.system.service.CustomFieldTemplate.ICustomFieldTemplateService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: CustomFieldTemplateServiceImpl
* @Description: 自定义属性配置的业务逻辑处理service
* @author author
* @date 2020年7月29日 下午3:30:55
*
 */
@Service("ICustomFieldTemplateService")
public class CustomFieldTemplateServiceImpl implements ICustomFieldTemplateService {

    @Autowired
    private CustomFieldTemplateDao fieldTemplateDao;

    /**
     * 
    * @Title: selectTableStructureByTableName
    * @Description: 获取测试数据库中对应tablename的属性信息，包括字段，备注
    * @author author
    * @param tableName 表名
    * @return List<TblCustomFieldTemplate>  自定义字段列表
    * @throws
     */
    @Override
    @DataSource(name="tmpDataSource")//切换测试数据源
    public List<TblCustomFieldTemplate> selectTableStructureByTableName(String tableName) {
        List<TblCustomFieldTemplate> fieldTemp=fieldTemplateDao.selectTableStructureByTableName(tableName);
        return fieldTemp;
    }

   /**
    * 
   * @Title: selectFieldByCustomForm
   * @Description: 通过表明获取自定义的属性字段
   * @author author
   * @param tableName 表名
   * @return map :自定义字段的map格式（自定义字段在数据库中以json格式存储，此处转化为map）
   * @throws
    */
    @Override
    public Map<String,Object> selectFieldByCustomForm(String tableName) {
        Map<String,Object> map=new HashMap<>();
        List<TblCustomFieldTemplate> fieldTemps = fieldTemplateDao.selectFieldByCustomForm(tableName);
        for (TblCustomFieldTemplate fieldTemp:fieldTemps){
            if(fieldTemp.getCustomField()!=null) {
            	//由于自定义属性字段以json格式存储，因此返回结果转换为map，对应的key-value即为存储的json的key-value
                map = JSON.parseObject(fieldTemp.getCustomField());
            }
        }
        return map;
    }
   
    /**
     * 
    * @Title: findFieldByFieldName
    * @Description: 通过表明和字段名获取字段是否存在，1不存在，2存在
    * @author author
    * @param customForm 表名
    * @param fieldName 字段名
    * @return Integer 1不存在，2存在
     */
    @Override
    public Integer findFieldByFieldName(String customForm, String fieldName) {
        List<TblCustomFieldTemplate> fieldTemps = fieldTemplateDao.selectFieldByCustomForm(customForm);
        if(fieldTemps!=null&&fieldTemps.size()>0){
            Map<String,Object> map = JSON.parseObject(fieldTemps.get(0).getCustomField());
            List<ExtendedField> extendedFields = JSONObject.parseArray(map.get("field").toString(),ExtendedField.class);
            //自定义字段以JSON格式存储，因此需要一个个查找是否存在
            for (ExtendedField extendedField:extendedFields){
                if(extendedField.getFieldName().equals(fieldName)){
                    return 2;
                }
            }
        }
        return 1;
    }

   
    /**
     * 
    * @Title: saveFieldTemplate
    * @Description: 保存自定义属性字段
    * @author author
    * @param fieldTemplate 自定义属性配置字段信息
    * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public void saveFieldTemplate(TblCustomFieldTemplate fieldTemplate,HttpServletRequest request) {
    	//先设置公共的属性值
        CommonUtil.setBaseValue(fieldTemplate,request);
        List<TblCustomFieldTemplate> fieldTemps=
                fieldTemplateDao.selectFieldByCustomForm(fieldTemplate.getCustomForm());
        //更新数据
        if(fieldTemps!=null&&fieldTemps.size()>0){
            fieldTemplateDao.updCustomFieldTemplate(fieldTemplate);
        }else{
        	//新增数据
            fieldTemplateDao.addCustomFieldTemplate(fieldTemplate);
        }
    }

    /**
     * 
    * @Title: selectTableStructureByTableNameItmp
    * @Description: 获取开发数据库中对应tablename的属性信息，包括字段，备注
    * @author author
    * @param tableName 表名
    * @return List<TblCustomFieldTemplate>自定义字段信息
    * @throws
     */
    @Override
    public List<TblCustomFieldTemplate> selectTableStructureByTableNameItmp(String tableName) {
        List<TblCustomFieldTemplate> fieldTemp=fieldTemplateDao.selectTableStructureByTableNameItmp(tableName);
        return fieldTemp;
    }
}
