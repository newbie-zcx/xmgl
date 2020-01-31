package com.scsoft.xgsb.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.xgsb.common.exception.BusinessException;
import com.scsoft.xgsb.common.utils.FavFTPUtil;
import com.scsoft.xgsb.common.utils.FileUtils;
import com.scsoft.xgsb.file.entity.FileInfo;
import com.scsoft.xgsb.file.mapper.FileMapper;
import com.scsoft.xgsb.file.service.IFileService;
import com.scsoft.scpt.common.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 附件管理 服务实现类
 * </p>
 *
 * @author zhaopengfei
 * @CreateDate 2019-05-29
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileInfo> implements IFileService {
     @Resource
     private FileMapper fileMapper;
     @Value("${file.ftp.use}")
    private boolean ftpisused;
    @Override
    public PageResult<FileInfo> list(Page<FileInfo> page, Map<String,Object> map, String condition){
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<FileInfo>();
        IPage<FileInfo> paged = fileMapper.selectPage(page, wrapper.allEq(map, false).orderByDesc("create_date"));
        return new PageResult<FileInfo>(paged.getRecords(),paged.getTotal());
     }
    @Override
     public boolean add(FileInfo file)throws BusinessException {
       //  if (fileMapper.getByName(user.getUserName()) != null) {
       //     throw new BusinessException("已经存在");
        //   }
        return fileMapper.insert(file) > 0;
     }
    @Override
     public boolean update(FileInfo file){
        return fileMapper.updateById(file)>0;
     }
    @Override
     public boolean delete(Integer fileId){
     return fileMapper.deleteById(fileId)>0;
     }
    @Override
     public boolean logicDelete(FileInfo file){
          file.setIsDel(1);
       return fileMapper.updateById(file)>0;
     }
    @Override
     public List<FileInfo> getfileList(Map<String,Object> map){
        return fileMapper.selectByMap(map);
     }

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws BusinessException
     */
    @Transactional
    @Override
    public String upload(MultipartFile file) throws BusinessException, IOException {
        String fileName = FileUtils.getFileName();
        // 获取文件名称含后缀
        String fileNameext = fileName + FileUtils.getFileNameSub(file.getOriginalFilename());
       if (ftpisused){
           FavFTPUtil.upload(fileNameext,file.getInputStream());
//           return true;
       }
        //基础路径
        String basePath = FileUtils.getBasePath();
        //获取文件保存路径
        String folder = FileUtils.getFolder();
        try {
            // 路径
            Path filePath = Files.createDirectories(Paths.get(basePath, folder));
            // 全路径
            Path fullPath = Paths.get(basePath, folder, fileNameext);
            Files.write(fullPath, file.getBytes(), StandardOpenOption.CREATE);
            //保存文件信息
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFilePurpose("1");
            fileInfo.setFilename(fileNameext);
            fileInfo.setExtname(FileUtils.getFileNameSub(file.getOriginalFilename()));
            fileInfo.setFilePath(filePath.toString());
            fileInfo.setFullPath(fullPath.toString());
            fileInfo.setSourceName(file.getOriginalFilename());
            fileInfo.setFileSize(file.getSize()+"");
            fileInfo.setFileDate(new Date());
            fileMapper.insert(fileInfo);
            String  id= (fileInfo.getId()).toString();
            return id;
        } catch (Exception e) {
            Path path = Paths.get(basePath, folder);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return "";
        }
    }


    /**
     * 文件下载
     *
     * @param fileId
     * @param res
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
    @Override
    public void downloadFile(String fileId, HttpServletResponse res) throws BusinessException, UnsupportedEncodingException {
        if (fileId == null) {
            throw new BusinessException(1001, "文件不能为空");
        }
        // 通过文件名查找文件信息
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<FileInfo>();
        wrapper.eq("id",fileId);
        FileInfo fileInfo = fileMapper.selectOne(wrapper);
        if (fileInfo == null) {
            throw new BusinessException(2001, "文件名不存在");
        }

        //设置响应头
        res.setContentType("application/force-download");// 设置强制下载不打开
        res.addHeader("Content-Disposition", "attachment;fileName=" +
                new String(fileInfo.getSourceName().getBytes("gbk"), "iso8859-1"));// 设置文件名
        res.setHeader("Context-Type", "application/xmsdownload");

        //判断文件是否存在
        File file = new File(Paths.get(fileInfo.getFilePath(), fileInfo.getFilename()).toString());
        if (file.exists()) {
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = res.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("下载成功");
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(9999, e.getMessage());
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 文件查看
     */
    @Override
    public FileInfo getImage(String fileId) throws IOException {
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<FileInfo>();
        wrapper.eq("id",fileId);
        FileInfo fileInfo = fileMapper.selectOne(wrapper);
        if (fileInfo == null) {
            return null;
        }
        Path path = Paths.get(fileInfo.getFilePath(), fileInfo.getFilename());
        fileInfo.setContent(Files.newInputStream(path));
        return fileInfo;
    }

    @Override
    public Map<String, Object> uploadImage(MultipartFile file) {
        try {
            String fileId = upload(file);
            Map<String,Object> rtMap = new HashMap<>();
            rtMap.put("url", fileId);
            return rtMap;
        } catch (Exception e) {

        }
        return null;
    }

    /*
     * 在线预览图片
     */
    public  void showImage(String path, HttpServletResponse res) throws IOException {
        res.setContentType("text/html; charset=UTF-8");
        res.setContentType("image/jpeg");
        FileInputStream fis = new FileInputStream(path);
        OutputStream os = res.getOutputStream();
        try {
            int count = 0;
            byte[] buffer = new byte[1024 * 1024];
            while ((count = fis.read(buffer)) != -1) {
                os.write(buffer, 0, count);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null){
                os.close();}
            if (fis != null){
                fis.close();}
        }
    }
}
