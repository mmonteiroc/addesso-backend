package com.mmonteiroc.addesso.manager.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;


/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 13/06/2020
 * Package:com.mmonteiroc.addesso.manager.administration
 * Project: addesso
 */
@Service
public class FileStorageManager {
    @Autowired
    private Environment environment;


    public Resource loadFileAsResource(String path) {
        try {
            Path filePath = Paths.get(path);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public String saveMultiparFiles(final MultipartFile file) throws IOException {
        final byte[] bytes = file.getBytes();
        String uploads = environment.getProperty("UPLOADS_DIRECTORY");


        String year = LocalDate.now().getYear() + "";
        String month = LocalDate.now().getMonthValue() + "";
        String day = LocalDate.now().getDayOfMonth() + "";
        String finalDir = year + File.separator + month + File.separator + day + File.separator;


        File directory = new File(uploads + finalDir);
        if (!directory.exists()) directory.mkdirs();

        /*
         * We check that the filename doesnt bring any path
         *
         * if  doesn't find the / or \ gives -1 +1 means
         * does a substring from the 0 char, so we dont loose anything
         * */
        String name = file.getOriginalFilename();
        name = name.substring(name.lastIndexOf('/') + 1);
        name = name.substring(name.lastIndexOf('\\') + 1);

        final Path path = Paths.get(directory.getPath() + File.separator + name);
        Files.write(path, bytes);

        return name;
    }

}
