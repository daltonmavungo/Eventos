package com.eventos.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConviteEnviadoEvent extends ApplicationEvent {

    private final String codigoConvite;
    private final String tituloEvento;
    private final String dataEvento;

    // Convidado (quem recebe o convite)
    private final String emailConvidado;
    private final String telefoneConvidado;
    private final String nomeConvidado;

    // Organizador do evento (quem criou o evento)
    private final Integer organizadorId;
    private final String emailOrganizador;
    private final String telefoneOrganizador;
    private final String nomeOrganizador;

    public ConviteEnviadoEvent(Object source, String codigoConvite, String tituloEvento,
                                String dataEvento,
                                String emailConvidado, String telefoneConvidado, String nomeConvidado,
                                Integer organizadorId, String emailOrganizador,
                                String telefoneOrganizador, String nomeOrganizador) {
        super(source);
        this.codigoConvite       = codigoConvite;
        this.tituloEvento        = tituloEvento;
        this.dataEvento          = dataEvento;
        this.emailConvidado      = emailConvidado;
        this.telefoneConvidado   = telefoneConvidado;
        this.nomeConvidado       = nomeConvidado;
        this.organizadorId       = organizadorId;
        this.emailOrganizador    = emailOrganizador;
        this.telefoneOrganizador = telefoneOrganizador;
        this.nomeOrganizador     = nomeOrganizador;
    }
}
