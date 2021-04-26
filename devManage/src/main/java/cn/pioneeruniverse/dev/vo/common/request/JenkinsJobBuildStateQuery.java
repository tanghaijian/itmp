package cn.pioneeruniverse.dev.vo.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JenkinsJobBuildStateQuery implements Serializable {

    // 构建
    public static final int JOB_TYPE_BUILD = 1;
    // 部署
    public static final int JOB_TYPE_DEPLOY = 2;

    /**
     * tbl_system_jenkins id
     */
    private Integer systemId;
    /**
     * tbl_system_scm id
     */
    private Long systemScmId;
    /**
     * 任务类型 （1：构建，2：部署）
     */
    private Integer jobType;
    /**
     * 是否定时任务
     */
    private Boolean timedTaskFlag;

}
