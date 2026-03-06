package com.eventos.listener;

import com.eventos.event.*;
import com.eventos.notification.EmailNotificacaoService;
import com.eventos.notification.SseNotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificacaoListener {

    private final EmailNotificacaoService email;
    private final SseNotificacaoService   sse;

    @Async("notificacaoExecutor")
    @EventListener
    public void onPagamento(PagamentoRealizadoEvent e) {
        log.info("🔔 Pagamento realizado | evento={}", e.getTituloEvento());
        email.alertarPagamento(
                e.getEmailOrganizador(), e.getNomeOrganizador(),
                e.getTituloEvento(), e.getValor(),
                e.getMetodoPagamento(), e.getPagamentoId()
        );
    }

    @Async("notificacaoExecutor")
    @EventListener
    public void onEventoCriado(EventoCriadoEvent e) {
        log.info("🔔 Evento criado | titulo={}", e.getTitulo());
        email.alertarEventoCriado(
                e.getEmailOrganizador(), e.getNomeOrganizador(),
                e.getTitulo(), e.getLocal(), e.getDataEvento()
        );
        sse.alertarEventoCriado(e.getEventoId(), e.getTitulo());
    }

    @Async("notificacaoExecutor")
    @EventListener
    public void onConviteEnviado(ConviteEnviadoEvent e) {
        log.info("🔔 Convite enviado | codigo={}", e.getCodigoConvite());
        email.alertarConviteEnviado(
                e.getEmailConvidado(), e.getNomeConvidado(),
                e.getTituloEvento(), e.getDataEvento(),
                e.getCodigoConvite()
        );
        sse.alertarConvite(e.getOrganizadorId(), e.getNomeConvidado(), e.getTituloEvento());
    }

    @Async("notificacaoExecutor")
    @EventListener
    public void onConfirmacao(ConfirmacaoPresencaEvent e) {
        log.info("🔔 Confirmação | status={}", e.getStatus());
        email.alertarConfirmacao(
                e.getEmailOrganizador(), e.getNomeOrganizador(),
                e.getNomeConvidado(), e.getTituloEvento(), e.getStatus()
        );
        sse.alertarConfirmacao(
                e.getOrganizadorId(), e.getNomeConvidado(),
                e.getTituloEvento(), e.getStatus()
        );
    }
}