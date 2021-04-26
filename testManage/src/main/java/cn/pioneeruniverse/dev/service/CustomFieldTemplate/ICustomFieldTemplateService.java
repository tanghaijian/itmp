package cn.pioneeruniverse.dev.service.CustomFieldTemplate;

import cn.pioneeruniverse.dev.entity.ExtendedField;
import java.util.List;

public interface ICustomFieldTemplateService {
    /**
     *
     * @Title: findFieldByReqFeature
     * @Description: 获取测试任务的扩展字段
     * @author author
     * @param id 测试任务ID
     * @return
     * @throws
     */
    List<ExtendedField> findFieldByReqFeature(Long id);
    /**
     *
     * @Title: findFieldByDefect
     * @Description: 获取测试缺陷的扩展字段
     * @author author
     * @param id 缺陷ID
     * @return
     * @throws
     */
    List<ExtendedField> findFieldByDefect(Long id);
    /**
     *
     * @Title: findFieldByTestCase
     * @Description: 获取测试案例的扩展字段
     * @author author
     * @param id 测试案例ID
     * @return
     * @throws
     */
    List<ExtendedField> findFieldByTestCase(Long id);

}
