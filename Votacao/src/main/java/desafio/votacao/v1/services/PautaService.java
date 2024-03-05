package desafio.votacao.v1.services;

import desafio.votacao.v1.entities.Pauta;
import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.requests.PautaPostRequestBody;
import desafio.votacao.v1.requests.PautaPutRequestBody;

import java.util.List;

public interface PautaService {
    List<Pauta> listAll();

    List<Pauta> findByNome(String name);

    Pauta findByIdOrThrowBadRequestException(long pautaId);
    boolean verificaUnicidadeCpf(List<String> cpfAssociados);
    Pauta save(PautaPostRequestBody pautaPostRequestBody, List<String> cpfAssociados);
    void delete(long pautaId);

    void replace(PautaPutRequestBody pautaPutRequestBody);
    void prossegueComVotacao(Long pautaId);

    Sessao getSessaoFromPauta(Long id);


}
