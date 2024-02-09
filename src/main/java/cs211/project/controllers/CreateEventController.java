package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.EventList;
import cs211.project.services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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

public class CreateEventController extends MenuBarController {
    @FXML
    private TextField nameEvent;
    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private TextField timeStart;
    @FXML
    private TextField timeEnd;
    @FXML
    private TextField ticket;
    @FXML
    private TextArea detailTextArea;
    @FXML
    private DatePicker startJoinDate;
    @FXML
    private DatePicker endJoinDate;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    @FXML
    private DatePicker teamStart;
    @FXML
    private DatePicker teamEnd;
    private Datasource<EventList> eventListDatasource;
    private EventList eventList;
    private Account account;
    private Event event;
    private Object[] objects;

    @FXML
    private void initialize(){
        eventListDatasource = new EventListFileDatasource("data","event-list.csv");
        eventList = eventListDatasource.readData();

        dateStart.setEditable(false);
        dateEnd.setEditable(false);
        startJoinDate.setEditable(false);
        endJoinDate.setEditable(false);
        teamStart.setEditable(false);
        teamEnd.setEditable(false);
        bPane.setVisible(false);
        slide.setTranslateX(-200);

        detailTextArea.setWrapText(true);

        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        Boolean isLightTheme = (Boolean) objects[1];
        setObject(objects);

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);
    }

    @FXML
    protected void onNextClick(ActionEvent events) throws IOException {
        String errorText = "";
        LocalDate currentDate = LocalDate.now();
        String eventName = nameEvent.getText();
        LocalDate startDate = dateStart.getValue();
        LocalDate endDate = dateEnd.getValue();
        String startTime = timeStart.getText();
        String endTime = timeEnd.getText();
        String ticketNum = ticket.getText();
        LocalDate startJoin = startJoinDate.getValue();
        LocalDate endJoin = endJoinDate.getValue();
        String detail = detailTextArea.getText();
        LocalDate startTeamDate = teamStart.getValue();
        LocalDate endTeamDate = teamEnd.getValue();

        event = eventList.findEventByEventName(eventName);

        if (event != null) {
            errorText += "This event's name already in used.\n";
            clear(nameEvent);
        } else {
            if (eventName.equals("") || startDate == null || endDate == null || startTime.equals("") || endTime.equals("")
                    || ticketNum.equals("") || startJoin == null || endJoin == null || startTeamDate == null || endTeamDate == null) {
                errorText += "Please fill all information.\n";
            }

            if (isContainSpecialCharacter(eventName)) {
                errorText += "EVENT NAME:\nEvent name must not contain special character.\n";
            }

            if (eventName.length()<3) {
                errorText += "EVENT NAME:\nLength of name must be more than 3.\n";
            }

            try {
                if (!currentDate.isBefore(startDate) && (!endDate.isAfter(startDate) || !startDate.isEqual(endDate))) {
                    errorText += "DATE START:\nStart date must be after the current date.\n";
                }
            } catch (Exception e) {
                errorText += "DATE START:\nInvalid Date.\n";
            }

            try {
                if (!currentDate.isBefore(endDate) && (!endDate.isAfter(startDate) || !startDate.isEqual(endDate))) {
                    errorText += "DATE END:\nEnd date must be after the current date and the Start Date.\n";
                }
            } catch (Exception e) {
                errorText += "DATE END:\nInvalid Date.\n";
            }

            try {
                LocalTime timeStartEvent = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime timeEndEvent = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));

                if (timeStartEvent.isBefore(timeEndEvent)) {
                    if (!timeStartEvent.isBefore(timeEndEvent.minusHours(3))) {
                        errorText += "TIME EVENT:\nStart time must be at least 4 hours before the end date.\n";
                    }
                } else {
                    errorText += "TIME EVENT:\nEnd time must be after the Start Date.\n";
                }
            } catch (DateTimeParseException e) {
                errorText += "INVALID TIME EVENT:\nPlease use HH:mm format.\n";
            }

            try {
                int tickets = Integer.parseInt(ticketNum);

                if (tickets < 20) {
                    errorText += "AMOUNT TICKET:\nTicket value cannot be less than 20.\n";
                }
            } catch (NumberFormatException e) {
                errorText += "INVALID AMOUNT TICKET:\nPlease enter a valid integer value for the ticket.\n";
            }

            try {
                if (currentDate.isAfter(startJoin) || startJoin.isAfter(endJoin) || startJoin.isAfter(endDate)) {
                    errorText += "JOIN EVENT START DATE:\nJoin event start date must be after the current date\nand before the end date.\n";
                }
            } catch (Exception e) {
                errorText += "JOIN EVENT START DATE:\nInvalid Date.\n";
            }

            try {
                if (currentDate.isAfter(endJoin) || endJoin.isAfter(endDate) || endJoin.isBefore(startJoin)) {
                    errorText += "JOIN EVENT END DATE:\nJoin event end date must be after the current date,\njoin event start date and before the end date.\n";
                }
            } catch (Exception e) {
                errorText += "JOIN EVENT END DATE:\nInvalid Date.\n";
            }

            try {
                if (currentDate.isAfter(startTeamDate) || startTeamDate.isAfter(endTeamDate) || startTeamDate.isAfter(endDate)) {
                    errorText += "JOIN TEAM START DATE:\nJoin team start date must be after the current date\nand before the end date.\n";
                }
            } catch (Exception e) {
                errorText += "JOIN TEAM START DATE:\nInvalid Date.\n";
            }
            try {
                if (currentDate.isAfter(endTeamDate) || endTeamDate.isAfter(endDate) || endTeamDate.isBefore(startTeamDate)) {
                    errorText += "JOIN TEAM END DATE:\nJoin team end date must be after the current date,\njoin team start date and before the end date.\n";
                }
            } catch (Exception e) {
                errorText += "JOIN TEAM END DATE:\nInvalid Date.\n";
            }
        }

        if(errorText.equals("")) {
            boolean confirmFinish = showConfirmationDialog("Confirm Finish Event", "Are you sure you want to finish the event?");
            if (confirmFinish){
                int tickets = Integer.parseInt(ticketNum);
                eventList.addNewEvent(eventName, startDate, endDate, startTime, endTime, tickets,
                        detail, startJoin, endJoin, 0, "/images/default-event.png", account.getUsername(), startTeamDate, endTeamDate);

                eventListDatasource.writeData(eventList);
                eventList = eventListDatasource.readData();

                event = eventList.findEventByEventName(eventName);

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
                        event.setPicURL(destDir + "/" + filename);
                        eventListDatasource.writeData(eventList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
                eventList = eventListDatasource.readData();
                event = eventList.findEventByEventName(eventName);

                FXRouter.goTo("event-history", objects);
            }
        } else {
            showErrorAlert(errorText);
            errorText = "";
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clear(TextField name){
        name.setText("");
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

    public boolean isContainSpecialCharacter(String cha) {
        String specialChar = "~`!@#$%^&*()={[}]|\\:;\"'<,>.?/";
        for(char c : cha.toCharArray()){
            if (specialChar.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }
}
