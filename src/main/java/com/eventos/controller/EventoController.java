package com.eventos.controller;

import com.eventos.dto.EventoDTO;
import com.eventos.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Gestão de eventos")
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    @Operation(summary = "Listar todos os eventos")
    public ResponseEntity<List<EventoDTO>> listarTodos() {
        return ResponseEntity.ok(eventoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento encontrado"),
        @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<EventoDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(eventoService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar eventos por utilizador")
    public ResponseEntity<List<EventoDTO>> listarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(eventoService.listarPorUsuario(usuarioId));
    }

    @PostMapping
    @Operation(summary = "Criar novo evento")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Evento criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Utilizador não encontrado")
    })
    public ResponseEntity<EventoDTO> criar(@Valid @RequestBody EventoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evento")
    public ResponseEntity<EventoDTO> atualizar(@PathVariable Integer id,
                                                @Valid @RequestBody EventoDTO dto) {
        return ResponseEntity.ok(eventoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar evento")
    @ApiResponse(responseCode = "204", description = "Evento eliminado")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        eventoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
