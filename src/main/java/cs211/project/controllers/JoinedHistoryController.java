package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.EventList;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class JoinedHistoryController extends MenuBarController{

    @FXML
    private ListView<String> eventOrganizeListView;
    @FXML
    private ListView<String> eventFinishListView;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private HBox hBox;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    @FXML
    private Button cancelEvent;
    private Datasource<EventList> eventListDatasource;
    private Datasource<AccountList> accountListDatasource;
    private AccountList accountList;
    private EventList eventList;
    private Account account;
    private Event event;
    private Object[] objects;
    private Object[] objectsSend;
    private String selectedEvent;
    private Boolean isLightTheme;

    @FXML
    public void initialize() {
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        isLightTheme = (Boolean) objects[1];

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);
        setObject(objects);
        clearEventInfo();
        hBox.setAlignment(javafx.geometry.Pos.CENTER);

        accountListDatasource = new UserEventListFileDatasource("data","user-joined-event.csv");
        eventListDatasource = new EventListFileDatasource("data", "event-list.csv");

        accountList = accountListDatasource.readData();
        eventList = eventListDatasource.readData();

        showOrganizeList(account);
        showFinishList(account);

        eventOrganizeListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null)  {
                eventFinishListView.getSelectionModel().clearSelection();
                cancelEvent.setVisible(true);
                clearEventInfo();
                showEventOrganizeInfo(displayUserJoinedEvents(newValue));
                selectedEvent = newValue;
            }
        });

        eventFinishListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                eventOrganizeListView.getSelectionModel().clearSelection();
                cancelEvent.setVisible(false);
                clearEventInfo();
                showEventFinishInfo(displayUserJoinedEvents(newValue));
                selectedEvent = newValue;
            }
        });

        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    private Event displayUserJoinedEvents(String eventName) {
        return eventList.findEventByEventName(eventName);
    }

    private void showOrganizeList(Account account) {
        eventOrganizeListView.getItems().clear();

        String username = account.getUsername();
        Account user = accountList.findAccountByUsername(username);

        for (String eventName : user.getAllEventUser()) {
            event = eventList.findEventByEventName(eventName);
            LocalDate currentDate = LocalDate.now();
            if (currentDate.isBefore(event.getEndDate())) {
                eventOrganizeListView.getItems().add(eventName);
            } else if (event.getEndDate().isEqual(currentDate)){
                LocalTime currentTime = LocalTime.now();
                if (LocalTime.parse(event.getEndTime()).isAfter(currentTime)) {
                    eventOrganizeListView.getItems().add(eventName);
                }
            }
        }
    }

    private void showFinishList(Account account) {
        eventFinishListView.getItems().clear();

        String username = account.getUsername();
        Account user = accountList.findAccountByUsername(username);

        for (String eventName : user.getAllEventUser()) {
            event = eventList.findEventByEventName(eventName);
            LocalDate currentDate = LocalDate.now();
            if (currentDate.isAfter(event.getEndDate())) {
                eventFinishListView.getItems().add(eventName);
            } else if (event.getEndDate().isEqual(currentDate)){
                LocalTime currentTime = LocalTime.now();;
                if (LocalTime.parse(event.getEndTime()).isBefore(currentTime)) {
                    eventFinishListView.getItems().add(eventName);
                }
            }
        }
    }

    private void showEventOrganizeInfo(Event event) {
        eventNameLabel.setText(event.getEventName());
        dateLabel.setText(event.getStartDate() + " - " + event.getEndDate());
        statusLabel.setText("Still being organized.");
        if (!event.getPicURL().equals("/images/default-event.png")) {
            imageView.setImage(new Image("file:" + event.getPicURL(), true));
        } else {
            imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
        }
    }

    private void showEventFinishInfo(Event event) {
        eventNameLabel.setText(event.getEventName());
        dateLabel.setText(event.getStartDate() + " - " + event.getEndDate());
        statusLabel.setText("Finished");
        if (!event.getPicURL().equals("/images/default-event.png")) {
            imageView.setImage(new Image("file:" + event.getPicURL(), true));
        } else {
            imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
        }
    }

    private void clearEventInfo() {
        eventNameLabel.setText("");
        dateLabel.setText("");
        statusLabel.setText("");
        imageView.setImage(null);
    }

    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("home-page", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateEventInfo() {
        clearEventInfo();
        eventOrganizeListView.getItems().clear();
        showOrganizeList(account);
    }

    @FXML
    public void onCancelEventClick() {
        if (selectedEvent != null) {
            account = accountList.findAccountByUsername(account.getUsername());
            event = eventList.findEventByEventName(selectedEvent);

            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation");
            confirmationDialog.setHeaderText("Delete Event");
            confirmationDialog.setContentText("Are you sure you want to delete this event: " + selectedEvent + "?");

            confirmationDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    account.deleteUserEventName(selectedEvent);
                    event.ticketCancel();
                    eventListDatasource.readData();
                    eventListDatasource.writeData(eventList);
                    accountListDatasource.readData();
                    accountListDatasource.writeData(accountList);
                    updateEventInfo();
                }
            });
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }
    @FXML
    public void onOpenSchedule() {
        if(selectedEvent!=null){
            objectsSend = new Object[3];
            objectsSend[0] = account;
            objectsSend[1] = eventList.findEventByEventName(selectedEvent);
            objectsSend[2] = isLightTheme;
            try{
                FXRouter.goTo("event-schedule",objectsSend);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}