package com.github.felixbyrjall.userauth.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				/*registry.addMapping("/api/auth/**")
						.allowedOrigins("http://localhost:3000")
						.allowedMethods("GET", "POST")
						.allowedHeaders("*")
						.allowCredentials(true);

				registry.addMapping("/api/csrf")
						.allowedOrigins("http://localhost:3000")
						.allowedMethods("GET")
						.allowedHeaders("*")
						.allowCredentials(true);
			}*/
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:3000")
						.allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE")
						.allowedHeaders("*")
						.allowCredentials(true);
			};

		};
	}
}
