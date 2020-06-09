package com.controller;
import com.entity.File;
import com.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
@RequestMapping
public class FileController {

   @Autowired
   private FileService fileService;

    @PostMapping("/files")
    @ResponseBody
    public File uploadFile(MultipartFile file, HttpServletRequest request) throws IOException {
        return fileService.saveFile(file,request);
    }
}
