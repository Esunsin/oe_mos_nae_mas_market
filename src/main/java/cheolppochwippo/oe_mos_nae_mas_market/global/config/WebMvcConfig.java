package cheolppochwippo.oe_mos_nae_mas_market.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 스프링 빈으로 등록
public class WebMvcConfig implements WebMvcConfigurer {

	private final long MAX_AGE_SECS = 3600;


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		String[] allowedOrigins = {"https://fronts-two.vercel.app", "http://fronts-two.vercel.app",
			"http://localhost:3000"};
		// 모든 경로에 대해
		registry.addMapping("/**")
			.exposedHeaders("Authorization")
			// Origin이 http:localhost:3000에 대해
			.allowedOriginPatterns(allowedOrigins)
			// GET, POST, PUT, PATCH, DELETE, OPTIONS 메서드를 허용한다.
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(true)
			.maxAge(MAX_AGE_SECS);
	}
}
