package org.iesalixar.daw2.NataliaFernandezOspina.dwese_auth_service.Config;

import org.iesalixar.daw2.NataliaFernandezOspina.dwese_auth_service.services.CustomUserDetailsService;
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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Activa la seguridad basada en métodos
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Configura la cadena de filtros de seguridad.
     *
     * @param http el objeto {@link HttpSecurity} para construir la configuración de seguridad.
     * @return la configuración de la cadena de filtros de seguridad.
     * @throws Exception si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuración de las rutas protegidas
        http
                .securityMatcher("/api/**")  // Solo protegerá las rutas que comiencen con /api/
                .cors(withDefaults())  // Habilita la configuración de CORS predeterminada
                .csrf(csrf -> csrf.disable())  // Deshabilita la protección CSRF para las APIs
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // No se creará sesión, las APIs son sin estado
                )
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/v1/auth/**").permitAll()  // Permite acceso sin token a rutas de autenticación
                                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")  // Solo los usuarios con el rol 'ADMIN' pueden gestionar usuarios
                                .anyRequest().authenticated()  // Requiere autenticación para cualquier otra ruta
                );

        return http.build();  // Construye la configuración
    }


    /**
     * Configura el proveedor de autenticación.
     *
     * @return una instancia de {@link DaoAuthenticationProvider}.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configura el codificador de contraseñas para cifrar las contraseñas de los usuarios
     * utilizando BCrypt.
     *
     * @return una instancia de {@link PasswordEncoder} que utiliza BCrypt para cifrar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Entrando en el método passwordEncoder");
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        logger.info("Saliendo del método passwordEncoder");
        return encoder;
    }

    /**
     * Configura y expone un bean de tipo {@link AuthenticationManager}.
     *
     * @param configuration Objeto de tipo {@link AuthenticationConfiguration} que contiene
     *                      la configuración de autenticación de Spring Security.
     * @return Una instancia de {@link AuthenticationManager} configurada con los detalles
     *         especificados en la aplicación.
     * @throws Exception Si ocurre algún error al obtener el AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Obtiene y devuelve el AuthenticationManager desde la configuración proporcionada
        return configuration.getAuthenticationManager();
    }
}
