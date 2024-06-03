package br.com.effecti.licitacao.provider;

import br.com.effecti.licitacao.model.Licitacao;
import br.com.effecti.licitacao.model.LicitacaoTipoEnum;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ComprasNetLicitacaoProvider implements LicitacaoProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComprasNetLicitacaoProvider.class);
    private static final Map<String, LicitacaoTipoEnum> TIPO_LICITACAO_ENUM_MAP;

    @Value("${comprasnet.consultaLicitacoesUrl}")
    private String url;

    static {
        TIPO_LICITACAO_ENUM_MAP = new HashMap<>();
        TIPO_LICITACAO_ENUM_MAP.put("pregão", LicitacaoTipoEnum.PREGAO);
        TIPO_LICITACAO_ENUM_MAP.put("tomada de preço", LicitacaoTipoEnum.TOMADA_PRECO);
        TIPO_LICITACAO_ENUM_MAP.put("concorrência", LicitacaoTipoEnum.CONCORRENCIA);
        TIPO_LICITACAO_ENUM_MAP.put("rdc", LicitacaoTipoEnum.RDC);
        TIPO_LICITACAO_ENUM_MAP.put("convite", LicitacaoTipoEnum.CONVITE);
        TIPO_LICITACAO_ENUM_MAP.put("concurso", LicitacaoTipoEnum.CONCURSO);
    }

    @Override
    public LicitacaoIntegracao getLicitacoesFromRemoteHost() throws IOException {
        List<Licitacao> licitacoes = new ArrayList<>();
        List<String> erros = new ArrayList<>();
        int pagina = 1;

        while (true) {
            Document doc = Jsoup.parse(new URL(url + pagina).openStream(), "ISO-8859-1", url);

            Elements licitacoesForms = doc.select("form");

            for (Element licitacaoForm : licitacoesForms) {
                try {
                    Licitacao licitacao = new Licitacao();
                    licitacao.setOrgao(getOrgao(licitacaoForm));
                    licitacao.setCodigoUasg(getCodigoUASG(licitacaoForm));
                    licitacao.setLicitacaoNumero(getLicitacaoNumero(licitacaoForm));
                    licitacao.setLicitacaoTipoEnum(getTipoLicitacao(licitacaoForm));
                    licitacao.setObjeto(getObjeto(licitacaoForm));
                    licitacao.setEditalInicio(getDataHoraInicio(licitacaoForm));
                    licitacao.setEditalFim(getDataHoraFim(licitacaoForm));
                    licitacao.setEndereco(getEndereco(licitacaoForm));
                    licitacao.setCidade(getCidade(licitacaoForm));
                    licitacao.setEstado(getEstado(licitacaoForm));
                    licitacao.setTelefone(getTelefone(licitacaoForm));
                    licitacao.setFax(getFax(licitacaoForm));
                    if (!licitacaoForm.select("b:contains(Abertura da Proposta)").isEmpty()) {
                        licitacao.setAberturaProposta(getAberturaProposta(licitacaoForm));
                    }
                    licitacao.setEntregaProposta(getEntregaProposta(licitacaoForm));
                    licitacao.setIdentificador(generateIdentificadorFromLicitacao(licitacao));
                    licitacoes.add(licitacao);
                } catch (Exception e) {
                    LOGGER.error("Ocorreu um erro ao obter integracao: ", e);
                    erros.add(e.getMessage());
                }
            }

            pagina++;

            if (!hasProximaPagina(doc)) {
                break;
            }
        }

        return new LicitacaoIntegracao(licitacoes, erros);
    }

    private static Long generateIdentificadorFromLicitacao(Licitacao licitacao) {
        return Long.parseLong(licitacao.getLicitacaoNumero().replace("/", "")) + licitacao.getCodigoUasg();
    }
    private static Boolean hasProximaPagina(Document licitacaoPage) {
        return !licitacaoPage.select("input[type=button][name=proxima][value=Proximas]").isEmpty();
    }
    private static LocalDateTime getEntregaProposta(Element licitacaoForm) {
        String entregaProposta = removeSpecialSpaceChar(licitacaoForm.select("b:contains(Entrega da Proposta)").first().nextSibling().toString().trim());
        DateTimeFormatter entregaPropostaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm'Hs'");
        return LocalDateTime.parse(entregaProposta, entregaPropostaFormatter);
    }

    private static LocalDateTime getAberturaProposta(Element licitacaoForm) {
        String aberturaProposta = removeSpecialSpaceChar(licitacaoForm.select(
                "b:contains(Abertura da Proposta)").first().nextSibling().toString().trim());
        String aberturaPropostaStr = aberturaProposta.split(",")[0];
        DateTimeFormatter aberturaPropostaFormatter = DateTimeFormatter.ofPattern(" 'em' dd/MM/yyyy 'às' HH:mm'Hs'");
        return LocalDateTime.parse(aberturaPropostaStr, aberturaPropostaFormatter);
    }

    private static String getFax(Element licitacaoForm) {
        return removeSpecialSpaceChar(licitacaoForm.select("b:contains(Fax)").first().nextSibling().toString().trim());
    }

    private static String getTelefone(Element licitacaoForm) {
        return removeSpecialSpaceChar(licitacaoForm.select("b:contains(Telefone)").first().nextSibling().toString().trim());
    }

    private static String getEstado(Element licitacaoForm) {
        String endereco = getEndereco(licitacaoForm);
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(endereco);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private static String getCidade(Element licitacaoForm) {
        String endereco = getEndereco(licitacaoForm);
        String[] enderecoSplit = endereco.split("-");
        return enderecoSplit[enderecoSplit.length - 1].split("\\(")[0].trim();
    }

    private static String getEndereco(Element licitacaoForm) {
        return removeSpecialSpaceChar(licitacaoForm.select("b:contains(Endereço)").first().nextSibling().toString().trim());
    }

    private static LocalDateTime getDataHoraInicio(Element licitacaoForm) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'das' HH:mm");
        String[] editalHoras = licitacaoForm.select("b:contains(Edital a partir de)").first().nextSibling().toString().split("às");
        String editalInicioStr = removeSpecialSpaceChar(editalHoras[0]).strip();
        return LocalDateTime.parse(editalInicioStr, formatter);
    }

    private static LocalDateTime getDataHoraFim(Element licitacaoForm) {
        String[] editalHoras = licitacaoForm.select("b:contains(Edital a partir de)").first().nextSibling().toString().split("às");
        String editalInicioStr = removeSpecialSpaceChar(editalHoras[0]).strip();
        String editalFimStr = editalInicioStr.split(" das")[0] +
                (editalHoras.length > 2 ? editalHoras[2] : (editalHoras.length > 1 ? editalHoras[1] : "")).trim();
        DateTimeFormatter editalFimFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyyHH:mm 'Hs'");
        return LocalDateTime.parse(editalFimStr, editalFimFormatter);
    }

    private static String getObjeto(Element licitacaoForm) {
        return removeSpecialSpaceChar(licitacaoForm.select("b:contains(Objeto)").first().nextSibling().toString().trim());
    }

    private static Long getCodigoUASG(Element licitacaoForm) {
        Elements boldElements = licitacaoForm.select("b");

        for (Element element : boldElements) {
            String text = element.text();
            if (text.contains("Código da UASG")) {
                String[] parts = text.split("\n");
                String codigoStr = parts[0].trim().split("Código da UASG: ")[1].trim();
                return Long.valueOf(codigoStr);
            }
        }

        return null;
    }

    private static String getOrgao(Element licitacaoForm) {
        Elements boldElements = licitacaoForm.select("b");

        for (Element element : boldElements) {
            String text = element.text();
            if (text.contains("Código da UASG")) {
                String[] parts = text.split("\n");
                return parts[0].trim().split("Código da UASG")[0];
            }
        }

        return null;
    }

    private static String getLicitacaoNumero(Element licitacaoForm) {
        String boldElementsHTML = licitacaoForm.select("b").text();
        Pattern pattern = Pattern.compile("Nº\\s*(\\d+/\\d+)");
        Matcher matcher = pattern.matcher(boldElementsHTML);

        return matcher.find() ? matcher.group(1) : null;
    }

    private static LicitacaoTipoEnum getTipoLicitacao(Element licitacaoForm) {
        String text = licitacaoForm.text().toLowerCase();
        for (Map.Entry<String, LicitacaoTipoEnum> entry : TIPO_LICITACAO_ENUM_MAP.entrySet()) {
            if (text.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static String removeSpecialSpaceChar(String string) {
        return string.replace("&nbsp;", "");
    }
}
