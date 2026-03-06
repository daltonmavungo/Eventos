package com.eventos.service;

import com.eventos.dto.EventoDTO;
import com.eventos.event.EventoCriadoEvent;
import com.eventos.model.Evento;
import com.eventos.model.Usuario;
import com.eventos.repository.EventoRepository;
import com.eventos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository       eventoRepository;
    private final UsuarioRepository      usuarioRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional(readOnly = true)
    public List<EventoDTO> listarTodos() {
        return eventoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventoDTO buscarPorId(Integer id) {
        return toDTO(eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado: " + id)));
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> listarPorUsuario(Integer usuarioId) {
        return eventoRepository.findByUsuarioId(usuarioId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public EventoDTO criar(EventoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Utilizador não encontrado: " + dto.getUsuarioId()));

        Evento evento = toEntity(dto, usuario);
        evento = eventoRepository.save(evento);

        // 🔔 Disparar notificações (email + SMS + SSE)
        String data = evento.getDataEvento() != null ? evento.getDataEvento().toString() : "N/D";
        publisher.publishEvent(new EventoCriadoEvent(
            this,
            evento.getId(),
            evento.getTitulo(),
            evento.getLocal() != null ? evento.getLocal() : "N/D",
            data,
            usuario.getEmail(),
            usuario.getTelefone(),
            usuario.getNome()
        ));

        return toDTO(evento);
    }

    @Transactional
    public EventoDTO atualizar(Integer id, EventoDTO dto) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado: " + id));
        evento.setTitulo(dto.getTitulo());
        evento.setDescricao(dto.getDescricao());
        evento.setLocal(dto.getLocal());
        evento.setDataEvento(dto.getDataEvento());
        evento.setHoraEvento(dto.getHoraEvento());
        evento.setCapacidade(dto.getCapacidade());
        evento.setTipoEvento(dto.getTipoEvento());
        return toDTO(eventoRepository.save(evento));
    }

    @Transactional
    public void deletar(Integer id) {
        if (!eventoRepository.existsById(id))
            throw new EntityNotFoundException("Evento não encontrado: " + id);
        eventoRepository.deleteById(id);
    }

    private EventoDTO toDTO(Evento e) {
        return EventoDTO.builder()
                .id(e.getId()).titulo(e.getTitulo()).descricao(e.getDescricao())
                .local(e.getLocal()).dataEvento(e.getDataEvento()).horaEvento(e.getHoraEvento())
                .capacidade(e.getCapacidade()).tipoEvento(e.getTipoEvento())
                .usuarioId(e.getUsuario().getId()).criadoEm(e.getCriadoEm())
                .build();
    }

    private Evento toEntity(EventoDTO dto, Usuario usuario) {
        return Evento.builder()
                .titulo(dto.getTitulo()).descricao(dto.getDescricao()).local(dto.getLocal())
                .dataEvento(dto.getDataEvento()).horaEvento(dto.getHoraEvento())
                .capacidade(dto.getCapacidade()).tipoEvento(dto.getTipoEvento())
                .usuario(usuario).build();
    }
}
