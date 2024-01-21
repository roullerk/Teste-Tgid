package com.testetgid.transacaofinanceira.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testetgid.transacaofinanceira.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

}
