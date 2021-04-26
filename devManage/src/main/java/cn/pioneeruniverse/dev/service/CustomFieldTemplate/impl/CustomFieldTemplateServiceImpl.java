package cn.pioneeruniverse.dev.service.CustomFieldTemplate.impl;

import cn.pioneeruniverse.dev.dao.mybatis.TblDefectInfoMapper;
import cn.pioneeruniverse.dev.entity.ExtendedField;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import cn.pioneeruniverse.dev.service.CustomFieldTemplate.ICustomFieldTemplateService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service("ICustomFieldTemplateService")
public class CustomFieldTemplateServiceImpl implements ICustomFieldTemplateService {

    @Autowired
    private TblDefectInfoMapper defectInfoMapper;

    @Autowired
    private DevManageToSystemInterface devManageToSystemInterface;
    
    /**
     * 
    * @Title: findFieldByDefect
    * @Description: 获取缺陷的自定义属性字段
    * @author author
    * @param id 缺陷ID
    * @return List<ExtendedField>
     */
    @Override
    public List<ExtendedField> findFieldByDefect(Long id) {
        Map<String,Object> map=devManageToSystemInterface.findFieldByTableName("tbl_defect_info");
        String listTxt = JSONArray.toJSONString(map.get("field"));
        List<ExtendedField> extendedFields = JSONArray.parseArray(listTxt, ExtendedField.class);

        if(extendedFields!=null){
            Iterator<ExtendedField> it = extendedFields.iterator();
            while(it.hasNext()){
                ExtendedField extendedField = it.next();
                //无效的字段不添加
                if(extendedField.getStatus().equals("2")){
                    it.remove();
                }else{
                    String valueName=defectInfoMapper.getDafectFieldTemplateById(id,extendedField.getFieldName());
                    extendedField.setValueName(valueName==null?"":valueName);
                }
            }
        }
        return extendedFields;
    }
}
