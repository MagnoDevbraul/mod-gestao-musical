package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.entity.Instrumento;
import br.com.mod.gestaomusical.repository.InstrumentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentoService {

    private final InstrumentoRepository repository;

    public InstrumentoService(InstrumentoRepository repository) {
        this.repository = repository;
    }

    public List<Instrumento> listarTodos() {
        return repository.findAll();
    }

    public Optional<Instrumento> buscarPorId(Long id) {
        return repository.findById(id);
    }
}