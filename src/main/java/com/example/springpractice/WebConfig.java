package com.example.springpractice;

import com.example.springpractice.converter.IntegerToStringConverter;
import com.example.springpractice.converter.IpPortToStringConverter;
import com.example.springpractice.converter.StringToIntegerConverter;
import com.example.springpractice.converter.StringToIpPortConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new IpPortToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
    }
}
