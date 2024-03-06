package desafio.votacao.v1.unit.services;

import desafio.votacao.Utils.AssociadoEntitiesBuilder;
import desafio.votacao.Utils.PautaEntitiesBuilder;
import desafio.votacao.v1.entities.Associado;
import desafio.votacao.v1.entities.Pauta;
import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.exceptions.BadRequestException;
import desafio.votacao.v1.repository.AssociadoRepository;
import desafio.votacao.v1.requests.AssociadoPostRequestBody;
import desafio.votacao.v1.services.impl.AssociadoServiceImpl;
import desafio.votacao.v1.utilities.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Classe de testes que verificam o funcionamento do service do Associado
 */
@ExtendWith(SpringExtension.class)
class AssociadoServiceTest {

    @InjectMocks
    private AssociadoServiceImpl associadoService;
    @Mock
    private DateUtil dateUtil;

    @Mock
    private AssociadoRepository associadoRepositoryMock;



    @BeforeEach
    void setUp() {
        Mockito.when(associadoRepositoryMock.findAll())
                .thenReturn(List.of(AssociadoEntitiesBuilder.associadoBuilder()));

        Mockito.when(associadoRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AssociadoEntitiesBuilder.associadoBuilder()));

        Mockito.when(associadoRepositoryMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AssociadoEntitiesBuilder.associadoBuilder()));

        Mockito.when(associadoRepositoryMock.findByCpf(ArgumentMatchers.anyString()))
                .thenReturn(AssociadoEntitiesBuilder.associadoBuilder());

        Mockito.when(associadoRepositoryMock.save(ArgumentMatchers.any(Associado.class)))
                .thenReturn(AssociadoEntitiesBuilder.associadoBuilder());

        Mockito.doNothing().when(associadoRepositoryMock).delete(ArgumentMatchers.any(Associado.class));
    }

    @Test
    @DisplayName("listAll: Retorna uma Lista de Associados ao obter sucesso")
    void listAllReturnsListOfAssociadosWhenSuccessful() {
        String expectedName = AssociadoEntitiesBuilder.associadoBuilder().getNome();

        List<Associado> associados = associadoService.listAll();

        Assertions.assertThat(associados)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(associados.get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException: retorna um Associado ao obter sucesso" +
            " na procura por id")
    void findByIdOrThrowBadRequestExceptionReturnsAssociadoWhenSuccessful() {
        Long expectedId = AssociadoEntitiesBuilder.associadoBuilder().getId();

        Associado associado = associadoService.findByIdOrThrowBadRequestException(AssociadoEntitiesBuilder.associadoBuilder().getId());

        Assertions.assertThat(associado).isNotNull();

        Assertions.assertThat(associado.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByNome: retorna uma lista de Associados ao obter sucesso na procura por nome")
    void findByNameReturnsListOfAssociadoWhenSuccessful() {
        String expectedName = AssociadoEntitiesBuilder.associadoBuilder().getNome();

        List<Associado> associados = associadoService.findByNome("associado");

        Assertions.assertThat(associados)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(associados.get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByNome: retorna uma lista vazia ao não encontrar Associados por nome")
    void findByNameReturnsEmptyListOfAssociadoWhenAssociadoIsNotFound() {
        Mockito.when(associadoRepositoryMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> associadoService.findByNome("associado"))
                .withMessageContaining("Associado não encontrado!");
    }

    @Test
    @DisplayName("save: salva e retorna um associado ao obter sucesso")
    void saveReturnsAssociadoWhenSuccessful() {

        Associado associado = associadoService.save(AssociadoEntitiesBuilder.associadoPostRequestBodyBuilder());
        Assertions.assertThat(associado).isNotNull().isEqualTo(AssociadoEntitiesBuilder.associadoBuilder());
    }

    @Test
    @DisplayName("save: tenta salvar um associado, mas falha devido ao cpf " +
            "já estar registrado no banco de dados e então lança BadRequestException")
    void failsToSaveThrowsBadRequestExceptionBecauseOfEqualToAnotherAssociadoCpf() {
        AssociadoPostRequestBody associadoPostRequestBody = AssociadoEntitiesBuilder.associadoPostRequestBodyBuilder();
        Associado associado = associadoService.save(AssociadoEntitiesBuilder.associadoPostRequestBodyBuilder());
        associadoPostRequestBody.setCpf(associado.getCpf());
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> associadoService.save(associadoPostRequestBody))
                .withMessageContaining("O CPF do associado informado já está registrado no banco de dados. " +
                        "Verifique o associado de nome " + AssociadoEntitiesBuilder.associadoBuilder().getNome());
    }

    @Test
    @DisplayName("replace: atualiza um associado ao obter sucesso")
    void replaceUpdatesAssociadoWhenSuccessful() {
        Associado associado = AssociadoEntitiesBuilder.associadoBuilder();
        Pauta pauta = PautaEntitiesBuilder.pautaBuilder();
        pauta.setConcluida(true);
        associado.setPautas(List.of(pauta));
        Mockito.when(associadoRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(associado));
        Assertions.assertThatCode(() -> associadoService.replace(AssociadoEntitiesBuilder.associadoPutRequestBodyBuilder()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete: remove um associado ao obter sucesso")
    void deleteRemovesAssociadoWhenSuccessful() {
        Associado associado = AssociadoEntitiesBuilder.associadoBuilder();
        Pauta pauta = PautaEntitiesBuilder.pautaBuilder();
        pauta.setConcluida(true);
        associado.setPautas(List.of(pauta));
        Mockito.when(associadoRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(associado));
        Assertions.assertThatCode(() -> associadoService.delete(1))
                .doesNotThrowAnyException();

    }
    @Test
    @DisplayName("definirVoto: especifica o valor do voto do associado ao obter sucesso")
    void definirVotoSetsVotoFromAssociadoWhenSuccessful() {
        Sessao sessao = new Sessao();
        Pauta pauta = new Pauta();
        Associado associado = AssociadoEntitiesBuilder.associadoBuilder();
        sessao.setMomentoDoFim(new Date(System.currentTimeMillis() + 1899003116));
        pauta.setSessao(sessao);
        pauta.setSessaoIniciada(true);
        associado.setPautas(List.of(pauta));
        Mockito.when(associadoRepositoryMock.findByCpf(ArgumentMatchers.anyString()))
                .thenReturn(associado);
        Assertions.assertThatCode(() -> associadoService.definirVoto(false,"00001000000"))
                .doesNotThrowAnyException();

    }
    @Test
    @DisplayName("resetaVoto: atualiza o valor do voto do associado para o valor padrão 'true' ao obter sucesso")
    void resetaVotoSetsVotoFromAssociadoWhenSuccessful() {
        Assertions.assertThatCode(() -> associadoService.resetaVoto())
                .doesNotThrowAnyException();
    }

}