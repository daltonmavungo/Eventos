package com.eventos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Confirmação de presença num evento")
public class ConfirmacaoDTO {

    @NotBlank(message = "Código do convite é obrigatório")
    @Schema(description = "Código único do convite", example = "CONV-2025-ABCD1234")
    private String codigoConvite;

    @NotNull(message = "ID do convidado é obrigatório")
    @Schema(description = "ID do convidado", example = "2")
    private Integer convidadoId;

    @Schema(description = "Status da confirmação", example = "CONFIRMADO", allowableValues = {"CONFIRMADO","RECUSADO"})
    private String status;
}
