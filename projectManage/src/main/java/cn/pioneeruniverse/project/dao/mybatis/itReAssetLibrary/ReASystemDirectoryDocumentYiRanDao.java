package cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary;


import cn.pioneeruniverse.common.utils.ResponseMessageModel;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.vo.SystemDirectoryDocumentVO;
import org.apache.ibatis.annotations.Param;


public interface ReASystemDirectoryDocumentYiRanDao {

    /**
     * 系统目录文档查看
     * @param id
     * @return
     */
    SystemDirectoryDocumentVO selectSystemDirectoryDocumentById(Long id);

    /**
     *  id查询用户名
     * @param id
     * @return
     */
    String getUserNameById(Long id);

    /**
     * 系统目录文档签出
     * @param id
     * @return
     */
    SystemDirectoryDocumentVO systemDirectoryDocumentSignOffById(Long id);

    /**
     * 签出状态改变
     * @param id
     * @param userId
     * @return
     */
    Integer updateDirectoryDocumentSignOffStatusById(@Param("id") Long id,@Param("userId") Long userId);

    /**
     *  暂存
     * @param systemDirectoryDocumentVO
     * @return
     */
    Integer addTemporaryStorageById(SystemDirectoryDocumentVO systemDirectoryDocumentVO);

    /**
     *  提交
     * @param systemDirectoryDocumentVO
     * @return
     */
    Integer directoryDocumentSubmitById(SystemDirectoryDocumentVO systemDirectoryDocumentVO);

    /**
     * 获取系统目录文档章节信息
     * @param id
     * @return
     */
    TblSystemDirectoryDocumentChapters getDirectoryDocumentChaptersById(Long id);

    /**
     * 历史数据增加
     * @return
     */
    Integer insertDirectoryDocumentHistory(TblSystemDirectoryDocumentChapters tblSystemDirectoryDocumentChapters);

    /**
     * 取消
     * @param id
     * @return
     */
    Integer cancelById(Long id);

    /**
     * 获取文档版本
     * @param systemDirectoryDocumentId
     * @return
     */
    TblSystemDirectoryDocument selectDocumentVersionById(Long systemDirectoryDocumentId);

    /**
     *  提交文档文档版本+1
     * @param systemDirectoryDocumentId
     * @return
     */
    Integer updateDocumentVersionById(Long systemDirectoryDocumentId);

    /**
     * 系统目录文档历史增加
     * @param tblSystemDirectoryDocument
     * @return
     */
    Integer insertSystemDirectoryDocumentHistory(TblSystemDirectoryDocument tblSystemDirectoryDocument);
    /**
     * 根据用户账号查询ID
     * @param userAccount
     * @return
     */
    Long selectUserId(@Param("userAccount") String userAccount);
}
