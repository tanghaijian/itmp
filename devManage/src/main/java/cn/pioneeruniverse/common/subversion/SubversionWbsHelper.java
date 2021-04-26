package cn.pioneeruniverse.common.subversion;

import java.util.List;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import cn.pioneeruniverse.common.utils.JsonUtil;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 10:40 2018/12/5
 * @Modified By:
 */
@Component
@Lazy
public class SubversionWbsHelper {

    /**
     * @param
     * @return java.util.List<java.lang.String>
     * @Description 获取svn上全部人员
     * @MethodName getSvnAllUser
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/5 13:49
     */
    public List<String> getSvnAllUser(Client client) throws Exception {
        Object[] objects = client.invoke("getAllUser");
        String result = objects[0].toString();
        return JsonUtil.fromJson(result, List.class);
    }

    /**
     * @param groupNames
     * @return java.util.Map<java.lang.String,java.util.List<java.lang.String>>
     * @Description 获取svn上组下的人员
     * @MethodName getSvnAllUser
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/5 13:59
     */
    public Map<String, List<String>> getSvnAllUser(Client client, List<String> groupNames) throws Exception {
        Object[] objects = client.invoke("getAllUserInGroup", groupNames);
        String result = objects[0].toString();
        return JsonUtil.fromJson(result, Map.class);

    }

    /**
     * @param reposName
     * @param path
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @Description 获取svn某文件路径下的人员及其权限
     * @MethodName getSvnAllUserAuth
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/13 17:43
     */
    public Map<String, String> getSvnAllUserAuth(Client client, String reposName, String path) throws Exception {
        Object[] objects = client.invoke("getUserAuthInRepoPath", reposName, path);
        String result = objects[0].toString();
        return JsonUtil.fromJson(result, Map.class);
    }

    /**
     * @param userMap
     * @return void
     * @Description 添加svn账户
     * @MethodName addSvnUser
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/5 15:26
     */
    public void addSvnUser(Client client, Map<String, String> userMap) throws Exception {
        String userMapJson = JsonUtil.toJson(userMap);
        client.invoke("addUser", userMapJson);
    }

    /**
     * @param userNames
     * @return void
     * @Description 删除svn上的账户
     * @MethodName delSvnUser
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/5 15:28
     */
    public void delSvnUser(Client client, List<String> userNames) throws Exception {
        client.invoke("delUser", userNames);
    }

    /**
     * @param userMap
     * @return void
     * @Description 修改svn账户密码
     * @MethodName modifySvnUserPassword
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/5 15:47
     */
    public void modifySvnUserPassword(Client client, Integer accessProtocol, Map<String, String> userMap) throws Exception {
        String userMapJson = JsonUtil.toJson(userMap);
        client.invoke("modifyUserPassword", accessProtocol, userMapJson);
    }


    /**
     * @param groupNames
     * @return void
     * @Description 添加svn组（未带用户）
     * @MethodName addSvnGroup
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/5 16:11
     */
    public void addSvnGroup(Client client, List<String> groupNames) throws Exception {
        client.invoke("addGroup", groupNames);
    }

    /**
     * @param groupMap
     * @return void
     * @Description 添加svn组（带用户）
     * @MethodName addSvnGroup
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/5 16:20
     */
    public void addSvnGroup(Client client, Map<String, String> groupMap) throws Exception {
        String groupMapJson = JsonUtil.toJson(groupMap);
        client.invoke("addGroupWithUser", groupMapJson);
    }

    /**
     * @param client
     * @param reposName
     * @param path
     * @param modifyOperates
     * @return void
     * @Description 修改svn配置文件
     * @MethodName modifySvnConfigFile
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/20 9:46
     */
    public String modifySvnConfigFile(Client client, Integer accessProtocol, String reposName, String path, String modifyOperates) throws Exception {
        return client.invoke("modifyUserAuthorityByRepoPath", accessProtocol, reposName, path, modifyOperates)[0].toString();
    }

    /**
     * @param reposName
     * @return void
     * @Description 创建svn仓库
     * @MethodName createSvnRepository
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/5 15:37
     */
    public void createSvnRepository(Client client, String reposName) throws Exception {
        client.invoke("createRepository", reposName);
    }

    /**
     * @param client
     * @param reposName
     * @return void
     * @Description 修改svn仓库下的svnserve.confg文件
     * @MethodName modifySvnserveConfgFileForRepository
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/21 13:40
     */
    public void modifySvnserveConfgFileForRepository(Client client, String reposName) throws Exception {
        client.invoke("modifySvnServeConfgForRepoistory", reposName);
    }

    /**
     * @param ip
     * @return org.apache.cxf.endpoint.Client
     * @Description 创建wbs客户端
     * @MethodName createClient
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/14 14:02
     */
    public Client createClient(String ip) {
        Client client = JaxWsDynamicClientFactory.newInstance().createClient("http://" + ip + ":8090/svnInterfaceHelper/soap/svnHelper?wsdl");
        HTTPConduit conduit = (HTTPConduit) client.getConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(60000); // 连接超时时间ms
        policy.setReceiveTimeout(60000);// 请求超时时间.(读取超时)ms
        conduit.setClient(policy);
        return client;
    }

}
