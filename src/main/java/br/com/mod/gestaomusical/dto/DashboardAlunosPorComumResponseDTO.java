package br.com.mod.gestaomusical.dto;

public class DashboardAlunosPorComumResponseDTO {

    private String comum;
    private long quantidade;

    public DashboardAlunosPorComumResponseDTO() {
    }

    public DashboardAlunosPorComumResponseDTO(
            String comum,
            long quantidade) {

        this.comum = comum;
        this.quantidade = quantidade;
    }

    public String getComum() {
        return comum;
    }

    public void setComum(String comum) {
        this.comum = comum;
    }

    public long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }
}