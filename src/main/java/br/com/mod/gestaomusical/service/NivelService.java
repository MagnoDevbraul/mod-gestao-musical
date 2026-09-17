package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.NivelRequestDTO;
import br.com.mod.gestaomusical.entity.Nivel;
import br.com.mod.gestaomusical.repository.NivelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class NivelService {

    private final NivelRepository repository;

    public NivelService(NivelRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Nivel> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Nivel> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Nivel salvar(NivelRequestDTO dto) {

        validar(dto);

        Nivel nivel = new Nivel();
        nivel.setNome(dto.getNome().trim());

        return repository.save(nivel);
    }

    @Transactional
    public Nivel atualizar(Long id, NivelRequestDTO dto) {

        validar(dto);

        Nivel nivel = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Nível não encontrado"
                ));

        nivel.setNome(dto.getNome().trim());

        return repository.save(nivel);
    }

    private void validar(NivelRequestDTO dto) {

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome do nível é obrigatório"
            );
        }
    }
}