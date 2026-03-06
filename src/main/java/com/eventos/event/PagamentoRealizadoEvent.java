package com.eventos.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.math.BigDecimal;

@Getter
public class PagamentoRealizadoEvent extends ApplicationEvent {

    private final Integer pagamentoId;
    private final BigDecimal valor;
    private final String metodoPagamento;
    private final String tituloEvento;

    // Dados do organizador (destinatário do alerta)
    private final String emailOrganizador;
    private final String telefoneOrganizador;
    private final String nomeOrganizador;

    public PagamentoRealizadoEvent(Object source, Integer pagamentoId, BigDecimal valor,
                                    String metodoPagamento, String tituloEvento,
                                    String emailOrganizador, String telefoneOrganizador,
                                    String nomeOrganizador) {
        super(source);
        this.pagamentoId   = pagamentoId;
        this.valor         = valor;
        this.metodoPagamento = metodoPagamento;
        this.tituloEvento  = tituloEvento;
        this.emailOrganizador    = emailOrganizador;
        this.telefoneOrganizador = telefoneOrganizador;
        this.nomeOrganizador     = nomeOrganizador;
    }
}
