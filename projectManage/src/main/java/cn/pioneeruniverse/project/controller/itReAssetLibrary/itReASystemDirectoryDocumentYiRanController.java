package cn.pioneeruniverse.project.controller.itReAssetLibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.utils.Base64Util;
import cn.pioneeruniverse.common.utils.ResponseMessageModel;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.project.service.itReAssetsLibrary.ItReASystemDirectoryDocumentYiRanService;
import cn.pioneeruniverse.project.vo.SystemDirectoryDocumentVO;


/**
 * 文档库操作
 */
@RestController
@RequestMapping("itReSystemDirectoryDocumentOperate")
public class itReASystemDirectoryDocumentYiRanController {


    private static final Logger logger = LoggerFactory.getLogger(itReASystemDirectoryDocumentYiRanController.class);

    @Autowired
    private ItReASystemDirectoryDocumentYiRanService aSystemDirectoryDocumentYiRanService;
    @Autowired
    private S3Util s3Util;
    @Value("${s3.documentBucket}")
    private String documentBucket;

   /**
    * 
   * @Title: systemDirectoryDocumentSignOffById
   * @Description: md文档签出
   * @author author
   * @param id 文档ID
   * @param userAccount 签出人
   * @param request
   * @return Map key systemDirectory  value :签出的文档信息
                     code             value:0签出异常，>0签出正常
                     content          value:存于s3中的文档内容
    */
    @RequestMapping("signOff")
    public Map systemDirectoryDocumentSignOffById(Long id,String userAccount, HttpServletRequest request){
        Map map = aSystemDirectoryDocumentYiRanService.systemDirectoryDocumentSignOffById(id,userAccount, request);
        return map;
    }


    /**
     * 
    * @Title: systemDirectoryDocumentTemporaryStorage
    * @Description: 暂存文档
    * @author author
    * @param request
    * @param userAccount 操作人
    * @param systemDirectory 系统文档信息
    * @return
    * @throws IOException ResponseMessageModel
     */
    @RequestMapping("temporaryStorage")
    private ResponseMessageModel systemDirectoryDocumentTemporaryStorage(HttpServletRequest request,String userAccount, String systemDirectory) throws IOException {
        SystemDirectoryDocumentVO directory = JSONObject.parseObject(systemDirectory, SystemDirectoryDocumentVO.class);
        return aSystemDirectoryDocumentYiRanService.addTemporaryStorageById(request, directory,userAccount);
    }

    /**
     * 
    * @Title: systemDirectoryDocumentSubmit
    * @Description: 保存文档，文档以markdown和html两种形式进行保存
    * @author author
    * @param request
    * @param systemDirectory 文档信息
    * @param contentHtml 文档的html内容
    * @param userAccount 操作人
    * @return ResponseMessageModel
     */
    @RequestMapping("submit")
    public ResponseMessageModel systemDirectoryDocumentSubmit(HttpServletRequest request,String systemDirectory, String contentHtml,String userAccount){
        SystemDirectoryDocumentVO directory = JSONObject.parseObject(systemDirectory, SystemDirectoryDocumentVO.class);
        directory.setContentHtml(contentHtml);
        return aSystemDirectoryDocumentYiRanService.directoryDocumentSubmitById(request, directory,userAccount);
    }

    /**
     * 取消
     * @param id
     * @return
     */
    @RequestMapping("cancel")
    public ResponseMessageModel cancel(Long id){
      return aSystemDirectoryDocumentYiRanService.cancel(id);
    }



//    @PostMapping("mkdUpload")
//    public Boolean upload(@RequestParam("file") MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws IOException{
//        String filePath = ResourceUtils.getURL("classpath:").getPath() + "img/";
//        System.out.println(filePath);
//        //本地图片地址
//        String url = "C:\\Users\\lenovo\\Pictures\\Saved Pictures\\u=2174909441,2495215020&fm=26&gp=0.jpg";
//        //本地图片转换成base64字符串
//        String str = ASystemDirectoryDocumentYiRanController.ImageToBase64ByLocal(url);
//        System.out.println(str);
//        //base64字符串转换成图片
//        //设置图片上传路径
//        String urlUpload = request.getSession().getServletContext().getRealPath("/upload");
//        boolean flag = ASystemDirectoryDocumentYiRanController.Base64ToImage(str, filePath);
//        System.out.println(flag);
//        return  flag;
//        //        System.out.println("imageName:"+file.getOriginalFilename());
////        String url = null;
////        Map mapValues = new HashMap();
////        try{
////            url = this.toFile(file);
////            mapValues.put("url",url);
////            return mapValues;
////        }catch (Exception e){
////            logger.error(e.getMessage());
////            mapValues.put("url",url);
////            mapValues.put("msg",e.getMessage());
////            return mapValues;
////        }
//
//    }




    /**
     * 
     * @deprecated
    * @Title: queryPic
    * @Description: 测试用代码,读取图片文件，并且以servlet流展示到页面上
    * @author author
    * @param filename
    * @param request
    * @param response
    * @throws IOException
    * @throws
     */
    @GetMapping("queryPic/{filename}")
    public void queryPic(@PathVariable("filename")String filename, HttpServletRequest request, HttpServletResponse response) throws IOException  {
        if (filename != null){
            response.setContentType("image/jpeg");
            //根据实际情况读取文件
            File file = new File("e:\\test\\"+filename+".jpg");
            FileInputStream is = new FileInputStream(file);

            if (is != null){
                int i = is.available(); // 得到文件大小
                byte data[] = new byte[i];
                is.read(data); // 读数据
                is.close();
                response.setContentType("image/jpeg");  // 设置返回的文件类型
                OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
                toClient.write(data); // 输出数据
                toClient.close();
            }
        }
    }




    /**
     * MultipartFile 转换为file
     * @param multipartFile
     * @return
     */
    public String  toFile(MultipartFile multipartFile) throws IOException {
            String mongoKey = null;
            FileInputStream fis = null;
            File files = new File("../projectManage/src/main/resources/");
            if(!files.exists()){
                files.mkdirs();
            }
            try {
                String fileNameOld = multipartFile.getOriginalFilename();
                files = new File(files+"\\"+fileNameOld);// 相对路径，如果没有则要建立一个新的output。txt文件
                files.createNewFile();
                fis = new FileInputStream(files);
                mongoKey = s3Util.putImage(documentBucket, multipartFile.getOriginalFilename(), fis);
                return mongoKey;
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }finally {
                if (fis != null) {
                    fis.close();
                }
            }
    }


    /**
     *  base64图片上传到S3
     * @param file
     * @param fileName
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping("mkdUpload")
    public Map upload(String file,String fileName, HttpServletRequest req) throws  Exception{
        //本地图片地址
//        String url = "C:\\Users\\lenovo\\Pictures\\Saved Pictures\\u=2174909441,2495215020&fm=26&gp=0.jpg";
        //本地图片转换成base64字符串
//        String str = ASystemDirectoryDocumentYiRanController.ImageToBase64ByLocal(url);
        //        FileInputStream  fis = null;
//        String base64Name = null;
//        String files = uuid+ ".jpg";
//        String parentPath = "../projectManage/src/main/resources/";
//        try{
//            File fileLoad = new File(parentPath,files);
//              if(!fileLoad.getParentFile().exists()){
//                fileLoad.getParentFile().mkdir();
//            }
////            String fileNameOld = uuid.toString(); //文件名
////            fileLoad = new File(fileLoad+"\\"+fileNameOld);// 相对路径，如果没有则要建立一个新的output。txt文件
//            fileLoad.createNewFile(); // 创建新文件
//            fis = new FileInputStream(file);
//
//            base64Name = s3Util.putImage(documentBucket, files, fis);
//            mapValues.put("base64Name",base64Name);
//            mapValues.put("code","1");
//            return mapValues;
//        }catch (Exception e){
//            e.printStackTrace();
//            mapValues.put("base64Name",base64Name);
//            mapValues.put("msg","0");
//            return mapValues;
//        }finally {
//            if (fis != null) {
//                fis.close();
//            }
//        }
        Map mapValues = new HashMap();
        UUID uuid = UUID.randomUUID();
        String url = null;

        try{
            String writeBase = this.writeBase64(uuid.toString(), file,fileName);
            if (!writeBase.equals("error")){
                url = this.getBase64Value(writeBase, fileName);
            }
            mapValues.put("url",url);
            mapValues.put("code","1");
            return mapValues;
        }catch (Exception e){
            e.printStackTrace();
            mapValues.put("url",url);
            mapValues.put("code","0");
            return mapValues;
        }
    }


    /**
     * Base64 上传到s3
     * @param
     * @return
     * @throws IOException
     */
    private String writeBase64(String uuid, String base64Value,String fileName) throws IOException {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        String mongoKey = null;

        try{
            File file= new File("../projectManage/src/main/resources/");
            if(!file.exists()){
                file.mkdirs();
            }
            String fileNameOld = uuid; //文件名
//            fileNameOld = fileNameOld.substring(fileNameOld.lastIndexOf("\\") + 1);
            file = new File(file+"\\"+fileNameOld);// 相对路径，如果没有则要建立一个新的output。txt文件
            file.createNewFile(); // 创建新文件

            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            buf.append(base64Value);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();

            mongoKey = s3Util.putImage(documentBucket, fileNameOld, fis);
            return mongoKey;
//            logger.info("上传后的mongoKey:"+mongoKey);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return "error";
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }


    /**
     *  获取s3存储的base64值
     * @param base64Code
     * @return
     */
    public String getBase64Value(String base64Code,String fileName) {
        try{
            String stringByS3 = s3Util.getStringByS3(documentBucket, base64Code);
            byte[] byteImg = Base64Util.base64ToByte(stringByS3);//把前端传来的base64转成字节码
            UUID uuid=UUID.randomUUID();
            String files = uuid + fileName;
            String parentPath = "c:/upload/";
            // 生成文件
            File imageFile = new File(parentPath,files);
            if(!imageFile.getParentFile().exists()){
                imageFile.getParentFile().mkdir();
            }
            OutputStream imageStream = new FileOutputStream(imageFile);
            imageStream.write(byteImg);
            imageStream.flush();
            imageStream.close();
            //图片路径
            String netUrl = parentPath+files;
            return netUrl;
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }

    }


}
