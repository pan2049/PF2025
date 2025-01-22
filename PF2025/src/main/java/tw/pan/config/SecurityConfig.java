package tw.pan.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import tw.pan.service.impl.security.CustomAuthenticationFailureHandler;
import tw.pan.service.impl.security.CustomLoginSuccessHandler;
import tw.pan.service.impl.security.CustomLogoutSuccessHandler;
import tw.pan.service.impl.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	@Autowired
	private CustomLoginSuccessHandler customLoginSuccessHandler;
	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.userDetailsService(customUserDetailsService) // 設定登錄驗證
		.authorizeHttpRequests((authorizeHttpRequests) ->
			authorizeHttpRequests
				.requestMatchers("/index/control", "/index/alertSet").hasRole("S")
				.requestMatchers("/index/**", "/login/**", "/logout/**").permitAll()
		)
		.formLogin(login -> login // 設定登入
				.loginProcessingUrl("/login/login") // 設定登入URL
				.failureHandler(customAuthenticationFailureHandler) // 使用自定義的失敗處理器
				.successHandler(customLoginSuccessHandler)) // 登入後導往監控首頁
		.logout(logout -> logout // 設定登出
				.logoutUrl("/logout") // 設定登出URL
				.logoutSuccessHandler(customLogoutSuccessHandler)) // 登出後導往登入頁面
		.cors(cors -> cors.configurationSource(request -> {
			CorsConfiguration config = new CorsConfiguration();
//			config.setAllowCredentials(true);
			config.setAllowedOrigins(Arrays.asList("http://localhost"));
//			config.setAllowedOrigins(Arrays.asList(request.getHeader("origin")));
//			config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//			config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
//			config.setExposedHeaders(Arrays.asList("X-Total-Count"));
			return config;
		})) // 允許所有來源
		.sessionManagement(session -> session // 管理會話
				.maximumSessions(1) // 設定最大併發會話數
				.maxSessionsPreventsLogin(false) // 新的登錄請求會踢掉舊的會話
				.sessionRegistry(sessionRegistry())) // 跟蹤和管理用戶的會話訊息
		.csrf(csrf -> csrf.disable()); // 關閉CSRF防護機制
		return http.build();
	}
	
	@Bean
	SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
	
	@Bean
	PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
}
