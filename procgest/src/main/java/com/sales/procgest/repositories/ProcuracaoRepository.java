package com.sales.procgest.repositories;

import com.sales.procgest.entities.Procuracao;
import com.sales.procgest.entities.StatusProcuracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProcuracaoRepository extends JpaRepository<Procuracao, Long> {

    List<Procuracao> findByDataVencimentoBetween(LocalDate inicio, LocalDate fim);
    List<Procuracao> findByStatus(StatusProcuracao status);
    List<Procuracao> findByNomeProcurador(String nomeProcurador);

}
