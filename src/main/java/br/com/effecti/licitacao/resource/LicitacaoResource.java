package br.com.effecti.licitacao.resource;

import br.com.effecti.licitacao.model.Licitacao;
import br.com.effecti.licitacao.service.LicitacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path="/licitacoes")
public class LicitacaoResource {

    private final LicitacaoService licitacaoService;

    public LicitacaoResource(LicitacaoService licitacaoService) {
        this.licitacaoService = licitacaoService;
    }

    @GetMapping(path="/update")
    public ResponseEntity<List<Licitacao>> update() throws IOException {
        return ResponseEntity.ok(licitacaoService.updateLicitacoesFromComprasNet());
    }

    @GetMapping
    public ResponseEntity<List<Licitacao>> findAll() {
        return ResponseEntity.ok(licitacaoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Licitacao> findById(@PathVariable Long id) {
        return ResponseEntity.ok(licitacaoService.findById(id).get());
    }

    @PutMapping("/{id}/marcar-como-lida")
    public ResponseEntity<Licitacao> marcarLicitacaoComoLida(@PathVariable Long id) {
        return ResponseEntity.ok(licitacaoService.setLidaTrueById(id));
    }

}
