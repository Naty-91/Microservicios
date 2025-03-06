package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Role;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.User;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Carga los detalles del usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario para buscar.
     * @return UserDetails con la información del usuario.
     * @throws UsernameNotFoundException Si no se encuentra el usuario.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario en la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Construir y devolver un objeto UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
                        .toArray(new String[0]))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }
}
