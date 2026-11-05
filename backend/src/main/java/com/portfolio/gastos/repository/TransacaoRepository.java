package com.portfolio.gastos.repository;

import com.portfolio.gastos.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findAllByOrderByDataDescIdDesc();

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.tipo = 'RECEITA'")
    BigDecimal somarReceitas();

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.tipo = 'DESPESA'")
    BigDecimal somarDespesas();
}