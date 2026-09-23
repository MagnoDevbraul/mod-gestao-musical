package br.com.mod.gestaomusical.security;

import br.com.mod.gestaomusical.entity.Permissao;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UsuarioUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioUserDetailsService(
            UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuário não encontrado."
                        )
                );

        if (usuario.getPerfilUsuario() == null) {
            throw new UsernameNotFoundException(
                    "Usuário sem perfil cadastrado."
            );
        }

        if (!Boolean.TRUE.equals(usuario.getPerfilUsuario().getAtivo())) {
            throw new UsernameNotFoundException(
                    "Perfil do usuário está inativo."
            );
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        /*
         * Mantém o perfil do usuário como ROLE.
         *
         * Exemplo:
         * SECRETARIA -> ROLE_SECRETARIA
         */
        authorities.add(
                new SimpleGrantedAuthority(
                        "ROLE_" + usuario.getPerfilUsuario().getNome()
                )
        );

        /*
         * Adiciona somente as permissões ativas
         * vinculadas ao perfil do usuário.
         *
         * Exemplo:
         * AUDITORIA_CONSULTAR
         */
        usuario.getPerfilUsuario()
                .getPermissoes()
                .stream()
                .filter(permissao ->
                        Boolean.TRUE.equals(permissao.getAtivo())
                )
                .map(Permissao::getNome)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);


        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(authorities)
                .disabled(!Boolean.TRUE.equals(usuario.getAtivo()))
                .build();
    }
}