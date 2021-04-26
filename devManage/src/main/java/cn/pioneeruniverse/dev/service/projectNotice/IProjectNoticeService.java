package cn.pioneeruniverse.dev.service.projectNotice;

import cn.pioneeruniverse.dev.entity.TblNoticeInfo;

public interface IProjectNoticeService {

    /**
     *  查询公告表主键id
     * @param tblNoticeInfo
     * @return
     */
    Long selectNoticeInfoByID(TblNoticeInfo tblNoticeInfo);

    /**
     * 项目公告数据增加
     * @param tblNoticeInfo
     * @return
     */
    Integer insertProjectNotice(TblNoticeInfo tblNoticeInfo);
}
