package desafio.votacao.v1.unit.services;

import desafio.votacao.Utils.PautaEntitiesBuilder;
import desafio.votacao.v1.entities.Pauta;
import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.exceptions.BadRequestException;
import desafio.votacao.v1.repository.PautaRepository;
import desafio.votacao.v1.services.impl.AssociadoServiceImpl;
import desafio.votacao.v1.services.impl.PautaServiceImpl;
import desafio.votacao.v1.services.impl.SessaoServiceImpl;
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
 * Classe de testes que verificam o funcionamento do service da Pauta
 */
@ExtendWith(SpringExtension.class)
class PautaServiceTest {

    @InjectMocks
    private PautaServiceImpl pautaService;

    @Mock
    private PautaRepository pautaRepositoryMock;

    @Mock
    private SessaoServiceImpl sessaoServiceMock;
    @Mock
    private AssociadoServiceImpl associadoServiceMock;


    @BeforeEach
    void setUp() {
        Mockito.when(pautaRepositoryMock.findAll())
                .thenReturn(List.of(PautaEntitiesBuilder.pautaBuilder()));

        Mockito.when(pautaRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(PautaEntitiesBuilder.pautaBuilder()));

        Mockito.when(pautaRepositoryMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PautaEntitiesBuilder.pautaBuilder()));

        Mockito.when(associadoServiceMock.verificaUnicidadeDoCpfAoCriarAssociado(ArgumentMatchers.any())).thenReturn(true);

        Mockito.when(pautaRepositoryMock.save(ArgumentMatchers.any(Pauta.class)))
                .thenReturn(PautaEntitiesBuilder.pautaBuilder());

        Mockito.doNothing().when(pautaRepositoryMock).delete(ArgumentMatchers.any(Pauta.class));
    }

    @Test
    @DisplayName("listAll: Retorna uma Lista de Pautas ao obter sucesso")
    void listAllReturnsListOfPautasWhenSuccessful() {
        String expectedName = PautaEntitiesBuilder.pautaBuilder().getNome();

        List<Pauta> pautas = pautaService.listAll();

        Assertions.assertThat(pautas)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(pautas.get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException: retorna um Pauta ao obter sucesso" +
            " na procura por id")
    void findByIdOrThrowBadRequestExceptionReturnsPautaWhenSuccessful() {
        Long expectedId = PautaEntitiesBuilder.pautaBuilder().getId();

        Pauta pauta = pautaService.findByIdOrThrowBadRequestException(PautaEntitiesBuilder.pautaBuilder().getId());

        Assertions.assertThat(pauta).isNotNull();

        Assertions.assertThat(pauta.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByNome: retorna uma lista de Pautas ao obter sucesso na procura por nome")
    void findByNameReturnsListOfPautaWhenSuccessful() {
        String expectedName = PautaEntitiesBuilder.pautaBuilder().getNome();

        List<Pauta> pautas = pautaService.findByNome("pauta");

        Assertions.assertThat(pautas)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(pautas.get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByNome: retorna uma lista vazia ao não encontrar Pautas por nome")
    void findByNameReturnsEmptyListOfPautaWhenPautaIsNotFound() {
        Mockito.when(pautaRepositoryMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> pautaService.findByNome("pauta"))
                .withMessageContaining("Pauta não encontrada!");
    }

    @Test
    @DisplayName("save: Salva e retorna um pauta ao obter sucesso")
    void saveReturnsPautaWhenSuccessful() {


        Pauta pauta = pautaService.save(PautaEntitiesBuilder.pautaPostRequestBodyBuilder(), List.of("cpf"));
        Assertions.assertThat(pauta).isNotNull();
        Assertions.assertThat(pauta.getId()).isEqualTo(PautaEntitiesBuilder
                .pautaBuilder().getId());
        Assertions.assertThat(pauta.getNome()).isEqualTo(PautaEntitiesBuilder
                .pautaBuilder().getNome());
        Assertions.assertThat(pauta.getDescricao()).isEqualTo(PautaEntitiesBuilder
                .pautaBuilder().getDescricao());

    }


    @Test
    @DisplayName("replace: Atualiza um pauta ao obter sucesso")
    void replaceUpdatesPautaWhenSuccessful() {
        Assertions.assertThatCode(() -> pautaService.replace(PautaEntitiesBuilder.pautaPutRequestBodyBuilder()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete: Remove um pauta ao obter sucesso")
    void deleteRemovesPautaWhenSuccessful() {

        Assertions.assertThatCode(() -> pautaService.delete(1))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("getSessaoFromPauta: Obtém a sessao de uma pauta")
    void getSessaoFromPauta() {

        Sessao sessao = pautaService.getSessaoFromPauta(1L);
        Assertions.assertThat(sessao)
                .isNotNull();
    }

    @Test
    @DisplayName("prossegueComVotacaoTempoPadrao: Começa o processo de votacao ao obter sucesso")
    void prossegueComVotacaoTempoPadraoInicio() {
        Pauta pauta = PautaEntitiesBuilder.pautaBuilder();
        pauta.setSessaoIniciada(false);

        Mockito.when(pautaRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(pauta));

        Assertions.assertThatCode(() -> pautaService.prossegueComVotacaoTempoPadrao(1L))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("prossegueComVotacaoTempoPadrao: Tenta dar seguimento no processo de votação, mas" +
            "falha e lança BadRequestException, pois o período da sessão de votação ainda " +
            "não acabou")
    void prossegueComVotacaoTempoPadraoLancaBadRequestException() {
        Pauta pauta = PautaEntitiesBuilder.pautaBuilder();
        pauta.setSessaoIniciada(true);

        Mockito.when(pautaRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(pauta));

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> pautaService.prossegueComVotacaoTempoPadrao(1L));
    }

    @Test
    @DisplayName("prossegueComVotacaoTempoPadrao: Termina o processo de votacao ao obter sucesso")
    void prossegueComVotacaoTempoPadraoFim() {
        Pauta pauta = PautaEntitiesBuilder.pautaBuilder();
        pauta.setSessaoIniciada(true);
        pauta.getSessao().setMomentoDoFim(new Date(System.currentTimeMillis() - 6000000));

        Mockito.when(pautaRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(pauta));

        Assertions.assertThatCode(() -> pautaService.prossegueComVotacaoTempoPadrao(1L))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("prossegueComVotacaoTempoDefinido: Começa o processo de votacao ao obter sucesso")
    void prossegueComVotacaoTempoDefinidoInicio() {
        Pauta pauta = PautaEntitiesBuilder.pautaBuilder();
        pauta.setSessaoIniciada(false);

        Mockito.when(pautaRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(pauta));

        Assertions.assertThatCode(() -> pautaService.prossegueComVotacaoTempoDefinido(1L, 1))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("prossegueComVotacaoTempoDefinido: Tenta dar seguimento no processo de votação, mas" +
            "falha e lança BadRequestException, pois o período da sessão de votação ainda " +
            "não acabou")
    void prossegueComVotacaoTempoDefinidoLancaBadRequestException() {
        Pauta pauta = PautaEntitiesBuilder.pautaBuilder();
        pauta.setSessaoIniciada(true);

        Mockito.when(pautaRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(pauta));

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> pautaService.prossegueComVotacaoTempoDefinido(1L, 2));
    }

    @Test
    @DisplayName("prossegueComVotacaoTempoDefinido: Termina o processo de votacao ao obter sucesso")
    void prossegueComVotacaoTempoDefinidoFim() {
        Pauta pauta = PautaEntitiesBuilder.pautaBuilder();
        pauta.setSessaoIniciada(true);
        pauta.getSessao().setMomentoDoFim(new Date(System.currentTimeMillis() - 6000000));

        Mockito.when(pautaRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(pauta));

        Assertions.assertThatCode(() -> pautaService.prossegueComVotacaoTempoDefinido(1L, 2))
                .doesNotThrowAnyException();

    }


}