package com.portfolio.gastos.controller;

import com.portfolio.gastos.model.Transacao;
import com.portfolio.gastos.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transacao> listar() {
        return service.listar();
    }

    @PostMapping
    public Transacao criar(@Valid @RequestBody Transacao t) {
        return service.salvar(t);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resumo")
    public Map<String, BigDecimal> resumo() {
        return service.resumo();
    }
}