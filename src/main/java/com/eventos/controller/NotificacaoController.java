package com.eventos.controller;

import com.eventos.notification.SseNotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Map;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
@Tag(name = "Notificações", description = "Alertas em tempo real via SSE")
public class NotificacaoController {

    private final SseNotificacaoService sseService;

    /**
     * Frontend conecta assim:
     *   const es = new EventSource('/api/notificacoes/stream/1');
     *   es.addEventListener('PAGAMENTO', e => alert(JSON.parse(e.data).mensagem));
     *   es.addEventListener('EVENTO_CRIADO', e => alert(JSON.parse(e.data).mensagem));
     *   es.addEventListener('CONVITE', e => alert(JSON.parse(e.data).mensagem));
     *   es.addEventListener('CONFIRMACAO', e => alert(JSON.parse(e.data).mensagem));
     */
    @GetMapping(value = "/stream/{usuarioId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Ligar ao stream SSE de notificações em tempo real",
               description = "Abre conexão SSE. Eventos: PAGAMENTO | EVENTO_CRIADO | CONVITE | CONFIRMACAO")
    public SseEmitter stream(@PathVariable Integer usuarioId) {
        return sseService.conectar(usuarioId);
    }

    @GetMapping("/status")
    @Operation(summary = "Utilizadores conectados ao SSE")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(Map.of(
            "conectados", sseService.totalConectados(),
            "estado",     "online"
        ));
    }
}
