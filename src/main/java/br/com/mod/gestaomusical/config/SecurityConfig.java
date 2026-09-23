package br.com.mod.gestaomusical.config;

import br.com.mod.gestaomusical.repository.UsuarioRepository;
import br.com.mod.gestaomusical.security.UsuarioUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.nio.charset.StandardCharsets;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SessionRegistry sessionRegistry) throws Exception {

        http
                /*
                 * O MOD utiliza uma API REST com autenticação baseada
                 * em sessão HTTP. O CSRF permanece desabilitado nesta
                 * etapa enquanto o backend é testado diretamente.
                 */
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        /*
                         * Swagger e documentação OpenAPI permanecem públicos.
                         */
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        /*
                         * O login precisa ser acessível antes de existir
                         * uma sessão autenticada.
                         */
                        .requestMatchers(
                                "/auth/login"
                        ).permitAll()

                        // Todos os outros endpoints exigem autenticação.
                        .anyRequest().authenticated()
                )

                /*
                 * HTTP Basic foi desabilitado porque não mantém uma
                 * sessão confiável para identificação de usuários online.
                 */
                .httpBasic(AbstractHttpConfigurer::disable)

                /*
                 * Quando uma requisição protegida for feita sem sessão
                 * válida, a API deve responder 401 em JSON.
                 *
                 * Isso evita redirecionamentos e páginas HTML de login,
                 * comportamento inadequado para uma API REST.
                 */
                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(401);

                                    response.setContentType(
                                            MediaType.APPLICATION_JSON_VALUE
                                    );

                                    response.setCharacterEncoding(
                                            StandardCharsets.UTF_8.name()
                                    );

                                    response.getWriter().write(
                                            """
                                            {
                                              "mensagem": "Usuário não autenticado"
                                            }
                                            """
                                    );
                                }
                        )
                )

                /*
                 * O login cria uma sessão HTTP após a validação
                 * do e-mail e da senha pelo Spring Security.
                 */
                .formLogin(form -> form

                        .loginProcessingUrl("/auth/login")

                        .usernameParameter("email")
                        .passwordParameter("senha")

                        .successHandler(
                                (request, response, authentication) -> {

                                    response.setStatus(200);

                                    response.setContentType(
                                            MediaType.APPLICATION_JSON_VALUE
                                    );

                                    response.setCharacterEncoding(
                                            StandardCharsets.UTF_8.name()
                                    );

                                    response.getWriter().write(
                                            """
                                            {
                                              "mensagem": "Login realizado com sucesso"
                                            }
                                            """
                                    );
                                }
                        )

                        .failureHandler(
                                (request, response, exception) -> {

                                    response.setStatus(401);

                                    response.setContentType(
                                            MediaType.APPLICATION_JSON_VALUE
                                    );

                                    response.setCharacterEncoding(
                                            StandardCharsets.UTF_8.name()
                                    );

                                    response.getWriter().write(
                                            """
                                            {
                                              "mensagem": "E-mail ou senha inválidos"
                                            }
                                            """
                                    );
                                }
                        )

                        .permitAll()
                )

                /*
                 * O logout invalida a sessão atual e remove o JSESSIONID.
                 * Isso também permite atualizar corretamente o registro
                 * de usuários ativos.
                 */
                .logout(logout -> logout

                        .logoutUrl("/auth/logout")

                        .invalidateHttpSession(true)

                        .clearAuthentication(true)

                        .deleteCookies("JSESSIONID")

                        .logoutSuccessHandler(
                                (request, response, authentication) -> {

                                    response.setStatus(200);

                                    response.setContentType(
                                            MediaType.APPLICATION_JSON_VALUE
                                    );

                                    response.setCharacterEncoding(
                                            StandardCharsets.UTF_8.name()
                                    );

                                    response.getWriter().write(
                                            """
                                            {
                                              "mensagem": "Logout realizado com sucesso"
                                            }
                                            """
                                    );
                                }
                        )
                )

                .sessionManagement(session -> {

                    /*
                     * A sessão só é criada quando necessária.
                     */
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.IF_REQUIRED
                    );

                    /*
                     * Cada conta pode manter apenas uma sessão ativa.
                     * Um novo login invalida a sessão anterior.
                     */
                    session.maximumSessions(1)
                            .maxSessionsPreventsLogin(false)
                            .sessionRegistry(sessionRegistry);
                });

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UsuarioRepository usuarioRepository) {

        return new UsuarioUserDetailsService(
                usuarioRepository
        );
    }

    /*
     * Senhas armazenadas utilizando BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    /*
     * Mantém o registro das sessões autenticadas.
     * Será usado para identificar usuários atualmente online.
     */
    @Bean
    public SessionRegistry sessionRegistry() {

        return new SessionRegistryImpl();
    }

    /*
     * Mantém o SessionRegistry sincronizado quando uma
     * sessão HTTP é criada, expirada ou destruída.
     */
    @Bean
    public static HttpSessionEventPublisher httpSessionEventPublisher() {

        return new HttpSessionEventPublisher();
    }
}