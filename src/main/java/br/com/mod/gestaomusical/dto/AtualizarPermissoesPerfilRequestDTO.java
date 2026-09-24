package br.com.mod.gestaomusical.dto;

import java.util.Set;

public class AtualizarPermissoesPerfilRequestDTO {

    private Set<Long> permissaoIds;

    public AtualizarPermissoesPerfilRequestDTO() {
    }

    public Set<Long> getPermissaoIds() {
        return permissaoIds;
    }

    public void setPermissaoIds(Set<Long> permissaoIds) {
        this.permissaoIds = permissaoIds;
    }
}