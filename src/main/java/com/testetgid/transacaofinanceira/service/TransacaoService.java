package com.testetgid.transacaofinanceira.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testetgid.transacaofinanceira.model.Empresa;
import com.testetgid.transacaofinanceira.model.Transacao;

import com.testetgid.transacaofinanceira.repository.TransacaoRepository;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private NotificacaoService notificacaoService;

    public void realizarTransacao(Transacao transacao) {

        // Realiza a transação
        clienteService.realizarTransacao(transacao);
        empresaService.realizarTransacao(transacao);

        // salva a transação
        if (transacao != null) {
            transacaoRepository.save(transacao);
            if (transacao.getCliente() != null) {
                notificacaoService.notificarCliente(transacao, transacao.getCliente());
            }
            if (transacao.getEmpresa() != null) {
                Empresa empresa = transacao.getEmpresa();
                notificacaoService.notificarCallback(transacao, empresa, "Transação Realizada!");
            }
        }
    }

    public Transacao obterTransacaoPorId(Long id) {
        Objects.requireNonNull(id, "Id não pode ser nulo");
        // Optional<Transacao> transacao = transacaoRepository.findById(id);
        return transacaoRepository.findById(id).orElse(null);
    }

    public List<Transacao> listarTransacoes() {
        return transacaoRepository.findAll();
    }
}
