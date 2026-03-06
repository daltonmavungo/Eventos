package com.eventos.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventoCriadoEvent extends ApplicationEvent {

    private final Integer eventoId;
    private final String titulo;
    private final String local;
    private final String dataEvento;

    // Organizador
    private final String emailOrganizador;
    private final String telefoneOrganizador;
    private final String nomeOrganizador;

    public EventoCriadoEvent(Object source, Integer eventoId, String titulo,
                              String local, String dataEvento,
                              String emailOrganizador, String telefoneOrganizador,
                              String nomeOrganizador) {
        super(source);
        this.eventoId  = eventoId;
        this.titulo    = titulo;
        this.local     = local;
        this.dataEvento = dataEvento;
        this.emailOrganizador    = emailOrganizador;
        this.telefoneOrganizador = telefoneOrganizador;
        this.nomeOrganizador     = nomeOrganizador;
    }
}
