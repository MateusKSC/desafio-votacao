package desafio.votacao.v1.controllers;

import desafio.votacao.v1.entities.Sessao;
import desafio.votacao.v1.services.SessaoService;
import desafio.votacao.v1.utilities.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe de controller da Sessao
 */
@RestController
@RequestMapping("v1/sessoes")
@Log4j2
@RequiredArgsConstructor
public class SessaoController {

    private final DateUtil dateUtil;
    private final SessaoService sessaoService;

    @GetMapping
    public ResponseEntity<List<Sessao>> list() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(sessaoService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Sessao> findById(@PathVariable long id) {
        return ResponseEntity.ok(sessaoService.findByIdOrThrowBadRequestException(id));
    }
}
