package desafio.votacao.v1.unit.repository;

import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.repository.SessaoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

/**
 * Classe de testes que verificam o funcionamento do repository da Sessao
 */
@DataJpaTest
@DisplayName("Testes for Sessao Repository")
class SessaoRepositoryTest {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Test
    @DisplayName("save: cria e então salva uma Sessao ao obter sucesso")
    public void saveSessaoWhenSuccessful(){
        Sessao sessao = new Sessao();
        Sessao savedSessao = sessaoRepository.save(sessao);
        Assertions.assertThat(savedSessao).isNotNull();
        Assertions.assertThat(savedSessao.getId()).isNotNull();
        Assertions.assertThat(savedSessao.getId()).isEqualTo(sessao.getId());
    }
    @Test
    @DisplayName("findById: encontra uma Sessao pelo id e então retorna um Optional de Sessao" +
            "ao obter sucesso")
    public void findSessaoByIdWhenSuccessful(){
        Sessao sessao = new Sessao();
        Sessao savedSessao = sessaoRepository.save(sessao);
        Optional<Sessao> foundSessao = sessaoRepository.findById(savedSessao.getId());

        Assertions.assertThat(foundSessao.get())
                .isNotNull()
                .isEqualTo(savedSessao);
    }
    @Test
    @DisplayName("findById: falha ao tentar encontrar uma Sessao por id e então retorna nulo")
    public void failsToFindSessaoById(){
        Sessao sessao = new Sessao();
        sessaoRepository.save(sessao);
        Optional<Sessao> foundSessao = sessaoRepository.findById(10L);

        Assertions.assertThat(foundSessao).isEmpty();
    }

}