package com.service;

import com.entity.File;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface FileService {
    File saveFile(MultipartFile file, HttpServletRequest request)throws IOException;
}
