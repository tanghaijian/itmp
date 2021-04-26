package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblNoticeInfo;

/**
 * 项目公告
 */
public interface ProjectNoticeMapper {

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
