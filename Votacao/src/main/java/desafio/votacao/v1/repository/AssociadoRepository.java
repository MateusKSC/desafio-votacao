package desafio.votacao.v1.repository;

import desafio.votacao.v1.entities.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface de Jpa Repository específica do Associado
 */
@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {
   List<Associado> findByNome(String nome);
   Associado findByCpf(String cpf);
   List<Associado> findAllByCpf(String cpf);

}
