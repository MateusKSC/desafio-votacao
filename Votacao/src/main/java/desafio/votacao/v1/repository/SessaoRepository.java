package desafio.votacao.v1.repository;

import desafio.votacao.v1.entities.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de Jpa Repository específica da Sessao
 */
@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {

}
