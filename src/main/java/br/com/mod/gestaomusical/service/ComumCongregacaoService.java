package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.ComumCongregacaoRequestDTO;
import br.com.mod.gestaomusical.entity.ComumCongregacao;
import br.com.mod.gestaomusical.repository.ComumCongregacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ComumCongregacaoService {

    private final ComumCongregacaoRepository repository;

    public ComumCongregacaoService(
            ComumCongregacaoRepository repository) {

        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ComumCongregacao> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ComumCongregacao> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ComumCongregacao salvar(
            ComumCongregacaoRequestDTO dto) {

        validar(dto);

        ComumCongregacao comum = new ComumCongregacao();

        comum.setNome(dto.getNome().trim());
        comum.setSetorId(dto.getSetorId());

        return repository.save(comum);
    }

    @Transactional
    public ComumCongregacao atualizar(
            Long id,
            ComumCongregacaoRequestDTO dto) {

        validar(dto);

        ComumCongregacao comum = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Comum não encontrada"
                ));

        comum.setNome(dto.getNome().trim());
        comum.setSetorId(dto.getSetorId());

        return repository.save(comum);
    }

    private void validar(ComumCongregacaoRequestDTO dto) {

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome da Comum é obrigatório"
            );
        }

        if (dto.getSetorId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Setor é obrigatório"
            );
        }
    }
}