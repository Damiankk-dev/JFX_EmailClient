package emailClient.main;

import emailClient.main.controller.services.FetchFoldersService;
import emailClient.main.controller.services.FolderUpdaterService;
import emailClient.main.model.EmailAccount;
import emailClient.main.model.EmailMessage;
import emailClient.main.model.EmailTreeItem;
import emailClient.main.view.IconResolver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {
    private EmailMessage selectedMessage;
    private EmailTreeItem<String> selectedFolder;
    private ObservableList<EmailAccount> emailAccounts = FXCollections.observableArrayList();
    public ObservableList<EmailAccount> getEmailAccounts(){
        return emailAccounts;
    }
    private IconResolver iconResolver = new IconResolver();
    private FolderUpdaterService folderUpdaterService;
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");
    public EmailTreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }
    private List<Folder> folderList = new ArrayList<Folder>();

    public List<Folder> getFolderList() {
        return this.folderList;
    }

    public EmailManager(){
        folderUpdaterService = new FolderUpdaterService((folderList));
        folderUpdaterService.start();
    }

    public void addEmailAccount(EmailAccount emailAccount){
        emailAccounts.add(emailAccount);
        EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        treeItem.setGraphic(iconResolver.getIconForFolder(emailAccount.getAddress()));
        FetchFoldersService fetchFoldersService = new FetchFoldersService(emailAccount.getStore(), treeItem, folderList);
        fetchFoldersService.start();
        treeItem.setExpanded(true);
        foldersRoot.getChildren().add(treeItem);
    }

    public EmailTreeItem<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public EmailMessage getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public void setRead() {
        try{
            selectedMessage.setRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            selectedFolder.decrementMessagesCount();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setUnread() {
        try{
            selectedMessage.setRead(false);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, false);
            selectedFolder.incrementMessagesCount();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteSelectedMessage() {
        try{
            selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true);
            selectedFolder.getEmailMessages().remove(selectedMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
