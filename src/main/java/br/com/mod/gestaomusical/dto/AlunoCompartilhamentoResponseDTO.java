package br.com.mod.gestaomusical.dto;

import java.time.LocalDateTime;

public class AlunoCompartilhamentoResponseDTO {

    private Long id;

    private Long alunoId;
    private String alunoNome;

    private Long comumOrigemId;
    private String comumOrigemNome;

    private Long comumDestinoId;
    private String comumDestinoNome;

    private Long compartilhadoPorUsuarioId;
    private String compartilhadoPorUsuarioNome;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public AlunoCompartilhamentoResponseDTO() {
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

    public Long getComumOrigemId() {
        return comumOrigemId;
    }

    public void setComumOrigemId(Long comumOrigemId) {
        this.comumOrigemId = comumOrigemId;
    }

    public String getComumOrigemNome() {
        return comumOrigemNome;
    }

    public void setComumOrigemNome(String comumOrigemNome) {
        this.comumOrigemNome = comumOrigemNome;
    }

    public Long getComumDestinoId() {
        return comumDestinoId;
    }

    public void setComumDestinoId(Long comumDestinoId) {
        this.comumDestinoId = comumDestinoId;
    }

    public String getComumDestinoNome() {
        return comumDestinoNome;
    }

    public void setComumDestinoNome(String comumDestinoNome) {
        this.comumDestinoNome = comumDestinoNome;
    }

    public Long getCompartilhadoPorUsuarioId() {
        return compartilhadoPorUsuarioId;
    }

    public void setCompartilhadoPorUsuarioId(Long compartilhadoPorUsuarioId) {
        this.compartilhadoPorUsuarioId = compartilhadoPorUsuarioId;
    }

    public String getCompartilhadoPorUsuarioNome() {
        return compartilhadoPorUsuarioNome;
    }

    public void setCompartilhadoPorUsuarioNome(String compartilhadoPorUsuarioNome) {
        this.compartilhadoPorUsuarioNome = compartilhadoPorUsuarioNome;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}