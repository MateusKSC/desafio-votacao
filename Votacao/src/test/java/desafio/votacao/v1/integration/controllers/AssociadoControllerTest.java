package desafio.votacao.v1.integration.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import desafio.votacao.Utils.AssociadoEntitiesBuilder;
import desafio.votacao.v1.entities.Associado;
import desafio.votacao.v1.integration.SqlProvider;
import desafio.votacao.v1.mapper.AssociadoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de testes de integração que verificam o funcionamento da comunicação pelo controller do Associado
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Associado Controller")
class AssociadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();

    private String retornoComoJson;
    private String envioComoJSON;


    @BeforeEach
    public void inicializar() throws JsonProcessingException {
        envioComoJSON = mapper.writeValueAsString(AssociadoEntitiesBuilder.associadoRequestBuilder());
        retornoComoJson = mapper.writeValueAsString(AssociadoEntitiesBuilder.associadoResponseBuilder());
    }

    @Test
    @DisplayName("Teste POST/SUCESSO salvar um Associado")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.resetarDB)
    void saveAssociado() throws Exception {
        mockMvc.perform(post("/v1/associados")
                        .content(envioComoJSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(retornoComoJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST/ERROR salvar um Associado com cpf de formato invalido")
    void saveAssociadoError() throws Exception {
        Associado associado = AssociadoEntitiesBuilder.associadoRequestBuilder();
        associado.setCpf("cpf inválido");
        envioComoJSON = mapper.writeValueAsString(associado);

        mockMvc.perform(post("/v1/associados")
                        .content(envioComoJSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Teste PUT atualizar um Associado")
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.insertAssociado),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.resetarDB)
    })
    void replaceAssociado() throws Exception {
        Associado associadoResponse = AssociadoMapper.INSTANCE.toAssociado(
                AssociadoEntitiesBuilder.associadoPutRequestBodyBuilder());
        envioComoJSON = mapper.writeValueAsString(associadoResponse);
        mockMvc.perform(put("/v1/associados/")
                        .content(envioComoJSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Teste DELETE deletar um Associado")
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.insertAssociado),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.resetarDB)
    })
    void deleteAssociado() throws Exception {
        mockMvc.perform(delete("/v1/associados/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE));

        mockMvc.perform(get("/v1/associados/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.insertAssociado),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.resetarDB)
    })
    @DisplayName("GET/SUCESSO buscar um Associado")
    void buscarAssociadoPorCpf() throws Exception {
        mockMvc.perform(get("/v1/associados/documento/{cpf}", "11111111111")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(retornoComoJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET/Error ao buscar pelo id uma pauta inexistente ")
    public void testeGetCpfDesconhecido() throws Exception {
        mockMvc.perform(get("/v1/associados/documento/{cpf}", "88888888888")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
