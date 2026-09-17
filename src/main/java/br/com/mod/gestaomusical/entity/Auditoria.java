package br.com.mod.gestaomusical.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "acao", nullable = false)
    private String acao;

    @Column(name = "tabela_afetada", nullable = false)
    private String tabelaAfetada;

    @Column(name = "registro_id")
    private Long registroId;

    @Column(name = "descricao")
    private String descricao;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dados_anteriores", columnDefinition = "jsonb")
    private Map<String, Object> dadosAnteriores;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dados_novos", columnDefinition = "jsonb")
    private Map<String, Object> dadosNovos;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public Auditoria() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    @PrePersist
    protected void aoCriar() {
        if (this.criadoEm == null) {
            this.criadoEm = LocalDateTime.now();
        }
    }
}