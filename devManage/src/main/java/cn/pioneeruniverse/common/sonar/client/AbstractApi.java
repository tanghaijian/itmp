package cn.pioneeruniverse.common.sonar.client;

import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
/**
 *
 * @ClassName:AbstractApi
 * @Description:抽象
 * @author author
 * @date 2020年8月20日
 *
 */

public abstract class AbstractApi {

    private String url;//url地址
    private HTTPBasicAuthFilter auth;//token

    public AbstractApi(String url, HTTPBasicAuthFilter auth) {
        this.url = url;
        this.auth = auth;
    }

    protected String getUrl() {
        return url;
    }

    protected HTTPBasicAuthFilter getAuth() {
        return auth;
    }
}
