package com.testetgid.transacaofinanceira.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Empresa {

    @Id
    private String cnpj;

    private String nome;
    private String endereco;
    private String email;
    private String telefone;

    private BigDecimal saldo;

    @OneToMany(mappedBy = "empresa")
    private List<Transacao> transacoes;

    @OneToMany(mappedBy = "empresa")
    private List<TaxaSistema> taxas;

    public Empresa() {
    }

    public Empresa(String cnpj) {
        this.cnpj = cnpj;
    }

    public Empresa(String cnpj, String nome, String endereco, String email, String telefone, BigDecimal saldo) {
        this.cnpj = cnpj;
        this.nome = nome;
        this.endereco = endereco;
        this.email = email;
        this.telefone = telefone;
        this.saldo = saldo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }

    public List<TaxaSistema> getTaxas() {
        return taxas;
    }

    public void setTaxas(List<TaxaSistema> taxas) {
        this.taxas = taxas;
    }

}
