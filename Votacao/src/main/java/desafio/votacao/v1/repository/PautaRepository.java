package desafio.votacao.v1.repository;

import desafio.votacao.v1.entities.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface de Jpa Repository específica da Pauta
 */
@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

   List<Pauta> findByNome(String nome);

}
