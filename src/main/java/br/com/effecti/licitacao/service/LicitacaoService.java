package br.com.effecti.licitacao.service;

import br.com.effecti.licitacao.model.Integracao;
import br.com.effecti.licitacao.model.IntegracaoStatusEnum;
import br.com.effecti.licitacao.model.Licitacao;
import br.com.effecti.licitacao.provider.LicitacaoIntegracao;
import br.com.effecti.licitacao.provider.LicitacaoProvider;
import br.com.effecti.licitacao.repository.LicitacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LicitacaoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicitacaoService.class);
    private final LicitacaoRepository licitacaoRepository;
    private final LicitacaoProvider licitacaoProvider;
    private final IntegracaoService integracaoService;

    public LicitacaoService(LicitacaoRepository licitacaoRepository, LicitacaoProvider licitacaoProvider,
                            IntegracaoService integracaoService) {
        this.licitacaoRepository = licitacaoRepository;
        this.licitacaoProvider = licitacaoProvider;
        this.integracaoService = integracaoService;
    }

    public List<Licitacao> updateLicitacoesFromComprasNet() throws IOException {
        if (integracaoService.hasIntegracaoPendente()) {
            throw new IllegalStateException("Já existe uma integração pendente");
        }

        Integracao integracao = integracaoService.createIntegracaoPendente();

        try {
            LicitacaoIntegracao licitacaoIntegracao = licitacaoProvider.getLicitacoesFromRemoteHost();
            List<Licitacao> licitacoes = licitacaoIntegracao.getLicitacoes();

            Set<Long> identificadores = licitacoes.stream().map(Licitacao::getIdentificador).collect(Collectors.toSet());
            Set<Long> identificadoresExistentes = licitacaoRepository.findExistingIdentifiers(identificadores);
            licitacoes.removeIf(licitacao -> identificadoresExistentes.contains(licitacao.getIdentificador()));

            integracao.setStatus(licitacaoIntegracao.hasErros() ? IntegracaoStatusEnum.CONCLUIDA_COM_ERRO : IntegracaoStatusEnum.CONCLUIDA);
            integracao.setErros(licitacaoIntegracao.getErros().toString());
            return licitacaoRepository.saveAll(licitacoes);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro no processo de integração: ", e);
            integracao.setErros(e.getMessage());
            integracao.setStatus(IntegracaoStatusEnum.ERRO);
            throw new IllegalStateException("Ocorreu um erro no processo de integração", e);
        } finally {
            integracaoService.saveIntegracao(integracao);
        }
    }

    public List<Licitacao> findAll() {
        return licitacaoRepository.findAll();
    }

    public Optional<Licitacao> findById(Long id) {
        return licitacaoRepository.findById(id);
    }

    public Licitacao save(Licitacao licitacao) {
        return licitacaoRepository.save(licitacao);
    }

    public void deleteById(Long id) {
        licitacaoRepository.deleteById(id);
    }

    public Licitacao setLidaTrueById(Long id) {
        Licitacao licitacao = licitacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Licitação não encontrada com o ID: " + id));

        licitacao.setLida(true);
        return licitacaoRepository.save(licitacao);
    }
}
