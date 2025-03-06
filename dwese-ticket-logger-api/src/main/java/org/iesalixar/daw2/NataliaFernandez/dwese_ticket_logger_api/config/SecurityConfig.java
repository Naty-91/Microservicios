package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.config;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)  // Activa la seguridad basada en métodos
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    /**
     * Configura el filtro de seguridad para las solicitudes HTTP, especificando las
     * rutas permitidas y los roles necesarios para acceder a diferentes endpoints.
     *
     * @param http instancia de {@link HttpSecurity} para configurar la seguridad.
     * @return una instancia de {@link SecurityFilterChain} que contiene la configuración de seguridad.
     * @throws Exception si ocurre un error en la configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Entrando en el método securityFilterChain");


        // Configuración de seguridad
        http
                .securityMatcher("/api/**")
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/ticket-logger/tickets").hasRole("USER")         // Solo USER
                        .requestMatchers("/api/v1/ticket-logger/admin").hasRole("ADMIN")       // Solo ADMIN
                        .requestMatchers(
                                "/api/v1/ticket-logger/regions",
                                "/api/v1/ticket-logger/provinces",
                                "/api/v1/ticket-logger/supermarkets",
                                "/api/v1/ticket-logger/locations",
                                "/api/v1/ticket-logger/categories").hasRole("MANAGER") // Solo MANAGER
                        .requestMatchers("/api/v1/authenticate", "/api/v1/register").permitAll() // Solo USER
                        .anyRequest().authenticated()

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}