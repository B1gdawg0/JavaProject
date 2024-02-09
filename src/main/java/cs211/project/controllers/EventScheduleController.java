package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.collections.ActivityList;
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

public class EventScheduleController extends MenuBarController {
    @FXML
    private TableView<Activity> activityTableView;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    @FXML
    private Label infoActivity;
    @FXML
    private ScrollPane infoScroll;
    private Account account;
    private Object[] objects;
    private Object[] objectsSend;
    private Activity selectedActivity;

    @FXML
    public void initialize() {
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        Boolean isLightTheme = (Boolean) objects[2];

        objectsSend = new Object[2];
        objectsSend[0] = account;
        objectsSend[1] = isLightTheme;
        setObject(objectsSend);

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);

        showTable(((Event) objects[1]).loadActivityInEvent());
        infoActivity.setPrefWidth(680);
        infoActivity.setWrapText(true);
        infoActivity.setText("");
        bPane.setVisible(false);
        slide.setTranslateX(-200);

        activityTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Activity>() {
            @Override
            public void changed(ObservableValue observable, Activity oldValue, Activity newValue) {
                if (newValue == null) {
                    selectedActivity = null;
                } else {
                    selectedActivity = newValue;
                    if(!selectedActivity.getInfoActivity().equals("null")) {
                        infoActivity.setText(selectedActivity.getInfoActivity());
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

        TableColumn<Activity, String> startDateActivityColumn = new TableColumn<>("start-date");
        startDateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Activity, String> endDateActivityColumn = new TableColumn<>("end-date");
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

    @FXML
    protected void onBackClick() throws IOException {
        try {
            FXRouter.goTo("joined-history", objectsSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
