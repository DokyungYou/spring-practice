package com.example.springpractice;

import com.example.springpractice.converter.IntegerToStringConverter;
import com.example.springpractice.converter.IpPortToStringConverter;
import com.example.springpractice.converter.StringToIntegerConverter;
import com.example.springpractice.converter.StringToIpPortConverter;
import com.example.springpractice.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 우선순위 때문에 주석처리 (포멧터 < 컨버터)
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new IpPortToStringConverter());
        registry.addConverter(new StringToIpPortConverter());

        // 숫자 -> 문자, 문자-> 숫자
        registry.addFormatter(new MyNumberFormatter());
    }
}
