package desafio.votacao.repository;

import desafio.votacao.entities.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

   List<Pauta> findByNome(String nome);

}
