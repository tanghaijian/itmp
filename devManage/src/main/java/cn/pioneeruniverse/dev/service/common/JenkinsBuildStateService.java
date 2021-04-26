package cn.pioneeruniverse.dev.service.common;

import cn.pioneeruniverse.dev.vo.common.request.JenkinsJobBuildStateQuery;

public interface JenkinsBuildStateService {

    /**
     * 查询系统在当前环境是否正在构建部署
     * @param query
     * @return
     */
    boolean isBuildding(JenkinsJobBuildStateQuery query);

}
