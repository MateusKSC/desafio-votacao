package desafio.votacao.v1.services.impl;

import desafio.votacao.v1.entities.Associado;
import desafio.votacao.v1.entities.Pauta;
import desafio.votacao.v1.exceptions.BadRequestException;
import desafio.votacao.v1.mapper.AssociadoMapper;
import desafio.votacao.v1.repository.AssociadoRepository;
import desafio.votacao.v1.requests.AssociadoPostRequestBody;
import desafio.votacao.v1.requests.AssociadoPutRequestBody;
import desafio.votacao.v1.requests.AssociadoVotoDTO;
import desafio.votacao.v1.services.AssociadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Implementação das funções de Serviço do Associado
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class AssociadoServiceImpl implements AssociadoService {

    private final AssociadoRepository associadoRepository;

    public List<Associado> listAll() {
        return associadoRepository.findAll();
    }

    public List<Associado> encontrarPeloNome(String name) {
        List<Associado> associados = associadoRepository.findByNome(name);
        verificaNomeInexistente(associados);
        return associados;
    }
    private void verificaNomeInexistente(List<Associado> associados){
        if (associados.isEmpty()) {
            throw new BadRequestException("Associado não encontrado!");
        }
    }

    public Associado encontrarPeloCpf(String cpf) {
        Associado associado = associadoRepository.findByCpf(cpf);
        verificaSeCpfEstaVazio(cpf);
        verificaCpfInexistente(associado,cpf);
        return associado;
    }

    private void verificaSeCpfEstaVazio(String cpf){
        if (cpf.isEmpty()) throw new BadRequestException("O campo do CPF do associado não pode estar vazio!");
    }
    private void verificaCpfInexistente(Associado associado, String cpf){
        if (associado == null) throw new BadRequestException("Associado de cpf " + cpf + " não encontrado!");
    }

    public Associado encontrarPeloId(long associadoId) {
        return associadoRepository.findById(associadoId)
                .orElseThrow(() -> new BadRequestException("Associado não encontrado!"));
    }

    @Transactional
    public Associado save(AssociadoPostRequestBody associadoPostRequestBody) {
        Associado associado = AssociadoMapper.INSTANCE.toAssociado(associadoPostRequestBody);
        Associado savedAssociado = new Associado();
        if (verificaUnicidadeDoCpfAoCriarAssociado(associadoPostRequestBody.getCpf())) {
            savedAssociado = associadoRepository.save(associado);
            log.info("O associado foi salvo com sucesso!");
        }
        return savedAssociado;
    }

    public void replace(AssociadoPutRequestBody associadoPutRequestBody) {
        Associado savedAssociado = encontrarPeloId(associadoPutRequestBody.getId());
        if (!(verificaSeAssociadoPossuiPautaAberta(savedAssociado))) {
            verificaUnicidadeDoCpfAoAlterarAssociadoExistente(savedAssociado.getCpf(), savedAssociado.getId());
            Associado associado = AssociadoMapper.INSTANCE.toAssociado(associadoPutRequestBody);
            associado.setId(savedAssociado.getId());
            associado.setPautas(savedAssociado.getPautas());
            associadoRepository.save(associado);
            log.info("O associado foi atualizado com sucesso!");
        }
    }

    public void delete(long associadoId) {
        Associado associado = encontrarPeloId(associadoId);
        if (!(verificaSeAssociadoPossuiPautaAberta(associado))) {
            associadoRepository.delete(associado);
            log.info("O associado foi deletado com sucesso!");
        }
    }

    public void definirVoto(AssociadoVotoDTO associadoVotoDTO) {
        Associado associadoSalvo = encontrarPeloId(associadoVotoDTO.getId());
        Associado associadoParaSalvamento = new Associado();
        verificaSePautaEstaEmVotacao(associadoSalvo);
        verificaSeSessaoEstaAberta(associadoSalvo);
        associadoParaSalvamento.setId(associadoSalvo.getId());
        associadoParaSalvamento.setNome(associadoSalvo.getNome());
        associadoParaSalvamento.setVoto(associadoVotoDTO.isVoto());
        associadoParaSalvamento.setPautas(associadoSalvo.getPautas());
        associadoParaSalvamento.setCpf(associadoSalvo.getCpf());

        associadoRepository.save(associadoParaSalvamento);
    }
    private void verificaSePautaEstaEmVotacao(Associado associado) {
        if (!(verificaSeAssociadoPossuiPautaAberta(associado) && verificaSeASessaoDeVotacaoIniciou(associado))) throw new
                BadRequestException("O associado não faz parte de nenhuma pauta em fase de votação!");
    }

    private void verificaUnicidadeDoCpfAoAlterarAssociadoExistente(String cpf, Long id) {
        List<Associado> associados = associadoRepository.findAllByCpf(cpf);
        if (!(associados.size() == 1 && id.equals(associados.get(0).getId()))) {
            throw new BadRequestException("O CPF do associado informado já está registrado no " +
                    "banco de dados. Verifique o associado de nome " + associados.get(0).getNome());
        }
    }

    public boolean verificaUnicidadeDoCpfAoCriarAssociado(String cpf) {
        List<Associado> associados = associadoRepository.findAllByCpf(cpf);
        if (!(associados.isEmpty())) {
            throw new BadRequestException("O CPF do associado informado já está registrado no " +
                    "banco de dados. Verifique o associado de nome " + associados.get(0).getNome());
        }
        return true;
    }

    public boolean verificaSeAssociadoPossuiPautaAberta(Associado associado) {
        verificaSeAssociadoNaoPossuiPautas(associado);
        if(obtemUltimaPauta(associado).isConcluida()) throw new BadRequestException("O associado ainda faz parte de uma pauta ativa!");
        return true;
    }
    private void verificaSeAssociadoNaoPossuiPautas(Associado associado) {
        if (associado.getPautas().isEmpty()) throw new BadRequestException("O associado não faz parte de nenhuma pauta!");
    }


    private void verificaSeSessaoEstaAberta(Associado associado) {
        Date presente = new Date();
        if(presente.after(obtemUltimaPauta(associado).getSessao().getMomentoDoFim())) throw new
                BadRequestException("O periodo da sessao de voto já acabou!");
    }

    private boolean verificaSeASessaoDeVotacaoIniciou(Associado associado) {
        if (!(obtemUltimaPauta(associado).isSessaoIniciada())) {
            throw new BadRequestException("A sessão de votos da pauta ainda não" +
                    " foi aberta!");
        }
        return true;
    }
    public void resetaVoto() {
        List<Associado> associados = associadoRepository.findAll();

        for (Associado associado : associados) {
            associado.setVoto(true);
            associadoRepository.save(associado);
        }
    }
    private Pauta obtemUltimaPauta(Associado associado) {
        Pauta pauta;
        if(associado.getPautas().size() == 1) {
            pauta = associado.getPautas().get(0);
        }
        else{
            pauta = associado.getPautas().get(associado.getPautas()
                    .size() - 1);
        }
        return pauta;
    }
}
