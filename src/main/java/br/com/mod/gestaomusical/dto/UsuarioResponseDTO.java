package br.com.mod.gestaomusical.dto;

public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;

    private Long perfilUsuarioId;
    private String perfilUsuarioNome;

    private Boolean ativo;

    private Long comumId;
    private String comumNome;

    public UsuarioResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPerfilUsuarioId() {
        return perfilUsuarioId;
    }

    public void setPerfilUsuarioId(Long perfilUsuarioId) {
        this.perfilUsuarioId = perfilUsuarioId;
    }

    public String getPerfilUsuarioNome() {
        return perfilUsuarioNome;
    }

    public void setPerfilUsuarioNome(String perfilUsuarioNome) {
        this.perfilUsuarioNome = perfilUsuarioNome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getComumId() {
        return comumId;
    }

    public void setComumId(Long comumId) {
        this.comumId = comumId;
    }

    public String getComumNome() {
        return comumNome;
    }

    public void setComumNome(String comumNome) {
        this.comumNome = comumNome;
    }
}