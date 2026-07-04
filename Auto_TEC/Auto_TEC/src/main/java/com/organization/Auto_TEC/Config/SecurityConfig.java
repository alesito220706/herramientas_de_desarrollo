package com.organization.Auto_TEC.Config;

import com.organization.Auto_TEC.Service.CombinedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CombinedUserDetailsService combinedUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(CombinedUserDetailsService combinedUserDetailsService) {
        this.combinedUserDetailsService = combinedUserDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(combinedUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Seguridad básica: Deshabilitar CSRF (necesario para APIs/JWT)
            .csrf(csrf -> csrf.disable())
            
            // 2. Definir el proveedor de autenticación
            .authenticationProvider(authenticationProvider())

            // 3. AGREGAR FILTRO JWT: Se ejecuta antes del filtro de usuario/contraseña
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

            // 4. Autorización de rutas
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos permitidos para todos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                
                // Rutas públicas de navegación y autenticación
                .requestMatchers("/", "/index", "/animacion", "/contacto", "/gestion",
                                "/modelos", "/registro", "/servicios", "/ventas", 
                                "/financiamiento", "/error", "/auth/**", "/api/auth/**").permitAll()
                
                // Rutas con permisos específicos
                .requestMatchers("/citas/**").hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers("/dashboard/**", "/admin/**").hasRole("ADMIN")
                
                // Cualquier otra ruta requiere estar autenticado
                .anyRequest().authenticated()
            )

            // 5. Gestión de sesión híbrida (Permite cookies para la web y soporta JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            // 6. Configuración del Login por Formulario (Thymeleaf)
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login") // La URL que procesa el POST del login
                .successHandler(customAuthenticationSuccessHandler())
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )

            // 7. Manejo de excepciones (Redirigir a login si no hay acceso)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> 
                    response.sendRedirect("/auth/login"))
            )

            // 8. Configuración del Logout
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomLoginSuccessHandler(); 
    }
}