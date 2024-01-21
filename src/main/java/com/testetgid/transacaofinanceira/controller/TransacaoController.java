package com.testetgid.transacaofinanceira.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testetgid.transacaofinanceira.model.Transacao;
import com.testetgid.transacaofinanceira.service.TransacaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<Transacao> realizarTransacao(@Valid @RequestBody Transacao transacao) {
        transacaoService.realizarTransacao(transacao);
        return new ResponseEntity<>(transacao, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> obterTransacaoPorId(@PathVariable Long id) {
        Transacao transacao = transacaoService.obterTransacaoPorId(id);
        if (transacao != null) {
            return new ResponseEntity<>(transacao, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> listarTransacoes() {
        List<Transacao> transacoes = transacaoService.listarTransacoes();
        return new ResponseEntity<>(transacoes, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
