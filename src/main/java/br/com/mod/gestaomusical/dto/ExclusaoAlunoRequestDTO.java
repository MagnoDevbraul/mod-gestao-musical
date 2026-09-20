package br.com.mod.gestaomusical.dto;

public class ExclusaoAlunoRequestDTO {

    private Long alunoId;
    private String motivo;

    public ExclusaoAlunoRequestDTO() {
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}