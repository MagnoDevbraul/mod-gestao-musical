package br.com.mod.gestaomusical.dto;

public class AlunoCompartilhamentoRequestDTO {

    private Long alunoId;
    private Long comumDestinoId;

    // Usuário que realizou o compartilhamento
    private Long usuarioId;

    public AlunoCompartilhamentoRequestDTO() {
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getComumDestinoId() {
        return comumDestinoId;
    }

    public void setComumDestinoId(Long comumDestinoId) {
        this.comumDestinoId = comumDestinoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}