package com.espe.edu.AuthServer.service;

import com.espe.edu.AuthServer.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoConfirmacion(Usuario usuario) {
        String subject = "Verifica tu cuenta";
        String body = "Hola " + usuario.getNombre() + ",\n\n"
                + "Gracias por registrarte. Confirma tu cuenta accediendo al siguiente enlace:\n"
                + "http://localhost:8080/auth/verify?email=" + usuario.getEmail() + "\n\n"
                + "Este enlace expira en 1 hora.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(usuario.getEmail());
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
