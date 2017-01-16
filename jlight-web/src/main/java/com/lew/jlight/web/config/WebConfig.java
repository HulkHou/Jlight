package com.lew.jlight.web.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan("com.lew.jlight.web.filter")
public class WebConfig {
}
