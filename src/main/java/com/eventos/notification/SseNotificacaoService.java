package com.eventos.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-Sent Events — notificações em tempo real no browser.
 *
 * O frontend conecta-se assim:
 *   const es = new EventSource('/api/notificacoes/stream/1'); // 1 = usuarioId
 *   es.addEventListener('PAGAMENTO',      e => mostrarAlerta(e.data));
 *   es.addEventListener('EVENTO_CRIADO',  e => mostrarAlerta(e.data));
 *   es.addEventListener('CONVITE',        e => mostrarAlerta(e.data));
 *   es.addEventListener('CONFIRMACAO',    e => mostrarAlerta(e.data));
 */
@Service
@Slf4j
public class SseNotificacaoService {

    private final Map<Integer, SseEmitter> conexoes = new ConcurrentHashMap<>();

    public SseEmitter conectar(Integer usuarioId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitter.onCompletion(() -> { conexoes.remove(usuarioId); log.info("SSE desconectado: {}", usuarioId); });
        emitter.onTimeout(   () -> { conexoes.remove(usuarioId); log.info("SSE timeout: {}", usuarioId); });
        emitter.onError(   e -> { conexoes.remove(usuarioId); log.warn("SSE erro: {} — {}", usuarioId, e.getMessage()); });
        conexoes.put(usuarioId, emitter);
        log.info("SSE conectado: {} | total activo: {}", usuarioId, conexoes.size());
        push(usuarioId, "LIGADO", Map.of("mensagem", "Ligação em tempo real estabelecida."));
        return emitter;
    }

    public void alertarPagamento(Integer usuarioId, String tituloEvento, String valor) {
        push(usuarioId, "PAGAMENTO", Map.of(
            "titulo",    "Pagamento Recebido",
            "mensagem",  "Novo pagamento no evento: " + tituloEvento,
            "valor",     "AOA " + valor,
            "timestamp", agora()
        ));
    }

    public void alertarEventoCriado(Integer usuarioId, String tituloEvento) {
        push(usuarioId, "EVENTO_CRIADO", Map.of(
            "titulo",    "Evento Criado",
            "mensagem",  "O teu evento foi criado: " + tituloEvento,
            "timestamp", agora()
        ));
    }

    public void alertarConvite(Integer organizadorId, String nomeConvidado, String tituloEvento) {
        push(organizadorId, "CONVITE", Map.of(
            "titulo",    "Convite Enviado",
            "mensagem",  "Convite enviado a " + nomeConvidado + " para: " + tituloEvento,
            "timestamp", agora()
        ));
    }

    public void alertarConfirmacao(Integer organizadorId, String nomeConvidado,
                                    String tituloEvento, String status) {
        boolean ok = "CONFIRMADO".equalsIgnoreCase(status);
        push(organizadorId, "CONFIRMACAO", Map.of(
            "titulo",    ok ? "Presença Confirmada" : "Presença Recusada",
            "mensagem",  nomeConvidado + (ok ? " confirmou" : " recusou") + " presença em: " + tituloEvento,
            "status",    status,
            "timestamp", agora()
        ));
    }

    public int totalConectados() { return conexoes.size(); }

    private void push(Integer usuarioId, String tipo, Object dados) {
        SseEmitter em = conexoes.get(usuarioId);
        if (em == null) return;
        try {
            em.send(SseEmitter.event().name(tipo).data(dados));
            log.info("SSE [{}] → utilizador {}", tipo, usuarioId);
        } catch (IOException e) {
            conexoes.remove(usuarioId);
        }
    }

    private String agora() {
        return LocalDateTime.now().toString().replace("T", " ").substring(0, 19);
    }
}
