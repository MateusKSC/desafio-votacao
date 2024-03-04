package desafio.votacao.controllers;

import desafio.votacao.Utils.PautaEntitiesBuilder;
import desafio.votacao.entities.Pauta;
import desafio.votacao.entities.Sessao;
import desafio.votacao.requests.PautaPostRequestBody;
import desafio.votacao.services.PautaService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
class PautaControllerTest {
    @InjectMocks
    private PautaController pautaController;


    @Mock
    private PautaService pautaServiceMock;

    @Mock
    private DateUtil dateUtil;


    @BeforeEach
    void setUp() {
        Mockito.when(pautaServiceMock.listAll())
                .thenReturn(List.of(PautaEntitiesBuilder.pautaBuilder()));

        Mockito.when(pautaServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(PautaEntitiesBuilder.pautaBuilder());

        Mockito.when(pautaServiceMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PautaEntitiesBuilder.pautaBuilder()));


        Mockito.when(pautaServiceMock.getSessaoFromPauta(ArgumentMatchers.anyLong()))
                .thenReturn(new Sessao());

        Mockito.doNothing().when(pautaServiceMock).prossegueComVotacao(ArgumentMatchers.anyLong());

        Mockito.doNothing().when(pautaServiceMock).delete(ArgumentMatchers.anyLong());

    }

    @Test
    @DisplayName("list: Retorna uma Lista de Pautas ao obter sucesso")
    void listReturnsListOfPautasWhenSuccessful() {
        String expectedName = PautaEntitiesBuilder.pautaBuilder().getNome();

        List<Pauta> pautas = pautaController.list().getBody();

        Assertions.assertThat(pautas)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(pautas.get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById: retorna um Pauta ao obter sucesso na procura por id")
    void findByIdReturnsPautaWhenSuccessful() {
        Long expectedId = PautaEntitiesBuilder.pautaBuilder().getId();

        Pauta pauta = pautaController.findById(PautaEntitiesBuilder.pautaBuilder().getId()).getBody();

        Assertions.assertThat(pauta).isNotNull();

        Assertions.assertThat(pauta.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById: falha ao tentar encontrar um Pauta por id e então retorna nulo")
    public void failsToFindPautaById(){
        Mockito.when(pautaServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(null);
        ResponseEntity<Pauta> foundPauta = pautaController.findById(10L);

        Assertions.assertThat(foundPauta.getBody()).isNull();
    }

    @Test
    @DisplayName("findByNome: retorna uma lista de Pautas ao obter sucesso na procura por nome")
    void findByNomeReturnsListOfPautaWhenSuccessful() {
        String expectedName = PautaEntitiesBuilder.pautaBuilder().getNome();

        List<Pauta> pautas = pautaController.findByNome("pauta").getBody();

        Assertions.assertThat(pautas)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(pautas.get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByNome: retorna uma lista vazia ao não encontrar Pautas por nome")
    void findByNomeReturnsEmptyListOfPautaWhenPautaIsNotFound() {
        Mockito.when(pautaServiceMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());
        List<Pauta> pautas = pautaController.findByNome("pauta").getBody();

        Assertions.assertThat(pautas)
                .isNotNull()
                .isEmpty();
    }


    @Test
    @DisplayName("getSessaoFromPauta: retorna a sessao da pauta ao obter sucesso")
    void getSessaoFromPautaReturnsSessaoOfPautaWhenSuccessful() {
        Sessao sessao = new Sessao();
        sessao.setMomentoDoInicio(new Date());
        sessao.setMomentoDoFim(new Date());
        sessao = pautaController.getSessaoFromPauta(ArgumentMatchers.anyLong()).getBody();

        Assertions.assertThat(sessao)
                .isNotNull();
        Assertions.assertThat(sessao.getMomentoDoInicio())
                .isNull();
        Assertions.assertThat(sessao.getMomentoDoFim())
                .isNull();
    }


    @Test
    @DisplayName("save: Salva e retorna uma pauta ao obter sucesso")
    void saveReturnsPautaWhenSuccessful() {
        List<String> strings = new ArrayList<>();
        String string = "";
        strings.add(string);
        Mockito.when(pautaServiceMock.save(ArgumentMatchers.any(PautaPostRequestBody.class),
                        eq(strings)))
                .thenReturn(PautaEntitiesBuilder.pautaBuilder());

        Pauta pauta = pautaController.save(
                PautaEntitiesBuilder.pautaPostRequestBodyBuilder(),strings).getBody();

        Assertions.assertThat(pauta).isNotNull().isEqualTo(PautaEntitiesBuilder.pautaBuilder());

    }
    @Test
    @DisplayName("replace: Atualiza um pauta ao obter sucesso")
    void replaceUpdatesPautaWhenSuccessful() {

        Assertions.assertThatCode(() -> pautaController.replace(PautaEntitiesBuilder.pautaPutRequestBodyBuilder()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = pautaController.replace(PautaEntitiesBuilder.pautaPutRequestBodyBuilder());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    @DisplayName("prossegueComVotacao: gerencia e avança o processo de votacao quando obtém sucesso")
    void prossegueComVotacaoManagesVotacaoProcessWhenSuccessful() {

        Assertions.assertThatCode(() -> pautaController.prossegueComVotacao(1L))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = pautaController.prossegueComVotacao(1L);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    @DisplayName("delete: Remove um pauta ao obter sucesso")
    void deleteRemovesPautaWhenSuccessful() {

        Assertions.assertThatCode(() -> pautaController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = pautaController.delete(1);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}