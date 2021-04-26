package cn.pioneeruniverse.common.sonar.client;


import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
/**
 *
 * @ClassName:UsersApi
 * @Description:用户api
 * @author author
 * @date 2020年8月19日
 *
 */
public class UsersApi extends AbstractApi{

    public UsersApi(String url, HTTPBasicAuthFilter auth) {
        super(url, auth);
    }

  

}
