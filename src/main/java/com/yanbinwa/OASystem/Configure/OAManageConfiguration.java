package com.yanbinwa.OASystem.Configure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.yanbinwa.OASystem")
public class OAManageConfiguration
{

}
