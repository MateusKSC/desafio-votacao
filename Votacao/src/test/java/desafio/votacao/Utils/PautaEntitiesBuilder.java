package desafio.votacao.Utils;

import desafio.votacao.entities.Pauta;
import desafio.votacao.entities.Sessao;
import desafio.votacao.requests.PautaPostRequestBody;
import desafio.votacao.requests.PautaPutRequestBody;

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
}
