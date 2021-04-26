package cn.pioneeruniverse;

import java.util.*;
import java.util.regex.Pattern;

import cn.pioneeruniverse.system.service.menu.IMenuService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.system.entity.TblDataDic;
import cn.pioneeruniverse.system.entity.TblMenuButtonInfo;
import cn.pioneeruniverse.system.entity.TblRoleInfo;
import cn.pioneeruniverse.system.service.dataDic.IDataDicService;
import cn.pioneeruniverse.system.service.role.IRoleService;

@Component
public class SystemInitDataRunner implements CommandLineRunner {
    Logger log = LoggerFactory.getLogger(SystemInitDataRunner.class);

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IDataDicService iDataDicService;
    @Autowired
    private IMenuService iMenuService;


    @Override
    public void run(String... args) throws Exception {
        log.info("数据初始化开始...");
        //初始化URL鉴权Redis
        iRoleService.authRoleMenu();
        //数据字典初始化
        initDataDic();
        iMenuService.putAllMenuUrlToRedis();
        iRoleService.getAdminRole();
        log.info("数据初始化结束...");
    }

    /**
     * 将数据字典数据放入Redis
     */
    private void initDataDic() {
        TblDataDic dic = new TblDataDic();
        List<TblDataDic> dicList = iDataDicService.selectList(dic);
        String lastTermCode = "";
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (int i = 0; i < dicList.size(); i++) {
            dic = dicList.get(i);
            if (i == 0) {
                lastTermCode = dic.getTermCode();
            }
            if (lastTermCode.equals(dic.getTermCode())) {
                map.put(dic.getValueCode(), dic.getValueName());
            } else {
                redisUtils.set(lastTermCode, JSON.toJSONString(map));
                map.clear();
                map.put(dic.getValueCode(), dic.getValueName());
                lastTermCode = dic.getTermCode();
            }
            if (i == dicList.size() - 1) {
                redisUtils.set(dic.getTermCode(), JSON.toJSONString(map));
            }
        }
    }
}
