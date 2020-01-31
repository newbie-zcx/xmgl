package com.scsoft.xgsb.common.utils;

import com.scsoft.scpt.utils.DateUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.SocketException;

/**
 * @title: FavFTPUtil
 * @Description:
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/5/29 8:40
 * @Version: 1.0
 */
@Component
public class FavFTPUtil {
    private FTPClient ftpClient;
    public static String ip ;
    private static String username ;
    private static String password;
    private static int port;
    private static String basePath;
    @Value("${file.ftp.ip}")
    public void setIp(String ip) {
        FavFTPUtil.ip = ip;
    }
    @Value("${file.ftp.username}")
    public void setUsername(String username) {
        FavFTPUtil.username = username;
    }
    @Value("${file.ftp.password}")
    public void setPassword(String password) {
        FavFTPUtil.password = password;
    }
    @Value("${file.ftp.port}")
    public void setPort(int port) {
        FavFTPUtil.port = port;
    }
    @Value("${file.ftp.basePath}")
    public void setBasePath(String basePath) {
        FavFTPUtil.basePath = basePath;
    }

    /**
     * Description: 向FTP服务器上传文件
     * @param fileName ftp文件名称
     * @param input 文件流
     * @return 成功返回true，否则返回false File.separatorChar
     */
    public static boolean upload(String fileName,InputStream input) {
        String ftpPath=DateUtil.getYear()+DateUtil.getCurrentMonth() + "/"+DateUtil.getCurrentDay();
         boolean sucess=uploadFile(ip,username,password,port,basePath,ftpPath,fileName,input);
        return sucess;
    }
    public static boolean delete(String pathname, String filename) {
        boolean sucess=deleteFile(ip,port,username,password,pathname,filename);
        return sucess;
    }
    public static void  download(String remotePath, String localPath,
                                       String fileName) {
        downloadFtpFile(ip,username,password,port,remotePath,localPath,fileName);
    }


        /**
         * 获取FTPClient对象
         *
         * @param ftpHost     FTP主机服务器
         * @param ftpPassword FTP 登录密码
         * @param ftpUserName FTP登录用户名
         * @param ftpPort     FTP端口 默认为21
         * @return
         */
    public static FTPClient getFTPClient(String ftpHost, String ftpUserName,
                                         String ftpPassword, int ftpPort) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                System.out.println("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                System.out.println("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FTP的端口错误,请正确配置。");
        }
        return ftpClient;
    }

    /*
     * 从FTP服务器下载文件
     *
     * @param ftpHost FTP IP地址
     * @param ftpUserName FTP 用户名
     * @param ftpPassword FTP用户名密码
     * @param ftpPort FTP端口
     * @param remotePath FTP服务器上的相对路径
     * @param localPath 下载到本地的位置 格式：H:/download
     * @param fileName 文件名称
     */
    public static void downloadFtpFile(String ftpHost, String ftpUserName,
                                       String ftpPassword, int ftpPort, String remotePath, String localPath,
                                       String fileName) {

        FTPClient ftpClient = null;

        try {
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(remotePath);

            FTPFile[] fs = ftpClient.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath +File.separatorChar + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }
        /*    File localFile = new File(localPath + File.separatorChar + fileName);
            OutputStream os = new FileOutputStream(localFile);
            ftpClient.retrieveFile(fileName, os);
            os.close();*/
            ftpClient.logout();

        } catch (FileNotFoundException e) {
            System.out.println("没有找到" + remotePath + "文件");
            e.printStackTrace();
        } catch (SocketException e) {
            System.out.println("连接FTP失败.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件读取错误。");
            e.printStackTrace();
        }

    }

    /**
     * Description: 向FTP服务器上传文件
     * @param ftpHost FTP服务器hostname
     * @param ftpUserName 账号
     * @param ftpPassword 密码
     * @param ftpPort 端口
     * @param basePath FTP服务器基础目录/home/ftpuser/www/images
     * @param ftpPath  FTP服务器中文件所在路径 格式： 例如分日期存放：/2019/05/29。文件的全路径为basePath+ftpPath
     * @param fileName ftp文件名称
     * @param input 文件流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String ftpHost, String ftpUserName,
                                     String ftpPassword, int ftpPort, String basePath, String ftpPath,
                                     String fileName, InputStream input) {
        boolean success = false;
        FTPClient ftpClient = null;
        try {
            int reply;
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return success;
            }

            //切换到上传目录
            if (!ftpClient.changeWorkingDirectory(basePath+ftpPath)) {
                //如果目录不存在创建目录
                String[] dirs = ftpPath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {continue;}
                    tempPath += "/" + dir;
                    ftpClient.changeWorkingDirectory(tempPath);
                    if (!ftpClient.changeWorkingDirectory(tempPath)) {
                        ftpClient.makeDirectory(ftpPath);
                        ftpClient.changeWorkingDirectory(tempPath);
                      /*  if (!ftpClient.makeDirectory(tempPath)) {
                            return success;
                        } else {
                            ftpClient.changeWorkingDirectory(tempPath);
                        }*/
                    }
                }
            }

            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
       /*     ftpClient.changeWorkingDirectory(ftpPath);*/
            //上传文件
            if (!ftpClient.storeFile(fileName, input)) {
                return success;
            }
//            ftpClient.storeFile(fileName, input);
            input.close();
            ftpClient.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }


    /**
     * 删除文件
     * @param hostname FTP服务器地址
     * @param port FTP服务器端口号
     * @param username FTP登录帐号
     * @param password FTP登录密码
     * @param pathname FTP服务器保存目录
     * @param filename 要删除的文件名称
     * @return
     */
    public static boolean deleteFile(String hostname, int port, String username, String password, String pathname, String filename){
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        try {
            //连接FTP服务器
            ftpClient.connect(hostname, port);
            //登录FTP服务器
            ftpClient.login(username, password);
            //验证FTP服务器是否登录成功
            int replyCode = ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(replyCode)){
                return flag;
            }
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.dele(filename);
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(ftpClient.isConnected()){
                try {
                    ftpClient.logout();
                } catch (IOException e) {

                }
            }
        }
        return flag;
    }

}
