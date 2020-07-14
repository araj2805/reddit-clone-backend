package org.taker.reddit.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.taker.reddit.exception.SpringRedditException;
import org.taker.reddit.model.NotificationEmail;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {


    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendMail(NotificationEmail notificationEmail) throws MailException {

        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("springreddit@email.com");
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try {
            mailSender.send(mimeMessagePreparator);
            log.info("Actvation Mail Sent");
        } catch (MailException e) {
            log.error("Exception occured when sending mail to " + notificationEmail.getRecipient());
            throw new SpringRedditException("Exception occured when sending mail to " + notificationEmail.getRecipient());
        }
    }
}
