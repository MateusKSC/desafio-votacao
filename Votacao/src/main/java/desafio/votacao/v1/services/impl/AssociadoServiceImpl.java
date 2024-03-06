package desafio.votacao.v1.services.impl;

import desafio.votacao.v1.entities.Associado;
import desafio.votacao.v1.exceptions.BadRequestException;
import desafio.votacao.v1.mapper.AssociadoMapper;
import desafio.votacao.v1.repository.AssociadoRepository;
import desafio.votacao.v1.requests.AssociadoPostRequestBody;
import desafio.votacao.v1.requests.AssociadoPutRequestBody;
import desafio.votacao.v1.services.AssociadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public List<Associado> findByNome(String name) {
        List<Associado> associados = associadoRepository.findByNome(name);
        if (associados.isEmpty()) {
            throw new BadRequestException("Associado não encontrado!");
        }
        return associados;
    }

    public Associado findByCpf(String cpf) {
        Associado associado = associadoRepository.findByCpf(cpf);

        if (associado == null) {
            if (cpf.isEmpty()) {
                throw new BadRequestException("O campo do CPF do associado não pode estar vazio.");
            } else {
                throw new BadRequestException("Associado de cpf " + cpf + " não encontrado!");
            }
        }
        return associado;
    }

    public Associado findByIdOrThrowBadRequestException(long associadoId) {
        return associadoRepository.findById(associadoId)
                .orElseThrow(() -> new BadRequestException("Associado não encontrado!"));
    }

    @Transactional
    public Associado save(AssociadoPostRequestBody associadoPostRequestBody) {
        Associado associado = AssociadoMapper.INSTANCE.toAssociado(associadoPostRequestBody);
        Associado savedAssociado = new Associado();
        if (isCpfUnique(associadoPostRequestBody.getCpf(), associado, "save")) {
            savedAssociado = associadoRepository.save(associado);
            log.info("Associado salvo com sucesso!");
        }
        return savedAssociado;
    }
    public void replace(AssociadoPutRequestBody associadoPutRequestBody) {
        Associado savedAssociado = findByIdOrThrowBadRequestException(associadoPutRequestBody.getId());
        if(!(doesAssociadoHavePauta(savedAssociado))) {
            Associado associado = AssociadoMapper.INSTANCE.toAssociado(associadoPutRequestBody);
            associado.setId(savedAssociado.getId());
            associado.setPautas(savedAssociado.getPautas());
            associadoRepository.save(associado);
        }
        else{
            throw new BadRequestException("O associado ainda faz parte de uma pauta ativa!");
        }
    }
    public void delete(long associadoId) {
        Associado associado = findByIdOrThrowBadRequestException(associadoId);
        if(!(doesAssociadoHavePauta(associado))) {
            associadoRepository.delete(associado);
            log.info("O associado foi deletado com sucesso!");
        }
        else{
            throw new BadRequestException("O associado ainda faz parte de uma pauta ativa!");
        }
    }

    public void definirVoto(boolean voto, String cpf) {
        Associado savedAssociado = findByCpf(cpf);
        Associado associadoToBeSaved = new Associado();
        if (doesAssociadoHavePauta(savedAssociado) && didSessaoStart(savedAssociado)) {
            associadoToBeSaved.setId(savedAssociado.getId());
            associadoToBeSaved.setNome(savedAssociado.getNome());
            associadoToBeSaved.setVoto(voto);
            associadoToBeSaved.setPautas(savedAssociado.getPautas());
            associadoRepository.save(associadoToBeSaved);
        }
        else{
            throw new BadRequestException("O associado não faz parte de nenhuma" +
                    " pauta em fase de votação!");
        }
    }

    public void resetaVoto() {
        List<Associado> associados = associadoRepository.findAll();

        for (Associado associado : associados) {
            associado.setVoto(true);
            associadoRepository.save(associado);
        }
    }

    public boolean isCpfUnique(String cpf, Associado associadoEmValidacao, String requestMethod) {
        boolean associadoCpfUnique = false;
        List<Associado> associados = associadoRepository.findAll();
        if (!(associados.isEmpty())) {
            if (requestMethod.equals("put")) {
                for (Associado associado : associados) {
                    if ((associado.getCpf().equals(cpf)) && !(associado.getId().equals(associadoEmValidacao.getId()))) {
                        throw new BadRequestException("O CPF do associado informado já está registrado no " +
                                "banco de dados. Verifique o associado de nome " + associado.getNome());
                    } else {
                        associadoCpfUnique = true;
                    }
                }
            } else if (requestMethod.equals("save")) {
                for (Associado associado : associados) {
                    if (associado.getCpf().equals(cpf)) {
                        throw new BadRequestException("O CPF do associado informado já está registrado no " +
                                "banco de dados. Verifique o associado de nome " + associado.getNome());
                    } else {
                        associadoCpfUnique = true;
                    }
                }
            }
        } else {
            associadoCpfUnique = true;
        }
        return associadoCpfUnique;
    }

    public boolean doesAssociadoHavePauta(Associado associado) {
        boolean pautaExists;
        if (!(associado.getPautas().isEmpty())) {
            pautaExists = !(associado.getPautas().get(associado.getPautas()
                    .size() - 1).isConcluida());
        } else {
            pautaExists = false;

        }

        return pautaExists;
    }

    public boolean didSessaoStart(Associado associado) {
        if (!(associado.getPautas().get(associado.getPautas()
                .size() - 1).isSessaoIniciada())) {
            throw new BadRequestException("A sessão de votos da pauta ainda não" +
                    " foi aberta!");
        }
        return true;
    }
}
