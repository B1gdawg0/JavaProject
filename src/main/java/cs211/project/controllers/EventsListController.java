package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collections.*;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventsListController extends MenuBarController {
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label ticketLeftLabel;
    @FXML
    private Label teamLeftLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView imageView;
    @FXML
    private HBox hBox;
    @FXML
    private Button bookTicket;
    @FXML
    private Button applyStaff;
    @FXML
    private Button applyParticipant;
    @FXML
    private GridPane eventGridPane;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    private Datasource<EventList> eventListDatasource;
    private Datasource<AccountList> accountListDatasource;
    private AccountList accountList;
    private EventList eventList;
    private Account account;
    private Account ban;
    private Event selectedEvent;
    private Team teamFound;
    private Object[] objects;
    private Object[] objectsSend;
    private String textSearch;
    private int column;
    private int row;

    @FXML
    public void initialize() {
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        Boolean isLightTheme = (Boolean) objects[1];

        setObject(objects);

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);

        imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        bPane.setVisible(false);

        textSearch = "";
        clearEventInfo();

        eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
        accountListDatasource = new UserEventListFileDatasource("data","user-joined-event.csv");
        UserEventListFileDatasource banListDatasource = new UserEventListFileDatasource("data","ban-user.csv");

        eventList = eventListDatasource.readData();
        accountList = accountListDatasource.readData();
        AccountList banList = banListDatasource.readData();

        ban = banList.findAccountByUsername(account.getUsername());
        slide.setTranslateX(-200);
        account = accountList.findAccountByUsername(account.getUsername());
        showGrid(eventList);
    }
    public void showGrid(EventList eventLists){
        LocalDate currentDate = LocalDate.now();
        eventGridPane.getChildren().clear();
        column = 0;
        row
                = 1;
        if (textSearch.equals("")) {
            for (Event event: eventLists.getEvents()) {
                if (event.getEndDate().isAfter(currentDate)) {
                    createGrid(event);
                }else if (event.getEndDate().isEqual(currentDate)) {
                    createGrid(event);
                }
            }
        }else {
            for (Event event: eventLists.getSearch()) {
                createGrid(event);
            }
        }
    }

    private void createGrid(Event event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/event-item.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            EventItemController eventItemController = fxmlLoader.getController();
            eventItemController.setData(event);

            if (column == 2) {
                column = 0;
                row++;
            }
            anchorPane.setOnMouseClicked(events -> {
                clearEventInfo();
                imageView.setVisible(true);
                selectedEvent = event;
                showEventInfo(event);
            });

            eventGridPane.add(anchorPane, column++, row);
            eventGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
            eventGridPane.setPrefWidth(460);
            eventGridPane.setMaxWidth(Region.USE_PREF_SIZE);

            eventGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
            eventGridPane.setPrefHeight(460);
            eventGridPane.setMaxHeight(Region.USE_PREF_SIZE);

            GridPane.setMargin(anchorPane, new Insets(20));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showEventInfo(Event event) {
        LocalDate currentDate = LocalDate.now();
        TeamListFileDatasource teamListDatasource = new TeamListFileDatasource("data", "team.csv");
        TeamList teamList = teamListDatasource.readData();
        if (event.getEndDate().isAfter(currentDate)){
            bookTicket.setVisible(true);
            applyStaff.setVisible(true);
            applyParticipant.setVisible(true);
            eventNameLabel.setText(event.getEventName());
            ticketLeftLabel.setText(String.format("%d", event.getTicketLeft()));
            teamLeftLabel.setText(String.format("%d", teamList.allNumberOfStaffLeft(event.getEventName())));
            if (!event.getPicURL().equals("/images/default-event.png")) {
                imageView.setImage(new Image("file:" + event.getPicURL(), true));
            } else {
                imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
            }
        } else if (event.getEndDate().isEqual(currentDate)){
            if (LocalTime.parse(event.getEndTime()).isAfter(LocalTime.now())) {
                bookTicket.setVisible(true);
                applyStaff.setVisible(true);
                applyParticipant.setVisible(true);
                eventNameLabel.setText(event.getEventName());
                ticketLeftLabel.setText(String.format("%d", event.getTicketLeft()));
                teamLeftLabel.setText(String.format("%d", teamList.allNumberOfStaffLeft(event.getEventName())));
                if (!event.getPicURL().equals("/images/default-event.png")) {
                    imageView.setImage(new Image("file:" + event.getPicURL(), true));
                } else {
                    imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
                }
            } else {
                bookTicket.setVisible(false);
                applyStaff.setVisible(false);
                applyParticipant.setVisible(false);
                eventNameLabel.setText(event.getEventName());
                ticketLeftLabel.setText(String.format("%d", event.getTicketLeft()));
                teamLeftLabel.setText(String.format("%d", teamList.allNumberOfStaffLeft(event.getEventName())));
                if (!event.getPicURL().equals("/images/default-event.png")) {
                    imageView.setImage(new Image("file:" + event.getPicURL(), true));
                } else {
                    imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
                }

            }
        } else {
            bookTicket.setVisible(false);
            applyStaff.setVisible(false);
            applyParticipant.setVisible(false);
            eventNameLabel.setText(event.getEventName());
            ticketLeftLabel.setText(String.format("%d", event.getTicketLeft()));
            teamLeftLabel.setText(String.format("%d", teamList.allNumberOfStaffLeft(event.getEventName())));
            if (!event.getPicURL().equals("/images/default-event.png")) {
                imageView.setImage(new Image("file:" + event.getPicURL(), true));
            } else {
                imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
            }
        }
    }

    private void clearEventInfo() {
        eventNameLabel.setText("");
        ticketLeftLabel.setText("");
        teamLeftLabel.setText("");
        imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
    }

    @FXML
    public void onSearchEventButton() {
        textSearch = searchTextField.getText().trim();
        try {
            eventList.searchEvent(textSearch);
            showGrid(eventList);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onDetailClick() {
        if (selectedEvent != null) {
            try {
                objectsSend = new Object[3];
                objectsSend[0] = account;
                objectsSend[1] = selectedEvent;
                objectsSend[2] = (Boolean) objects[1];
                FXRouter.goTo("event-details", objectsSend);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }

    @FXML
    protected void onBookTicketsClick() {
        if (selectedEvent != null) {
            selectedEvent.loadTeamInEvent();
            teamFound = null;
            boolean found = false;
            LocalDate currentDate = LocalDate.now();
            try {
                if (!selectedEvent.getEventManager().equals(account.getUsername())) {
                    TeamList teams = selectedEvent.getTeams();
                    for (Team team : teams.getTeams()) {
                        for (Staff staff : team.getStaffs().getStaffList()) {
                            if (staff.getId().equals(Integer.toString(account.getId()))) {
                                found = true;
                                teamFound = team;
                                break;
                            }
                        }
                    }
                }

                if (selectedEvent.isEventManager(account.getUsername())) {
                    showErrorAlert("You can't join your own event.");
                }else if(account.isEventName(selectedEvent.getEventName())) {
                    showErrorAlert("You have already booked a ticket for this event.");
                }else if(!selectedEvent.getArrayListActivities().isEmpty() && selectedEvent.getActivities().userIsParticipant(account.getUsername())){
                    showErrorAlert("Sorry, you're participant in this event.");
                }else if(ban.isEventName(selectedEvent.getEventName())){
                    showErrorAlert("Sorry, you have ban from this event.");
                }else if(found){
                    showErrorAlert("\"You are have team in this event already \nYour team is \"" + teamFound);
                }else if(selectedEvent.getStartJoinDate().isAfter(currentDate)){
                    showErrorAlert("This event will open for book at "+selectedEvent.getStartJoinDate() + ".");
                } else if(selectedEvent.getEndJoinDate().isBefore(currentDate)){
                    showErrorAlert("Sorry this event is close to book.");
                }else if(selectedEvent.getTicketLeft() > 0) {
                    selectedEvent.ticketBuy();

                    eventListDatasource.readData();
                    eventListDatasource.writeData(eventList);
                    account.addUserEventName(selectedEvent.getEventName());
                    accountListDatasource.writeData(accountList);

                    objectsSend = new Object[3];
                    objectsSend[0] = account;
                    objectsSend[1] = selectedEvent;
                    objectsSend[2] = (Boolean) objects[1];
                    FXRouter.goTo("event-schedule", objectsSend);
                }
                else {
                    showErrorAlert("Sorry, tickets for this event are sold out.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }
    @FXML
    protected void onApplyToStaffClick() {
        BanListFileDatasource banStaffListDatasource = new BanListFileDatasource("data","ban-staff-list.csv");
        StaffList banStaffList = banStaffListDatasource.readData();
        Staff banStaff = banStaffList.checkStaffInList(String.valueOf(account.getId()));
        if(selectedEvent != null){
            Boolean getBan = false;
            LocalDate currentDate = LocalDate.now();
            if(banStaff != null){
                for(String eventBanName : banStaff.getBannedEvent()){
                    if (eventBanName.equals(selectedEvent.getEventName())) {
                        getBan = true;
                        break;
                    }
                }
            }
            selectedEvent.loadTeamInEvent();
            if(!selectedEvent.getArrayListActivities().isEmpty() && selectedEvent.getActivities().userIsParticipant(account.getUsername())){
                showErrorAlert("Sorry, you're participant in this event.");
            }
            else if(account.isEventName(selectedEvent.getEventName())) {
                showErrorAlert("You have already booked a ticket for this event.");
            }else if(ban.isEventName(selectedEvent.getEventName())){
                showErrorAlert("Sorry, you have ban from this event.");
            }else if(getBan){
                showErrorAlert("Sorry, you have ban from being staff in this event");
                getBan = false;
            }else {
                if (!selectedEvent.getEventManager().equals(account.getUsername())) {
                        TeamList teams = selectedEvent.getTeams();
                        teamFound = null;
                        boolean found = false;
                        for (Team team : teams.getTeams()) {
                            for (Staff staff : team.getStaffs().getStaffList()) {
                                if (staff.getId().equals(Integer.toString(account.getId()))) {
                                    found = true;
                                    teamFound = team;
                                    break;
                                }
                            }
                        }
                        if (found){
                            showInfoPopup("You are have team in this event already \nYour team is " + teamFound);
                        }
                        else if(selectedEvent.getTeamStartDate().isAfter(currentDate)){
                            showErrorAlert("This event will open for apply at "+selectedEvent.getTeamStartDate() + ".");
                        }else if(selectedEvent.getTeamEndDate().isBefore(currentDate)){
                            showErrorAlert("Sorry this event is close to apply.");
                        } else {
                            try {
                                TeamListFileDatasource data = new TeamListFileDatasource("data", "team.csv");
                                try {
                                    String teamName = teams.findLowestStaffTeam().getTeamName();
                                    data.updateStaffInTeam(selectedEvent.getEventName(), teamName, new Staff(account), "+");
                                    showInfoPopup("You are in " + teamName + " team");
                                    FXRouter.goTo("team-schedule", objects);
                                } catch (NullPointerException e) {
                                    showErrorAlert("Sorry, there are no available seats at the moment.");
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                } else {
                    showErrorAlert("You can't join your own event.");
                }
            }
        }else{
            showErrorAlert("Please select some event ");
        }

    }
    @FXML
    protected void onApplyToParticipantClick() {
        if(selectedEvent != null) {
            selectedEvent.loadTeamInEvent();
            teamFound = null;
            boolean found = false;
            try {
                if (!selectedEvent.getEventManager().equals(account.getUsername())) {
                    TeamList teams = selectedEvent.getTeams();
                    for (Team team : teams.getTeams()) {
                        for (Staff staff : team.getStaffs().getStaffList()) {
                            if (staff.getId().equals(Integer.toString(account.getId()))) {
                                found = true;
                                teamFound = team;
                                break;
                            }
                        }
                    }
                }
                selectedEvent.loadActivityInEvent();
                if (ban.isEventName(selectedEvent.getEventName())) {
                    showInfoPopup("Sorry, you have ban from this event.");
                }else if(found){
                    showErrorAlert("\"You are have team in this event already \nYour team is \"" + teamFound);
                } else if (!selectedEvent.getEventManager().equals(account.getUsername())) {
                    if (account.isEventName(selectedEvent.getEventName())) {
                        showErrorAlert("You have already booked a ticket for this event.");
                    } else if (!selectedEvent.getArrayListActivities().isEmpty()) {
                        ActivityListFileDatasource datasource = new ActivityListFileDatasource("data", "activity-list.csv");
                        ActivityList activityList = datasource.readData();
                        activityList.findActivityInEvent(selectedEvent.getEventName());
                        if (!activityList.userIsParticipant(account.getUsername())) {
                            if (selectedEvent.checkParticipantIsFull()) {
                                activityList.addParticipant(account.getUsername());
                                datasource.writeData(activityList);
                                FXRouter.goTo("participant-schedule", objects);
                            } else {
                                showErrorAlert("Sorry, participant in full.");
                            }
                        } else {
                            showErrorAlert("Sorry, you're participant in this event.");
                        }
                    } else {
                        showErrorAlert("Sorry, this event not ready for apply to participant.");
                    }
                } else {
                    showErrorAlert("You can't join your own event.");
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        else {
            showErrorAlert("Please select event");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/cs211/project/views/st-theme.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("custom-alert");;
        alert.showAndWait();
    }

    private void showInfoPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/cs211/project/views/st-theme.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("custom-alert");;
        alert.showAndWait();
    }

    @FXML
    protected void onChangeTheme() {
        Boolean isLightTheme = (Boolean) objects[1];
        if (isLightTheme) {
            loadTheme(false);
            changeLogoImage(false);
        } else {
            loadTheme(true);
            changeLogoImage(true);
        }
        isLightTheme = !isLightTheme;
        objects[1] = isLightTheme;
    }
}
