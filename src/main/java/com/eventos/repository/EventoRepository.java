package com.eventos.repository;

import com.eventos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {

    List<Evento> findByUsuarioId(Integer usuarioId);

    List<Evento> findByTipoEvento(String tipoEvento);

    List<Evento> findByDataEventoBetween(LocalDate inicio, LocalDate fim);

    @Query("SELECT e FROM Evento e WHERE e.titulo LIKE %:titulo%")
    List<Evento> findByTituloContaining(@Param("titulo") String titulo);
}
