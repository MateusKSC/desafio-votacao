package desafio.votacao.v1.integration.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import desafio.votacao.Utils.PautaEntitiesBuilder;
import desafio.votacao.v1.entities.Pauta;
import desafio.votacao.v1.integration.SqlProvider;
import desafio.votacao.v1.mapper.PautaMapper;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de testes de integração que verificam o funcionamento da comunicação pelo controller da Pauta
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Pauta Controller")
class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();

    private String envioComoJSON;


    @BeforeEach
    public void inicializar() throws JsonProcessingException {
        envioComoJSON = mapper.writeValueAsString(PautaEntitiesBuilder.pautaRequestBuilder());
    }

    @Test
    @DisplayName("Teste POST/SUCESSO salvar uma Pauta")
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.insertAssociado),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.resetarDB)
    })
    void savePauta() throws Exception {
        Pauta pautaRequest = PautaEntitiesBuilder.pautaResponseBuilder();
        Pauta pautaResponse = PautaEntitiesBuilder.pautaResponseBuilder();
        pautaRequest.setDataDeCriacao(null);
        pautaResponse.setDataDeCriacao(null);
        envioComoJSON = mapper.writeValueAsString(pautaRequest);
        MvcResult result = mockMvc.perform(post("/v1/pautas/associados/11111111111")
                        .content(envioComoJSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andReturn();
        String content = result.getResponse().getContentAsString();
        Pauta pautaPostReturn = mapper.readValue(content,Pauta.class);
        pautaPostReturn.setDataDeCriacao(null);
        Assertions.assertThat(pautaPostReturn).isEqualTo(pautaResponse);
    }
    @Test
    @DisplayName("Teste PUT atualizar uma Pauta")
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.insertPauta),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.resetarDB)
    })
    void replacePauta() throws Exception {
        Pauta pautaResponse = PautaMapper.INSTANCE.toPauta(
                PautaEntitiesBuilder.pautaPutRequestBodyBuilder());
        envioComoJSON = mapper.writeValueAsString(pautaResponse);
        mockMvc.perform(put("/v1/pautas/")
                        .content(envioComoJSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Teste DELETE deletar uma Pauta")
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.insertPauta),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.resetarDB)
    })
    void deletePauta() throws Exception {
        mockMvc.perform(delete("/v1/pautas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        mockMvc.perform(get("/v1/pautas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.insertAssociado),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.insertSessao),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.insertPauta),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.resetarDB)
    })
    @DisplayName("GET/SUCESSO buscar uma Pauta")
    void buscarPautaPorId() throws Exception {
        Pauta pautaResponse = PautaEntitiesBuilder.pautaResponseBuilder();
        pautaResponse.setDataDeCriacao(null);
        MvcResult result = mockMvc.perform(get("/v1/pautas/{id}", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        Pauta pautaPostReturn = mapper.readValue(content,Pauta.class);
        pautaPostReturn.setDataDeCriacao(null);
        Assertions.assertThat(pautaPostReturn).isEqualTo(pautaResponse);

    }
    @Test
    @DisplayName("GET/Error ao buscar pelo id uma pauta inexistente ")
    public void testeGetIdDesconhecido() throws Exception {
        mockMvc.perform(get("/v1/pautas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
