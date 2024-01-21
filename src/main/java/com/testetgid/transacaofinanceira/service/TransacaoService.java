package com.testetgid.transacaofinanceira.service;

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
}
