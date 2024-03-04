package desafio.votacao.controllers;

import desafio.votacao.Utils.AssociadoEntitiesBuilder;
import desafio.votacao.Utils.PautaEntitiesBuilder;
import desafio.votacao.entities.Associado;
import desafio.votacao.requests.AssociadoPostRequestBody;
import desafio.votacao.requests.AssociadoPutRequestBody;
import desafio.votacao.requests.PautaPostRequestBody;
import desafio.votacao.services.AssociadoService;
import desafio.votacao.utilities.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
class AssociadoControllerTest {
    @InjectMocks
    private AssociadoController associadoController;


    @Mock
    private AssociadoService associadoServiceMock;

    @Mock
    private DateUtil dateUtil;


    @BeforeEach
    void setUp() {
        Mockito.when(associadoServiceMock.listAll())
                .thenReturn(List.of(AssociadoEntitiesBuilder.associadoBuilder()));

        Mockito.when(associadoServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AssociadoEntitiesBuilder.associadoBuilder());

        Mockito.when(associadoServiceMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AssociadoEntitiesBuilder.associadoBuilder()));

        Mockito.when(associadoServiceMock.findByCpf(ArgumentMatchers.anyString()))
                .thenReturn(AssociadoEntitiesBuilder.associadoBuilder());

        Mockito.when(associadoServiceMock.save(ArgumentMatchers.any(AssociadoPostRequestBody.class)))
                .thenReturn(AssociadoEntitiesBuilder.associadoBuilder());

        Mockito.doNothing().when(associadoServiceMock).delete(ArgumentMatchers.anyLong());

        Mockito.doNothing().when(associadoServiceMock).replace(ArgumentMatchers.any(AssociadoPutRequestBody.class));

        Mockito.doNothing().when(associadoServiceMock).definirVoto(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("list: Retorna uma Lista de Associados ao obter sucesso")
    void listReturnsListOfAssociadosWhenSuccessful() {
        String expectedName = AssociadoEntitiesBuilder.associadoBuilder().getNome();

        List<Associado> associados = associadoController.list().getBody();

        Assertions.assertThat(associados)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(associados.get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById: retorna um Associado ao obter sucesso na procura por id")
    void findByIdReturnsAssociadoWhenSuccessful() {
        Long expectedId = AssociadoEntitiesBuilder.associadoBuilder().getId();

        Associado associado = associadoController.findById(AssociadoEntitiesBuilder.associadoBuilder().getId()).getBody();

        Assertions.assertThat(associado).isNotNull();

        Assertions.assertThat(associado.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById: falha ao tentar encontrar um Associado por id e então retorna nulo")
    public void failsToFindAssociadoById(){
        Mockito.when(associadoServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(null);
        ResponseEntity<Associado> foundAssociado = associadoController.findById(10L);

        Assertions.assertThat(foundAssociado.getBody()).isNull();
    }

    @Test
    @DisplayName("findByNome: retorna uma lista de Associados ao obter sucesso na procura por nome")
    void findByNomeReturnsListOfAssociadoWhenSuccessful() {
        String expectedName = AssociadoEntitiesBuilder.associadoBuilder().getNome();

        List<Associado> associados = associadoController.findByNome("associado").getBody();

        Assertions.assertThat(associados)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(associados.get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByNome: retorna uma lista vazia ao não encontrar Associados por nome")
    void findByNomeReturnsEmptyListOfAssociadoWhenAssociadoIsNotFound() {
        Mockito.when(associadoServiceMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());
        List<Associado> associados = associadoController.findByNome("associado").getBody();

        Assertions.assertThat(associados)
                .isNotNull()
                .isEmpty();
    }
    @Test
    @DisplayName("findByCpf: encontra um Associado pelo cpf e então retorna um Optional de Associado" +
            "ao obter sucesso")
    public void findAssociadoByCpfWhenSuccessful(){
        String expectedCpf = AssociadoEntitiesBuilder.associadoBuilder().getCpf();

        Associado associado = associadoController.findByCpf(AssociadoEntitiesBuilder.associadoBuilder().getCpf()).getBody();

        Assertions.assertThat(associado).isNotNull();

        Assertions.assertThat(associado.getCpf()).isNotNull().isEqualTo(expectedCpf);
    }
    @Test
    @DisplayName("findByCpf: falha ao tentar encontrar um Associado por cpf e então retorna nulo")
    public void failsToFindAssociadoByCpf(){
        Mockito.when(associadoServiceMock.findByCpf(ArgumentMatchers.anyString()))
                .thenReturn(null);

        Associado foundAssociado = associadoController.findByCpf("Different_Cpf").getBody();

        Assertions.assertThat(foundAssociado).isNull();
    }


    @Test
    @DisplayName("save: Salva e retorna um associado ao obter sucesso")
    void saveReturnsAssociadoWhenSuccessful() {


        Associado associado = associadoController.save(AssociadoEntitiesBuilder.associadoPostRequestBodyBuilder()).getBody();

        Assertions.assertThat(associado).isNotNull().isEqualTo(AssociadoEntitiesBuilder.associadoBuilder());

    }
    @Test
    @DisplayName("replace: Atualiza um associado ao obter sucesso")
    void replaceUpdatesAssociadoWhenSuccessful() {

        Assertions.assertThatCode(() -> associadoController.replace(AssociadoEntitiesBuilder.associadoPutRequestBodyBuilder()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = associadoController.replace(AssociadoEntitiesBuilder.associadoPutRequestBodyBuilder());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    @DisplayName("delete: Remove um associado ao obter sucesso")
    void deleteRemovesAssociadoWhenSuccessful() {

        Assertions.assertThatCode(() -> associadoController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = associadoController.delete(1);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DisplayName("definirVoto: Define o vote do Associado ao obter sucesso")
    void definirVotoDefinesVotoFromAssociadoWhenSuccessful() {

        ResponseEntity<Void> entity = associadoController.definirVoto(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}