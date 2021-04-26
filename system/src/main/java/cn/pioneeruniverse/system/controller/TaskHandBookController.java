package cn.pioneeruniverse.system.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.common.utils.BrowserUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.S3Util;

/**
 * 
* @ClassName: TaskHandBookController
* @Description: 操作手册上传下载
* @author author
* @date 2020年8月20日 下午1:43:06
*
 */
@RestController
@RequestMapping("handBook")
public class TaskHandBookController {
	
	private final static Logger logger = LoggerFactory.getLogger(TaskHandBookController.class);
	
	@Autowired
    private S3Util s3Util;

    @Autowired
    private RedisUtils redisUtils;
    
    /**
     * 
    * @Title: uploadPlugin
    * @Description: 上传操作手册
    * @author author
    * @param files：文件
    * @param request
    * @return
    * @throws
     */
    @RequestMapping(value = "uploadHandBook", method = RequestMethod.POST)
    public AjaxModel uploadPlugin(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        try {
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        InputStream inputStream = file.getInputStream();
                        String fileName = file.getOriginalFilename();
                        String[] strArray = fileName.split("\\.");
                        String string = strArray[strArray.length -1];
                        String fileNameOld = "操作手册."+string;
                        if (BrowserUtil.isMSBrowser(request)) {
                            fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\") + 1);
                        }
                        //文件信息存入缓存
                        String keyname = s3Util.putObject(s3Util.getDevTaskBucket(), fileNameOld, inputStream);
                        Map<String, Object> map = new HashMap<>();
                        map.put("fileS3Key", keyname);
                        map.put("fileS3Bucket", s3Util.getDevTaskBucket());
                        map.put("fileNameOld", fileNameOld);
                        redisUtils.set("TASK_HANDBOOK_S3", map);
                    }
                }
            }
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error("上传工作手册异常，异常原因：" + e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }


    /**
     * 
    * @Title: downloadPlugin
    * @Description: 下载操作手册
    * @author author
    * @param response
    * @throws
     */
    @RequestMapping(value = "downloadHandBook", method = RequestMethod.GET)
    public void downloadPlugin(HttpServletResponse response) {
        Map<String, Object> map = (Map<String, Object>) redisUtils.get("TASK_HANDBOOK_S3");
        s3Util.downObject(map.get("fileS3Bucket").toString(), map.get("fileS3Key").toString(), map.get("fileNameOld").toString(), response);
    }


}
