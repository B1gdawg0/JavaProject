package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.collections.ActivityList;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalTime;

public class FinishActivityController extends MenuBarController {
    @FXML
    private TableView<Activity> activityTableView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label timeStartLabel;
    @FXML
    private Label timeStopLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    private ActivityListFileDatasource data;
    private Account account;
    private Event selectedEvent;
    private Activity selectedActivity;
    private Object[] objects;
    private Object[] objectsSend;

    @FXML
    public void initialize(){
        clearInfo();
        updateData();
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        selectedEvent = (Event) objects[1];
        Boolean isLightTheme = (Boolean) objects[2];

        objectsSend = new Object[2];
        objectsSend[0] = account;
        objectsSend[1] = isLightTheme;

        setObject(objectsSend);

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);

        showTable(selectedEvent.loadActivityInEvent());

        bPane.setVisible(false);
        slide.setTranslateX(-200);

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

    public void showTable(ActivityList list){
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
            if(activity.getStatus().equals("0")) activityTableView.getItems().add(activity);
        }
    }

    public void showInfo(Activity activity){
        nameLabel.setText(activity.getActivityName());
        timeStartLabel.setText(activity.getStartTimeActivity());
        timeStopLabel.setText(activity.getEndTimeActivity());
        dateLabel.setText(activity.getDate());
    }

    public void clearInfo(){
        nameLabel.setText("");
        timeStopLabel.setText("");
        timeStartLabel.setText("");
        dateLabel.setText("");
    }

    public void updateData(){
        data = new ActivityListFileDatasource("data", "activity-list.csv");
    }

    public void finishActivity(){
        ActivityList list = data.readData();
        list.findActivityInEvent(selectedActivity.getEventName());
        list.setActivityStatus(selectedActivity.getActivityName(),"1");
        data.writeData(list);
        updateData();
        list = data.readData();
        list.findActivityInEvent(selectedEvent.getEventName());
        showTable(selectedEvent.loadActivityInEvent());
    }


    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("event-history", objectsSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
