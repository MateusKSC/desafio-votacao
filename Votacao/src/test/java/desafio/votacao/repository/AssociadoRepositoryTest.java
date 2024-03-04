package desafio.votacao.repository;

import desafio.votacao.entities.Associado;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static desafio.votacao.Utils.AssociadoEntitiesBuilder.associadoBuilder;

@DataJpaTest
@DisplayName("Testes para Associado Repository")
class AssociadoRepositoryTest {

    @Autowired
    private AssociadoRepository associadoRepository;

    @Test
    @DisplayName("save: cria e então salva um Associado ao obter sucesso")
    public void saveAssociadoWhenSuccessful(){
        Associado associado = associadoBuilder();
        Associado savedAssociado = associadoRepository.save(associado);
        Assertions.assertThat(savedAssociado).isNotNull();
        Assertions.assertThat(savedAssociado.getId()).isNotNull();
        Assertions.assertThat(savedAssociado.getNome()).isEqualTo(associado.getNome());
    }
    @Test
    @DisplayName("save: salva e então atualiza um Associado ao obter sucesso")
    public void updateAssociadoWhenSuccessful(){
        Associado associado = associadoBuilder();
        Associado savedAssociado = associadoRepository.save(associado);
        savedAssociado.setNome("Associado_Updated");
        Associado associadoUpdated = associadoRepository.save(savedAssociado);
        Assertions.assertThat(associadoUpdated).isNotNull();
        Assertions.assertThat(associadoUpdated.getId()).isNotNull();
        Assertions.assertThat(associadoUpdated.getNome()).isEqualTo(savedAssociado.getNome());
    }
    @Test
    @DisplayName("delete: salva e então remove um Associado ao obter sucesso")
    public void deleteAssociadoWhenSuccessful(){
        Associado associado = associadoBuilder();
        Associado savedAssociado = associadoRepository.save(associado);
        associadoRepository.delete(savedAssociado);
        Optional<Associado> optionalAssociado = associadoRepository.findById(savedAssociado.getId());

        Assertions.assertThat(optionalAssociado).isEmpty();
    }
    @Test
    @DisplayName("findById: encontra um Associado pelo id e então retorna um Optional de Associado" +
            "ao obter sucesso")
    public void findAssociadoByIdWhenSuccessful(){
        Associado associado = associadoBuilder();
        Associado savedAssociado = associadoRepository.save(associado);
        Optional<Associado> foundAssociado = associadoRepository.findById(savedAssociado.getId());

        Assertions.assertThat(foundAssociado.get())
                .isNotNull()
                .isEqualTo(savedAssociado);
    }
    @Test
    @DisplayName("findById: falha ao tentar encontrar um Associado por id e então retorna nulo")
    public void failsToFindAssociadoById(){
        Associado associado = associadoBuilder();
        associadoRepository.save(associado);
        Optional<Associado> foundAssociado = associadoRepository.findById(10L);

        Assertions.assertThat(foundAssociado).isEmpty();
    }

    @Test
    @DisplayName("findByNome: retorna uma lista de Associados ao obter sucesso na procura por nome")
    public void findAssociadoByNomeWhenSuccessful(){
        Associado associado = associadoBuilder();
        Associado savedPauta = associadoRepository.save(associado);
        List<Associado> associados = associadoRepository.findByNome(associado.getNome());

        Assertions.assertThat(associados)
                .isNotEmpty()
                .contains(savedPauta);
    }

    @Test
    @DisplayName("findByNome: retorna uma lista vazia ao não encontrar Associados por nome")
    public void failsToFindAssociadoByName(){
        Associado associado = associadoBuilder();
        associadoRepository.save(associado);
        List<Associado> associados = associadoRepository.findByNome("Different_Name");

        Assertions.assertThat(associados).isEmpty();
    }

    @Test
    @DisplayName("findByCpf: encontra um Associado pelo cpf e então retorna um Optional de Associado" +
            "ao obter sucesso")
    public void findAssociadoByCpfWhenSuccessful(){
        Associado associado = associadoBuilder();
        Associado savedAssociado = associadoRepository.save(associado);
        Associado foundAssociado = associadoRepository.findByCpf(associado.getCpf());

        Assertions.assertThat(foundAssociado)
                .isNotNull()
                .isEqualTo(savedAssociado);
    }
    @Test
    @DisplayName("findByCpf: falha ao tentar encontrar um Associado por cpf e então retorna nulo")
    public void failsToFindAssociadoByCpf(){
        Associado associado = associadoBuilder();
        associadoRepository.save(associado);
        Associado foundAssociado = associadoRepository.findByCpf("Different_Cpf");

        Assertions.assertThat(foundAssociado).isNull();
    }

    @Test
    @DisplayName("save: tenta salvar um associado, mas falha ao causar ConstraintViolationException")
    public void failsToSaveAssociado_ThrowConstraintViolationException(){
        Associado associado = new Associado();
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> associadoRepository.save(associado))
                .withMessageContaining("O nome do associado precisa ser informado!")
                .withMessageContaining("O CPF precisa ser informado!");
    }

}