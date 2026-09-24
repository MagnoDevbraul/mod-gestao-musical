package br.com.mod.gestaomusical.dto;

public class AtualizarAlunoRequestDTO {

    private String nome;
    private Boolean possuiInstrumento;

    public AtualizarAlunoRequestDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getPossuiInstrumento() {
        return possuiInstrumento;
    }

    public void setPossuiInstrumento(Boolean possuiInstrumento) {
        this.possuiInstrumento = possuiInstrumento;
    }
}