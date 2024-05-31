package br.com.effecti.licitacao.provider;

import br.com.effecti.licitacao.model.Licitacao;

import java.util.List;

public class LicitacaoIntegracao {
    private List<Licitacao> licitacoes;
    private List<String> erros;

    public LicitacaoIntegracao() {

    }

    public LicitacaoIntegracao(List<Licitacao> licitacoes, List<String> erros) {
        this.licitacoes = licitacoes;
        this.erros = erros;
    }

    public List<Licitacao> getLicitacoes() {
        return licitacoes;
    }

    public void setLicitacoes(List<Licitacao> licitacoes) {
        this.licitacoes = licitacoes;
    }

    public List<String> getErros() {
        return erros;
    }

    public void setErros(List<String> erros) {
        this.erros = erros;
    }

    public boolean hasErros() {
        return this.erros != null && !this.erros.isEmpty();
    }
}
