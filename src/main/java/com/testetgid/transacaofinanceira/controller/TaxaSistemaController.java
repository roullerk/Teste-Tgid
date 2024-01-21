package com.testetgid.transacaofinanceira.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testetgid.transacaofinanceira.model.Empresa;
import com.testetgid.transacaofinanceira.model.TaxaSistema;
import com.testetgid.transacaofinanceira.service.TaxaSistemaService;

@RestController
@RequestMapping("/api/taxas")
public class TaxaSistemaController {

    @Autowired
    private TaxaSistemaService taxaSistemaService;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarTaxa(@RequestBody TaxaSistema taxaSistema) {
        taxaSistemaService.cadastrarTaxa(taxaSistema);
        return ResponseEntity.ok("Taxa Criada com sucesso!");
    }

    @PostMapping("/associar/{cnpj}")
    public ResponseEntity<String> associarTaxaEmpresa(@PathVariable String cnpj,
            @RequestBody TaxaSistema taxaSistema) {
        taxaSistemaService.associarTaxaEmpresa(taxaSistema, new Empresa(cnpj));
        return ResponseEntity.ok("Taxa associada à empresa com sucesso!");
    }

    @GetMapping("/obterTaxas/{cnpj}")
    public ResponseEntity<List<TaxaSistema>> obterTaxasParaTransacao(@PathVariable String cnpj) {
        List<TaxaSistema> taxas = taxaSistemaService.obterTaxasParaTransacao(new Empresa(cnpj));
        return ResponseEntity.ok(taxas);
    }
}
