package br.com.mod.gestaomusical.dto;

public class ComumCongregacaoRequestDTO {

    private String nome;
    private Long setorId;

    public ComumCongregacaoRequestDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getSetorId() {
        return setorId;
    }

    public void setSetorId(Long setorId) {
        this.setorId = setorId;
    }
}