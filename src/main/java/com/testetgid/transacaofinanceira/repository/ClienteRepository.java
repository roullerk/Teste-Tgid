package com.testetgid.transacaofinanceira.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testetgid.transacaofinanceira.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    boolean existsByCpf(String cpf);

    Cliente findByCpf(String cpf);

}
