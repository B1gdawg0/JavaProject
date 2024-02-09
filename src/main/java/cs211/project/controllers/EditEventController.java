package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.Staff;
import cs211.project.models.Team;
import cs211.project.models.collections.*;
import cs211.project.services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class EditEventController extends MenuBarController {
    @FXML
    private TextField eventNameTextField;
    @FXML
    private DatePicker eventDatePickerStart;
    @FXML
    private DatePicker eventDatePickerEnd;
    @FXML
    private TextField timeStartEventTextField;
    @FXML
    private TextField timeEndEventTextField;
    @FXML
    private TextField amountTicketTextField;
    @FXML
    private TextArea detailTextArea;
    @FXML
    private DatePicker startJoinDate;
    @FXML
    private DatePicker endJoinDate;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    @FXML
    private HBox hBox;
    @FXML
    private DatePicker teamStart;
    @FXML
    private DatePicker teamEnd;
    private Object[] objectsSend;
    private Datasource<EventList> eventListDatasource;
    private Datasource<AccountList> accountListDatasource;
    private Datasource<AccountList> joinedEventDatasource;
    private Datasource<TeamList> teamListDatasource;
    private Datasource<StaffList> banStaffListDatasource;
    private Datasource<AccountList> banUserDatasource;
    private Datasource<ActivityList> activityListDatasource;
    private ActivityList activityList ;
    private AccountList banUserList;
    private StaffList banStaffList;
    private TeamList teamList;
    private EventList eventList;
    private Event event;
    private LocalDate currentDate;
    private String errorMessage;
    private Event events;
    private AccountList accountList;
    private Account account;
    private AccountList accountJoinList;

    @FXML
    private void initialize() {
        clearErrorMessage();

        Object[] objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        events = (Event) objects[1];
        Boolean isLightTheme = (Boolean) objects[2];

        objectsSend = new Object[2];
        objectsSend[0] = account;
        objectsSend[1] = isLightTheme;
        setObject(objectsSend);

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);

        currentDate = LocalDate.now();

        eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
        accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        joinedEventDatasource = new UserEventListFileDatasource("data", "user-joined-event.csv");
        teamListDatasource = new TeamListFileDatasource("data","team.csv");
        banStaffListDatasource = new BanListFileDatasource("data","ban-staff-list.csv");
        banUserDatasource = new UserEventListFileDatasource("data","ban-user.csv");
        activityListDatasource = new ActivityListFileDatasource("data", "activity-list.csv");

        eventList = eventListDatasource.readData();
        accountList = accountListDatasource.readData();
        accountJoinList = joinedEventDatasource.readData();
        teamList = teamListDatasource.readData();
        banUserList = banUserDatasource.readData();
        banStaffList = banStaffListDatasource.readData();
        activityList = activityListDatasource.readData();

        event = eventList.findEventByEventName(events.getEventName());
        account = accountList.findAccountByUsername(events.getEventManager());

        showInfo(event);

        eventDatePickerEnd.setEditable(false);
        eventDatePickerStart.setEditable(false);
        endJoinDate.setEditable(false);
        startJoinDate.setEditable(false);
        teamStart.setEditable(false);
        teamEnd.setEditable(false);
        detailTextArea.setWrapText(true);

        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    private void showInfo(Event event) {
        eventNameTextField.setText(event.getEventName());
        eventDatePickerStart.setValue(event.getStartDate());
        eventDatePickerEnd.setValue(event.getEndDate());
        timeStartEventTextField.setText(event.getStartTime());
        timeEndEventTextField.setText(event.getEndTime());
        startJoinDate.setValue(event.getStartJoinDate());
        endJoinDate.setValue(event.getEndJoinDate());
        amountTicketTextField.setText(String.valueOf(event.getTicket()));
        detailTextArea.setText(event.getDetail());
        teamStart.setValue(event.getTeamStartDate());
        teamEnd.setValue(event.getTeamEndDate());

        if(!event.getPicURL().equals("/images/default-event.png")){
            imageView.setImage(new Image("file:"+event.getPicURL(), true));
        } else {
            imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
        }
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
    }

    @FXML
    protected void onFinishClick() {
        String eventName = eventNameTextField.getText().trim();
        LocalDate eventDateStart = eventDatePickerStart.getValue();
        LocalDate eventDateEnd = eventDatePickerEnd.getValue();
        String timeStartEvent = timeStartEventTextField.getText().trim();
        String timeEndEvent = timeEndEventTextField.getText().trim();
        String amountTicket = amountTicketTextField.getText().trim();
        String detail = detailTextArea.getText().trim();
        LocalDate joinDateStart = startJoinDate.getValue();
        LocalDate joinDateEnd = endJoinDate.getValue();
        LocalDate startTeamDate = teamStart.getValue();
        LocalDate endTeamDate = teamEnd.getValue();

        if (!eventName.isEmpty() && eventDateStart != null && eventDateEnd != null &&
                !timeStartEvent.isEmpty() && !timeEndEvent.isEmpty() &&
                !amountTicket.isEmpty() && joinDateStart != null &&
                joinDateEnd != null) {

            changeNameDisplay(eventName);
            changeDateStart(eventDateStart, eventDateEnd);
            changeDateEnd(eventDateStart, eventDateEnd);
            changeTimeStartEvent(timeStartEvent, timeEndEvent);
            changeAmountTicket(amountTicket);
            changeDetail(detail);
            changeJoinDateStart(joinDateStart, joinDateEnd);
            changeJoinDateEnd(joinDateStart, joinDateEnd);
            changeStartTeamDate(startTeamDate, endTeamDate);
            changeEndTeamDate(startTeamDate, endTeamDate);

            if (errorMessage.equals("")) {
                Boolean confirmFinish = showConfirmationDialog("Confirm Finish Event", "Are you sure you want to finish the event?");

                if (confirmFinish) {
                    eventListDatasource.writeData(eventList);
                    joinedEventDatasource.writeData(accountJoinList);
                    activityListDatasource.writeData(activityList);
                    banUserDatasource.writeData(banUserList);
                    banStaffListDatasource.writeData(banStaffList);
                    teamListDatasource.writeData(teamList);
                    showInfo(event);
                    onBackClick();
                }

            } else {
                showErrorAlert(errorMessage);
                clearErrorMessage();
            }
        } else {
            showErrorAlert("Please fill all information.");
            clearErrorMessage();
        }
    }

    @FXML
    public  void onImageView(ActionEvent events){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG GIF", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        Node source = (Node) events.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                String[] fileSplit = file.getName().split("\\.");
                String filename = "Event_" + event.getEventName() + "_image" + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                imageView.setImage(new Image(target.toUri().toString()));
                event.setPicURL(destDir + "/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeNameDisplay(String name) {
        if (name.equals(event.getEventName())) {
            return;
        }
        try {
            if (name.length() < 3) {
                errorMessage += "EVENT NAME:\nLength of name be must more than 3.\n";
            }
            if(isContainSpecialCharacter(name)){
                errorMessage += "EVENT NAME:\nEvent name must not contain special character.\n";
            }
            if(name.length() >= 3 && !isContainSpecialCharacter(name)){
                String thisEvent = event.getEventName();
                for (Account account1 : accountJoinList.getAccount()) {
                    for (String event1 : account1.getAllEventUser()) {
                        if (event1.equals(thisEvent)) {
                            account1.changeName(event1, name);
                        }
                    }
                }

                for(Team team : teamList.getTeams()){
                    if(team.getEvent().getEventName().equals(thisEvent))team.setEvent(name);
                }

                for(Staff staff : banStaffList.getStaffList()){
                    for(String eventName : staff.getBannedEvent()){
                        if(eventName.equals(thisEvent)){
                            staff.removeEventThatGetBanned(thisEvent);
                            staff.addEventThatGetBanned(name);
                        }
                    }
                }

                for(Account account : banUserList.getAccount()){
                    for(String eventName : account.getAllEventUser()){
                        if(eventName.equals(thisEvent)){
                            account.changeName(thisEvent, name);
                        }
                    }
                }

                activityList.changeNameEvent(thisEvent,name);
                event.setEventName(name);

            }
        } catch (Exception e) {
            errorMessage += "EVENT NAME:\nInvalid name.\n";
        }
    }

    private void changeDateStart(LocalDate startDate, LocalDate endDate) {
        if (startDate.isEqual(event.getStartDate())) {
            return;
        }
        try {
            if (currentDate.isBefore(startDate) && (endDate.isAfter(startDate) || startDate.isEqual(endDate))) {
                event.setStartDate(startDate);
            } else {
                errorMessage += "DATE START:\nStart date must be after the current date.\n";
            }
        } catch (Exception e) {
            errorMessage += "DATE START:\nInvalid Date.\n";
        }
    }

    private void changeDateEnd(LocalDate startDate, LocalDate endDate) {
        if (endDate.isEqual(event.getEndDate())){
            return;
        }
        try {
            if (currentDate.isBefore(endDate) && (endDate.isAfter(startDate) || startDate.isEqual(endDate))) {
                event.setEndDate(endDate);
            } else {
                errorMessage += "DATE END:\nEnd date must be after the current date and the Start Date.\n";
            }
        } catch (Exception e) {
            errorMessage += "DATE END:\nInvalid Date.\n";
        }
    }

    private void changeJoinDateStart(LocalDate startJoin, LocalDate endJoin) {
        if (startJoin.isEqual(event.getStartDate())) {
            return;
        }
        try {
            if (currentDate.isAfter(startJoin) || startJoin.isAfter(endJoin) || startJoin.isAfter(eventDatePickerEnd.getValue())) {
                errorMessage += "JOIN EVENT START DATE:\nJoin event start date must be after the current date\nand before the end date.\n";
            }else{
                event.setStartJoinDate(startJoin);
            }
        } catch (Exception e) {
            errorMessage += "JOIN EVENT START DATE:\nInvalid Date.\n";
        }
    }

    private void changeJoinDateEnd(LocalDate startJoin, LocalDate endJoin) {
        if (endJoin.isEqual(event.getEndDate())){
            return;
        }
        try {
            if (currentDate.isAfter(endJoin) || endJoin.isAfter(eventDatePickerEnd.getValue()) || endJoin.isBefore(startJoin)) {
                errorMessage += "JOIN EVENT END DATE:\nJoin event end date must be after the current date,\njoin event start date and before the end date.\n";
            }else {
                event.setEndJoinDate(endJoin);
            }
        } catch (Exception e) {
            errorMessage += "JOIN EVENT END DATE:\nInvalid Date.\n";
        }
    }

    private void changeTimeStartEvent(String timeStartEvent, String timeEndEvent) {
        if (timeStartEvent.equals(event.getStartTime())) {
            return;
        }

        try {
            LocalTime startTime = LocalTime.parse(timeStartEvent, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime endTime = LocalTime.parse(timeEndEvent, DateTimeFormatter.ofPattern("HH:mm"));

            if (startTime.isBefore(endTime)) {
                if (startTime.isBefore(endTime.minusHours(3))) {
                    event.setStartTime(timeStartEvent);
                    event.setEndTime(timeEndEvent);
                } else {
                    errorMessage += "TIME EVENT:\nStart date must be at least 4 hours before the end date.\n";
                }
            } else {
                errorMessage += "TIME EVENT:\nEnd date must be after the Start Date.\n";
            }
        } catch (DateTimeParseException e) {
            errorMessage += "INVALID TIME EVENT:\nPlease use HH:mm format.\n";
        }
    }

    private void changeStartTeamDate(LocalDate startTeamDate, LocalDate endTeamDate){
        if (startTeamDate.isEqual(event.getTeamStartDate())) {
            return;
        }
        try {
            if (currentDate.isAfter(startTeamDate) || startTeamDate.isAfter(endTeamDate) || startTeamDate.isAfter(teamEnd.getValue())) {
                errorMessage += "JOIN TEAM START DATE:\nJoin team start date must be after the current date\nand before the end date.\n";
            }else{
                event.setTeamStartDate(startTeamDate);
            }
        } catch (Exception e) {
            errorMessage += "JOIN TEAM START DATE:\nInvalid Date.\n";
        }
    }

    private void changeEndTeamDate(LocalDate startTeamDate, LocalDate endTeamDate){
        if (endTeamDate.isEqual(event.getTeamEndDate())){
            return;
        }
        try {
            if (currentDate.isAfter(endTeamDate) || endTeamDate.isAfter(teamEnd.getValue()) || endTeamDate.isBefore(startTeamDate)) {
                errorMessage += "JOIN TEAM END DATE:\nJoin team end date must be after the current date,\njoin team start date and before the end date.\n";
            }else {
                event.setTeamEndDate(endTeamDate);
            }
        } catch (Exception e) {
            errorMessage += "JOIN TEAM END DATE:\nInvalid Date.\n";
        }
    }

    private void changeAmountTicket(String ticket) {
        if (Integer.parseInt(ticket) == event.getTicket()) {
            return;
        }

        try {
            int newTicketValue = Integer.parseInt(ticket);

            if (newTicketValue < event.getTicketLeft()) {
                errorMessage += "AMOUNT TICKET:\nTicket value cannot be less than the current ticket left\n";
            } else {
                event.setTicket(newTicketValue);
            }
        } catch (NumberFormatException e) {
            errorMessage += "INVALID AMOUNT TICKET:\nPlease enter a valid integer value for the ticket\n.";
        }
    }


    private void changeDetail(String detail) {
        try {
            event.setDetail(detail);
        } catch (Exception e) {
            showErrorAlert("DETAIL:\nInvalid Text.\n");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == confirmButton;
    }

    private void clearErrorMessage() {
        errorMessage = "";
    }

    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("event-history", objectsSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

}
