package com.testetgid.transacaofinanceira.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testetgid.transacaofinanceira.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {
    Empresa findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);
}
