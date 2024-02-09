package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class RePassPageController extends MenuBarController {

    @FXML
    private PasswordField passwordOld;
    @FXML
    private PasswordField passwordNew;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label myText;
    @FXML
    private Pane myRectangle;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane slide;
    @FXML
    private Button menuButton;
    @FXML
    private BorderPane bPane;
    @FXML
    private HBox hBox;
    @FXML
    private Button backButton;
    @FXML
    private PasswordField passwordConfirm;
    private Datasource<AccountList> accountListDataSource;
    private AccountList accountList;
    private Account account;
    private Object[] objects;
    @FXML
    public void initialize(){
        objects = (Object[]) FXRouter.getData();
        Account accounts = (Account) objects[0];
        Boolean isLightTheme = (Boolean) objects[1];

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);
        setObject(objects);

        accountListDataSource = new AccountListDatasource("data","user-info.csv");
        accountList = accountListDataSource.readData();
        account = accountList.findAccountByUsername(accounts.getUsername());
        if(account.getRole().equals("admin")){
            menuButton.setVisible(false);
            backButton.setLayoutX(14);
            backButton.setLayoutY(18);
        }
        if(!account.getPictureURL().equals("/images/default-profile.png")){
            imageView.setImage(new Image("file:"+account.getPictureURL(), true));
        }else {
            imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        }
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        usernameLabel.setText(account.getUsername());
        myText.setVisible(false);
        myRectangle.setVisible(false);
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }
    @FXML
    public void onConfirmClick(){
        String oldPass = passwordOld.getText();
        String newPass = passwordNew.getText();
        String confirmPass = passwordConfirm.getText();
            if (account.getPassword().equals(oldPass) && !newPass.equals("")) {
                if(!isContainSpecialCharacter(newPass)) {
                    if (confirmPass.equals(newPass)) {
                        account.setPassword(newPass);
                        accountListDataSource.writeData(accountList);
                        clearText();
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
                    } else {
                        showErrorAlert("New password not match with Confirm password.");
                    }
                }else{
                    clearText();
                    showErrorAlert("Password must not contain special character.");
                }
            } else if (newPass.equals("") || oldPass.equals("") || confirmPass.equals("")) {
                clearText();
                showErrorAlert("Please fill all the information.");
            } else {
                clearText();
                showErrorAlert("Incorrect Password");
            }
    }
    public void clearText(){
        passwordOld.setText("");
        passwordNew.setText("");
        passwordConfirm.setText("");
    }
    @FXML
    protected void onBackClick() throws IOException {
        if (account.getRole().equals("user")) {
            FXRouter.goTo("profile-setting", objects);
        } else {
            FXRouter.goTo("user-status", objects);
        }
    }
    public boolean isContainSpecialCharacter(String cha){
        String specialChar = "~`!@#$%^&*()={[}]|\\:;\"'<,>.?/";
        for(char c : cha.toCharArray()){
            if (specialChar.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
