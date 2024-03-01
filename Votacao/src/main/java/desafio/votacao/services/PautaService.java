package desafio.votacao.services;

import desafio.votacao.entities.Pauta;
import desafio.votacao.entities.Sessao;
import desafio.votacao.requests.PautaPostRequestBody;
import desafio.votacao.requests.PautaPutRequestBody;

import java.util.List;

public interface PautaService {
    List<Pauta> listAll();

    List<Pauta> findByName(String name);

    Pauta findByIdOrThrowBadRequestException(long pautaId);
    boolean verificaUnicidadeCpf(List<String> cpfAssociados);
    Pauta save(PautaPostRequestBody pautaPostRequestBody, List<String> cpfAssociados);
    void delete(long pautaId);

    void replace(PautaPutRequestBody pautaPutRequestBody);
    void processoDeVotacao(Long pautaId);

    Sessao getSessaoFromPauta(Long id);

}
