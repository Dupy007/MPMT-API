package com.dupy.MPMT.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private SimpleMailMessage message;

    @BeforeEach
    void setUp() {
        message = new SimpleMailMessage();
        message.setFrom("admin@mpmt.test");
        message.setTo("test@example.com");
        message.setSubject("Test Subject");
        message.setText("Test Body");
    }

    @Test
    void testSendEmail() {
        emailService.sendEmail("test@example.com", "Test Subject", "Test Body");

        verify(mailSender, times(1)).send(Mockito.any(SimpleMailMessage.class));
    }
}
