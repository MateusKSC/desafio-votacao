package desafio.votacao.services;

import desafio.votacao.Utils.AssociadoEntitiesBuilder;
import desafio.votacao.Utils.PautaEntitiesBuilder;
import desafio.votacao.entities.Associado;
import desafio.votacao.entities.Pauta;
import desafio.votacao.entities.Sessao;
import desafio.votacao.exceptions.BadRequestException;
import desafio.votacao.repository.AssociadoRepository;
import desafio.votacao.repository.PautaRepository;
import desafio.votacao.services.impl.AssociadoServiceImpl;
import desafio.votacao.services.impl.PautaServiceImpl;
import desafio.votacao.services.impl.SessaoServiceImpl;
import desafio.votacao.utilities.DateUtil;
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

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class PautaServiceTest {

    @InjectMocks
    private PautaServiceImpl pautaService;
    @Mock
    private DateUtil dateUtil;

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

        Mockito.when(associadoServiceMock.isCpfUnique(ArgumentMatchers.any(),
                ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(true);

        Mockito.when(associadoServiceMock.findByCpf(ArgumentMatchers.any()))
                .thenReturn(AssociadoEntitiesBuilder.associadoBuilder());

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


        Pauta pauta = pautaService.save(PautaEntitiesBuilder.pautaPostRequestBodyBuilder(),List.of("cpf"));
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
    @DisplayName("prossegueComVotacao: Começa o processo de votacao ao obter sucesso")
    void prossegueComVotacaoInicio() {
        Pauta pauta = PautaEntitiesBuilder.pautaBuilder();
        pauta.setSessaoIniciada(false);

        Mockito.when(pautaRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(pauta));

        Assertions.assertThatCode(() -> pautaService.prossegueComVotacao(1L))
                .doesNotThrowAnyException();

    }
    @Test
    @DisplayName("prossegueComVotacao: Tenta dar seguimento no processo de votacao, mas" +
            "falha e lança BadRequestException, pois o período da sessao de votacao ainda " +
            "não acabou")
    void prossegueComVotacaoLançaBadRequestException() {
        Pauta pauta = PautaEntitiesBuilder.pautaBuilder();
        pauta.setSessaoIniciada(true);

        Mockito.when(pautaRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(pauta));

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> pautaService.prossegueComVotacao(1L));
    }
    @Test
    @DisplayName("prossegueComVotacao: Termina o processo de votacao ao obter sucesso")
    void prossegueComVotacaoFim() {
        Pauta pauta = PautaEntitiesBuilder.pautaBuilder();
        pauta.setSessaoIniciada(true);
        pauta.getSessao().setMomentoDoFim(new Date(System.currentTimeMillis() - 6000000));

        Mockito.when(pautaRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(pauta));

        Assertions.assertThatCode(() -> pautaService.prossegueComVotacao(1L))
                .doesNotThrowAnyException();

    }


}