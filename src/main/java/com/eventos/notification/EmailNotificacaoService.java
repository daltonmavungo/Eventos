package com.eventos.notification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificacaoService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.remetente}")
    private String remetente;

    @Value("${app.mail.nome-remetente}")
    private String nomeRemetente;

    // ──────────────────────────────────────
    // PAGAMENTO CONFIRMADO
    // ──────────────────────────────────────
    @Async("notificacaoExecutor")
    public void alertarPagamento(String emailDest, String nomeDest, String tituloEvento,
                                  BigDecimal valor, String metodo, Integer pagamentoId) {
        String assunto = "✅ Pagamento recebido — " + tituloEvento;
        String html = template("Pagamento Recebido", "#27ae60",
            "<h2 style='color:#27ae60;margin:0 0 16px'>✅ Novo Pagamento Registado</h2>" +
            "<p>Olá <strong>" + nomeDest + "</strong>,</p>" +
            "<p>Um pagamento foi confirmado no teu evento.</p>" +
            tabela(new String[][]{
                {"Evento",    tituloEvento},
                {"Referência","#" + pagamentoId},
                {"Valor",     "AOA " + valor.toPlainString()},
                {"Método",    metodo}
            })
        );
        enviar(emailDest, assunto, html);
    }

    // ──────────────────────────────────────
    // EVENTO CRIADO
    // ──────────────────────────────────────
    @Async("notificacaoExecutor")
    public void alertarEventoCriado(String emailDest, String nomeDest, String titulo,
                                     String local, String data) {
        String assunto = "📅 Evento criado — " + titulo;
        String html = template("Evento Criado", "#2980b9",
            "<h2 style='color:#2980b9;margin:0 0 16px'>📅 Evento Criado com Sucesso!</h2>" +
            "<p>Olá <strong>" + nomeDest + "</strong>,</p>" +
            "<p>O teu evento foi registado no sistema.</p>" +
            tabela(new String[][]{
                {"Título", titulo},
                {"Data",   data},
                {"Local",  local}
            })
        );
        enviar(emailDest, assunto, html);
    }

    // ──────────────────────────────────────
    // CONVITE ENVIADO → vai para o CONVIDADO
    // ──────────────────────────────────────
    @Async("notificacaoExecutor")
    public void alertarConviteEnviado(String emailConvidado, String nomeConvidado,
                                       String tituloEvento, String dataEvento,
                                       String codigoConvite) {
        String assunto = "🎟️ Tens um convite — " + tituloEvento;
        String html = template("Convite para Evento", "#8e44ad",
            "<h2 style='color:#8e44ad;margin:0 0 16px'>🎟️ Foste Convidado!</h2>" +
            "<p>Olá <strong>" + nomeConvidado + "</strong>,</p>" +
            "<p>Tens um convite para participar no seguinte evento:</p>" +
            tabela(new String[][]{
                {"Evento", tituloEvento},
                {"Data",   dataEvento}
            }) +
            "<div style='margin:24px 0;text-align:center;'>" +
            "  <p style='margin:0 0 8px;color:#666;'>O teu código de convite:</p>" +
            "  <span style='display:inline-block;background:#8e44ad;color:#fff;padding:12px 28px;" +
            "border-radius:6px;font-size:22px;font-weight:bold;letter-spacing:3px;'>" +
            codigoConvite + "</span>" +
            "</div>" +
            "<p style='color:#888;font-size:13px;'>Usa este código para confirmar a tua presença.</p>"
        );
        enviar(emailConvidado, assunto, html);
    }

    // ──────────────────────────────────────
    // CONFIRMAÇÃO → vai para o ORGANIZADOR
    // ──────────────────────────────────────
    @Async("notificacaoExecutor")
    public void alertarConfirmacao(String emailOrg, String nomeOrg, String nomeConvidado,
                                    String tituloEvento, String status) {
        boolean confirmou = "CONFIRMADO".equalsIgnoreCase(status);
        String cor     = confirmou ? "#27ae60" : "#e74c3c";
        String icone   = confirmou ? "🎉" : "❌";
        String assunto = icone + " " + (confirmou ? "Confirmação" : "Recusa") + " de presença — " + tituloEvento;
        String html = template(confirmou ? "Presença Confirmada" : "Presença Recusada", cor,
            "<h2 style='color:" + cor + ";margin:0 0 16px'>" + icone + " Resposta ao Convite</h2>" +
            "<p>Olá <strong>" + nomeOrg + "</strong>,</p>" +
            "<p>O convidado respondeu ao convite para o teu evento.</p>" +
            tabela(new String[][]{
                {"Evento",    tituloEvento},
                {"Convidado", nomeConvidado},
                {"Resposta",  "<strong style='color:" + cor + "'>" + status + "</strong>"}
            })
        );
        enviar(emailOrg, assunto, html);
    }

    // ──────────────────────────────────────
    // HELPERS
    // ──────────────────────────────────────
    private void enviar(String para, String assunto, String html) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
            h.setFrom(remetente, nomeRemetente);
            h.setTo(para);
            h.setSubject(assunto);
            h.setText(html, true);
            mailSender.send(msg);
            log.info("📧 Email enviado → {} | {}", para, assunto);
        } catch (Exception e) {
            log.error("❌ Falha email para {}: {}", para, e.getMessage());
        }
    }

    private String tabela(String[][] linhas) {
        StringBuilder sb = new StringBuilder(
            "<table style='width:100%;border-collapse:collapse;margin:16px 0;'>");
        for (String[] linha : linhas) {
            sb.append("<tr>")
              .append("<td style='padding:10px 14px;border:1px solid #e0e0e0;background:#f8f8f8;" +
                      "font-weight:600;width:38%;color:#555;'>").append(linha[0]).append("</td>")
              .append("<td style='padding:10px 14px;border:1px solid #e0e0e0;'>").append(linha[1]).append("</td>")
              .append("</tr>");
        }
        return sb.append("</table>").toString();
    }

    private String template(String titulo, String cor, String conteudo) {
        return "<!DOCTYPE html><html><body style='margin:0;padding:0;background:#f4f4f4;font-family:Arial,sans-serif;'>" +
               "<div style='max-width:600px;margin:30px auto;background:#fff;border-radius:10px;" +
               "overflow:hidden;box-shadow:0 4px 12px rgba(0,0,0,.1);'>" +
               "<div style='background:" + cor + ";padding:24px 30px;'>" +
               "<h1 style='color:#fff;margin:0;font-size:20px;'>🎪 Gestão de Eventos — " + titulo + "</h1>" +
               "</div>" +
               "<div style='padding:28px 30px;'>" + conteudo + "</div>" +
               "<div style='background:#f8f8f8;padding:14px 30px;text-align:center;" +
               "color:#aaa;font-size:12px;border-top:1px solid #eee;'>" +
               "Email automático · Não responda a esta mensagem</div>" +
               "</div></body></html>";
    }
}
