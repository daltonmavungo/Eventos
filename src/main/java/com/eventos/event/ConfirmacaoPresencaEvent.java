package com.eventos.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConfirmacaoPresencaEvent extends ApplicationEvent {

    private final String codigoConvite;
    private final String status; // CONFIRMADO ou RECUSADO
    private final String tituloEvento;
    private final String nomeConvidado;

    // Organizador (recebe o alerta)
    private final Integer organizadorId;
    private final String emailOrganizador;
    private final String telefoneOrganizador;
    private final String nomeOrganizador;

    public ConfirmacaoPresencaEvent(Object source, String codigoConvite, String status,
                                     String tituloEvento, String nomeConvidado,
                                     Integer organizadorId, String emailOrganizador,
                                     String telefoneOrganizador, String nomeOrganizador) {
        super(source);
        this.codigoConvite       = codigoConvite;
        this.status              = status;
        this.tituloEvento        = tituloEvento;
        this.nomeConvidado       = nomeConvidado;
        this.organizadorId       = organizadorId;
        this.emailOrganizador    = emailOrganizador;
        this.telefoneOrganizador = telefoneOrganizador;
        this.nomeOrganizador     = nomeOrganizador;
    }
}
