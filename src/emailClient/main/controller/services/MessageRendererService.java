package emailClient.main.controller.services;

import emailClient.main.model.EmailMessage;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

public class MessageRendererService extends Service {

    private EmailMessage emailMessage;
    private WebEngine webEngine;
    private StringBuffer stringBuffer;

    public MessageRendererService(WebEngine webEngine) {
        this.webEngine = webEngine;
        stringBuffer = new StringBuffer();
        this.setOnSucceeded(e -> {
            displayMessage();
        });
    }

    public void setEmailMessage(EmailMessage emailMessage){
        this.emailMessage = emailMessage;
    }

    private void displayMessage(){
        webEngine.loadContent(stringBuffer.toString());
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    loadMessage();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private void loadMessage() throws MessagingException, IOException {
        stringBuffer.setLength(0); //clear the Buffer
        Message message = emailMessage.getMessage();
        String contentType = message.getContentType();
        if (isSimpleType(contentType)){
            stringBuffer.append(message.getContent().toString());
        } else if (isMultipartType(contentType)){
            Multipart multipart = (Multipart) message.getContent();
            loadMultipart(multipart, stringBuffer);
        }
    }

    private void loadMultipart(Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {
        for(int i = multipart.getCount() - 1; i >=0; i--){
            BodyPart bodyPart = multipart.getBodyPart(i);
            String bodyPartContentType = bodyPart.getContentType();
            if(isSimpleType(bodyPartContentType)){
                stringBuffer.append(bodyPart.getContent().toString());
            } else if (isMultipartType(bodyPartContentType)){
                Multipart multipart2 = (Multipart) bodyPart.getContent();
                loadMultipart(multipart2, stringBuffer);
            } else if (!isTextPlain(bodyPartContentType)){
                MimeBodyPart mbp = (MimeBodyPart) bodyPart;
                emailMessage.addAttachment(mbp);
            }
        }
    }

    private boolean isTextPlain(String contentType){
        return contentType.contains("TEXT/PLAIN");
    }

    private boolean isSimpleType(String contentType){
        if(contentType.contains("TEXT/HTML") ||
        contentType.contains("text")){
            return true;
        } else {
            return false;
        }
    }

    private boolean isMultipartType(String contentType){
        if(contentType.contains("multipart")){
            return  true;
        } else{
            return false;
        }
    }
}
