package com.tarea4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * Configuracion de CORS (Cross-Origin Resource Sharing) para la aplicacion.
 * Permite peticiones desde la aplicacion Flask (localhost:5000).
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configura CORS para todos los endpoints de la aplicacion.
     * Permite acceso desde localhost:5000 (aplicacion Flask).
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:5000",
                    "http://127.0.0.1:5000",
                    "http://localhost:8080",
                    "http://127.0.0.1:8080"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Bean alternativo para configuracion CORS mas granular.
     * Util para casos donde se necesite mas control.
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Origenes permitidos
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:5000",
            "http://127.0.0.1:5000",
            "http://localhost:8080",
            "http://127.0.0.1:8080"
        ));

        // Metodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers permitidos
        config.setAllowedHeaders(Arrays.asList(
            "Content-Type",
            "Authorization",
            "X-Requested-With",
            "Accept",
            "Origin"
        ));

        // Permitir credenciales (cookies, auth headers)
        config.setAllowCredentials(true);

        // Tiempo de cache para preflight requests (en segundos)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
