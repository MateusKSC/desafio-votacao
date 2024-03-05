package desafio.votacao.v1.unit.repository;

import desafio.votacao.v1.entities.Pauta;
import desafio.votacao.v1.repository.PautaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static desafio.votacao.Utils.PautaEntitiesBuilder.pautaBuilder;

@DataJpaTest
@DisplayName("Testes para Pauta Repository")
class PautaRepositoryTest {

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    @DisplayName("save: cria e então salva uma pauta ao obter sucesso")
    public void savePautaWhenSuccessful(){
        Pauta pauta = pautaBuilder();
        Pauta savedPauta = pautaRepository.save(pauta);
        Assertions.assertThat(savedPauta).isNotNull();
        Assertions.assertThat(savedPauta.getId()).isNotNull();
        Assertions.assertThat(savedPauta.getNome()).isEqualTo(pauta.getNome());
    }
    @Test
    @DisplayName("save: salva e então atualiza uma pauta ao obter sucesso")
    public void updatePautaWhenSuccessful(){
        Pauta pauta = pautaBuilder();
        Pauta savedPauta = pautaRepository.save(pauta);
        savedPauta.setNome("Pauta_Updated");
        Pauta pautaUpdated = pautaRepository.save(savedPauta);
        Assertions.assertThat(pautaUpdated).isNotNull();
        Assertions.assertThat(pautaUpdated.getId()).isNotNull();
        Assertions.assertThat(pautaUpdated.getNome()).isEqualTo(savedPauta.getNome());
    }
    @Test
    @DisplayName("delete: salva e então remove um Associado ao obter sucesso")
    public void deletePautaWhenSuccessful(){
        Pauta pauta = pautaBuilder();
        Pauta savedPauta = pautaRepository.save(pauta);
        pautaRepository.delete(savedPauta);
        Optional<Pauta> optionalPauta = pautaRepository.findById(savedPauta.getId());

        Assertions.assertThat(optionalPauta).isEmpty();
    }
    @Test
    @DisplayName("findById: encontra uma pauta pelo id e então retorna um Optional de pauta" +
            "ao obter sucesso")
    public void findPautaByIdWhenSuccessful(){
        Pauta pauta = pautaBuilder();
        Pauta savedPauta = pautaRepository.save(pauta);
        Optional<Pauta> foundPauta = pautaRepository.findById(savedPauta.getId());

        Assertions.assertThat(foundPauta.get())
                .isNotNull()
                .isEqualTo(savedPauta);
    }
    @Test
    @DisplayName("findById: falha ao tentar encontrar uma pauta por id e então retorna nulo")
    public void failsToFindPautaById(){
        Pauta pauta = pautaBuilder();
        pautaRepository.save(pauta);
        Optional<Pauta> foundPauta = pautaRepository.findById(10L);

        Assertions.assertThat(foundPauta).isEmpty();
    }
    @Test
    @DisplayName("findByNome: retorna uma lista de Associados ao obter sucesso na procura por nome")
    public void findPautaByNameWhenSuccessful(){
        Pauta pauta = pautaBuilder();
        Pauta savedPauta = pautaRepository.save(pauta);
        List<Pauta> pautas = pautaRepository.findByNome(pauta.getNome());

        Assertions.assertThat(pautas)
                .isNotEmpty()
                .contains(savedPauta);
    }
    @Test
    @DisplayName("findByNome: retorna uma lista vazia ao não encontrar Associados por nome")
    public void failsToFindPautaByName(){
        Pauta pauta = pautaBuilder();
        pautaRepository.save(pauta);
        List<Pauta> pautas = pautaRepository.findByNome("Different_Name");

        Assertions.assertThat(pautas).isEmpty();
    }
    @Test
    @DisplayName("save: tenta salvar um associado, mas falha ao causar ConstraintViolationException")
    public void failsToSavePauta_ThrowConstraintViolationException(){
        Pauta pauta = new Pauta();
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> pautaRepository.save(pauta))
                .withMessageContaining("O nome da pauta precisa ser informado!")
                .withMessageContaining("A descricao da pauta precisa ser informada!");
    }


}