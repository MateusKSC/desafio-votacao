package desafio.votacao.v1.services;

import desafio.votacao.v1.entities.Associado;
import desafio.votacao.v1.requests.AssociadoPostRequestBody;
import desafio.votacao.v1.requests.AssociadoPutRequestBody;
import desafio.votacao.v1.requests.AssociadoVotoDTO;

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
    List<Associado> encontrarPeloNome(String name);

    /**
     * Encontra um Associado pelo cpf
     */
    Associado encontrarPeloCpf(String cpf);

    /**
     * Encontra um Associado pelo id
     */
    Associado encontrarPeloId(long id);

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

   void definirVoto(AssociadoVotoDTO associadoVotoDTO);

    void resetaVoto();

    /**
     * Substitui as informações de um associado
     */
    void replace(AssociadoPutRequestBody associadoPutRequestBody);
}
