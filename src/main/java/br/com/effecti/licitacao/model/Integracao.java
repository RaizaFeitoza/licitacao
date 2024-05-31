package br.com.effecti.licitacao.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Integracao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "STATUS")
    private IntegracaoStatusEnum status;

    @Column(name = "ERROS", columnDefinition="TEXT")
    private String erros;

    @Column(name = "DATA_HORA_INICIO")
    private LocalDateTime dataHoraInicio;

    @Column(name = "DATA_HORA_FIM")
    private LocalDateTime dataHoraFim;

    public Integracao() {

    }

    public Integracao(Long id, IntegracaoStatusEnum status, String erros, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        this.id = id;
        this.status = status;
        this.erros = erros;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IntegracaoStatusEnum getStatus() {
        return status;
    }

    public void setStatus(IntegracaoStatusEnum status) {
        this.status = status;
    }

    public String getErros() {
        return erros;
    }

    public void setErros(String erros) {
        this.erros = erros;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }
}
