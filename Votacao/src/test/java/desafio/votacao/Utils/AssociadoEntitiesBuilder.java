package desafio.votacao.Utils;

import desafio.votacao.v1.entities.Associado;
import desafio.votacao.v1.entities.Pauta;
import desafio.votacao.v1.requests.AssociadoPostRequestBody;
import desafio.votacao.v1.requests.AssociadoPutRequestBody;

import java.util.List;

public class AssociadoEntitiesBuilder {
    public static Associado associadoBuilder(){
        Pauta pauta = new Pauta();
        pauta.setSessaoIniciada(true);
        return Associado.builder()
                .id(1L)
                .nome("AssociadoTest")
                .cpf("00000000000")
                .pautas(List.of(pauta))
                .build();
    }
    public static  AssociadoPostRequestBody associadoPostRequestBodyBuilder(){
        return AssociadoPostRequestBody.builder()
                .nome("AssociadoTest")
                .cpf("00001000000")
                .build();
    }
    public static  AssociadoPutRequestBody associadoPutRequestBodyBuilder(){
        return AssociadoPutRequestBody.builder()
                .id(1L)
                .nome("AssociadoTest")
                .cpf("00002000000")
                .build();
    }
    public static Associado associadoRequestBuilder(){
        return Associado.builder()
                .nome("Associado Teste")
                .cpf("11111111111")
                .build();
    }
    public static Associado associadoResponseBuilder(){
        return Associado.builder()
                .id(1L)
                .nome("Associado Teste")
                .cpf("11111111111")
                .build();
    }
}
