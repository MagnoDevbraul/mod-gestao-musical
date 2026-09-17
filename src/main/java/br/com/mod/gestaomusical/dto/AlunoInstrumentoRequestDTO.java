package br.com.mod.gestaomusical.dto;

public class AlunoInstrumentoRequestDTO {

    private Long alunoId;
    private Long instrumentoId;
    private String tipo;
    private Long tonalidadeId;

    public AlunoInstrumentoRequestDTO() {
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getInstrumentoId() {
        return instrumentoId;
    }

    public void setInstrumentoId(Long instrumentoId) {
        this.instrumentoId = instrumentoId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getTonalidadeId() {
        return tonalidadeId;
    }

    public void setTonalidadeId(Long tonalidadeId) {
        this.tonalidadeId = tonalidadeId;
    }
}