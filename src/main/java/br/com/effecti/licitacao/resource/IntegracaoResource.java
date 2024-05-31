package br.com.effecti.licitacao.resource;

import br.com.effecti.licitacao.model.Integracao;
import br.com.effecti.licitacao.service.IntegracaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/integracoes")
public class IntegracaoResource {

    private final IntegracaoService integracaoService;

    public IntegracaoResource(IntegracaoService integracaoService) {
        this.integracaoService = integracaoService;
    }

    @GetMapping
    public ResponseEntity<List<Integracao>> findAll() {
        return ResponseEntity.ok(integracaoService.findAll());
    }

}
