package cn.pioneeruniverse.dev.service.JiraWorkTask;

import cn.pioneeruniverse.common.entity.JiraDevelopmentVO;

import javax.servlet.http.HttpServletRequest;

public interface IJiraWorkTaskService {

    /**
     * jira数据增加
     * @param jiraDevelopmentVO
     * @return
     */
    void synJiraWorkTask(JiraDevelopmentVO jiraDevelopmentVO, HttpServletRequest request);

}
