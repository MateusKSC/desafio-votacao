package desafio.votacao.repository;

import desafio.votacao.entities.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {
   List<Associado> findByNome(String nome);
   Associado findByCpf(String cpf);

}
