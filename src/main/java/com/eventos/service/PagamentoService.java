package com.eventos.service;

import com.eventos.dto.PagamentoDTO;
import com.eventos.event.PagamentoRealizadoEvent;
import com.eventos.model.Evento;
import com.eventos.model.Pagamento;
import com.eventos.model.Usuario;
import com.eventos.repository.EventoRepository;
import com.eventos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository    pagamentoRepository;
    private final EventoRepository       eventoRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional(readOnly = true)
    public List<PagamentoDTO> listarTodos() {
        return pagamentoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagamentoDTO buscarPorId(Integer id) {
        return toDTO(pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado: " + id)));
    }

    @Transactional(readOnly = true)
    public List<PagamentoDTO> listarPorEvento(Integer eventoId) {
        return pagamentoRepository.findByEventoId(eventoId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal totalPagoPorEvento(Integer eventoId) {
        BigDecimal total = pagamentoRepository.sumValorPagoByEventoId(eventoId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional
    public PagamentoDTO registar(PagamentoDTO dto) {
        Evento evento = eventoRepository.findById(dto.getEventoId())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado: " + dto.getEventoId()));
        Pagamento pagamento = Pagamento.builder()
                .valor(dto.getValor()).status("PENDENTE")
                .metodoPagamento(dto.getMetodoPagamento()).evento(evento).build();
        return toDTO(pagamentoRepository.save(pagamento));
    }

    @Transactional
    public PagamentoDTO confirmarPagamento(Integer id) {
        Pagamento p = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado: " + id));
        p.setStatus("PAGO");
        p.setPagoEm(LocalDateTime.now());
        p = pagamentoRepository.save(p);

        // 🔔 Disparar notificações ao organizador do evento
        Usuario org = p.getEvento().getUsuario();
        publisher.publishEvent(new PagamentoRealizadoEvent(
            this, p.getId(), p.getValor(),
            p.getMetodoPagamento() != null ? p.getMetodoPagamento() : "N/D",
            p.getEvento().getTitulo(),
            org.getEmail(), org.getTelefone(), org.getNome()
        ));

        return toDTO(p);
    }

    @Transactional
    public PagamentoDTO cancelar(Integer id) {
        Pagamento p = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado: " + id));
        p.setStatus("CANCELADO");
        return toDTO(pagamentoRepository.save(p));
    }

    private PagamentoDTO toDTO(Pagamento p) {
        return PagamentoDTO.builder()
                .id(p.getId()).valor(p.getValor()).status(p.getStatus())
                .metodoPagamento(p.getMetodoPagamento()).pagoEm(p.getPagoEm())
                .eventoId(p.getEvento().getId()).build();
    }
}
