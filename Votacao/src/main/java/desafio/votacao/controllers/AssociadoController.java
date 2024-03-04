package desafio.votacao.controllers;

import desafio.votacao.entities.Associado;
import desafio.votacao.requests.AssociadoPostRequestBody;
import desafio.votacao.requests.AssociadoPutRequestBody;
import desafio.votacao.services.AssociadoService;
import desafio.votacao.utilities.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("v1/associados")
@Log4j2
@RequiredArgsConstructor
public class AssociadoController {

    private final DateUtil dateUtil;
    private final AssociadoService associadoService;

    @GetMapping
    public ResponseEntity<List<Associado>> list() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(associadoService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Associado> findById(@PathVariable long id) {
        return ResponseEntity.ok(associadoService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Associado>> findByNome(@RequestParam String name) {
        return ResponseEntity.ok(associadoService.findByNome(name));
    }
    @GetMapping(path = "/{cpf}")
    public ResponseEntity<Associado> findByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(associadoService.findByCpf(cpf));
    }

    @PostMapping
    public ResponseEntity<Associado> save(@RequestBody @Valid AssociadoPostRequestBody associadoPostRequestBody) {
        return new ResponseEntity<>(associadoService.save(associadoPostRequestBody), HttpStatus.CREATED);
    }

    @PostMapping(path = "votacao/{cpf}/{voto}")
    public ResponseEntity<Void> definirVoto(@PathVariable boolean voto, @PathVariable String cpf) {
        associadoService.definirVoto(voto, cpf);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody @Valid AssociadoPutRequestBody AssociadoPutRequestBody) {
        associadoService.replace(AssociadoPutRequestBody);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping(path = "/{Id}")
    public ResponseEntity<Void> delete(@PathVariable long Id) {
        associadoService.delete(Id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
