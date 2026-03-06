package com.eventos.repository;

import com.eventos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    List<Pagamento> findByEventoId(Integer eventoId);

    List<Pagamento> findByStatus(String status);

    @Query("SELECT SUM(p.valor) FROM Pagamento p WHERE p.evento.id = :eventoId AND p.status = 'PAGO'")
    BigDecimal sumValorPagoByEventoId(@Param("eventoId") Integer eventoId);
}
