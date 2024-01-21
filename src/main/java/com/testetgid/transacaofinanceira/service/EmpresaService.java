package com.testetgid.transacaofinanceira.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testetgid.transacaofinanceira.model.Empresa;
import com.testetgid.transacaofinanceira.model.TaxaSistema;
import com.testetgid.transacaofinanceira.model.Transacao;
import com.testetgid.transacaofinanceira.model.Transacao.TipoTransacao;
import com.testetgid.transacaofinanceira.repository.EmpresaRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private TaxaSistemaService taxaSistemaService;

    private Validator validator;

    // Garante que a instância seja criada apenas uma vez
    public EmpresaService() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public Empresa salvarEmpresa(Empresa empresa) {
        validarCnpj(empresa.getCnpj());

        // Verifica se CNPJ já tem cadastro
        if (empresaRepository.existsByCnpj(empresa.getCnpj())) {
            throw new IllegalArgumentException("CNPJ Já cadastrado.");
        }
        // se não houver, salva o cliente
        return empresaRepository.save(empresa);
    }

    public Empresa obterEmpresaPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj);
    }

    public List<Empresa> listarEmpresas() {
        return empresaRepository.findAll();
    }

    public void realizarTransacao(Transacao transacao) {
        validarTransacao(transacao);

        Empresa empresa = transacao.getEmpresa();
        BigDecimal valorLiquido = calcularValorLiquido(transacao);

        // Verifica se a transação é depósito ou saque
        if (transacao.getTipo() == TipoTransacao.DEPOSITO) {
            // Atualiza saldo da empresa
            if (empresa != null) {
                // Atualiza saldo para deposito
                empresa.setSaldo(empresa.getSaldo().add(valorLiquido));
                // salva empresa com a transação
                empresaRepository.save(empresa);
            }
        } else if (transacao.getTipo() == TipoTransacao.SAQUE) {
            // Verifica se a empresa tem saldo para o saque
            if (empresa != null) {
                if (empresa.getSaldo().compareTo(valorLiquido) < 0) {
                    throw new IllegalArgumentException("Saldo para saque insuficiente!");
                }
                // Atualiza saldo para saque
                empresa.setSaldo(empresa.getSaldo().subtract(valorLiquido));
                // salva empresa com a transação
                empresaRepository.save(empresa);
            }
        }
    }

    public void validarCnpj(String cnpj) {
        Set<ConstraintViolation<Empresa>> violations = validator.validateValue(Empresa.class, "cnpj", cnpj);

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }
    }

    private void validarTransacao(Transacao transacao) {
        // Verifica se o valor é positivo
        if (transacao.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser positivo.");
        }
        // verifica se o cliente existe
        if (transacao.getCliente() == null) {
            throw new IllegalArgumentException("Cliente deve estar associado.");
        }
    }

    private BigDecimal calcularValorLiquido(Transacao transacao) {

        BigDecimal valorBruto = transacao.getValor();

        // Obtem as taxas associadas à empresa
        List<TaxaSistema> taxas = taxaSistemaService.obterTaxasParaTransacao(transacao.getEmpresa());

        // Aplica os abatimentos
        for (TaxaSistema taxa : taxas) {
            valorBruto = valorBruto.subtract(taxa.getValor());
        }

        return valorBruto;
    }
}
