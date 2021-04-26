package cn.pioneeruniverse.common.gitlab.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


/**
 * 
* @ClassName: UserDTO
* @Description: git仓库用户VO
* @author author
* @date 2020年9月4日 下午5:01:33
*
 */
@JsonNaming(PropertyNamingStrategy.class)
public class UserDTO extends User {
    private static final long serialVersionUID = -179463997228543862L;


    //用户ID
    private Long userId;

    //git访问等级
    private Integer gitLabAccessLevel;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getGitLabAccessLevel() {
        return gitLabAccessLevel;
    }

    public void setGitLabAccessLevel(Integer gitLabAccessLevel) {
        this.gitLabAccessLevel = gitLabAccessLevel;
    }
}
