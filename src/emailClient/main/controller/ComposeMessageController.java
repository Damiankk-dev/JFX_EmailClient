package emailClient.main.controller;

import emailClient.main.EmailManager;
import emailClient.main.model.EmailAccount;
import emailClient.main.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import java.net.URL;
import java.util.ResourceBundle;

public class ComposeMessageController extends BaseController implements Initializable {
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
        System.out.println(htmlEditor.getHtmlText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailAccountChoice.setItems(emailManager.getEmailAccounts());
        emailAccountChoice.setValue(emailManager.getEmailAccounts().get(0));
    }
}
