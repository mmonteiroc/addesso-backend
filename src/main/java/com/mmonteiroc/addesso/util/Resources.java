package com.mmonteiroc.addesso.util;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 13/06/2020
 * Package:com.mmonteiroc.addesso.util
 * Project: addesso
 */
public class Resources {

    private static final String SEPARATOR = File.separator;


    public static String getUploadDir(LocalDateTime uploadDate) {
        String year = uploadDate.getYear() + "";
        String month = uploadDate.getMonthValue() + "";
        String day = uploadDate.getDayOfMonth() + "";
        return year + SEPARATOR + month + SEPARATOR + day + SEPARATOR;
    }
}
