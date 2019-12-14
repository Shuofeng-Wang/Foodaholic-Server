package com.jhuoose.foodaholic.utils;

import com.jhuoose.foodaholic.Server;
import com.sendgrid.*;

import java.io.IOException;
import java.util.List;

public class EmailUtil {
    private static EmailUtil ourInstance;

    static {
        ourInstance = new EmailUtil(Server.getSendGrid());
    }

    public static EmailUtil getInstance() {
        return ourInstance;
    }

    private SendGrid sendGrid;
    private Email from = new Email("no-reply@foodaholic-server.herokuapp.com", "Foodaholic");

    private EmailUtil(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void sendTestEmail() throws IOException {
        String subject = "Hello World from the SendGrid Java Library!";
        Email to = new Email("lihr04@outlook.com", "Haoran Li");
        Content content = new Content("text/plain", "Hello, Email!");
        Mail mail = new Mail(from, subject, to, content);
        sendEmail(mail);
    }

    public void sendEmailToOne(String subject, String email, String body) throws IOException {
        Email to = new Email(email);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);
        sendEmail(mail);
    }

    public void sendEmail(String subject, List<String> emailList, String body) throws IOException {
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        var personalization = new Personalization();
        for (String email: emailList) {
            Email to = new Email(email);
            personalization.addTo(to);
        }
        mail.addPersonalization(personalization);
        Content content = new Content("text/plain", body);
        mail.addContent(content);
        sendEmail(mail);
    }

    public void sendEmail(Mail mail) throws IOException {
        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sendGrid.api(request);
        } catch (IOException e) {
            throw e;
        }
    }
}
