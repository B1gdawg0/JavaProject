package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.collections.ActivityList;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.time.LocalTime;

public class ParticipantScheduleController extends MenuBarController {
    @FXML
    private TableView<Activity> activityTableView;
    @FXML
    private ComboBox chooseEvent;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    @FXML
    private Label infoActivity;
    @FXML
    private ScrollPane infoScroll;
    private Datasource<ActivityList> activityListDatasource;
    private ActivityList activityList;
    private Account account;
    private Object[] objects;
    private String eventName;

    @FXML
    public void initialize() {
        infoActivity.setText("");
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        Boolean isLightTheme = (Boolean) objects[1];

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);
        setObject(objects);

        activityListDatasource = new ActivityListFileDatasource("data", "activity-list.csv");
        activityList = activityListDatasource.readData();
        for(Activity activity:activityList.getAllActivities()){
            if(activity.userIsParticipant(account.getUsername())){
                chooseEvent.getItems().add(activity.getEventName());
            }
        }
        infoActivity.setPrefWidth(698);
        infoActivity.setWrapText(true);
        infoActivity.setText("");
        bPane.setVisible(false);
        slide.setTranslateX(-200);
        activityTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Activity>() {
            @Override
            public void changed(ObservableValue observable, Activity oldValue, Activity newValue) {
                if (newValue == null) {
                    infoActivity.setText("");
                } else {
                    if(newValue.getInfoActivity() != null) {
                        infoActivity.setText(newValue.getInfoActivity());
                    }
                    else {
                        infoActivity.setText("");
                    }
                    infoScroll.setContent(infoActivity);
                }
            }
        });
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

        TableColumn<Activity, LocalTime> participantColumn = new TableColumn<>("Participant");
        participantColumn.setCellValueFactory(new PropertyValueFactory<>("participantName"));

        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);
        activityTableView.getColumns().add(participantColumn);

        activityTableView.getItems().clear();

        for (Activity activity: activityList.getActivities()) {
            activityTableView.getItems().add(activity);
        }
    }

    @FXML
    protected void onSearchClick() {
        eventName = (String) chooseEvent.getValue();
        updateSchedule();
        showTable(activityList);
    }
    private void updateSchedule(){
        activityList = activityListDatasource.readData();
        activityList.findActivityInEvent(eventName);
    }
}
