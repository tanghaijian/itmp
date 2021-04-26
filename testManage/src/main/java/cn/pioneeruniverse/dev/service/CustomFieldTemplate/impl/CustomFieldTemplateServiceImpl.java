package cn.pioneeruniverse.dev.service.CustomFieldTemplate.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

import cn.pioneeruniverse.dev.dao.mybatis.TblCaseInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.entity.ExtendedField;
import cn.pioneeruniverse.dev.feignInterface.TestManageToSystemInterface;
import cn.pioneeruniverse.dev.service.CustomFieldTemplate.ICustomFieldTemplateService;

/**
 * Description: 拓展字段ServiceImpl
 * Author:
 * Date: 2018/12/10 下午 1:41
 */
@Service("ICustomFieldTemplateService")
public class CustomFieldTemplateServiceImpl implements ICustomFieldTemplateService {

    @Autowired
    private TestManageToSystemInterface testManageToSystemInterface;
    @Autowired
    private TblRequirementFeatureMapper requirementFeatureMapper;
    @Autowired
    private TblDefectInfoMapper defectInfoMapper;
    @Autowired
    private TblCaseInfoMapper tblCaseInfoMapper;


    /**
     * 
    * @Title: findFieldByReqFeature
    * @Description: 获取测试任务的扩展字段
    * @author author
    * @param id 测试任务ID
    * @return
    * @throws
     */
    @Override
    public List<ExtendedField> findFieldByReqFeature(Long id) {
        Map<String,Object> map=testManageToSystemInterface.findFieldByTableName("tbl_requirement_feature");
        String listTxt = JSONArray.toJSONString(map.get("field"));
        List<ExtendedField> extendedFields = JSONArray.parseArray(listTxt, ExtendedField.class);

        //List<ExtendedField> extendedFields = JSONObject.parseArray(map.get("field"),ExtendedField.class);
        if(extendedFields!=null) {
            Iterator<ExtendedField> it = extendedFields.iterator();
            while (it.hasNext()) {
                ExtendedField extendedField = it.next();
                if (extendedField.getStatus().equals("2")) {//移除无效的
                    it.remove();
                } else {
                    String valueName = requirementFeatureMapper.getFeatureFieldTemplateById(id, extendedField.getFieldName());
                    extendedField.setValueName(valueName==null?"":valueName);
                }
            }
        }
        return extendedFields;
    }

    /**
     * 
    * @Title: findFieldByDefect
    * @Description: 获取测试缺陷的扩展字段
    * @author author
    * @param id 缺陷ID
    * @return
    * @throws
     */
    @Override
    public List<ExtendedField> findFieldByDefect(Long id) {
        Map<String,Object> map=testManageToSystemInterface.findFieldByTableName("tbl_defect_info");
        String listTxt = JSONArray.toJSONString(map.get("field"));
        List<ExtendedField> extendedFields = JSONArray.parseArray(listTxt, ExtendedField.class);

        if(extendedFields!=null){
            Iterator<ExtendedField> it = extendedFields.iterator();
            while(it.hasNext()){
                ExtendedField extendedField = it.next();
                if(extendedField.getStatus().equals("2")){//移除无效的
                    it.remove();
                }else{
                    String valueName=defectInfoMapper.getDafectFieldTemplateById(id,extendedField.getFieldName());
                    extendedField.setValueName(valueName==null?"":valueName);
                }
            }
        }
        return extendedFields;
    }

    /**
     * 
    * @Title: findFieldByTestCase
    * @Description: 获取测试案例的扩展字段
    * @author author
    * @param id 测试案例ID
    * @return
    * @throws
     */
    @Override
    public List<ExtendedField> findFieldByTestCase(Long id) {
        Map<String,Object> map=testManageToSystemInterface.findFieldByTableName("tbl_case_info");
        String listTxt = JSONArray.toJSONString(map.get("field"));
        List<ExtendedField> extendedFields = JSONArray.parseArray(listTxt, ExtendedField.class);
        if(extendedFields!=null) {
            Iterator<ExtendedField> it = extendedFields.iterator();
            while (it.hasNext()) {
                ExtendedField extendedField = it.next();
                if (extendedField.getStatus().equals("2")) {//删除无效
                    it.remove();
                } else {
                    String valueName = tblCaseInfoMapper.getCaseFieldTemplateById(id,extendedField.getFieldName());
                    extendedField.setValueName(valueName==null?"":valueName);
                }
            }
        }
        return extendedFields;
    }
}
