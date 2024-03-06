package desafio.votacao.v1.unit.controllers;

import desafio.votacao.v1.controllers.SessaoController;
import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.services.SessaoService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * Classe de testes que verificam o funcionamento do controller da Sessao
 */
@ExtendWith(SpringExtension.class)
class SessaoControllerTest {
    @InjectMocks
    private SessaoController sessaoController;


    @Mock
    private SessaoService sessaoServiceMock;

    @Mock
    private DateUtil dateUtil;


    @BeforeEach
    void setUp() {
        Mockito.when(sessaoServiceMock.listAll())
                .thenReturn(List.of(new Sessao()));

        Mockito.when(sessaoServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(new Sessao());
    }

    @Test
    @DisplayName("list: Retorna uma Lista de Sessoes ao obter sucesso")
    void listReturnsListOfSessaosWhenSuccessful() {

        List<Sessao> sessoes = sessaoController.list().getBody();

        Assertions.assertThat(sessoes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("findById: retorna uma Sessao ao obter sucesso na procura por id")
    void findByIdReturnsSessaoWhenSuccessful() {

        Sessao sessao = sessaoController.findById(1L).getBody();
        Assertions.assertThat(sessao).isNotNull();
    }

    @Test
    @DisplayName("findById: falha ao tentar encontrar uma Sessao por id e então retorna nulo")
    public void failsToFindSessaoById(){
        Mockito.when(sessaoServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(null);
        ResponseEntity<Sessao> foundSessao = sessaoController.findById(10L);

        Assertions.assertThat(foundSessao.getBody()).isNull();
    }
}