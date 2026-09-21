package br.com.mod.gestaomusical.dto;

public class SessaoAtualResponseDTO {

    private Long usuarioId;
    private String nome;
    private String email;
    private String perfil;
    private boolean online;

    public SessaoAtualResponseDTO() {
    }

    public SessaoAtualResponseDTO(
            Long usuarioId,
            String nome,
            String email,
            String perfil,
            boolean online) {

        this.usuarioId = usuarioId;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.online = online;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
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

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}