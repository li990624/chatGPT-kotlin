package com.chat.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // 允许对所有路径进行跨域访问，你可以根据需要修改为特定的路径
            .allowedOriginPatterns("*") // 允许所有来源的跨域请求，你可以指定允许的域名或 IP 地址
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方法
            .allowedHeaders("*") // 允许的请求头
            .allowCredentials(true) // 是否允许发送身份凭证（如 cookies）
            .maxAge(3600) // 预检请求的缓存时间（单位：秒）
    }
}