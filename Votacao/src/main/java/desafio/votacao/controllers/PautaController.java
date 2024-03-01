package desafio.votacao.controllers;

import desafio.votacao.entities.Pauta;
import desafio.votacao.requests.PautaPostRequestBody;
import desafio.votacao.requests.PautaPutRequestBody;
import desafio.votacao.services.PautaService;
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
@RequestMapping("v1/pautas")
@Log4j2
@RequiredArgsConstructor
public class PautaController {

    private final DateUtil dateUtil;
    private final PautaService pautaService;


    @GetMapping
    public ResponseEntity<List<Pauta>> list() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(pautaService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Pauta> findById(@PathVariable long id) {
        return ResponseEntity.ok(pautaService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Pauta>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(pautaService.findByName(name));
    }
    @PostMapping(path = "/associados/{cpf}")
    public ResponseEntity<Pauta> save(@RequestBody @Valid PautaPostRequestBody pautaPostRequestBody,@PathVariable List<String> cpf) {
        return new ResponseEntity<>(pautaService.save(pautaPostRequestBody, cpf), HttpStatus.CREATED);
    }
    @PostMapping(path = "/processos/votacao/pauta/{id}")
    public ResponseEntity<HttpStatus> processoDeVotacao(@RequestParam Long Id) {
        pautaService.processoDeVotacao(Id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody @Valid PautaPutRequestBody pautaPutRequestBody) {
        pautaService.replace(pautaPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{Id}")
    public ResponseEntity<Void> delete(@PathVariable long Id) {
        pautaService.delete(Id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
