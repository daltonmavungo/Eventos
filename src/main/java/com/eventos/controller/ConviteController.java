package com.eventos.controller;

import com.eventos.dto.ConfirmacaoDTO;
import com.eventos.dto.ConviteDTO;
import com.eventos.service.ConviteService;
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
@RequestMapping("/api/convites")
@RequiredArgsConstructor
@Tag(name = "Convites", description = "Gestão de convites e confirmações")
public class ConviteController {

    private final ConviteService conviteService;

    @GetMapping
    @Operation(summary = "Listar todos os convites")
    public ResponseEntity<List<ConviteDTO>> listarTodos() {
        return ResponseEntity.ok(conviteService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar convite por ID")
    public ResponseEntity<ConviteDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(conviteService.buscarPorId(id));
    }

    @GetMapping("/evento/{eventoId}")
    @Operation(summary = "Listar convites por evento")
    public ResponseEntity<List<ConviteDTO>> listarPorEvento(@PathVariable Integer eventoId) {
        return ResponseEntity.ok(conviteService.listarPorEvento(eventoId));
    }

    @GetMapping("/convidado/{convidadoId}")
    @Operation(summary = "Listar convites por convidado")
    public ResponseEntity<List<ConviteDTO>> listarPorConvidado(@PathVariable Integer convidadoId) {
        return ResponseEntity.ok(conviteService.listarPorConvidado(convidadoId));
    }

    @PostMapping
    @Operation(summary = "Enviar novo convite")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Convite enviado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<ConviteDTO> enviar(@Valid @RequestBody ConviteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(conviteService.enviar(dto));
    }

    @PostMapping("/confirmar")
    @Operation(summary = "Confirmar ou recusar presença via código de convite")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Confirmação registada"),
        @ApiResponse(responseCode = "404", description = "Convite não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ConviteDTO> confirmar(@Valid @RequestBody ConfirmacaoDTO confirmacaoDTO) {
        return ResponseEntity.ok(conviteService.confirmar(confirmacaoDTO));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar convite")
    @ApiResponse(responseCode = "204", description = "Convite cancelado")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        conviteService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
