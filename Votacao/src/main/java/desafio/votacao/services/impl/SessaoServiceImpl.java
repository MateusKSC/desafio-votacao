package desafio.votacao.services.impl;

import desafio.votacao.entities.Sessao;
import desafio.votacao.exceptions.BadRequestException;
import desafio.votacao.repository.SessaoRepository;
import desafio.votacao.services.SessaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() -> new BadRequestException("Sessao não encontrada!"));
    }

    public void save(Sessao sessao) {
            sessaoRepository.save(sessao);
    }

}
