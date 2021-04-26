package cn.pioneeruniverse.system.controller;

import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.common.velocity.tag.VelocityDataDict;
import cn.pioneeruniverse.system.entity.TblCustomFieldTemplate;
import cn.pioneeruniverse.system.service.CustomFieldTemplate.ICustomFieldTemplateService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 
* @ClassName: CustomFieldTemplateController
* @Description: (菜单自定义属性配置controller)
* @author author
* @date 2020年7月29日 上午10:07:56
*
 */
@RestController
@RequestMapping("fieldTemplate")
public class CustomFieldTemplateController{

    private final static Logger logger = LoggerFactory.getLogger(CustomFieldTemplateController.class);

    @Autowired
    private ICustomFieldTemplateService iCustomFieldTemplateService;

    /**
     * 
    * @Title: getTableName
    * @Description: (获取哪些表需要自定义属性：左侧列表)
    * @author author
    * @return map key值为status时，如果为1则正常返回，如果为2则异常返回；
    *             Key值为data时，则返回页面所需要的数据。以下方法同理
    * @throws
     */
    @RequestMapping(value = "getTableName", method = RequestMethod.POST)
    public Map<String,Object> getTableName() {
        Map<String,Object> map=new HashMap<>();
        try {
        	//通过数据字典中去获取哪些表需要自定义属性
            Map [] map1= getDictMap("TBL_CUSTOM_FIELD_TEMPLATE_TABLE",
                    "tableKey","tableValue");
            map.put("data", JSONObject.toJSONString(map1));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }

    /**
     * 
    * @Title: findFieldByTableName
    * @Description: (通过表名获取默认属性的字段，即通过schema去查找MySQL中定义的tablename的字段信息)
    * @author author
    * @param tableName：表名
    * @return map key: type-->map1 数据字典TBL_CUSTOM_FIELD_TEMPLATE_TYPE ，fieldTemp-->fieldTemp:数据库默认字段
    * @throws
     */
    @RequestMapping(value = "findFieldByTableName", method = RequestMethod.POST)
    public Map<String,Object> findFieldByTableName(String tableName) {
        Map<String,Object> map=new HashMap<>();
        try {
        	//扩展字段类型，整数、字符串。。。
            Map [] map1= getDictMap("TBL_CUSTOM_FIELD_TEMPLATE_TYPE",
                    "typeKey","typeValue");
            List<TblCustomFieldTemplate> fieldTemp =new ArrayList<>();
            //开发库通过itmp_db这个table_schema去查找
            if(tableName!=null && tableName.endsWith("_itmpdb")){
                 String  tableNameItmp=tableName.replaceAll("_itmpdb","");
                fieldTemp =iCustomFieldTemplateService.selectTableStructureByTableNameItmp(tableNameItmp);
                map=iCustomFieldTemplateService.selectFieldByCustomForm(tableName);
            }else {
            	//测试库通过tmp_db这个table_schema去查找
                fieldTemp =iCustomFieldTemplateService.selectTableStructureByTableName(tableName);
                map=iCustomFieldTemplateService.selectFieldByCustomForm(tableName);
            }


            map.put("type",map1);
            map.put("fieldTemp",fieldTemp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }
    /**
     * 
    * @Title: findFieldByFieldName
    * @Description: (通过字段名称寻找字段：用于页面判断自定义字段是否重复)
    * @author author
    * @param customForm:表名
    * @param fieldName：字段名
    * @return
    * @throws
     */
    @RequestMapping(value = "findFieldByFieldName", method = RequestMethod.POST)
    public Integer findFieldByFieldName(String customForm,String fieldName) {
        Integer status;
        try {
            status=iCustomFieldTemplateService.findFieldByFieldName(customForm,fieldName);
        } catch (Exception e) {
            status=3;
            logger.error(e.getMessage(), e);
        }
        return status;
    }
    /**
     * 
    * @Title: save
    * @Description: (自定义属性配置页面保存)
    * @author author
    * @param fieldTemplate：需要保存的数据
    * @param request
    * @return
    * @throws
     */
    @RequestMapping(value = "saveFieldTemplate", method = RequestMethod.POST)
    public AjaxModel save(TblCustomFieldTemplate fieldTemplate,HttpServletRequest request) {
        try {
            iCustomFieldTemplateService.saveFieldTemplate(fieldTemplate,request);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: getDictMap
    * @Description: (获取数据字典信息，并通过传入的不同key值进行重新组装)
    * @author author
    * @param dictName 数据字典中的termcode
    * @param putKey   重新组装的map的第一个key值
    * @param putValue重新组装的map的第二个key值
    * @return map数组
    * @throws
     */
    private Map [] getDictMap(String dictName,String putKey,String putValue){
        VelocityDataDict dict= new VelocityDataDict();
        Map<String,String> result= dict.getDictMap(dictName);
        Map [] map1 = new Map[result.size()];int i=0;
        for(Map.Entry<String, String> entry:result.entrySet()){
            Map<String, Object> map2 = new HashMap<>();
            map2.put(putValue,entry.getKey());
            map2.put(putKey,entry.getValue());
            map1[i]=map2;
            i++;
        }
        return map1;
    }
}