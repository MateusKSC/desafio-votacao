package desafio.votacao.v1.services.impl;

import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.exceptions.BadRequestException;
import desafio.votacao.v1.repository.SessaoRepository;
import desafio.votacao.v1.services.SessaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação das funções de Serviço da Sessao
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class SessaoServiceImpl implements SessaoService {

    private final SessaoRepository sessaoRepository;

    public List<Sessao> listAll() {
        return sessaoRepository.findAll();
    }

    public Sessao findByIdOrThrowBadRequestException(long sessaoId) {
        return sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new BadRequestException("Sessão não encontrada!"));
    }

    public void save(Sessao sessao) {
            sessaoRepository.save(sessao);
    }

}
