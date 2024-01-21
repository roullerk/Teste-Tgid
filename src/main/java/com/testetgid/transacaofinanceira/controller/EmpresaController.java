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

import com.testetgid.transacaofinanceira.model.Empresa;
import com.testetgid.transacaofinanceira.service.EmpresaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    public EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<Empresa> cadastrarEmpresa(@Valid @RequestBody Empresa empresa) {
        Empresa novaEmpresa = empresaService.salvarEmpresa(empresa);
        return new ResponseEntity<>(novaEmpresa, HttpStatus.CREATED);
    }

    @GetMapping("/{cnpj}")
    public ResponseEntity<Empresa> obterEmpresaPorCnpj(@PathVariable String cnpj) {
        Empresa empresa = empresaService.obterEmpresaPorCnpj(cnpj);
        if (empresa != null) {
            return new ResponseEntity<>(empresa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> listarEmpresas() {
        List<Empresa> empresas = empresaService.listarEmpresas();
        return new ResponseEntity<>(empresas, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
