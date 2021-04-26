package cn.pioneeruniverse.project.service.requirement;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 *
 * @ClassName:RequirementDocumentService
 * @Description:需求文档service
 * @author author
 * @date 2020年8月19日
 *
 */

public interface RequirementDocumentService {

    Map<String,Object> synItcdAttr(Long []ids, HttpServletRequest request);

    /**
     * 更新
     * @param date
     * @return int
     */

    int updataItcdAttr (Map<String,Object> date);

    /**
     * 导入excel
     * @param file
     * @param request
     * @return Map<String, Object>
     */

    Map<String, Object> importExcel(MultipartFile file, HttpServletRequest request);

    Map<String, Object> importCsv(MultipartFile file, HttpServletRequest request);
}
