package br.com.mod.gestaomusical.security;

import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioAutenticadoService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioAutenticadoService(
            UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    public Usuario obterUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            throw new IllegalStateException(
                    "Nenhum usuário autenticado foi encontrado."
            );
        }

        String email = authentication.getName();

        return usuarioRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Usuário autenticado não encontrado no banco."
                        )
                );
    }

    public Long obterUsuarioId() {
        return obterUsuarioAutenticado().getId();
    }

    public String obterEmail() {
        return obterUsuarioAutenticado().getEmail();
    }

    public String obterNome() {
        return obterUsuarioAutenticado().getNome();
    }

    public String obterPerfil() {

        Usuario usuario = obterUsuarioAutenticado();

        if (usuario.getPerfilUsuario() == null) {
            throw new IllegalStateException(
                    "Usuário autenticado não possui perfil."
            );
        }

        return usuario.getPerfilUsuario().getNome();
    }
}