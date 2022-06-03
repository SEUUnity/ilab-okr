package com.industics.ilab.okr.security.utils;

import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.*;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.Locale;
import java.util.UUID;

public class SFTP {

    public static byte[] fileToByte(File file) {
        byte[] data = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            data = baos.toByteArray();

            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static File byteToFile(byte[] buf, String prefix){
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file=null;
        try{

            file = File.createTempFile(String.valueOf(UUID.randomUUID()).
                    replace("-","").toUpperCase(Locale.ROOT), prefix);

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            if (bos != null){
                try{
                    bos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (fos != null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static File multipartFileToFile(MultipartFile multipartFile){
        File file=null;
        try{
            String fileName = multipartFile.getOriginalFilename();//原文件名
            String prefix=fileName.substring(fileName.lastIndexOf("."));// 获取文件后缀
            file = File.createTempFile(String.valueOf(UUID.randomUUID()).
                    replace("-","").toUpperCase(Locale.ROOT), prefix);

            multipartFile.transferTo(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        return file;
    }
    public static void deleteFile(String url){
        Session jschSession = null;
        try {
            JSch jsch = new JSch();
            jschSession = jsch.getSession(FtpServer.User, FtpServer.hostname, FtpServer.REMOTE_PORT);
            jschSession.setConfig("StrictHostKeyChecking", "no");

            // 通过密码的方式登录认证
            jschSession.setPassword(FtpServer.Password);
            jschSession.connect(FtpServer.SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");  //建立sftp文件传输管道
            sftp.connect(FtpServer.CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            // 传输本地文件到远程主机
            channelSftp.rm(url);

            channelSftp.exit();

        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }  finally{
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
    }
    public static String uploadFile(File file){
        String fileUrl="";
        Session jschSession = null;

        try {
            JSch jsch = new JSch();
            jschSession = jsch.getSession(FtpServer.User, FtpServer.hostname, FtpServer.REMOTE_PORT);
            jschSession.setConfig("StrictHostKeyChecking", "no");

            // 通过密码的方式登录认证
            jschSession.setPassword(FtpServer.Password);
            jschSession.connect(FtpServer.SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");  //建立sftp文件传输管道
            sftp.connect(FtpServer.CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;

            //开始传图片
            FileInputStream inputStream = new FileInputStream(file);
            // 传输本地文件到远程主机
            channelSftp.put(inputStream, FtpServer.imgUrl+"/"+file.getName());
            fileUrl=FtpServer.accessUrl+"/"+file.getName();//地址

            channelSftp.exit();

        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
        return fileUrl;
    }


    public static void main(String[] args){

        File imgFile=new File("C:/Users/86150/Desktop/fff.pdf");
        uploadFile(imgFile);
        System.out.println("文件传输完成！");
    }
}
