package com.eventos.controller;

import com.eventos.dto.PagamentoDTO;
import com.eventos.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "Gestão de pagamentos de eventos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @GetMapping
    @Operation(summary = "Listar todos os pagamentos")
    public ResponseEntity<List<PagamentoDTO>> listarTodos() {
        return ResponseEntity.ok(pagamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pagamento por ID")
    public ResponseEntity<PagamentoDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pagamentoService.buscarPorId(id));
    }

    @GetMapping("/evento/{eventoId}")
    @Operation(summary = "Listar pagamentos por evento")
    public ResponseEntity<List<PagamentoDTO>> listarPorEvento(@PathVariable Integer eventoId) {
        return ResponseEntity.ok(pagamentoService.listarPorEvento(eventoId));
    }

    @GetMapping("/evento/{eventoId}/total")
    @Operation(summary = "Total pago por evento")
    @ApiResponse(responseCode = "200", description = "Total calculado com sucesso")
    public ResponseEntity<BigDecimal> totalPagoPorEvento(@PathVariable Integer eventoId) {
        return ResponseEntity.ok(pagamentoService.totalPagoPorEvento(eventoId));
    }

    @PostMapping
    @Operation(summary = "Registar novo pagamento")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pagamento registado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<PagamentoDTO> registar(@Valid @RequestBody PagamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.registar(dto));
    }

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar pagamento")
    @ApiResponse(responseCode = "200", description = "Pagamento confirmado")
    public ResponseEntity<PagamentoDTO> confirmar(@PathVariable Integer id) {
        return ResponseEntity.ok(pagamentoService.confirmarPagamento(id));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar pagamento")
    @ApiResponse(responseCode = "200", description = "Pagamento cancelado")
    public ResponseEntity<PagamentoDTO> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(pagamentoService.cancelar(id));
    }
}
