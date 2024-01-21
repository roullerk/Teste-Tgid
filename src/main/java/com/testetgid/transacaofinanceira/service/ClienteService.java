package com.testetgid.transacaofinanceira.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testetgid.transacaofinanceira.model.Cliente;
import com.testetgid.transacaofinanceira.model.Empresa;
import com.testetgid.transacaofinanceira.model.TaxaSistema;
import com.testetgid.transacaofinanceira.model.Transacao;
import com.testetgid.transacaofinanceira.model.Transacao.TipoTransacao;
import com.testetgid.transacaofinanceira.repository.ClienteRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TaxaSistemaService taxaSistemaService;

    @Autowired
    private NotificacaoService notificacaoService;

    private Validator validator;

    // Garante que a instância seja criada apenas uma vez
    public ClienteService() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Autowired
    private TransacaoService transacaoService;

    public Cliente salvarCliente(Cliente cliente) {
        validarCpf(cliente.getCpf());

        // Verifica se CPF já tem cadastro
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        // se não houver, salva o cliente
        return clienteRepository.save(cliente);
    }

    public Cliente obterClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public void realizarTransacao(Transacao transacao) {
        validarTransacao(transacao);

        Cliente cliente = transacao.getCliente();
        Empresa empresa = transacao.getEmpresa();
        BigDecimal valorLiquido = calcularValorLiquido(transacao);

        // Verifica se a transação é depósito ou saque
        if (transacao.getTipo() == TipoTransacao.DEPOSITO) {
            // Atualiza saldo do cliente
            cliente.setSaldo(cliente.getSaldo().add(valorLiquido));
        } else if (transacao.getTipo() == TipoTransacao.SAQUE) {
            // Verifica se a empresa tem saldo para o saque
            if (empresa.getSaldo().compareTo(valorLiquido) < 0) {
                throw new IllegalArgumentException("Saldo para saque insuficiente!");
            }
            // Atualiza o saldo da empresa
            empresa.setSaldo(empresa.getSaldo().subtract(valorLiquido));
            // Atualiza saldo do cliente
            cliente.setSaldo(cliente.getSaldo().add(valorLiquido));
        }

        // Atualiza o cliente com a transação realizada
        cliente.getTransacoes().add(transacao);
        // Salva o cliente com a transação
        clienteRepository.save(cliente);

        transacaoService.realizarTransacao(transacao);

        // Envia notificação para o cliente
        notificacaoService.notificarCliente(transacao, cliente);
    }

    private void validarCpf(String cpf) {
        Set<ConstraintViolation<Cliente>> violations = validator.validateValue(Cliente.class, "cpf", cpf);

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }
    }

    private void validarTransacao(Transacao transacao) {
        // Verifica se o valor é positivo
        if (transacao.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser positivo.");
        }
        // verifica se a empresa existe
        if (transacao.getEmpresa() == null) {
            throw new IllegalArgumentException("Empresa deve estar associada.");
        }
    }

    private BigDecimal calcularValorLiquido(Transacao transacao) {

        BigDecimal valorBruto = transacao.getValor();

        // Obtém as taxas associadas à empresa
        List<TaxaSistema> taxas = taxaSistemaService.obterTaxasParaTransacao(transacao.getEmpresa());

        // Aplica os abatimentos
        for (TaxaSistema taxa : taxas) {
            valorBruto = valorBruto.subtract(taxa.getValor());
        }

        return valorBruto;
    }
}
