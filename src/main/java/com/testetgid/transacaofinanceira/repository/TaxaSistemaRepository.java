package com.testetgid.transacaofinanceira.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testetgid.transacaofinanceira.model.Empresa;
import com.testetgid.transacaofinanceira.model.TaxaSistema;

public interface TaxaSistemaRepository extends JpaRepository<TaxaSistema, Long> {
    List<TaxaSistema> findByEmpresa(Empresa empresa);
}
