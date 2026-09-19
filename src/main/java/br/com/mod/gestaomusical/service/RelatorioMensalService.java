package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.RelatorioMensalResponseDTO;
import br.com.mod.gestaomusical.entity.RelatorioMensal;
import br.com.mod.gestaomusical.repository.RelatorioMensalCalculoRepository;
import br.com.mod.gestaomusical.repository.RelatorioMensalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
public class RelatorioMensalService {

    private final RelatorioMensalRepository relatorioMensalRepository;
    private final RelatorioMensalCalculoRepository calculoRepository;

    public RelatorioMensalService(
            RelatorioMensalRepository relatorioMensalRepository,
            RelatorioMensalCalculoRepository calculoRepository) {

        this.relatorioMensalRepository = relatorioMensalRepository;
        this.calculoRepository = calculoRepository;
    }

    @Transactional
    public RelatorioMensalResponseDTO gerarRelatorio(int ano, int mes) {

        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException(
                    "Mês inválido. Informe um valor entre 1 e 12."
            );
        }

        YearMonth anoMes = YearMonth.of(ano, mes);

        LocalDate periodoInicio = anoMes.atDay(1);
        LocalDate periodoFim = anoMes.atEndOfMonth();

        Object[] totais =
                calculoRepository.calcular(periodoInicio, periodoFim);

        RelatorioMensal relatorio =
                relatorioMensalRepository
                        .findByPeriodoInicioAndPeriodoFim(
                                periodoInicio,
                                periodoFim
                        )
                        .orElseGet(RelatorioMensal::new);

        relatorio.setPeriodoInicio(periodoInicio);
        relatorio.setPeriodoFim(periodoFim);

        relatorio.setTotalAlunosAtivos(
                paraInteger(totais[0])
        );

        relatorio.setTotalAlunosArquivados(
                paraInteger(totais[1])
        );

        relatorio.setTotalMts(
                paraInteger(totais[2])
        );

        relatorio.setTotalMsa(
                paraInteger(totais[3])
        );

        relatorio.setTotalMetodo(
                paraInteger(totais[4])
        );

        relatorio.setTotalHinario(
                paraInteger(totais[5])
        );

        relatorio.setTotalEscala(
                paraInteger(totais[6])
        );

        relatorio.setTotalAlunosSemMovimentacao60Dias(
                paraInteger(totais[7])
        );

        relatorio.setTotalCompartilhamentos(
                paraInteger(totais[8])
        );

        relatorio.setTotalExclusoesArquivamentos(
                paraInteger(totais[9])
        );

        relatorio.setTotalRestauracoes(
                paraInteger(totais[10])
        );

        relatorio.setTotalNotificacoes(
                paraInteger(totais[11])
        );

        relatorio.setTotalEventosAuditoria(
                paraInteger(totais[12])
        );

        // Atualiza a data/hora sempre que o relatório for gerado ou regerado
        relatorio.setGeradoEm(LocalDateTime.now());

        RelatorioMensal salvo =
                relatorioMensalRepository.save(relatorio);

        return converterParaDTO(salvo);
    }

    @Transactional(readOnly = true)
    public RelatorioMensalResponseDTO buscarPorPeriodo(
            int ano,
            int mes) {

        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException(
                    "Mês inválido. Informe um valor entre 1 e 12."
            );
        }

        YearMonth anoMes = YearMonth.of(ano, mes);

        LocalDate periodoInicio = anoMes.atDay(1);
        LocalDate periodoFim = anoMes.atEndOfMonth();

        RelatorioMensal relatorio =
                relatorioMensalRepository
                        .findByPeriodoInicioAndPeriodoFim(
                                periodoInicio,
                                periodoFim
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Relatório mensal não encontrado para o período informado."
                                )
                        );

        return converterParaDTO(relatorio);
    }

    private Integer paraInteger(Object valor) {

        if (valor == null) {
            return 0;
        }

        if (valor instanceof Number numero) {
            return numero.intValue();
        }

        throw new IllegalArgumentException(
                "Tipo numérico não suportado: "
                        + valor.getClass().getName()
        );
    }

    private RelatorioMensalResponseDTO converterParaDTO(
            RelatorioMensal relatorio) {

        RelatorioMensalResponseDTO dto =
                new RelatorioMensalResponseDTO();

        dto.setId(relatorio.getId());

        dto.setPeriodoInicio(
                relatorio.getPeriodoInicio()
        );

        dto.setPeriodoFim(
                relatorio.getPeriodoFim()
        );

        dto.setTotalAlunosAtivos(
                relatorio.getTotalAlunosAtivos()
        );

        dto.setTotalAlunosArquivados(
                relatorio.getTotalAlunosArquivados()
        );

        dto.setTotalMts(
                relatorio.getTotalMts()
        );

        dto.setTotalMsa(
                relatorio.getTotalMsa()
        );

        dto.setTotalMetodo(
                relatorio.getTotalMetodo()
        );

        dto.setTotalHinario(
                relatorio.getTotalHinario()
        );

        dto.setTotalEscala(
                relatorio.getTotalEscala()
        );

        dto.setTotalAlunosSemMovimentacao60Dias(
                relatorio.getTotalAlunosSemMovimentacao60Dias()
        );

        dto.setTotalCompartilhamentos(
                relatorio.getTotalCompartilhamentos()
        );

        dto.setTotalExclusoesArquivamentos(
                relatorio.getTotalExclusoesArquivamentos()
        );

        dto.setTotalRestauracoes(
                relatorio.getTotalRestauracoes()
        );

        dto.setTotalNotificacoes(
                relatorio.getTotalNotificacoes()
        );

        dto.setTotalEventosAuditoria(
                relatorio.getTotalEventosAuditoria()
        );

        dto.setGeradoEm(
                relatorio.getGeradoEm()
        );

        return dto;
    }
}