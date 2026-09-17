package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.dto.CargoMinisterioRequestDTO;
import br.com.mod.gestaomusical.entity.CargoMinisterio;
import br.com.mod.gestaomusical.repository.CargoMinisterioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CargoMinisterioService {

    private final CargoMinisterioRepository repository;

    public CargoMinisterioService(
            CargoMinisterioRepository repository) {

        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CargoMinisterio> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CargoMinisterio> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public CargoMinisterio salvar(
            CargoMinisterioRequestDTO dto) {

        validar(dto);

        CargoMinisterio cargo = new CargoMinisterio();
        cargo.setNome(dto.getNome().trim());

        return repository.save(cargo);
    }

    @Transactional
    public CargoMinisterio atualizar(
            Long id,
            CargoMinisterioRequestDTO dto) {

        validar(dto);

        CargoMinisterio cargo = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cargo/Ministério não encontrado"
                ));

        cargo.setNome(dto.getNome().trim());

        return repository.save(cargo);
    }

    private void validar(CargoMinisterioRequestDTO dto) {

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome do Cargo/Ministério é obrigatório"
            );
        }
    }
}