package cn.pioneeruniverse.dev.service.projectNotice.impl;

import cn.pioneeruniverse.dev.dao.mybatis.ProjectNoticeMapper;
import cn.pioneeruniverse.dev.entity.TblNoticeInfo;
import cn.pioneeruniverse.dev.service.projectNotice.IProjectNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectNoticeImpl implements IProjectNoticeService {


    @Autowired
    private ProjectNoticeMapper projectNoticeMapper;

    /**
     *  查询公告表主键id
     * @param tblNoticeInfo
     * @return Long 公告ID
     */
    @Override
    public Long selectNoticeInfoByID(TblNoticeInfo tblNoticeInfo) {
        return projectNoticeMapper.selectNoticeInfoByID(tblNoticeInfo);
    }

    /**
     * 项目公告数据增加
     * @param tblNoticeInfo
     * @return Integer
     */
    @Override
    public Integer insertProjectNotice(TblNoticeInfo tblNoticeInfo) {
        return projectNoticeMapper.insertProjectNotice(tblNoticeInfo);
    }

}
