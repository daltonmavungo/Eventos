package com.eventos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Confirmacao representa a confirmação de presença de um convidado num evento.
 * É derivada do estado do Convite (status = CONFIRMADO).
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Confirmacao {

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "enviado_em")
    private LocalDateTime confirmadoEm;

    @Column(name = "convidado_id")
    private Integer convidadoId;
}
