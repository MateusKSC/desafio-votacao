package desafio.votacao.v1.services;

import desafio.votacao.v1.entities.Sessao;

import java.util.List;

public interface SessaoService {
     List<Sessao> listAll();

     Sessao findByIdOrThrowBadRequestException(long sessaoId);
}
