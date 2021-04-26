package cn.pioneeruniverse.project.controller.assetLibrary;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.utils.ResponseMessageModel;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.project.service.assetsLibrary.ASystemDirectoryDocumentYiRanService;
import cn.pioneeruniverse.project.vo.SystemDirectoryDocumentVO;


/**
 * 
* @ClassName: ASystemDirectoryDocumentYiRanController
* @Description: it全流程测试Controller，生产中该菜单内容嵌入到IT全流程的页面中。
* @author author
* @date 2020年8月5日 上午10:17:58
*
 */
@RestController
@RequestMapping("systemDirectoryDocumentOperate")
public class ASystemDirectoryDocumentYiRanController {


    private static final Logger logger = LoggerFactory.getLogger(ASystemDirectoryDocumentYiRanController.class);

    @Autowired
    private ASystemDirectoryDocumentYiRanService aSystemDirectoryDocumentYiRanService;
    @Autowired
    private S3Util s3Util;
    @Value("${s3.documentBucket}")
    private String documentBucket;

    /**
     * 系统目录文档签出
     * @param id
     * @param request
     * @return  map key systemDirectory:系统目录数据
                        code 0失败，其他成功
                        content：文档内容
     */
    @RequestMapping("signOff")
    public Map systemDirectoryDocumentSignOffById(Long id, HttpServletRequest request){
        Map map = aSystemDirectoryDocumentYiRanService.systemDirectoryDocumentSignOffById(id, request);
        return map;
    }


    /**
     * 系统目录文档暂存，页面【暂存】
     * @param request
     * @param systemDirectory
     * @return
     * @throws IOException
     */
    @RequestMapping("temporaryStorage")
    private ResponseMessageModel systemDirectoryDocumentTemporaryStorage(HttpServletRequest request, String systemDirectory) throws IOException {
        SystemDirectoryDocumentVO directory = JSONObject.parseObject(systemDirectory, SystemDirectoryDocumentVO.class);
        return aSystemDirectoryDocumentYiRanService.addTemporaryStorageById(request, directory);
    }

    /**
     * 提交修改的文档 【提交】，保存分两部分，一部分是markdown语法，另外一部分是转换后的Html
     * @param request
     * @param systemDirectory
     * @return
     * @throws IOException
     */
    @RequestMapping("submit")
    public ResponseMessageModel systemDirectoryDocumentSubmit(HttpServletRequest request,String systemDirectory, String contentHtml){
        SystemDirectoryDocumentVO directory = JSONObject.parseObject(systemDirectory, SystemDirectoryDocumentVO.class);
        directory.setContentHtml(contentHtml);
        return aSystemDirectoryDocumentYiRanService.directoryDocumentSubmitById(request, directory);
    }

    /**
     * 【取消签出】
     * @param id
     * @return
     */
    @RequestMapping("cancel")
    public ResponseMessageModel cancel(Long id){
      return aSystemDirectoryDocumentYiRanService.cancel(id);
    }

    /**
     * 图片上传
     * @param base64Code：图片的base64码，以生成临时文件的方式上传到S3
     * @param fileType
     * @param req
     * @return map key s3key:s3中的key值
     *             code :0失败，1成功
     * @throws Exception
     */
    @PostMapping("mkdUpload")
    public Map upload(@RequestParam("base64Code") String  base64Code,@RequestParam("fileType") String fileType, HttpServletRequest req) throws  Exception {
        Map<String, Object> mapValues = new HashMap<>();
        File tempFile = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {

            byte[] fileBytes = Base64Utils.decodeFromString(base64Code);
            tempFile = new File(UUID.randomUUID().toString());

            fos = new FileOutputStream(tempFile);
            bos = new BufferedOutputStream(fos);
            bos.write(fileBytes);

            fis = new FileInputStream(tempFile);

            //保存进S3
            String mongoKey = s3Util.putFileUpload(documentBucket,
                    String.valueOf(System.currentTimeMillis()),
                    fis, fileType);
            mapValues.put("s3Key", mongoKey.equals("error") ? null : mongoKey);
            mapValues.put("code", mongoKey.equals("error") ? 0 : 1);
            return mapValues;
        } catch (Exception e) {
            e.printStackTrace();
            mapValues.put("s3Key", null);
            mapValues.put("code", 0);
            return mapValues;
        } finally {
            if (fis != null)
                fis.close();
            if (bos != null)
                bos.close();
            if (fos != null)
                fos.close();
            if (tempFile != null && tempFile.exists())
                tempFile.delete();
        }

    }

    /**
     * 从S3中获取保存的图片
     * @param keyName
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("queryPic/{keyName}")
    public void queryPic(@PathVariable("keyName")String keyName,HttpServletRequest request, HttpServletResponse response) throws IOException  {
        if (keyName != null){
            s3Util.downImgByStream( documentBucket,  keyName, response);
        }
    }

}
