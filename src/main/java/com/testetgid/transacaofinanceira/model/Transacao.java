package com.testetgid.transacaofinanceira.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Transacao {

    public enum TipoTransacao {
        DEPOSITO,
        SAQUE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;
    private BigDecimal valor;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Empresa empresa;

    private TipoTransacao tipo;

    public Transacao() {
    }

    public Transacao(LocalDateTime dataHora, TipoTransacao tipo, BigDecimal valor, Cliente cliente, Empresa empresa) {
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.valor = valor;
        this.cliente = cliente;
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

}
