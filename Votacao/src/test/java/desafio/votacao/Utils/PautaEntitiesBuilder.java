package desafio.votacao.Utils;

import desafio.votacao.v1.entities.Pauta;
import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.requests.PautaPostRequestBody;
import desafio.votacao.v1.requests.PautaPutRequestBody;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

public class PautaEntitiesBuilder {
    public static Pauta pautaBuilder(){
        Sessao sessao = Sessao.builder()
                .momentoDoInicio(new Date())
                .momentoDoFim(new Date(System.currentTimeMillis() + 60000))
                .build();
        return Pauta.builder()
                .id(1L)
                .nome("PautaTest")
                .descricao("PautaTesteDescricao")
                .associados(Collections.emptyList())
                .sessao(sessao)
                .build();
    }
    public static  PautaPostRequestBody pautaPostRequestBodyBuilder(){
        return PautaPostRequestBody.builder()
                .nome("PautaTest")
                .descricao("PautaTesteDescricao")
                .build();
    }
    public static  PautaPutRequestBody pautaPutRequestBodyBuilder(){
        return PautaPutRequestBody.builder()
                .id(1L)
                .nome("PautaTest")
                .descricao("PautaTesteDescricao")
                .build();
    }
    public static Pauta pautaRequestBuilder(){
        return Pauta.builder()
                .nome("Pauta Teste")
                .descricao("descricao")
                .associados(Collections.emptyList())
                .dataDeCriacao(Date.from(Instant.ofEpochSecond(1716210000)))
                .build();
    }
    public static Pauta pautaResponseBuilder(){
        return Pauta.builder()
                .id(1L)
                .nome("Pauta Teste")
                .descricao("descricao")
                .associados(Collections.emptyList())
                .dataDeCriacao(Date.from(Instant.ofEpochSecond(1716210000)))
                .build();
    }
}
