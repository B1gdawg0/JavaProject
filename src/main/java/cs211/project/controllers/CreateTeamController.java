package cs211.project.controllers;

import cs211.project.models.*;
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
import java.time.LocalTime;

public class CreateTeamController extends MenuBarController {
    @FXML
    private TableView<Activity> activityTableView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label timeStartLabel;
    @FXML
    private Label timeStopLabel;
    @FXML
    private TextField teamNameTextField;
    @FXML
    private TextField numberOfTeamMemberTextField;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    private Object[] objectsSend;
    private Object[] objects;
    private Account account;
    private Event event;
    public Activity selectedActivity;

    public void initialize() {
        clearInfo();

        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        event = (Event) objects[1];
        Boolean isLightTheme = (Boolean) objects[2];

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);

        event.loadEventInfo();
        ActivityList list = event.loadActivityInEvent();
        showTable(list);

        objectsSend = new Object[2];
        objectsSend[0] = account;
        objectsSend[1] = isLightTheme;
        setObject(objectsSend);

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
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    public void clearInfo() {
        nameLabel.setText("");
        timeStartLabel.setText("");
        timeStopLabel.setText("");
    }

    public void showInfo(Activity activity) {
        nameLabel.setText(activity.getActivityName());
        timeStartLabel.setText(activity.getStartTimeActivity());
        timeStopLabel.setText(activity.getEndTimeActivity());
    }

    public void showTable(ActivityList list) {
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

        for (Activity activity: list.getActivities()) {
            if (activity.getTeamName().equals("")) {
                activityTableView.getItems().add(activity);
            }
        }
    }

    public void createTeam(){
        if(selectedActivity != null){
            String teamName = teamNameTextField.getText();
            String numberStr = numberOfTeamMemberTextField.getText();

            Boolean found = false;
            TeamList teamList = event.loadTeamInEvent();

            for (Team team : teamList.getTeams()) if (team.getTeamName().equals(teamName)) found = true;

            if (!teamName.equals("")&&!numberStr.equals("")) {
                try {
                    int number = Integer.parseInt(numberStr);
                    if (number > 0) {
                        if (!found) {
                            Team team = new Team(teamName, number, event.getEventName());
                            team.createTeamInCSV();
                            selectedActivity.updateTeamInActivity(team);
                            ActivityList list = event.loadActivityInEvent();
                            showTable(list);
                            teamNameTextField.clear();
                            numberOfTeamMemberTextField.clear();
                        } else {
                            showErrorAlert("This team name already exist");
                        }
                    } else {
                        showErrorAlert("Number of people must more than zero");
                    }
                } catch(NumberFormatException e) {
                    showErrorAlert("Number of people must be number");
                }
            } else {
                showErrorAlert("Must fill all information before create team");
            }
        } else {
            showErrorAlert("please select some activity before create team");
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
    protected void onBackClick() {
        try {
            FXRouter.goTo("create-schedule", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onNextClick() {
        try {
            FXRouter.goTo("event-history", objectsSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
