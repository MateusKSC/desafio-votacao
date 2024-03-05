package desafio.votacao.v1.services;

import desafio.votacao.v1.entities.Associado;
import desafio.votacao.v1.requests.AssociadoPostRequestBody;
import desafio.votacao.v1.requests.AssociadoPutRequestBody;

import java.util.List;

public interface AssociadoService {
    List<Associado> listAll();

    List<Associado> findByNome(String name);

    Associado findByCpf(String cpf);

    Associado findByIdOrThrowBadRequestException(long id);

    Associado save(AssociadoPostRequestBody associadoPostRequestBody);

    void delete(long id);

    void definirVoto(boolean voto, String cpf);

    void resetaVoto();

    boolean isCpfUnique(String cpf, Associado associadoEmValidacao, String requestMethod);

    boolean doesAssociadoHavePauta(Associado associado);

    void replace(AssociadoPutRequestBody associadoPutRequestBody);
}
