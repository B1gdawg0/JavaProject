package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Team;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.CommentTeamListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import cs211.project.services.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.LocalTime;

public class CommentTeamController extends MenuBarController {
    @FXML
    private TextField commentTextField;
    @FXML
    private ComboBox<String> chooseTeam;
    @FXML
    private TableView<Activity> activityTableView;
    @FXML
    private Label eventLabel;
    @FXML
    private ScrollPane commentScrollPane;
    @FXML
    private TextFlow commentTextFlow;
    @FXML
    private Button sendClick;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    private Boolean isLightTheme;
    private Datasource<TeamList> commentDatasource;
    private Datasource<AccountList> accountListDatasource;
    private TeamList commentTeam;
    private String selectedTeam;
    private Account account;
    private AccountList accountList;
    private Team team;
    private Object[] objects;
    private Text commentTextElement;

    @FXML
    private void initialize() {
        commentTextField.setEditable(false);
        sendClick.setVisible(false);
        eventLabel.setVisible(false);

        commentDatasource = new CommentTeamListDatasource("data", "team-comment.csv");
        accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        Datasource<ActivityList> activityListDatasource = new ActivityListFileDatasource("data", "activity-list.csv");
        ActivityList activityList = activityListDatasource.readData();
        commentTeam = commentDatasource.readData();
        accountList = accountListDatasource.readData();

        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        isLightTheme = (Boolean) objects[1];
        setObject(objects);

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);

        team = new Team("",1,1,"");
        chooseTeam.getItems().addAll(team.getListTeam(account.getId()));

        chooseTeam.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                clearCommentTextFlow();

                commentTextField.setEditable(true);
                sendClick.setVisible(true);
                eventLabel.setVisible(true);
                selectedTeam = newValue;

                team = commentTeam.checkTeamExist(selectedTeam);

                showInfo();

                team.setFirstComment("");

                String[] commentLines = team.getComment().split("\\\\n");
                for (String line : commentLines) {
                    commentTextElement = new Text(line + "\n");
                    if (!isLightTheme) {
                        commentTextElement.setStyle("-fx-fill: white;");
                    } else {
                        commentTextElement.setStyle("-fx-fill: #000000;");
                    }
                    if (accountList.findAccountByName(line.trim()) != null) {
                        if (!isLightTheme) {
                            commentTextElement.setStyle("-fx-font-weight: bold; -fx-fill: white;");
                        } else {
                            commentTextElement.setStyle("-fx-font-weight: bold; -fx-fill: #000000;");
                        }
                    }
                    commentTextFlow.getChildren().add(commentTextElement);
                }
                commentScrollPane.setVvalue(1.0);

                activityTableView.getItems().clear();
                activityTableView.getColumns().clear();
                activityList.findActivityInEvent(team.getEvent().getEventName());
                showTable(activityList);
            }
        });
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    @FXML
    private void onSentAction() {
        commentTextFlow.getChildren().clear();

        team = commentTeam.checkTeamExist(selectedTeam);

        String commentText = commentTextField.getText();
        if (!commentText.trim().equals("")) {
            if (team.getComment().equals("")) {
                commentText = account.getName() + "\\n" + commentText;
                team.addComment(commentText);
            } else if (team.checkFirstComment(account.getName())) {
                commentText = team.getComment() + "\\n" + "\\n" + account.getName() + "\\n" + commentText;
                team.addComment(commentText);
            } else {
                commentText = team.getComment() + "\\n" + commentText;
                team.addComment(commentText);
            }

            commentDatasource.writeData(commentTeam);

            team = commentTeam.checkTeamExist(selectedTeam);

        }

        String[] commentLines = team.getComment().split("\\\\n");

        for (String line : commentLines) {
            commentTextElement = new Text(line + "\n");
            if (!isLightTheme) {
                commentTextElement.setStyle("-fx-fill: white;");
            } else {
                commentTextElement.setStyle("-fx-fill: #000000;");
            }
            if (accountList.findAccountByName(line.trim()) != null) {
                if (!isLightTheme) {
                    commentTextElement.setStyle("-fx-font-weight: bold; -fx-fill: white;");
                } else {
                    commentTextElement.setStyle("-fx-font-weight: bold; -fx-fill: #000000;");
                }
            }
            commentTextFlow.getChildren().add(commentTextElement);
        }

        team.setFirstComment(account.getName());
        clearCommentInfo();

        commentScrollPane.setVvalue(1.0);
    }

    private void showTable(ActivityList activityList) {
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, String> dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

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

        for (Activity activity: activityList.getActivities()) {
            activityTableView.getItems().add(activity);
        }

    }

    public void clearCommentInfo() {
        commentTextField.setText("");
    }

    public void clearCommentTextFlow() {
        commentTextFlow.getChildren().clear();
    }

    public void showInfo() {
        eventLabel.setText(team.getEvent().getEventName());
    }
}
