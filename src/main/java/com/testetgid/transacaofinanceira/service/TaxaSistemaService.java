package com.testetgid.transacaofinanceira.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testetgid.transacaofinanceira.model.Empresa;
import com.testetgid.transacaofinanceira.model.TaxaSistema;
import com.testetgid.transacaofinanceira.repository.TaxaSistemaRepository;

@Service
public class TaxaSistemaService {

    @Autowired
    private TaxaSistemaRepository taxaSistemaRepository;

    public void cadastrarTaxa(TaxaSistema taxaSistema) {
        if (taxaSistema != null) {
            taxaSistemaRepository.save(taxaSistema);
        } else {
            throw new IllegalArgumentException("Taxa não pode ser nula!");
        }
    }

    public void associarTaxaEmpresa(TaxaSistema taxaSistema, Empresa empresa) {
        if (taxaSistema.getEmpresa() != null) {
            throw new IllegalArgumentException("A taxa já está associada à empresa");
        }

        if (empresa == null) {
            throw new IllegalArgumentException("A empresa não pode ser nula");
        }

        // Define a empresa associada
        taxaSistema.setEmpresa(empresa);

        // Salva taxa associada
        taxaSistemaRepository.save(taxaSistema);
    }

    public List<TaxaSistema> obterTaxasParaTransacao(Empresa empresa) {
        return taxaSistemaRepository.findByEmpresa(empresa);
    }
}
