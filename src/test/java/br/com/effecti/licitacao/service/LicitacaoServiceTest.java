package br.com.effecti.licitacao.service;

import br.com.effecti.licitacao.model.Integracao;
import br.com.effecti.licitacao.model.IntegracaoStatusEnum;
import br.com.effecti.licitacao.model.Licitacao;
import br.com.effecti.licitacao.provider.LicitacaoIntegracao;
import br.com.effecti.licitacao.provider.LicitacaoProvider;
import br.com.effecti.licitacao.repository.LicitacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LicitacaoServiceTest {

    @Mock
    private LicitacaoRepository licitacaoRepository;

    @Mock
    private LicitacaoProvider licitacaoProvider;

    @Mock
    private IntegracaoService integracaoService;

    @InjectMocks
    private LicitacaoService licitacaoService;

    @Captor
    private ArgumentCaptor<List<Licitacao>> licitacaoListCaptor;

    private Licitacao licitacao;
    private Integracao integracao;

    @BeforeEach
    void setUp() {
        licitacao = new Licitacao();
        licitacao.setIdentificador(1L);
        licitacao.setLida(false);

        integracao = new Integracao();
        integracao.setStatus(IntegracaoStatusEnum.PENDENTE);
    }

    @Test
    void shouldUpdateLicitacoesFromProvider() throws IOException {
        when(integracaoService.hasIntegracaoPendente()).thenReturn(false);
        when(integracaoService.createIntegracaoPendente()).thenReturn(integracao);
        LicitacaoIntegracao licitacaoIntegracao = mock(LicitacaoIntegracao.class);
        List<Licitacao> licitacoesMock = new ArrayList<>();
        licitacoesMock.add(licitacao);
        when(licitacaoIntegracao.getLicitacoes()).thenReturn(licitacoesMock);
        when(licitacaoProvider.getLicitacoesFromRemoteHost()).thenReturn(licitacaoIntegracao);
        when(licitacaoRepository.findExistingIdentifiers(anySet())).thenReturn(Collections.emptySet());
        when(licitacaoRepository.saveAll(anyList())).thenReturn(Collections.singletonList(licitacao));

        List<Licitacao> result = licitacaoService.updateLicitacoesFromComprasNet();

        assertEquals(1, result.size());
        assertEquals(licitacao, result.get(0));
        verify(licitacaoRepository).saveAll(licitacaoListCaptor.capture());
        List<Licitacao> capturedLicitacoes = licitacaoListCaptor.getValue();
        assertEquals(1, capturedLicitacoes.size());
        assertEquals(licitacao, capturedLicitacoes.get(0));
        verify(integracaoService).saveIntegracao(integracao);
    }

    @Test
    void shouldThrowExceptionIfHasIntegracaoPendente() {
        when(integracaoService.hasIntegracaoPendente()).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> licitacaoService.updateLicitacoesFromComprasNet());

        verify(integracaoService, never()).createIntegracaoPendente();
    }

    @Test
    void shouldReturnAllLicitacoesFromDb() {
        when(licitacaoRepository.findAll()).thenReturn(Collections.singletonList(licitacao));

        List<Licitacao> result = licitacaoService.findAll();

        assertEquals(1, result.size());
        assertEquals(licitacao, result.get(0));
        verify(licitacaoRepository).findAll();
    }

    @Test
    void shouldReturnLicitacaoById() {
        when(licitacaoRepository.findById(1L)).thenReturn(Optional.of(licitacao));

        Optional<Licitacao> result = licitacaoService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(licitacao, result.get());
        verify(licitacaoRepository).findById(1L);
    }

    @Test
    void shouldSaveLicitacao() {
        when(licitacaoRepository.save(licitacao)).thenReturn(licitacao);

        Licitacao result = licitacaoService.save(licitacao);

        assertEquals(licitacao, result);
        verify(licitacaoRepository).save(licitacao);
    }

    @Test
    void shouldDeleteLicitacaoById() {
        licitacaoService.deleteById(1L);

        verify(licitacaoRepository).deleteById(1L);
    }

    @Test
    void shouldSetLidaTrueById() {
        when(licitacaoRepository.findById(1L)).thenReturn(Optional.of(licitacao));
        when(licitacaoRepository.save(licitacao)).thenReturn(licitacao);

        Licitacao result = licitacaoService.setLidaTrueById(1L);

        assertTrue(result.getLida());
        verify(licitacaoRepository).findById(1L);
        verify(licitacaoRepository).save(licitacao);
    }
}
