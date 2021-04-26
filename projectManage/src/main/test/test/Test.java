package test;

import base.BaseJunit;
import cn.pioneeruniverse.project.dao.mybatis.ProjectGroupMapper;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
import cn.pioneeruniverse.project.service.oamproject.OamProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 11:14 2019/1/11
 * @Modified By:
 */
public class Test extends BaseJunit {

    @Autowired
    ProjectGroupMapper projectGroupMapper;

    @Autowired
    OamProjectService oamProjectService;

    @org.junit.Test
    public void test() {
        //projectGroupMapper.untyingProjectGroup(1L);
        //List<Map<String, Object>> list = projectGroupMapper.findAllProjectGroupForZTree();
        //String a = "";

    }

}
