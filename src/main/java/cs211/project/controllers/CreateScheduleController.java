package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateScheduleController extends MenuBarController {
    @FXML
    private TextField activityTextField;
    @FXML
    private TextField infoActivityTextField;
    @FXML
    private ComboBox chooseHourTimeStart;
    @FXML
    private ComboBox chooseMinTimeStart;
    @FXML
    private ComboBox chooseHourTimeStop;
    @FXML
    private ComboBox chooseMinTimeStop;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private Label eventNameLabel;
    @FXML private Label eventStartDate;
    @FXML private Label eventEndDate;
    @FXML private Label eventStartTime;
    @FXML private Label eventEndTime;

    @FXML
    private TableView<Activity> activityTableView;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    private Object[] objectsSend;
    private Object[] objects;
    private ActivityList activityList;
    private Activity selectedActivity;
    private Event event;
    private Account account;
    private Datasource<ActivityList> activityListDatasource;


    @FXML
    public void initialize() {

        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        event = (Event) objects[1];
        Boolean isLightTheme = (Boolean) objects[2];

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);

        objectsSend = new Object[2];
        objectsSend[0] = account;
        objectsSend[1] = isLightTheme;
        setObject(objectsSend);

        activityListDatasource = new ActivityListFileDatasource("data", "activity-list.csv");
        eventStartDate.setText(String.valueOf(event.getStartDate()));
        eventEndDate.setText(String.valueOf(event.getEndDate()));
        eventStartTime.setText(event.getStartTime());
        eventEndTime.setText(event.getEndTime());
        updateSchedule();

        eventNameLabel.setText(event.getEventName());
        addComboBox(event);
        showTable(activityList);

        activityTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Activity>() {
            @Override
            public void changed(ObservableValue observable, Activity oldValue, Activity newValue) {
                if (newValue == null) {
                    selectedActivity = null;
                } else {
                    selectedActivity = newValue;
                }
            }
        });
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    private void showTable(ActivityList activityList) {
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, LocalDate> startDateActivityColumn = new TableColumn<>("startDate");
        startDateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Activity, LocalDate> endDateActivityColumn = new TableColumn<>("endDate");
        endDateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Activity, LocalTime> startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));

        TableColumn<Activity, LocalTime> endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));

        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(startDateActivityColumn);
        activityTableView.getColumns().add(endDateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);

        activityTableView.getItems().clear();

        for (Activity activity: activityList.getActivities()) {
            activityTableView.getItems().add(activity);
        }
    }

    private void addComboBox(Event event) {
        chooseHourTimeStart.getItems().addAll(event.getArrayHour());
        chooseMinTimeStart.getItems().addAll(event.getArrayMinute());
        chooseHourTimeStop.getItems().addAll(event.getArrayHour());
        chooseMinTimeStop.getItems().addAll(event.getArrayMinute());
    }

    private void updateSchedule() {
        activityList = activityListDatasource.readData();
        activityList.findActivityInEvent(event.getEventName());
    }

    @FXML
    protected void addActivityOnClick() {
        try {
            String eventName = event.getEventName();
            String activityName = activityTextField.getText();
            String hourStartStr = (String) chooseHourTimeStart.getValue();
            String minStartStr = (String) chooseMinTimeStart.getValue();
            LocalDate selectedStartDate =  startDate.getValue();
            LocalDate selectedEndDate = endDate.getValue();
            String hourEndStr = (String) chooseHourTimeStop.getValue();
            String minEndStr = (String) chooseMinTimeStop.getValue();
            String infoActivity =  infoActivityTextField.getText();
            if (!activityName.isEmpty() && selectedStartDate != null && selectedEndDate != null&& hourStartStr != null && minStartStr != null && hourEndStr != null && minEndStr != null) {
                int hourStart = Integer.parseInt(hourStartStr);
                int minStart = Integer.parseInt(minStartStr);
                int hourEnd = Integer.parseInt(hourEndStr);
                int minEnd = Integer.parseInt(minEndStr);

                LocalTime startTimeActivity = LocalTime.of(hourStart, minStart);
                LocalTime endTimeActivity = LocalTime.of(hourEnd, minEnd);
                LocalDateTime startActivityTime = LocalDateTime.of(selectedStartDate,startTimeActivity);
                LocalDateTime endActivityTime = LocalDateTime.of(selectedEndDate,endTimeActivity);
                if(event.checkTimeActivity(startActivityTime,endActivityTime)){
                    if(activityList.checkActivityName(activityName)) {
                        if (activityList.checkActivity(startActivityTime,endActivityTime)) {
                            if(infoActivity == ""){
                                infoActivity = null;
                            }
                            activityList.addActivity(activityName, selectedStartDate, selectedEndDate, startTimeActivity, endTimeActivity, "", "", "0", eventName, infoActivity);
                            activityListDatasource.writeData(activityList);
                            if (activityList.getActivities().isEmpty()) {
                                activityList.findActivityInEvent(eventName);
                            }

                            updateSchedule();
                            showTable(activityList);
                            activityTextField.clear();
                            chooseHourTimeStart.setValue(null);
                            chooseMinTimeStart.setValue(null);
                            chooseHourTimeStop.setValue(null);
                            chooseMinTimeStop.setValue(null);
                            startDate.setValue(null);
                            endDate.setValue(null);
                            infoActivityTextField.clear();
                        } else {
                            showErrorAlert("Please select other time");
                        }
                    } else {
                        showErrorAlert("Please select other name");
                    }
                } else {
                    showErrorAlert("Please select time in event \n" + event.getStartDate() + "     "+ event.getStartTime() + "\nto\n"+ event.getEndDate() + "     " + event.getEndTime());
                }
            } else if (activityName.isEmpty()) {
                showErrorAlert("Please fill the name");
            } else {
                showErrorAlert("Please fill the time");
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void deleteOnClick() {
        TeamListFileDatasource teamListDatasource = new TeamListFileDatasource("data","team.csv");
        TeamList teams = teamListDatasource.readData();
        if (selectedActivity != null) {
            if (!selectedActivity.getTeamName().equals("")) {
                teams.deleteTeam(event,selectedActivity.getTeamName());
            }

            selectedActivity.deleteActivity();
            teamListDatasource.writeData(teams);
            activityListDatasource.writeData(activityList);
            updateSchedule();
            showTable(activityList);
        } else {
            showErrorAlert("Please select activity");
        }
    }

    @FXML
    protected void backOnClick() {
        try {
            FXRouter.goTo("event-history", objectsSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void nextOnClick() {
        try {
            FXRouter.goTo("create-team", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
