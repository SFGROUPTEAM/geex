package com.hy.common;


import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;
import com.googlecode.sardine.util.SardineException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WebDavUtils {

    public  Map uploadingPic(String directory,MultipartFile file) {
        Map retMap= new HashMap();
        if(!file.isEmpty()){
            String type = null;// 文件类型
            String fileName = file.getOriginalFilename();// 文件原名称
            type = fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()):null;
            if (type!=null) {// 判断文件类型是否为空
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase())) {
                    Sardine sardine = null;
                    try {
                        sardine = SardineFactory.begin(ApplicationBase.webdavUsername, ApplicationBase.webdavPassword);
                        String path = ApplicationBase.webdavPath+directory;
                        if (sardine.exists(path)) {
                            System.out.println(" folder exists");
                        }else{
                            sardine.createDirectory(path);
                        }
                        InputStream fis = file.getInputStream();
                        sardine.put(path  + fileName, fis);
                        retMap.put("retCode","00");
                        retMap.put("retMsg","上传成功");
                        retMap.put("filePath",path  + fileName);
                    } catch (SardineException e) {
                        e.printStackTrace();
                        retMap.put("retCode","97");
                        retMap.put("retMsg","SardineException");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        retMap.put("retCode","98");
                        retMap.put("retMsg","FileNotFoundException");
                    } catch (IOException e) {
                        e.printStackTrace();
                        retMap.put("retCode","99");
                        retMap.put("retMsg","IOException");
                    }
                }else{
                    retMap.put("retCode","03");
                    retMap.put("retMsg","不支持该图片上传");
                    return  retMap;
                }
            }else{
                retMap.put("retCode","02");
                retMap.put("retMsg","图片类型无法识别");
                return  retMap;
            }
            return  retMap;
        }else{
            retMap.put("retCode","01");
            retMap.put("retMsg","文件不能为空");
            return  retMap;
        }
    }
}
