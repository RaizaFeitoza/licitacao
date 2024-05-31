package br.com.effecti.licitacao.service;

import br.com.effecti.licitacao.model.Integracao;
import br.com.effecti.licitacao.model.IntegracaoStatusEnum;
import br.com.effecti.licitacao.repository.IntegracaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IntegracaoService {

    private final IntegracaoRepository repository;

    public IntegracaoService(IntegracaoRepository integracaoRepository) {
        this.repository = integracaoRepository;
    }

    public Boolean hasIntegracaoPendente() {
        return repository.existsByStatus(IntegracaoStatusEnum.PENDENTE);
    }

    public Integracao createIntegracaoPendente() {
        Integracao integracao = new Integracao();
        integracao.setStatus(IntegracaoStatusEnum.PENDENTE);
        integracao.setDataHoraInicio(LocalDateTime.now());
        return repository.saveAndFlush(integracao);
    }

    public Integracao saveIntegracao(Integracao integracao) {
        integracao.setDataHoraFim(LocalDateTime.now());
        return repository.saveAndFlush(integracao);
    }

    public List<Integracao> findAll() {
        return repository.findAll();
    }
}
