package emailClient.main;

import emailClient.main.controller.EmailSendingResult;
import emailClient.main.controller.persistence.PersistenceAccess;
import emailClient.main.controller.persistence.ValidAccount;
import emailClient.main.controller.services.LoginService;
import emailClient.main.model.EmailAccount;
import emailClient.main.view.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private PersistenceAccess persistenceAccess = new PersistenceAccess();
    private EmailManager emailManager = new EmailManager();
    @Override
    public void start(Stage stage) throws Exception {
        ViewFactory viewFactory = new ViewFactory(emailManager);
        List<ValidAccount> validAccountList = persistenceAccess.loadFromPersistence();
        if(validAccountList.size()>0){
            viewFactory.showMainWindow();
            for (ValidAccount validAccount: validAccountList){
                EmailAccount emailAccount = new EmailAccount(validAccount.getAddress(), validAccount.getPassword());
                LoginService loginService = new LoginService(emailAccount, emailManager);
                loginService.start();
            }
        } else {
            viewFactory.showLoginWindow();
        }
    }

    private void checkPersistence() {

    }

    @Override
    public void stop() throws Exception {
        List<ValidAccount> validAccountList = new ArrayList<ValidAccount>();
        for (EmailAccount emailAccount : emailManager.getEmailAccounts()){
            validAccountList.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()));
        }
        persistenceAccess.saveToPersistence(validAccountList);
    }

}
