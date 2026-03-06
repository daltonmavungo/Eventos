package com.eventos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados do Pagamento")
public class PagamentoDTO {

    @Schema(description = "ID do pagamento", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Schema(description = "Valor do pagamento", example = "5000.00")
    private BigDecimal valor;

    @Schema(description = "Status do pagamento", example = "PENDENTE", allowableValues = {"PENDENTE","PAGO","CANCELADO","REEMBOLSADO"})
    private String status;

    @Size(max = 50)
    @Schema(description = "Método de pagamento", example = "TRANSFERENCIA")
    private String metodoPagamento;

    @Schema(description = "Data/hora do pagamento", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime pagoEm;

    @NotNull(message = "ID do evento é obrigatório")
    @Schema(description = "ID do evento", example = "1")
    private Integer eventoId;
}
