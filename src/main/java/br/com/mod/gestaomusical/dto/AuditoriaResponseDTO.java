package br.com.mod.gestaomusical.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class AuditoriaResponseDTO {

    private Long id;

    private Long usuarioId;
    private String usuarioNome;

    private String acao;
    private String tabelaAfetada;
    private Long registroId;
    private String descricao;

    private Map<String, Object> dadosAnteriores;
    private Map<String, Object> dadosNovos;

    private LocalDateTime criadoEm;

    public AuditoriaResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getTabelaAfetada() {
        return tabelaAfetada;
    }

    public void setTabelaAfetada(String tabelaAfetada) {
        this.tabelaAfetada = tabelaAfetada;
    }

    public Long getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Long registroId) {
        this.registroId = registroId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Map<String, Object> getDadosAnteriores() {
        return dadosAnteriores;
    }

    public void setDadosAnteriores(Map<String, Object> dadosAnteriores) {
        this.dadosAnteriores = dadosAnteriores;
    }

    public Map<String, Object> getDadosNovos() {
        return dadosNovos;
    }

    public void setDadosNovos(Map<String, Object> dadosNovos) {
        this.dadosNovos = dadosNovos;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}