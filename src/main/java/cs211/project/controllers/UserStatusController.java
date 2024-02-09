package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.FXRouter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDateTime;

public class UserStatusController extends ThemeController{
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ImageView imageUserView;
    @FXML
    private HBox hBox;
    @FXML
    private TableView<Account> accountTableView;
    private AccountList accountList;
    private Account user;
    private Object[] objects;

    @FXML
    private void initialize() {
        objects = (Object[]) FXRouter.getData();
        user = (Account) objects[0];
        Boolean isLightTheme = (Boolean) objects[1];

        loadTheme(isLightTheme);

        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        clearDataInfo();
        AccountListDatasource accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        accountList = accountListDatasource.readData();
        accountList.sort();
        showTable(accountList);
        accountTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearDataInfo();
            } else {
                showInfo(newValue);
            }
        });
    }

    public void showInfo(Account account) {
        user = accountList.findAccountByUsername(account.getUsername());
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getName());
        timeLabel.setText(user.getTime());
        if(!user.getPictureURL().equals("/images/default-profile.png")){
            imageUserView.setImage(new Image("file:"+user.getPictureURL(), true));
        }else {
            imageUserView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        }
    }
    private void showTable(AccountList accountList) {
        TableColumn<Account, ImageView> imageColumn = new TableColumn<>("Profile");
        imageColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(getImageView(param.getValue().getPictureURL())));

        TableColumn<Account, String> IDColumn = new TableColumn<>("ID");
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Account, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Account, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Account, LocalDateTime> lastOnlineColumn = new TableColumn<>("Last Online");
        lastOnlineColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        accountTableView.setItems(FXCollections.observableArrayList(accountList.getAccount()));

        accountTableView.getColumns().clear();
        accountTableView.getColumns().add(imageColumn);
        accountTableView.getColumns().add(IDColumn);
        accountTableView.getColumns().add(nameColumn);
        accountTableView.getColumns().add(usernameColumn);
        accountTableView.getColumns().add(lastOnlineColumn);


        for (Account account : accountList.getAccount()) {
            if (account.isAdmin(account.getRole())) {
                removeAccount(account);
            }
        }
    }
    private ImageView getImageView(String pictureURL) {
        ImageView imageView = new ImageView();
        if (!pictureURL.equals("/images/default-profile.png")) {
            imageView.setImage(new Image("file:" + pictureURL, true));
        } else {
            imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        }
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        return imageView;
    }

    public void removeAccount(Account accountToRemove) {
        accountTableView.getItems().remove(accountToRemove);
    }

    public void clearDataInfo () {
        usernameLabel.setText("");
        nameLabel.setText("");
        timeLabel.setText("");
        imageUserView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
    }
    @FXML
    private void onBackClick() throws IOException {
        FXRouter.goTo("login-page");
    }
    @FXML
    private void onChangePasswordClick() throws IOException {
        FXRouter.goTo("re-password", objects);
    }
}
