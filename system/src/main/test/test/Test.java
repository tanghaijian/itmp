package test;

import base.BaseJunit;
import cn.pioneeruniverse.system.entity.TblUserInfo;
import cn.pioneeruniverse.system.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 11:36 2019/1/22
 * @Modified By:
 */
public class Test extends BaseJunit {

    @Autowired
    IUserService iUserService;

    @org.junit.Test
    public void test() {
        TblUserInfo userInfo = new TblUserInfo();
        userInfo.setUserAccount("tmpTest");
        userInfo.setUserName("tmpTest");
        userInfo.setUserStatus(1);
        iUserService.insertUser(userInfo,null);
        String a = "";
    }

    @org.junit.Test
    public void menuTest(){

    }

}
