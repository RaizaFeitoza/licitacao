package br.com.effecti.licitacao.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Licitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IDENTIFICADOR")
    private Long identificador;

    @Column(name = "ORGAO", columnDefinition="TEXT")
    private String orgao;

    @Column(name = "LICITACAO_NUMERO")
    private String licitacaoNumero;

    @Column(name = "CODIGO_UASG")
    private Long codigoUasg;

    @Enumerated(EnumType.STRING)
    @Column(name = "LICITACAO_TIPO_ENUM")
    private LicitacaoTipoEnum licitacaoTipoEnum;

    @Column(name = "OBJETO", columnDefinition="TEXT")
    private String objeto;

    @Column(name = "EDITAL_INICIO")
    private LocalDateTime editalInicio;

    @Column(name = "EDITAL_FIM")
    private LocalDateTime editalFim;

    @Column(name = "ENDERECO", columnDefinition="TEXT")
    private String endereco;

    @Column(name = "CIDADE")
    private String cidade;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "TELEFONE")
    private String telefone;

    @Column(name = "FAX")
    private String fax;

    @Column(name = "ENTREGA_PROPOSTA")
    private LocalDateTime entregaProposta;

    @Column(name = "ABERTURA_PROPOSTA")
    private LocalDateTime aberturaProposta;

    @Column(name = "LIDA")
    private Boolean lida = false;

    public Licitacao(Long id, String orgao, String licitacaoNumero, LicitacaoTipoEnum licitacaoTipoEnum, String objeto,
                     LocalDateTime editalInicio, LocalDateTime editalFim, String endereco, String cidade,
                     String estado, String telefone, String fax, LocalDateTime entregaProposta, LocalDateTime aberturaProposta,
                     Boolean lida, Long codigoUasg, Long identificador) {
        this.id = id;
        this.orgao = orgao;
        this.licitacaoNumero = licitacaoNumero;
        this.licitacaoTipoEnum = licitacaoTipoEnum;
        this.objeto = objeto;
        this.editalInicio = editalInicio;
        this.editalFim = editalFim;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.telefone = telefone;
        this.fax = fax;
        this.entregaProposta = entregaProposta;
        this.aberturaProposta = aberturaProposta;
        this.lida = lida;
        this.codigoUasg = codigoUasg;
        this.identificador = identificador;
    }

    public Licitacao() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Long identificador) {
        this.identificador = identificador;
    }

    public String getOrgao() {
        return orgao;
    }

    public void setOrgao(String orgao) {
        this.orgao = orgao;
    }

    public String getLicitacaoNumero() {
        return licitacaoNumero;
    }

    public void setLicitacaoNumero(String licitacaoNumero) {
        this.licitacaoNumero = licitacaoNumero;
    }

    public LicitacaoTipoEnum getLicitacaoTipoEnum() {
        return licitacaoTipoEnum;
    }

    public void setLicitacaoTipoEnum(LicitacaoTipoEnum licitacaoTipoEnum) {
        this.licitacaoTipoEnum = licitacaoTipoEnum;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public LocalDateTime getEditalInicio() {
        return editalInicio;
    }

    public void setEditalInicio(LocalDateTime editalInicio) {
        this.editalInicio = editalInicio;
    }

    public LocalDateTime getEditalFim() {
        return editalFim;
    }

    public void setEditalFim(LocalDateTime editalFim) {
        this.editalFim = editalFim;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public LocalDateTime getEntregaProposta() {
        return entregaProposta;
    }

    public void setEntregaProposta(LocalDateTime entregaProposta) {
        this.entregaProposta = entregaProposta;
    }

    public LocalDateTime getAberturaProposta() {
        return aberturaProposta;
    }

    public void setAberturaProposta(LocalDateTime aberturaProposta) {
        this.aberturaProposta = aberturaProposta;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    public Long getCodigoUasg() {
        return codigoUasg;
    }

    public void setCodigoUasg(Long codigoUasg) {
        this.codigoUasg = codigoUasg;
    }
}
