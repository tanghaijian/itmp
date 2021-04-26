package cn.pioneeruniverse.common.gitlab.entity;

import java.io.Serializable;

/**
 * 
* @ClassName: BaseEntity
* @Description: gitlab基础实体
* @author author
* @date 2020年9月4日 下午4:43:03
*
 */
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 9118036486802878764L;
  //base url ：gitlab地址
    private String baseUri;
  //个人用户的 token
    private String gitApiToken; 

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getGitApiToken() {
        return gitApiToken;
    }

    public void setGitApiToken(String gitApiToken) {
        this.gitApiToken = gitApiToken;
    }
}
