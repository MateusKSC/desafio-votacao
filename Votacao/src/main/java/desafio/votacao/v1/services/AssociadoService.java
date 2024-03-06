package desafio.votacao.v1.services;

import desafio.votacao.v1.entities.Associado;
import desafio.votacao.v1.requests.AssociadoPostRequestBody;
import desafio.votacao.v1.requests.AssociadoPutRequestBody;

import java.util.List;

/**
 * Funções de Serviço do Associado
 */
public interface AssociadoService {

    /**
     * Lista todos os Associados
     */
    List<Associado> listAll();

    /**
     * Encontra um Associado pelo nome
     */
    List<Associado> findByNome(String name);

    /**
     * Encontra um Associado pelo cpf
     */
    Associado findByCpf(String cpf);

    /**
     * Encontra um Associado pelo id
     */
    Associado findByIdOrThrowBadRequestException(long id);

    /**
     * Salva um Associado do Banco
     */
    Associado save(AssociadoPostRequestBody associadoPostRequestBody);

    /**
     * Remove um Associado do Banco
     */
    void delete(long id);

    /**
     * Define o voto do Associado
     */
    void definirVoto(boolean voto, String cpf);

    /**
     * Reseta o valor das variáveis relativas ao voto
     */
    void resetaVoto();

    /**
     * Verifica se um cpf de um associado já está cadastrado
     */
    boolean isCpfUnique(String cpf, Associado associadoEmValidacao, String requestMethod);

    /**
     * Verifica se um associado está cadasstrado em uma pauta com votação em andamento.
     */
    boolean doesAssociadoHavePauta(Associado associado);

    /**
     * Substitui as informações de um associado
     */
    void replace(AssociadoPutRequestBody associadoPutRequestBody);
}
