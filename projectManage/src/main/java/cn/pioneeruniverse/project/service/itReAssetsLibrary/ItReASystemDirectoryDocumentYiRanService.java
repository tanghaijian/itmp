package cn.pioneeruniverse.project.service.itReAssetsLibrary;

import cn.pioneeruniverse.common.utils.ResponseMessageModel;
import cn.pioneeruniverse.project.dto.SystemDirectoryDocumentDTO;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.vo.SystemDirectoryDocumentVO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public interface ItReASystemDirectoryDocumentYiRanService {

    /**
     * 系统目录文档查看
     * @param id
     * @return
     */
    SystemDirectoryDocumentVO selectSystemDirectoryDocumentById(Long id, HttpServletRequest request);

    /**
     * 系统目录文档迁出
     * @param id
     * @param request
     * @return
     */
    Map systemDirectoryDocumentSignOffById(Long id,String userAccount, HttpServletRequest request);

    /**
     * 暂存
     * @param request
     * @param systemDirectoryDocumentVO
     * @return
     * @throws IOException
     */
    ResponseMessageModel addTemporaryStorageById(HttpServletRequest request, SystemDirectoryDocumentVO systemDirectoryDocumentVO,String userAccount) throws IOException;

    /**
     * 提交
     * @param request
     * @param systemDirectoryDocumentVO
     * @return
     * @throws IOException
     */
    ResponseMessageModel directoryDocumentSubmitById(HttpServletRequest request,SystemDirectoryDocumentVO systemDirectoryDocumentVO,String userAccount);

    /**
     * 取消
     * @param id
     * @return
     */
    ResponseMessageModel cancel(Long id);


}
