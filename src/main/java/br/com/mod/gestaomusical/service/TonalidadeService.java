package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.entity.Tonalidade;
import br.com.mod.gestaomusical.repository.TonalidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TonalidadeService {

    private final TonalidadeRepository repository;

    public TonalidadeService(TonalidadeRepository repository) {
        this.repository = repository;
    }

    public List<Tonalidade> listarTodos() {
        return repository.findAll();
    }

    public Optional<Tonalidade> buscarPorId(Long id) {
        return repository.findById(id);
    }
}