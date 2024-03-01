package desafio.votacao.services;

import desafio.votacao.entities.Sessao;

import java.util.List;

public interface SessaoService {
     List<Sessao> listAll();

     Sessao findByIdOrThrowBadRequestException(long sessaoId);
}
