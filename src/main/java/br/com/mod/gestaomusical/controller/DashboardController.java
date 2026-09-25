package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.DashboardResponseDTO;
import br.com.mod.gestaomusical.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(
            DashboardService service) {

        this.service = service;
    }

    @GetMapping("/secretaria")
    @PreAuthorize("hasAuthority('AUDITORIA_CONSULTAR')")
    public ResponseEntity<DashboardResponseDTO>
    obterDashboardSecretaria() {

        return ResponseEntity.ok(
                service.obterDashboardSecretaria()
        );
    }
}