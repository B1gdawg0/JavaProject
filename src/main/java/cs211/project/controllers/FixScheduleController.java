package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivityList;
import cs211.project.services.ActivityListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalTime;

public class FixScheduleController extends MenuBarController {
    @FXML
    private Label constantTeamOrParticipantLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label timeStartLabel;
    @FXML
    private Label timeStopLabel;
    @FXML
    private ComboBox chooseTeamOrParticipant;
    @FXML
    private RadioButton chooseRoleTeam;
    @FXML
    private RadioButton chooseRoleSingleParticipant;
    @FXML
    private TableView activityTableView;
    @FXML
    private ComboBox chooseOperator;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    private ActivityList activityList;
    private Account account;
    private Event selectedEvent;
    private Team team;
    private Activity selectedActivity;
    private Object[] objects;
    private String participantName;
    private String operator;
    private Object[] objectsSend;

    @FXML
    public void initialize(){
        clearInfo();
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        selectedEvent = (Event) objects[1];
        Boolean isLightTheme = (Boolean) objects[2];

        objectsSend = new Object[2];
        objectsSend[0] = account;
        objectsSend[1] = isLightTheme;

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);
        setObject(objectsSend);

        String[] op = new String[]{"add activity", "delete activity"};
        activityList = selectedEvent.loadActivityInEvent();
        chooseTeamOrParticipant.getItems().addAll(activityList.getParticipantInEvent());
        chooseRoleSingleParticipant.setSelected(true);
        chooseOperator.getItems().addAll(op);
        setChooseTeamVisible(false);
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    @FXML
    public void clearInfo(){
        nameLabel.setText("");
        timeStopLabel.setText("");
        timeStartLabel.setText("");
    }

    @FXML
    public void chooseRole(){

        if(chooseRoleTeam.isSelected()){
            activityTableView.getItems().clear();
            chooseTeamOrParticipant.getItems().clear();
            chooseTeamOrParticipant.getItems().addAll(selectedEvent.loadTeamInEvent().getTeams());
            chooseRoleSingleParticipant.setSelected(false);
            setChooseTeamVisible(true);
            constantTeamOrParticipantLabel.setText("Team:");
            chooseWhichTeamOrParticipant();


        }
        if(chooseRoleSingleParticipant.isSelected()){
            activityTableView.getItems().clear();
            chooseTeamOrParticipant.getItems().clear();
            chooseTeamOrParticipant.getItems().addAll(activityList.getParticipantInEvent());
            chooseRoleTeam.setSelected(false);
            constantTeamOrParticipantLabel.setText("Name:");
            clearInfo();
            if(chooseOperator.getValue() != null) {
                chooseWhichOperator();
                showActivity();
            }
        }
    }
    public void setChooseTeamVisible(boolean bool){
        chooseTeamOrParticipant.setVisible(bool);
        constantTeamOrParticipantLabel.setVisible(bool);
    }

    public void chooseWhichTeamOrParticipant(){
        if(chooseRoleTeam.isSelected()){
            if (team == null && operator == null) {
                team = (Team) chooseTeamOrParticipant.getSelectionModel().getSelectedItem();
            } else if(chooseTeamOrParticipant.getValue() != null){
                team = (Team) chooseTeamOrParticipant.getSelectionModel().getSelectedItem();
                if (operator != null) showActivity();
            }
        }
        else if (chooseRoleSingleParticipant.isSelected()){
            participantName = (String) chooseTeamOrParticipant.getSelectionModel().getSelectedItem();
        }
    }

    public void chooseWhichOperator(){
        operator = (String) chooseOperator.getSelectionModel().getSelectedItem();
        if(chooseRoleTeam.isSelected()){
            if(team != null && chooseTeamOrParticipant.getValue() != null) showActivity();
        } else if (chooseRoleSingleParticipant.isSelected()) {
            if(operator.equals("delete activity")){
                setChooseTeamVisible(false);
            }
            else {
                setChooseTeamVisible(true);
            }
            showActivity();
        }
    }

    public void showActivity(){
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Activity Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, String> dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Activity, LocalTime> startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));

        TableColumn<Activity, LocalTime> endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));

        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);

        activityTableView.getItems().clear();

        if(chooseRoleTeam.isSelected()) {
            if (operator.equals("add activity")) {
                for (Activity activity : activityList.getActivities()) {
                    if (activity.getTeamName().equals("")) activityTableView.getItems().add(activity);
                }
            } else if (operator.equals("delete activity")) {
                for (Activity activity : activityList.getActivities()) {
                    if (activity.getTeamName().equals(team.getTeamName())) activityTableView.getItems().add(activity);
                }
            }
        }
        else {
            if (operator.equals("add activity")) {
                for (Activity activity : activityList.getActivities()) {
                    if (activity.getParticipantName().equals("")) {
                        activityTableView.getItems().add(activity);
                    }
                }
            } else if (operator.equals("delete activity")) {
                for (Activity activity : activityList.getActivities()) {
                    if (!activity.getParticipantName().isEmpty()) {
                        activityTableView.getItems().add(activity);
                    }
                }
            }
        }
    }

    public void updateDataToTarget(){
        if(chooseRoleTeam.isSelected()){
            if (selectedActivity != null) {
                if (operator.equals("add activity")) {
                    selectedActivity.updateTeamInActivity(team);
                } else if (operator.equals("delete activity")) {
                    selectedActivity.updateTeamInActivity(null);
                }
                activityList = selectedEvent.loadActivityInEvent();
                showActivity();
            }
        }
        else if (chooseRoleSingleParticipant.isSelected()){
            if (selectedActivity != null) {
                if (operator.equals("add activity")) {
                    selectedActivity.setParticipantName(participantName);
                } else if (operator.equals("delete activity")) {
                    selectedActivity.setParticipantName("");
                }
                ActivityListFileDatasource activityListDatasource = new ActivityListFileDatasource("data", "activity-list.csv");
                activityListDatasource.writeData(activityList);
                activityList = selectedEvent.loadActivityInEvent();
                showActivity();
            }
        }
    }


    public void selectedParticipant(){
        chooseRoleSingleParticipant.setSelected(true);
        chooseRoleTeam.setSelected(false);
        chooseRole();
    }

    public void showInfo(Activity activity){
        nameLabel.setText(activity.getActivityName());
        timeStartLabel.setText(activity.getStartDate()+" "+activity.getStartTimeActivity());
        timeStopLabel.setText(activity.getEndDate()+" "+activity.getEndTimeActivity());
    }

    public void selectedTeam(){
        chooseRoleTeam.setSelected(true);
        chooseRoleSingleParticipant.setSelected(false);
        chooseRole();
        activityTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Activity>() {
            @Override
            public void changed(ObservableValue observable, Activity oldValue, Activity newValue) {
                if (newValue == null) {
                    clearInfo();
                    selectedActivity = null;
                } else {
                    showInfo(newValue);
                    selectedActivity = newValue;
                }
            }
        });
    }

    @FXML
    protected void backOnClick(){
        try {
            FXRouter.goTo("event-history", objectsSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
