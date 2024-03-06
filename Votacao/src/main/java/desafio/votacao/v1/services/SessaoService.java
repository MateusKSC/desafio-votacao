package desafio.votacao.v1.services;

import desafio.votacao.v1.entities.Sessao;

import java.util.List;
/**
 * Funções de Serviço da Sessao
 */
public interface SessaoService {
     /**
      * Lista todas as Sessoes
      */
     List<Sessao> listAll();

     /**
      * Encontra uma Sessao pelo id
      */
     Sessao findByIdOrThrowBadRequestException(long sessaoId);
}
