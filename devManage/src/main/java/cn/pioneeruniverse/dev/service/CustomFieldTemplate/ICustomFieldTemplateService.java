package cn.pioneeruniverse.dev.service.CustomFieldTemplate;

import cn.pioneeruniverse.dev.entity.ExtendedField;

import java.util.List;

public interface ICustomFieldTemplateService {
    /**
     *
     * @Title: findFieldByDefect
     * @Description: 获取缺陷的自定义树形字段
     * @author author
     * @param id 缺陷ID
     * @return
     * @throws
     */
    List<ExtendedField> findFieldByDefect(Long id);
}
