package desafio.votacao.services.impl;

import desafio.votacao.entities.Associado;
import desafio.votacao.exceptions.BadRequestException;
import desafio.votacao.mapper.AssociadoMapper;
import desafio.votacao.repository.AssociadoRepository;
import desafio.votacao.requests.AssociadoPostRequestBody;
import desafio.votacao.requests.AssociadoPutRequestBody;
import desafio.votacao.services.AssociadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
            if(cpf.isEmpty()){
                throw new BadRequestException("O campo do CPF do associado não pode estar vazio.");
            }
            else {
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
        if (isCpfUnique(associadoPostRequestBody.getCpf(), associado, "save")) {
            associadoRepository.save(associado);
            log.info("Associado salvo com sucesso");
        }
        return associado;
    }

    public void delete(long associadoId) {
        Associado associado = findByIdOrThrowBadRequestException(associadoId);
        associadoRepository.delete(findByIdOrThrowBadRequestException(associadoId));
        log.info("The given associado was successfully deleted");
    }

    public void definirVoto(AssociadoPutRequestBody associadoPutRequestBody) {
        Associado savedAssociado = findByCpf(associadoPutRequestBody.getCpf());
        if (doesAssociadoHavePauta(savedAssociado)) {
            Associado associado = AssociadoMapper.INSTANCE.toAssociado(associadoPutRequestBody);
            associado.setId(savedAssociado.getId());
            associado.setNome(savedAssociado.getNome());
            associado.setPautas(savedAssociado.getPautas());
            associadoRepository.save(associado);
        } else {
            throw new BadRequestException("O associado não está participando de nenhuma" +
                    " pauta ativa.");
        }
    }

    public void encerraVotacao() {
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
        boolean associadoHasPauta;
        if (!(associado.getPautas().isEmpty())) {
            if (!(associado.getPautas().get(associado.getPautas()
                    .size() - 1).isConcluida())) {
                associadoHasPauta = true;
            } else {
                associadoHasPauta = false;
            }
        } else {
            associadoHasPauta = false;

        }

        return associadoHasPauta;
    }
}
