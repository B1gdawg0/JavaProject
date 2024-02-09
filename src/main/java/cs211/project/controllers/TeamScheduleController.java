package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivityList;
import cs211.project.services.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.time.LocalTime;

public class TeamScheduleController extends MenuBarController{
    @FXML
    private ComboBox teamComboBox;
    @FXML
    private TableView<Activity> activityTableView;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    @FXML
    private Label teamNameLabel;
    @FXML
    private Label constantTeamName;
    private ActivityList activityList;
    private Object[] objects;
    private Account account;
    private Team team;
    private Datasource<ActivityList> activityListDatasource;

    @FXML
    public void initialize(){
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        Boolean isLightTheme = (Boolean) objects[1];

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);
        setObject(objects);

        activityListDatasource = new ActivityListFileDatasource("data", "activity-list.csv");
        activityList = activityListDatasource.readData();
        team = new Team("",1,1,"");
        teamComboBox.getItems().addAll(team.getUserInTeam(account.getId()));
        bPane.setVisible(false);
        slide.setTranslateX(-200);
        constantTeamName.setVisible(false);
        teamNameLabel.setText("");
    }
    private void showTable(ActivityList activityList) {
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, String> dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Activity, LocalTime> startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));

        TableColumn<Activity, LocalTime> endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));

        TableColumn<Activity, LocalTime> teamColumn = new TableColumn<>("team");
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        TableColumn<Activity, String> statusColumn = new TableColumn<>("status");
        statusColumn.setCellValueFactory(cellData -> {
            int status = Integer.parseInt(cellData.getValue().getStatus());
            if (status == 1) {
                return new SimpleStringProperty("Finish");
            } else {
                return new SimpleStringProperty("Still Organize");
            }
        });
        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);
        activityTableView.getColumns().add(teamColumn);
        activityTableView.getColumns().add(statusColumn);
        activityTableView.getItems().clear();

        for (Activity activity: activityList.getActivities()) {
            if(activity.getStatus().equals("0")) {
                activityTableView.getItems().add(activity);
            }
        }
        for (Activity activity: activityList.getActivities()) {
            if(activity.getStatus().equals("1"))
                activityTableView.getItems().add(activity);
        }
    }

    @FXML
    protected void onSearchClick() {
        String eventName = (String) teamComboBox.getValue();
        updateSchedule(eventName);
        showTable(activityList);
        teamNameLabel.setText(team.getNameTeamInEvent(account.getId(), eventName));
        constantTeamName.setVisible(true);
    }
    private void updateSchedule(String eventName){
        activityListDatasource = new ActivityListFileDatasource("data", "activity-list.csv");
        activityList = activityListDatasource.readData();
        activityList.findActivityInEvent(eventName);
    }
}
