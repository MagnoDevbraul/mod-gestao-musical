package br.com.mod.gestaomusical.security;

import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(usuario.getPerfilUsuario().getNome())
                .disabled(!Boolean.TRUE.equals(usuario.getAtivo()))
                .build();
    }
}