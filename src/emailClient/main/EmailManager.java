package emailClient.main;

import emailClient.main.controller.services.FetchFoldersService;
import emailClient.main.controller.services.FolderUpdaterService;
import emailClient.main.model.EmailAccount;
import emailClient.main.model.EmailTreeItem;
import javafx.scene.control.TreeItem;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {
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
        EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        FetchFoldersService fetchFoldersService = new FetchFoldersService(emailAccount.getStore(), treeItem, folderList);
        fetchFoldersService.start();
        treeItem.setExpanded(true);
        foldersRoot.getChildren().add(treeItem);
    }

}
