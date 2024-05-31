package br.com.effecti.licitacao.repository;

import br.com.effecti.licitacao.model.Licitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LicitacaoRepository extends JpaRepository<Licitacao, Long> {
    @Query("SELECT l.identificador FROM Licitacao l WHERE l.identificador IN :identificadores")
    Set<Long> findExistingIdentifiers(Set<Long> identificadores);
}
