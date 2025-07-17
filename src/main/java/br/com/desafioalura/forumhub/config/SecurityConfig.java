package br.com.desafioalura.forumhub.config;

import br.com.desafioalura.forumhub.security.JwtAuthenticationEntryPoint;
import br.com.desafioalura.forumhub.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity // Habilita uso de @PreAuthorize, @PostAuthorize, etc.
public class SecurityConfig {

     @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  //  @Autowired
 //   private JwtRequestFilter jwtRequestFilter;

@Autowired
private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsService jwtUserDetailsService; // Mantido para consistência, embora não usado diretamente aqui


    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // desabilita CSRF (necessário para APIs JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT não usa sessão
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(  "/swagger-ui.html", "/swagger-ui/index.html","/v3/api-docs").permitAll() // Permite acesso público ao Swagger UI
                        .anyRequest().authenticated()
                )

        // Adiciona o filtro JWT antes do filtro de autenticação de nome de utilizador/senha
        //http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
              //  .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
               // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Configuração CORS para permitir o frontend React e o novo frontend Spring Boot
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8081")); // Permite o frontend React e o novo frontend Spring Boot
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Permite o envio de cookies de credenciais
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
