package desafio.votacao.Utils;

import desafio.votacao.entities.Associado;
import desafio.votacao.entities.Pauta;
import desafio.votacao.requests.AssociadoPostRequestBody;
import desafio.votacao.requests.AssociadoPutRequestBody;

import java.util.Collections;
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
}
