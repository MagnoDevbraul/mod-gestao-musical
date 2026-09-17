package br.com.mod.gestaomusical.dto;

public class AlunoInstrumentoResponseDTO {

    private Long id;

    private Long alunoId;
    private String alunoNome;

    private Long instrumentoId;
    private String instrumentoNome;

    private String tipo;

    private Long tonalidadeId;
    private String tonalidadeNome;

    public AlunoInstrumentoResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }

    public Long getInstrumentoId() {
        return instrumentoId;
    }

    public void setInstrumentoId(Long instrumentoId) {
        this.instrumentoId = instrumentoId;
    }

    public String getInstrumentoNome() {
        return instrumentoNome;
    }

    public void setInstrumentoNome(String instrumentoNome) {
        this.instrumentoNome = instrumentoNome;
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

    public String getTonalidadeNome() {
        return tonalidadeNome;
    }

    public void setTonalidadeNome(String tonalidadeNome) {
        this.tonalidadeNome = tonalidadeNome;
    }
}