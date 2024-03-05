package desafio.votacao.v1.unit.services;

import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.repository.SessaoRepository;
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

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class SessaoServiceTest {
    @InjectMocks
    private SessaoServiceImpl sessaoService;


    @Mock
    private SessaoRepository sessaoRepositoryMock;

    @Mock
    private DateUtil dateUtil;


    @BeforeEach
    void setUp() {
        Mockito.when(sessaoRepositoryMock.findAll())
                .thenReturn(List.of(new Sessao()));

        Mockito.when(sessaoRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(new Sessao()));
    }

    @Test
    @DisplayName("list: Retorna uma Lista de Sessoes ao obter sucesso")
    void listReturnsListOfSessaosWhenSuccessful() {

        List<Sessao> sessoes = sessaoService.listAll();

        Assertions.assertThat(sessoes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("findById: retorna uma Sessao ao obter sucesso na procura por id")
    void findByIdReturnsSessaoWhenSuccessful() {

        Sessao sessao = sessaoService.findByIdOrThrowBadRequestException(1L);
        Assertions.assertThat(sessao).isNotNull();
    }
    @Test
    @DisplayName("save: salva uma sessao ao obter sucesso")
    void saveSessaoWhenSuccessful() {
        Assertions.assertThatCode(() -> sessaoService.save(new Sessao()))
                .doesNotThrowAnyException();
    }
}