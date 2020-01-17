package com.scsoft.wlyz.file.controller;

import com.scsoft.scpt.common.PageResult;
import com.scsoft.wlyz.common.exception.BusinessException;
import com.scsoft.wlyz.common.utils.FileUtils;
import com.scsoft.wlyz.file.entity.FileInfo;
import com.scsoft.wlyz.file.service.IFileService;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 附件管理 前端控制器
 * </p>
 * @author zhaopengfei
 * @CreateDate 2019-05-29
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */
@Controller
@RequestMapping("/system/file")
public class FileController extends BaseController {
    private String PREFIX = "module/system/file/";

    @Autowired
    private IFileService fileService;
    /**
     * 文件上传
     * 1. 文件上传后的文件名
     * 2. 上传后的文件路径 , 当前年月日时 如:2018060801  2018年6月8日 01时
     * 3. file po 类需要保存文件信息 ,旧名 ,大小 , 上传时间 , 是否删除 ,
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("uploadFile")
    @ResponseBody
    public JsonResult uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // 判断文件是否为空
        if (file.isEmpty()) {
            return JsonResult.error("文件不能为空");
        }
        try {
             String id=fileService.upload(file);
            return JsonResult.ok(200,id);
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("upload/multi/file")
    @ResponseBody
    public PageResult uploadMultiFile(@RequestParam("file") List<MultipartFile> fileList, HttpServletRequest request) {
        PageResult result = new PageResult();
        if(null == fileList){
            result.setCode(500);
            result.setMsg("未选择文件");
        }
        List<FileInfo> rtList = new ArrayList<>();
        for(MultipartFile file : fileList){
            try {
                String id = fileService.upload(file);
                FileInfo info = fileService.getImage(id);
                rtList.add(info);
            } catch (Exception e) {
                logger.error("error is ", e.getMessage());
            }
        }
        result.setCode(200);
        result.setData(rtList);
        return result;
    }

    /**
     * 文件上传
     * 1. 文件上传后的文件名
     * 2. 上传后的文件路径 , 当前年月日时 如:2018060801  2018年6月8日 01时
     * 3. file po 类需要保存文件信息 ,旧名 ,大小 , 上传时间 , 是否删除 ,
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("uploadImage")
    @ResponseBody
    public Map uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // 判断文件是否为空
        if (file.isEmpty()) {
            return JsonResult.error("图片不能为空");
        }
        try {
            Map<String, Object> rtMap =fileService.uploadImage(file);
            return rtMap;
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件下载
     * @param fileId
     * @param res
     */
    @RequestMapping(value = "/downloadFile/{fileId}")
    public void downloadFile(@PathVariable("fileId") String fileId, HttpServletResponse res) {
        try {
            fileService.downloadFile(fileId, res);
        } catch (BusinessException e) {
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 文件查看
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET )
    @ResponseBody
    public JsonResult view(@RequestParam("fileId") String fileId, HttpServletResponse res) throws IOException {

        FileInfo fileInfo = null;
        try {
            fileInfo = fileService.getImage(fileId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileInfo == null) {
            return JsonResult.error("文件不存在");
        }
//        HttpHeaders header = new HttpHeaders();
       // res.setContentType("text/html; charset=UTF-8");
        res.addHeader("content-type", "application/json");
        if (FileUtils.match(fileInfo.getFilename(), "jpg", "png", "gif", "bmp", "tif")) {
            res.setContentType(MediaType.IMAGE_JPEG.toString());
        } else if (FileUtils.match(fileInfo.getFilename(), "html", "htm")) {
            res.setContentType(MediaType.TEXT_HTML.toString());
        } else if (FileUtils.match(fileInfo.getFilename(), "pdf")) {
            res.setContentType(MediaType.APPLICATION_PDF.toString());
        } else {
            res.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        }
        OutputStream os = res.getOutputStream();
        FileInputStream fis = new FileInputStream(fileInfo.getFullPath());
//        header.add("X-Filename", fileInfo.getFilename());
        try {
            int count = 0;
            byte[] buffer = new byte[1024 * 1024];
            while ((count = fis.read(buffer)) != -1)
                os.write(buffer, 0, count);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null)
                os.close();
            if (fis != null)
                fis.close();
        }
        return null;

    }

    /**
     * 文件查看
     */
    @RequestMapping(value = "/viewPic", method = RequestMethod.GET )
    @ResponseBody
    public void viewPic(@RequestParam("fileId") String fileId, HttpServletResponse res) throws IOException {

        FileInfo fileInfo = null;
        try {
            fileInfo = fileService.getImage(fileId);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        HttpHeaders header = new HttpHeaders();
        // res.setContentType("text/html; charset=UTF-8");
        res.addHeader("content-type", "application/json");
        if (FileUtils.match(fileInfo.getFilename(), "jpg", "png", "gif", "bmp", "tif")) {
            res.setContentType(MediaType.IMAGE_JPEG.toString());
        } else if (FileUtils.match(fileInfo.getFilename(), "html", "htm")) {
            res.setContentType(MediaType.TEXT_HTML.toString());
        } else if (FileUtils.match(fileInfo.getFilename(), "pdf")) {
            res.setContentType(MediaType.APPLICATION_PDF.toString());
        } else {
            res.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        }
        OutputStream os = res.getOutputStream();
        FileInputStream fis = new FileInputStream(fileInfo.getFullPath());
//        header.add("X-Filename", fileInfo.getFilename());
        try {
            int count = 0;
            byte[] buffer = new byte[1024 * 1024];
            while ((count = fis.read(buffer)) != -1)
                os.write(buffer, 0, count);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null)
                os.close();
            if (fis != null)
                fis.close();
        }
    }
}