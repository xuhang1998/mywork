package com.service.impl;

import com.dao.FileDao;
import com.entity.File;
import com.service.FileService;
import com.utils.FileUtil;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Resource
    private FileDao fileDao;
    //从配置中读取值
    @Value("${files.path}")
    private String path;

    @Override
    public File saveFile(MultipartFile file, HttpServletRequest request) throws IOException {
        //文件名
        String fileOrigName = file.getOriginalFilename();
        if (!fileOrigName.contains(".")) {
            throw new IllegalArgumentException("缺少后缀名");
        }
        int size = (int)(file.getSize());
        //文件类型
        String contentType = file.getContentType();
        String filePath = path+"/"+fileOrigName;
        File file1 = new File();
        file1.setCreateTime(new Date());
        file1.setSize(size);
        file1.setType(contentType);
        file1.setUrl(fileOrigName);

        /*java.io.File dest = new java.io.File(filePath);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            fileDao.saveFile(file1);
            return file1;
        } catch (IllegalStateException e) {

            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }*/
        try{
            FileUtil.uploadFile(file.getBytes(),path,fileOrigName);
            fileDao.saveFile(file1);

            return file1;
        }catch(Exception e){

        }

        return file1;
    }
}
