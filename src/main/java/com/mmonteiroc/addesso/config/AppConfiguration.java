package com.mmonteiroc.addesso.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 04/06/2020
 * Package: com.mmonteiroc.addesso.config
 * Project: addesso
 */
@Configuration
public class AppConfiguration implements WebMvcConfigurer {

//    @Bean
//    public TokenFilter getTokenFilter() {
//        return new TokenFilter();
//    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }
}