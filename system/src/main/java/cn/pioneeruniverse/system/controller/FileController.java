package cn.pioneeruniverse.system.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.common.utils.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 文件公共操作
 * Author:liushan
 * Date: 2020/1/8 下午 5:39
 */
@RestController
@RequestMapping("file")
public class FileController extends BaseController {

    @Autowired
    private S3Util s3Util;
    
    /**
    *@author liushan
    *@Description s3下载文件
    *@Date 2020/1/8
    *@Param [attachementInfoDTO, response]
    *@return map Key：status=1 正常返回 ，2异常返回 
    **/
    @RequestMapping(value= "downloadFile")
    public Map<String,Object> downloadFile(TblAttachementInfoDTO attachementInfoDTO, HttpServletResponse response) {
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try{
            s3Util.downObject(attachementInfoDTO.getFileS3Bucket(), attachementInfoDTO.getFileS3Key(), attachementInfoDTO.getFileNameOld(), response);
        }catch(Exception e){
            return this.handleException(e, "下载文件失败！");
        }
        return result;
    }
    
    /**
    *@author liushan
    *@Description 查看MarkDown页面文件
    *@Date 2020/1/9
    *@Param [bucketName, fileS3Key]
    *@return map Key：status=1 正常返回 ，2异常返回 
    *                 data返回的数据
    **/
    @RequestMapping(value= "checkMarkDownFile")
    public Map<String,Object> checkMarkDownFile(String bucketName,String fileS3Key) {
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try{
            result.put("data", s3Util.getStringByS3(bucketName,fileS3Key));
        }catch(Exception e){
            return this.handleException(e, "操作失败！");
        }
        return result;
    }
}
