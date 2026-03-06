package com.eventos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados do Evento")
public class EventoDTO {

    @Schema(description = "ID do evento", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 150)
    @Schema(description = "Título do evento", example = "Conferência de Tecnologia 2025")
    private String titulo;

    @Schema(description = "Descrição do evento")
    private String descricao;

    @Size(max = 200)
    @Schema(description = "Local do evento", example = "Centro de Convenções, Luanda")
    private String local;

    @NotNull(message = "Data do evento é obrigatória")
    @Schema(description = "Data do evento", example = "2025-12-01")
    private LocalDate dataEvento;

    @Schema(description = "Hora do evento", example = "09:00:00")
    private LocalTime horaEvento;

    @Positive(message = "Capacidade deve ser positiva")
    @Schema(description = "Capacidade máxima", example = "200")
    private Integer capacidade;

    @Size(max = 50)
    @Schema(description = "Tipo do evento", example = "CONFERENCIA")
    private String tipoEvento;

    @NotNull(message = "ID do utilizador é obrigatório")
    @Schema(description = "ID do organizador", example = "1")
    private Integer usuarioId;

    @Schema(description = "Data de criação", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime criadoEm;
}
