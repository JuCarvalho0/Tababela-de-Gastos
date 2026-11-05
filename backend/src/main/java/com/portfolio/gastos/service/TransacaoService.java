package com.portfolio.gastos.service;

import com.portfolio.gastos.model.Transacao;
import com.portfolio.gastos.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class TransacaoService {

    private final TransacaoRepository repository;

    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    public List<Transacao> listar() {
        return repository.findAllByOrderByDataDescIdDesc();
    }

    public Transacao salvar(Transacao t) {
        return repository.save(t);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Map<String, BigDecimal> resumo() {
        BigDecimal receitas = repository.somarReceitas();
        BigDecimal despesas = repository.somarDespesas();
        BigDecimal saldo = receitas.subtract(despesas);
        return Map.of(
            "receitas", receitas,
            "despesas", despesas,
            "saldo", saldo
        );
    }
}