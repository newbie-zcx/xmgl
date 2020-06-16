package com.scsoft.xmgl.file.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xmgl.file.entity.FileInfo;
import com.scsoft.scpt.common.PageResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 附件管理 服务类
 * </p>
 * @author zhaopengfei
 * @CreateDate 2019-05-29
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */
public interface IFileService extends IService<FileInfo> {
     PageResult<FileInfo> list(Page<FileInfo> page, Map<String, Object> map, String condition);
     boolean add(FileInfo file);
     boolean update(FileInfo file);
     boolean delete(Integer fileId);
     boolean logicDelete(FileInfo file);
     List<FileInfo> getfileList(Map<String, Object> map);
     String upload(MultipartFile file) throws IOException;
     void downloadFile(String fileId, HttpServletResponse res) throws UnsupportedEncodingException;
     FileInfo getImage(String fileName) throws IOException;

     Map<String, Object> uploadImage(MultipartFile file);
}
