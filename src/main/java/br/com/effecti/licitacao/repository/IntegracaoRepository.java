package br.com.effecti.licitacao.repository;

import br.com.effecti.licitacao.model.Integracao;
import br.com.effecti.licitacao.model.IntegracaoStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegracaoRepository extends JpaRepository<Integracao, Long> {
    Boolean existsByStatus(IntegracaoStatusEnum status);
}
