package br.com.effecti.licitacao.provider;

import java.io.IOException;

public interface LicitacaoProvider {
    LicitacaoIntegracao getLicitacoesFromRemoteHost() throws IOException;
}
