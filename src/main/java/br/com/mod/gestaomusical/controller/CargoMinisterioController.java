package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.CargoMinisterioRequestDTO;
import br.com.mod.gestaomusical.entity.CargoMinisterio;
import br.com.mod.gestaomusical.service.CargoMinisterioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cargos-ministerio")
public class CargoMinisterioController {

    private final CargoMinisterioService service;

    public CargoMinisterioController(
            CargoMinisterioService service) {

        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CargoMinisterio>> listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargoMinisterio> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize(
            "hasAuthority('CARGO_MINISTERIO_CADASTRAR')"
    )
    public ResponseEntity<CargoMinisterio> salvar(
            @RequestBody CargoMinisterioRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize(
            "hasAuthority('CARGO_MINISTERIO_EDITAR')"
    )
    public ResponseEntity<CargoMinisterio> atualizar(
            @PathVariable Long id,
            @RequestBody CargoMinisterioRequestDTO dto) {

        return ResponseEntity.ok(
                service.atualizar(id, dto)
        );
    }
}