package com.eventos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados do Convite")
public class ConviteDTO {

    @Schema(description = "ID do convite", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Código único do convite", accessMode = Schema.AccessMode.READ_ONLY)
    private String codigoConvite;

    @Schema(description = "Status do convite", example = "PENDENTE", allowableValues = {"PENDENTE","CONFIRMADO","CANCELADO","RECUSADO"})
    private String status;

    @Schema(description = "Data/hora de envio", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime enviadoEm;

    @NotNull(message = "ID do convidado é obrigatório")
    @Schema(description = "ID do convidado (usuario)", example = "2")
    private Integer convidadoId;

    @NotNull(message = "ID do evento é obrigatório")
    @Schema(description = "ID do evento", example = "1")
    private Integer eventoId;
}
