package com.eventos.repository;

import com.eventos.model.Convite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConviteRepository extends JpaRepository<Convite, Integer> {

    List<Convite> findByEventoId(Integer eventoId);

    List<Convite> findByConvidadoId(Integer convidadoId);

    Optional<Convite> findByCodigoConvite(String codigoConvite);

    List<Convite> findByStatus(String status);

    boolean existsByCodigoConvite(String codigoConvite);

    long countByEventoIdAndStatus(Integer eventoId, String status);
}
