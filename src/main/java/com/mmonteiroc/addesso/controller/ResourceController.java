package com.mmonteiroc.addesso.controller;

import com.mmonteiroc.addesso.entity.UploadedFile;
import com.mmonteiroc.addesso.exceptions.entity.UploadedFileNotFoundException;
import com.mmonteiroc.addesso.manager.administration.FileStorageManager;
import com.mmonteiroc.addesso.manager.entity.UploadedFileManager;
import com.mmonteiroc.addesso.manager.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 12/06/2020
 * Package:com.mmonteiroc.addesso.entity
 * Project: addesso
 */
@RestController
public class ResourceController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private UploadedFileManager uploadedFileManager;

    @Autowired
    private Environment environment;

    @Autowired
    private FileStorageManager fileStorageManager;


    /*
     * Protected URL by the access token filter
     *
     * Goes in the AUTHORIZATION HEADER
     * */
    @GetMapping("/resource/{id}")
    @ResponseBody
    public ResponseEntity getFile(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try {
            UploadedFile file = this.uploadedFileManager.findById(id);
            LocalDateTime uploadDate = file.getUploadDate();
            String year = uploadDate.getYear() + "";
            String month = uploadDate.getMonthValue() + "";
            String day = uploadDate.getDayOfMonth() + "";
            String finalDir = year + File.separator + month + File.separator + day + File.separator;

            // Load file as Resource
            Resource resource = fileStorageManager.loadFileAsResource(environment.getProperty("UPLOADS_DIRECTORY") + finalDir + file.getName());

            // Try to determine file's content type
            String contentType = file.getContentType();


            // Fallback to the default content type if type could not be determined
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (UploadedFileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
