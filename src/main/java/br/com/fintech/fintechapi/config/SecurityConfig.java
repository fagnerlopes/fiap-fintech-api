package br.com.fintech.fintechapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuração de segurança da aplicação
 * Define regras de autenticação, autorização e CORS
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean do BCryptPasswordEncoder para hash de senhas
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean do AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configuração da cadeia de filtros de segurança
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desabilitar CSRF (comum para APIs REST)
            .csrf(csrf -> csrf.disable())
            
            // Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configurar autorização de requisições
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (sem autenticação)
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/health").permitAll()
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                
                // Todos os outros endpoints requerem autenticação
                .requestMatchers("/api/**").authenticated()
                
                // Qualquer outra requisição
                .anyRequest().authenticated()
            )
            
            // Configurar autenticação HTTP Basic
            .httpBasic(basic -> {})
            
            // Configurar gerenciamento de sessão
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );

        return http.build();
    }

    /**
     * Configuração de CORS para permitir requisições do frontend
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Origens permitidas (ajustar conforme necessário)
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",    // React development
            "http://localhost:5173",    // Vite development
            "http://localhost:8080"     // Outros frontends
        ));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Permitir credenciais (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Tempo de cache da configuração de CORS
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}

