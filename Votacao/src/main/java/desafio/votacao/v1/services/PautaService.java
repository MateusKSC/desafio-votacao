package desafio.votacao.v1.services;

import desafio.votacao.v1.entities.Pauta;
import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.requests.PautaPostRequestBody;
import desafio.votacao.v1.requests.PautaPutRequestBody;

import java.util.List;

public interface PautaService {
    List<Pauta> listAll();

    List<Pauta> findByNome(String name);

    Pauta findByIdOrThrowBadRequestException(long id);
    boolean verificaUnicidadeCpf(List<String> cpfAssociados);
    Pauta save(PautaPostRequestBody pautaPostRequestBody, List<String> cpfAssociados);
    void delete(long id);

    void replace(PautaPutRequestBody pautaPutRequestBody);
    void prossegueComVotacaoTempoPadrao(Long id);
    void prossegueComVotacaoTempoDefinido(Long id, int tempo);

    Sessao getSessaoFromPauta(Long id);


}
