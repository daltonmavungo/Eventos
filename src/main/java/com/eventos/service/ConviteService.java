package com.eventos.service;

import com.eventos.dto.ConfirmacaoDTO;
import com.eventos.dto.ConviteDTO;
import com.eventos.event.ConfirmacaoPresencaEvent;
import com.eventos.event.ConviteEnviadoEvent;
import com.eventos.model.Convite;
import com.eventos.model.Evento;
import com.eventos.model.Usuario;
import com.eventos.repository.ConviteRepository;
import com.eventos.repository.EventoRepository;
import com.eventos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConviteService {

    private final ConviteRepository      conviteRepository;
    private final EventoRepository       eventoRepository;
    private final UsuarioRepository      usuarioRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional(readOnly = true)
    public List<ConviteDTO> listarTodos() {
        return conviteRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConviteDTO buscarPorId(Integer id) {
        return toDTO(conviteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Convite não encontrado: " + id)));
    }

    @Transactional(readOnly = true)
    public List<ConviteDTO> listarPorEvento(Integer eventoId) {
        return conviteRepository.findByEventoId(eventoId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConviteDTO> listarPorConvidado(Integer convidadoId) {
        return conviteRepository.findByConvidadoId(convidadoId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ConviteDTO enviar(ConviteDTO dto) {
        Evento evento = eventoRepository.findById(dto.getEventoId())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado: " + dto.getEventoId()));

        Usuario convidado = usuarioRepository.findById(dto.getConvidadoId())
                .orElseThrow(() -> new EntityNotFoundException("Convidado não encontrado: " + dto.getConvidadoId()));

        String codigo = gerarCodigoUnico();
        Convite convite = Convite.builder()
                .codigoConvite(codigo).convidadoId(convidado.getId())
                .evento(evento).status("PENDENTE").build();
        convite = conviteRepository.save(convite);

        // 🔔 Disparar notificações
        Usuario organizador = evento.getUsuario();
        String dataEvento = evento.getDataEvento() != null ? evento.getDataEvento().toString() : "N/D";

        publisher.publishEvent(new ConviteEnviadoEvent(
            this, codigo, evento.getTitulo(), dataEvento,
            convidado.getEmail(), convidado.getTelefone(), convidado.getNome(),
            organizador.getId(), organizador.getEmail(), organizador.getTelefone(), organizador.getNome()
        ));

        return toDTO(convite);
    }

    @Transactional
    public ConviteDTO confirmar(ConfirmacaoDTO dto) {
        Convite convite = conviteRepository.findByCodigoConvite(dto.getCodigoConvite())
                .orElseThrow(() -> new EntityNotFoundException("Convite não encontrado: " + dto.getCodigoConvite()));

        if (!convite.getConvidadoId().equals(dto.getConvidadoId()))
            throw new IllegalArgumentException("Convidado não corresponde ao convite.");

        String novoStatus = dto.getStatus() != null ? dto.getStatus() : "CONFIRMADO";
        convite.setStatus(novoStatus);
        convite = conviteRepository.save(convite);

        // 🔔 Disparar notificações ao organizador
        Usuario organizador  = convite.getEvento().getUsuario();
        Usuario convidadoObj = usuarioRepository.findById(convite.getConvidadoId()).orElse(null);
        String nomeConvidado = convidadoObj != null ? convidadoObj.getNome() : "Convidado";

        publisher.publishEvent(new ConfirmacaoPresencaEvent(
            this, dto.getCodigoConvite(), novoStatus,
            convite.getEvento().getTitulo(), nomeConvidado,
            organizador.getId(), organizador.getEmail(),
            organizador.getTelefone(), organizador.getNome()
        ));

        return toDTO(convite);
    }

    @Transactional
    public void cancelar(Integer id) {
        Convite c = conviteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Convite não encontrado: " + id));
        c.setStatus("CANCELADO");
        conviteRepository.save(c);
    }

    private String gerarCodigoUnico() {
        String codigo;
        do { codigo = "CONV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(); }
        while (conviteRepository.existsByCodigoConvite(codigo));
        return codigo;
    }

    private ConviteDTO toDTO(Convite c) {
        return ConviteDTO.builder()
                .id(c.getId()).codigoConvite(c.getCodigoConvite()).status(c.getStatus())
                .enviadoEm(c.getEnviadoEm()).convidadoId(c.getConvidadoId())
                .eventoId(c.getEvento().getId()).build();
    }
}
