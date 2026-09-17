package br.com.mod.gestaomusical.dto;

public class RestaurarAlunoRequestDTO {

    private Long usuarioId;

    public RestaurarAlunoRequestDTO() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}