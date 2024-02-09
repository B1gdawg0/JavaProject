package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
public class LoginPageController extends ThemeController{
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Label invalidLabel;
    private Object[] objects;
    private boolean isLightTheme;
    @FXML
    public void initialize() {
        isLightTheme = true;
        logoImageView.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));

        invalidLabel.setVisible(false);
        loadTheme(isLightTheme);
    }
    @FXML
    public void loginButt() throws IOException {
        AccountListDatasource accountListDataSource = new AccountListDatasource("data", "user-info.csv");
        AccountList accountList = accountListDataSource.readData();
        String username = usernameText.getText().trim();
        String password = passwordText.getText().trim();
        Account account = accountList.findAccountByUsername(username);
        objects = new Object[2];
        objects[0] = account;
        objects[1] = isLightTheme;

        clearData();
        if(account != null || !usernameText.getText().equals("") || !passwordText.getText().equals("")){
            if (account.isPassword(password)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String time = LocalDateTime.now().format(formatter);
                account.setTime(time);
                accountListDataSource.writeData(accountList);
                    if(account.getRole().equals("admin")){
                        FXRouter.goTo("user-status", objects);
                    }else{
                        FXRouter.goTo("events-list", objects);
                    }

            } else {
                invalidLabel.setText("Wrong password.");
                invalidLabel.setVisible(true);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                invalidLabel.setVisible(false);
                            }},
                        1000
                    );
            }
        }else{
            invalidLabel.setVisible(true);
            new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        invalidLabel.setVisible(false);
                    }
                },
                1000
        );
        }
    }
    @FXML
    public void registerLink() throws IOException {
        FXRouter.goTo("register-page", isLightTheme);
    }
    public void clearData(){
        usernameText.setText("");
        passwordText.setText("");
    }
    @FXML
    protected void onChangeTheme() {
        if (isLightTheme) {
            loadTheme(false);
            changeLogoImage(false);
        } else {
            loadTheme(true);
            changeLogoImage(true);
        }
        isLightTheme = !isLightTheme;
    }
    @FXML
    private void onAboutUsClick()throws IOException{
        FXRouter.goTo("about-us", isLightTheme);
    }
    @FXML
    private void onTipsClick(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tips");
        alert.setHeaderText(null);
        alert.setContentText("1) ห้ามใช้ , (comma) ในการกรอกข้อมูลตัวอักษรในโปรแกรมโดยเด็ดขาด\n" +
                "2) ใช้รูปภาพนามสกุลไฟล์ .jpg, .jpeg, .png, .gif เท่านั้นในการตั้งรูปภาพ");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/cs211/project/views/st-theme.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("custom-alert");;
        alert.showAndWait();
    }

}
