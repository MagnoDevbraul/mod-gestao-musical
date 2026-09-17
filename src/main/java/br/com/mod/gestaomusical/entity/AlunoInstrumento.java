package br.com.mod.gestaomusical.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "aluno_instrumento")
public class AlunoInstrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrumento_id", nullable = false)
    private Instrumento instrumento;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tonalidade_id", nullable = false)
    private Tonalidade tonalidade;

    public AlunoInstrumento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Instrumento getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Tonalidade getTonalidade() {
        return tonalidade;
    }

    public void setTonalidade(Tonalidade tonalidade) {
        this.tonalidade = tonalidade;
    }
}