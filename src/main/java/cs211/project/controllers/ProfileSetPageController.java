package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ProfileSetPageController extends MenuBarController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label myText;
    @FXML
    private Pane myRectangle;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    @FXML
    private HBox hBox;
    private Datasource<AccountList> accountListDataSource;
    private AccountList accountList;
    private Account account;
    private Object[] objects;
    @FXML public void initialize(){
        accountListDataSource = new AccountListDatasource("data","user-info.csv");
        accountList = accountListDataSource.readData();
        accountListDataSource.readData();

        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        Boolean isLightTheme = (Boolean) objects[1];

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);
        setObject(objects);

        usernameLabel.setText(account.getUsername());
        nameLabel.setText(account.getName());
        myText.setVisible(false);
        myRectangle.setVisible(false);
        if(!account.getPictureURL().equals("/images/default-profile.png")){
            imageView.setImage(new Image("file:"+account.getPictureURL(), true));
        }else {
            imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        }
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    @FXML
    private void onChooseButtonClick(ActionEvent event){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG GIF", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                String[] fileSplit = file.getName().split("\\.");
                String filename = "account_" + account.getName() + "_image" + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                account = accountList.findAccountByUsername(account.getUsername());
                imageView.setImage(new Image(target.toUri().toString()));
                account.setPictureURL(destDir + "/" + filename);
                accountListDataSource.readData();
                accountListDataSource.writeData(accountList);
                objects[0] = account;
                setObject(objects);
                myText.setVisible(true);
                myRectangle.setVisible(true);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                myText.setVisible(false);
                                myRectangle.setVisible(false);
                            }
                        },
                        1000
                );
                update();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void update(){
        if(!account.getPictureURL().equals("/images/default-profile.png")){
            imageView.setImage(new Image("file:"+account.getPictureURL(), true));
        }else {
            imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        }
        accountList = accountListDataSource.readData();
        account = accountList.findAccountByUsername(account.getUsername());
    }
    @FXML
    public void rePassButt(ActionEvent event) throws IOException {
        FXRouter.goTo("re-password", objects);
    }
}
