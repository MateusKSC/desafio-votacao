package desafio.votacao.services;

import desafio.votacao.entities.Associado;
import desafio.votacao.requests.AssociadoPostRequestBody;
import desafio.votacao.requests.AssociadoPutRequestBody;

import java.util.List;

public interface AssociadoService {
    List<Associado> listAll();

    List<Associado> findByNome(String name);

    Associado findByCpf(String cpf);

    Associado findByIdOrThrowBadRequestException(long id);

    Associado save(AssociadoPostRequestBody associadoPostRequestBody);

    void delete(long id);

    void definirVoto(AssociadoPutRequestBody associadoPutRequestBody);

    void encerraVotacao();

    boolean isCpfUnique(String cpf, Associado associadoEmValidacao, String requestMethod);

    boolean doesAssociadoHavePauta(Associado associado);

}
