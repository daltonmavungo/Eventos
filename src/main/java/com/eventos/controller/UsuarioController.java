package com.eventos.controller;

import com.eventos.dto.UsuarioDTO;
import com.eventos.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Utilizadores", description = "Gestão de utilizadores do sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos os utilizadores")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar utilizador por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utilizador encontrado"),
        @ApiResponse(responseCode = "404", description = "Utilizador não encontrado")
    })
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo utilizador")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Utilizador criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar utilizador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utilizador atualizado"),
        @ApiResponse(responseCode = "404", description = "Utilizador não encontrado")
    })
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Integer id,
                                                 @Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar utilizador")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Utilizador eliminado"),
        @ApiResponse(responseCode = "404", description = "Utilizador não encontrado")
    })
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
