package desafio.votacao.v1.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.integration.SqlProvider;
import org.assertj.core.api.Assertions;
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

import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Sessao Controller")
class SessaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.insertSessao),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.resetarDB)
    })
    @DisplayName("GET/SUCESSO buscar uma Sessao")
    void buscarSessaoPorId() throws Exception {
        Sessao sessaoResponse = new Sessao();
        sessaoResponse.setMomentoDoInicio((Date.from(Instant.ofEpochSecond(1716210000))));
        sessaoResponse.setMomentoDoFim((Date.from(Instant.ofEpochSecond(1716210000))));
        sessaoResponse.setId(1L);
        MvcResult result = mockMvc.perform(get("/v1/sessoes/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Sessao sessaoPostReturn = mapper.readValue(content,Sessao.class);
        Assertions.assertThat(sessaoPostReturn).isEqualTo(sessaoResponse);

    }
    @Test
    @DisplayName("GET/Error ao buscar pelo id uma sessao inexistente ")
    public void testeGetIdDesconhecido() throws Exception {
        mockMvc.perform(get("/v1/sessoes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
