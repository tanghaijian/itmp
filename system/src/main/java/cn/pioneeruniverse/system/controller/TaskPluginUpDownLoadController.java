package cn.pioneeruniverse.system.controller;

import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.common.utils.BrowserUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.S3Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 
* @ClassName: TaskPluginUpDownLoadController
* @Description: eclipse插件上传下载
* @author author
* @date 2020年8月20日 下午1:47:34
*
 */
@RestController
@RequestMapping("taskPlugin")
public class TaskPluginUpDownLoadController {

    private final static Logger logger = LoggerFactory.getLogger(TaskPluginUpDownLoadController.class);

    @Autowired
    private S3Util s3Util;

    @Autowired
    private RedisUtils redisUtils;

    //s3桶
    @Value("${s3.devTaskBucket}")
    private String devTaskBucket;

    private static final String TASK_PLUGIN_S3_REDIS_KEY = "TASK_PLUGIN_S3";

    /**
     * 
    * @Title: uploadPlugin
    * @Description: 上传插件
    * @author author
    * @param files 插件文件
    * @param request
    * @return
    * @throws
     */
    @RequestMapping(value = "uploadPlugin", method = RequestMethod.POST)
    public AjaxModel uploadPlugin(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        try {
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        InputStream inputStream = file.getInputStream();
                        String fileNameOld = file.getOriginalFilename();
                        if (BrowserUtil.isMSBrowser(request)) {
                            fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\") + 1);
                        }
                        //存入S3中，并将返回的相关信息存入缓存
                        String keyname = s3Util.putObject(devTaskBucket, fileNameOld, inputStream);
                        Map<String, Object> map = new HashMap<>();
                        map.put("fileS3Key", keyname);
                        map.put("fileS3Bucket", devTaskBucket);
                        map.put("fileNameOld", fileNameOld);
                        redisUtils.set(TASK_PLUGIN_S3_REDIS_KEY, map);
                    }
                }
            }
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error("上传eclipse插件异常，异常原因：" + e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }


    /**
     * 
    * @Title: downloadPlugin
    * @Description: 下载插件
    * @author author
    * @param response
    * @throws
     */
    @RequestMapping(value = "downloadPlugin", method = RequestMethod.GET)
    public void downloadPlugin(HttpServletResponse response) {
        Map<String, Object> map = (Map<String, Object>) redisUtils.get(TASK_PLUGIN_S3_REDIS_KEY);
        s3Util.downObject(map.get("fileS3Bucket").toString(), map.get("fileS3Key").toString(), map.get("fileNameOld").toString(), response);
    }


}
