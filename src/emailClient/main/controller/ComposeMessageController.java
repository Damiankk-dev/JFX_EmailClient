package emailClient.main.controller;

import emailClient.main.EmailManager;
import emailClient.main.controller.services.EmailSenderService;
import emailClient.main.model.EmailAccount;
import emailClient.main.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComposeMessageController extends BaseController implements Initializable {
    private List<File> attachments = new ArrayList<File>();
    public ComposeMessageController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }
    @FXML
    private Label errorLabel;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private TextField recipientTextField;

    @FXML
    private TextField subjectTextField;
    @FXML
    private ChoiceBox<EmailAccount> emailAccountChoice;

    @FXML
    void sendButtonAction(ActionEvent event) {
        EmailSenderService emailSenderService = new EmailSenderService(
                emailAccountChoice.getValue(),
                subjectTextField.getText(),
                recipientTextField.getText(),
                htmlEditor.getHtmlText(),
                attachments
        );
        emailSenderService.start();
        emailSenderService.setOnSucceeded(e->{
            EmailSendingResult emailSendingResult = emailSenderService.getValue();
            switch (emailSendingResult){
                case SUCCESS:
                    Stage stage = (Stage) recipientTextField.getScene().getWindow();
                    viewFactory.closeStage(stage);
                    break;
                case FAILED_BY_PROVIDER:
                    errorLabel.setText("Provider error");
                    break;
                case FAILED_BY_UNEXPECTED_ERROR:
                    errorLabel.setText("Unexpected error");
                    break;
            }
        });
    }

    @FXML
    void attachButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null){
            attachments.add(selectedFile);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailAccountChoice.setItems(emailManager.getEmailAccounts());
        emailAccountChoice.setValue(emailManager.getEmailAccounts().get(0));
    }
}
