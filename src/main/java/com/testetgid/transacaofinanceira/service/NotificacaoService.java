package com.testetgid.transacaofinanceira.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.testetgid.transacaofinanceira.model.Cliente;
import com.testetgid.transacaofinanceira.model.Empresa;
import com.testetgid.transacaofinanceira.model.Transacao;

@Service
public class NotificacaoService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${webhook.site.url}")
    private String webhookSiteUrl;

    public void notificarCliente(Transacao transacao, Cliente cliente) {
        String destinatario = cliente.getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Notificação de Transação");
        message.setText("Transação realizada! Detalhes : " + transacao.toString());

        // Envia o e-mail
        javaMailSender.send(message);

        System.out.println("Notificação enviada por e-mail para " + destinatario);
    }

    public void notificarCallback(Transacao transacao, Empresa empresa, String mensagem) {

        String webhookEndPoint = "https://webhook.site/157aa9fa-d74f-409c-a209-689173e0784e";

        RestTemplate restTemplate = new RestTemplate();

        String requestBody = "Mensagem: " + mensagem + ". Detalhes da transação: " + transacao.toString();

        // Faz a requisição HTTP Poat para endpoint
        try {
            restTemplate.postForObject(webhookEndPoint, requestBody, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }
}
