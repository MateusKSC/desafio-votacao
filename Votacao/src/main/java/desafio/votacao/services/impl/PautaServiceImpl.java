package desafio.votacao.services.impl;

import desafio.votacao.entities.Associado;
import desafio.votacao.entities.Pauta;
import desafio.votacao.entities.Sessao;
import desafio.votacao.exceptions.BadRequestException;
import desafio.votacao.mapper.PautaMapper;
import desafio.votacao.repository.PautaRepository;
import desafio.votacao.requests.PautaPostRequestBody;
import desafio.votacao.requests.PautaPutRequestBody;
import desafio.votacao.services.PautaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class PautaServiceImpl implements PautaService {
    @Autowired
    private final PautaRepository pautaRepository;

    private final AssociadoServiceImpl associadoService;

    private final SessaoServiceImpl sessaoService;

    public List<Pauta> listAll() {
        return pautaRepository.findAll();
    }

    public List<Pauta> findByNome(String nome) {
        List<Pauta> pautas = pautaRepository.findByNome(nome);
        if (pautas.isEmpty()) {
            throw new BadRequestException("Pauta não encontrada!");
        }
        return pautas;
    }

    public Pauta findByIdOrThrowBadRequestException(long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Pauta não encontrada!"));
    }

    public Sessao getSessaoFromPauta(Long id){
        Pauta pauta = findByIdOrThrowBadRequestException(id);
        Sessao sessao;
        if(pauta.getSessao() != null){
            sessao = pauta.getSessao();
        }
        else{
            throw new BadRequestException("A pauta informada não possui nenhuma sessão" +
                    " de votação registrada!");
        }
        return (sessao);
    }

    public boolean verificaUnicidadeCpf(List<String> cpfAssociados) {
        boolean cpfUnico = false;
        for (String string : cpfAssociados) {
            if (Collections.frequency(cpfAssociados, string) == 1) {
                cpfUnico = true;
            } else {
                throw new BadRequestException("Alguns CPFs foram fornecidos de forma" +
                        " repetida! Tente novamente.");
            }
        }
        return cpfUnico;
    }

    @Transactional
    public Pauta save(PautaPostRequestBody pautaPostRequestBody, List<String> cpfAssociados) {
        String ultimoAssociado = "";
        List<Associado> associados = new ArrayList<>();
        Pauta pauta = new Pauta();
        pauta.setNome(pautaPostRequestBody.getNome());
        pauta.setDescricao(pautaPostRequestBody.getDescricao());
        for (String cpfAssociado : cpfAssociados) {
            if (associadoService.findByCpf(cpfAssociado) != null && (verificaUnicidadeCpf(cpfAssociados))) {
                if (!(associadoService.doesAssociadoHavePauta(
                        associadoService.findByCpf(cpfAssociado)))) {
                    ultimoAssociado = cpfAssociado;
                    associados.add(associadoService.findByCpf(cpfAssociado));
                } else {
                    throw new BadRequestException("O associado informado já possui uma pauta" +
                            " ativa no momento!");
                }
            } else {
                throw new BadRequestException("O registro da pauta não foi possível, pois " +
                        "o associado de cpf '" + ultimoAssociado + "' não foi encontrado!" +
                        " Tente novamente.");
            }
        }

        for (Associado associado : associados) {
            pauta.adicionaAssociados(associado);
        }

        Pauta savedPauta = pautaRepository.save(pauta);
        log.info("Pauta salva com sucesso");
        return (savedPauta);
    }

    public void delete(long pautaId) {
        Pauta pauta = findByIdOrThrowBadRequestException(pautaId);
        pautaRepository.delete(pauta);
        log.info("The given pauta was successfully deleted");
    }

    public void replace(PautaPutRequestBody pautaPutRequestBody) {
        Pauta savedPauta = findByIdOrThrowBadRequestException(pautaPutRequestBody.getId());
        if(!(savedPauta.isSessaoIniciada())) {
            Pauta pauta = PautaMapper.INSTANCE.toPauta(pautaPutRequestBody);
            pauta.setId(savedPauta.getId());
            pauta.setAssociados(savedPauta.getAssociados());
            pautaRepository.save(pauta);
        }
        else{
            throw new BadRequestException("A modificação de uma pauta só é permitida antes do" +
                    "processo de votação ter dado início!");
        }
    }
    public long calculaTempoRestante(Date agora, Date fim)
    {
        long diffInMs = Math.abs(fim.getTime() - agora.getTime());
        return TimeUnit.SECONDS.convert(diffInMs, TimeUnit.MILLISECONDS);
    }
    public void prossegueComVotacao(Long pautaId) {
        Date presente = new Date();
        Pauta pauta = findByIdOrThrowBadRequestException(pautaId);
        List<Associado> associados = pauta.getAssociados();
        if (pauta.isSessaoIniciada()) {
            if (!(pauta.isConcluida())) {
                if (presente.after(pauta.getSessao().getMomentoDoFim())) {
                    for (Associado associado : associados) {
                        pauta.incrementaVotos(associado.isVoto());
                    }
                    pauta.verificaResultadoVotacao();
                    associadoService.resetaVoto();
                    pauta.setConcluida(true);
                    pautaRepository.save(pauta);
                } else {
                    throw new BadRequestException("O tempo para votação ainda não acabou" +
                            " (restam " + calculaTempoRestante(presente, pauta.getSessao().getMomentoDoFim())
                            + " segundos)!");
                }
            } else {
                throw new BadRequestException("A pauta informada já teve sua votação " +
                        "finalizada.");
            }
        } else {
            Sessao sessao = new Sessao();
            pauta.setSessao(sessao);
            pauta.setSessaoIniciada(true);
            sessaoService.save(sessao);
            pautaRepository.save(pauta);
        }
    }

}
