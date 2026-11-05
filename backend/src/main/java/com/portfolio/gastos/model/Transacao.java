package com.portfolio.gastos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transacoes")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    @Column(nullable = false)
    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "Tipo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    @NotNull(message = "Data é obrigatória")
    @Column(nullable = false)
    private LocalDate data;

    public enum Tipo { RECEITA, DESPESA }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
}