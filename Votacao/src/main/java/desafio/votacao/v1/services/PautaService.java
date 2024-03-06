package desafio.votacao.v1.services;

import desafio.votacao.v1.entities.Pauta;
import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.requests.PautaPostRequestBody;
import desafio.votacao.v1.requests.PautaPutRequestBody;

import java.util.List;

/**
 * Funções de Serviço da Pauta
 */
public interface PautaService {
    /**
     * Lista todas as Pautas
     */
    List<Pauta> listAll();

    /**
     * Encontra uma pauta pelo nome
     */
    List<Pauta> findByNome(String name);

    /**
     * Encontra uma pauta pelo id
     */
    Pauta findByIdOrThrowBadRequestException(long id);

    /**
     * Verifica se o usuário forneceu CPFs repetidos
     */
    boolean verificaUnicidadeCpf(List<String> cpfAssociados);

    /**
     * Salva uma pauta
     */
    Pauta save(PautaPostRequestBody pautaPostRequestBody, List<String> cpfAssociados);

    /**
     * Deleta uma pauta
     */
    void delete(long id);

    /**
     * Substitui uma Pauta
     */
    void replace(PautaPutRequestBody pautaPutRequestBody);

    /**
     * Prossegue com o processo de votação utilizando o tempo padrão de sessao
     */
    void prossegueComVotacaoTempoPadrao(Long id);

    /**
     * Prossegue com o processo de votação utilizando um tempo definido de sessao
     */
    void prossegueComVotacaoTempoDefinido(Long id, int tempo);

    /**
     * Obtém a sessao de uma pauta
     */
    Sessao getSessaoFromPauta(Long id);

}
