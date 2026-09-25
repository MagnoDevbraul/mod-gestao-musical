package br.com.mod.gestaomusical.dto;

import java.time.LocalDateTime;

public class DashboardAtividadeResponseDTO {

    private Long id;
    private String tipoEvento;
    private String descricao;
    private LocalDateTime dataHora;

    public DashboardAtividadeResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}