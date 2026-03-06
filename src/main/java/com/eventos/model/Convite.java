package com.eventos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "convites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Convite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_convite", length = 50, nullable = false, unique = true)
    private String codigoConvite;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "enviado_em")
    private LocalDateTime enviadoEm;

    @Column(name = "convidado_id")
    private Integer convidadoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Evento evento;

    @PrePersist
    public void prePersist() {
        if (this.enviadoEm == null) {
            this.enviadoEm = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "PENDENTE";
        }
    }
}
