package org.iesalixar.daw2.NataliaFernandezOspina.dwese_api_gateway.Config;

import io.jsonwebtoken.Claims;
import org.iesalixar.daw2.NataliaFernandezOspina.dwese_api_gateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Filtro de autenticación JWT en el API Gateway.
 *
 * Este filtro intercepta todas las solicitudes HTTP que pasan por el API Gateway
 * y verifica la validez del token JWT en el encabezado Authorization.
 *
 * Si el token es válido, se extrae la información del usuario y se añade a los headers de la solicitud.
 * Si el token no es válido, se devuelve un error 401 (Unauthorized).
 */
@Component
public class JwtAuthFilter implements WebFilter {

    @Autowired
    private JwtUtil jwtUtil; // Utilidad para gestionar los tokens JWT

    /**
     * Lista de URLs que no requieren autenticación.
     * Cualquier solicitud que coincida con estos endpoints se permite sin validar el JWT.
     */
    private static final List<String> PUBLIC_URLS = List.of(
            "/api/v1/auth/authenticate",
            "/api/v1/auth/register"
    );

    /**
     * Método principal del filtro.
     * Intercepta las solicitudes y valida el token JWT antes de permitir su procesamiento.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Permitir acceso sin token a las rutas públicas
        if (PUBLIC_URLS.stream().anyMatch(url -> request.getURI().getPath().startsWith(url))) {
            return chain.filter(exchange);
        }

        // Obtener el token JWT del encabezado Authorization
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "🔴 Token no encontrado o mal formado.");
        }

        // Extraer el token quitando el prefijo "Bearer "
        String token = authHeader.substring(7);

        // Validar el token y extraer el nombre de usuario
        String username;
        try {
            username = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            return unauthorized(exchange, "❌ Token inválido: " + e.getMessage());
        }

        // Verificar si el token es válido
        if (!jwtUtil.validateToken(token, username)) {
            return unauthorized(exchange, "⚠️ Token expirado o inválido.");
        }

        // Extraer los claims (información contenida en el JWT)
        Claims claims = jwtUtil.extractAllClaims(token);
        List<String> roles = claims.get("roles", List.class); // Obtener la lista de roles del usuario

        // Crear una nueva solicitud con los headers personalizados
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-Authenticated-User", username) // Agrega el usuario autenticado en el header
                .header("X-Authenticated-Roles", String.join(",", roles)) // Agrega los roles en el header
                .build();

        // Continuar la ejecución del request con los nuevos headers
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    /**
     * Método auxiliar que devuelve un error HTTP 401 (Unauthorized).
     * Se usa cuando el token es inválido o está ausente.
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange, String mensaje) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
