package br.com.effecti.licitacao.service;

import br.com.effecti.licitacao.model.Integracao;
import br.com.effecti.licitacao.model.IntegracaoStatusEnum;
import br.com.effecti.licitacao.repository.IntegracaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntegracaoServiceTest {

    @Mock
    private IntegracaoRepository repository;

    @InjectMocks
    private IntegracaoService integracaoService;

    @Captor
    private ArgumentCaptor<Integracao> integracaoCaptor;

    private Integracao integracao;

    @BeforeEach
    void setUp() {
        integracao = new Integracao();
        integracao.setStatus(IntegracaoStatusEnum.PENDENTE);
        integracao.setDataHoraInicio(LocalDateTime.now());
    }

    @Test
    void shouldReturnTrueWhenHasIntegraacaoPendente() {
        when(repository.existsByStatus(IntegracaoStatusEnum.PENDENTE)).thenReturn(true);

        Boolean result = integracaoService.hasIntegracaoPendente();

        assertTrue(result);
        verify(repository).existsByStatus(IntegracaoStatusEnum.PENDENTE);
    }

    @Test
    void shouldCreateIntegracaoPendente() {
        when(repository.saveAndFlush(any(Integracao.class))).thenReturn(integracao);

        Integracao result = integracaoService.createIntegracaoPendente();

        assertNotNull(result);
        assertEquals(IntegracaoStatusEnum.PENDENTE, result.getStatus());
        assertNotNull(result.getDataHoraInicio());
        verify(repository).saveAndFlush(integracaoCaptor.capture());
        Integracao capturedIntegracao = integracaoCaptor.getValue();
        assertEquals(IntegracaoStatusEnum.PENDENTE, capturedIntegracao.getStatus());
        assertNotNull(capturedIntegracao.getDataHoraInicio());
    }

    @Test
    void shouldSaveIntegracao() {
        integracao.setDataHoraFim(LocalDateTime.now());
        when(repository.saveAndFlush(any(Integracao.class))).thenReturn(integracao);

        Integracao result = integracaoService.saveIntegracao(integracao);

        assertNotNull(result);
        assertEquals(integracao.getDataHoraFim(), result.getDataHoraFim());
        verify(repository).saveAndFlush(integracaoCaptor.capture());
        Integracao capturedIntegracao = integracaoCaptor.getValue();
        assertEquals(integracao.getDataHoraFim(), capturedIntegracao.getDataHoraFim());
    }

    @Test
    void shouldReturnIntegracoes() {
        when(repository.findAll()).thenReturn(Collections.singletonList(integracao));

        List<Integracao> result = integracaoService.findAll();

        assertEquals(1, result.size());
        assertEquals(integracao, result.get(0));
        verify(repository).findAll();
    }
}