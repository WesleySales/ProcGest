package com.sales.procgest.repositories;

import com.sales.procgest.entities.Procuracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcuracaoRepository extends JpaRepository<Procuracao, Long> {
}
