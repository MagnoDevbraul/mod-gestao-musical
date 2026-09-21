package br.com.mod.gestaomusical.security;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioSessaoService {

    private final SessionRegistry sessionRegistry;

    public UsuarioSessaoService(
            SessionRegistry sessionRegistry) {

        this.sessionRegistry = sessionRegistry;
    }

    /**
     * Verifica se existe pelo menos uma sessão válida para o e-mail informado.
     *
     * O SessionRegistry mantém os usuários autenticados como principals.
     * Sessões expiradas ou invalidadas não são consideradas ativas.
     */
    public boolean estaOnline(String email) {

        if (email == null || email.isBlank()) {
            return false;
        }

        for (Object principal : sessionRegistry.getAllPrincipals()) {

            if (!(principal instanceof UserDetails userDetails)) {
                continue;
            }

            if (!userDetails.getUsername().equalsIgnoreCase(email)) {
                continue;
            }

            List<SessionInformation> sessoesAtivas =
                    sessionRegistry.getAllSessions(
                            principal,
                            false
                    );

            if (!sessoesAtivas.isEmpty()) {
                return true;
            }
        }

        return false;
    }
}