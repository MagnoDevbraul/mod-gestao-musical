package br.com.mod.gestaomusical.dto;

public class AlunoCompartilhamentoRequestDTO {

    private Long alunoId;
    private Long comumDestinoId;

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
}