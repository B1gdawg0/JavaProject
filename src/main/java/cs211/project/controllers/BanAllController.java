package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collections.*;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class BanAllController extends MenuBarController {
    @FXML
    private Label constantTeamLabel;
    @FXML
    private ComboBox<Team> chooseTeam;
    @FXML
    private Label nameLabel;
    @FXML
    private RadioButton chooseRoleTeam;
    @FXML
    private RadioButton chooseRoleUserJoin;
    @FXML
    private ListView<Staff> staffListView;
    @FXML
    private ListView<Account> userListView;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    private Object[] objects;
    private Object[] objectsSend;
    private Team team;
    private Staff selectedStaff;
    private Account selectedUser;
    private Boolean notFirst;
    private Event selectedEvent;
    private Datasource<AccountList> accountListDatasource;
    private AccountList accountList;
    private Account account;


    @FXML
    public void initialize() {
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

        clearInfo();
        updateData();

        accountList = accountListDatasource.readData();

        TeamList list = selectedEvent.loadTeamInEvent();

        notFirst = false;
        setSelectedUser();
        chooseRoleUserJoin.setSelected(true);

        showUserJoin();

        chooseTeam.getItems().addAll(list.getTeams());
        setChooseTeamVisible(false);
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    @FXML
    public void clearInfo() {
        nameLabel.setText("");
    }

    @FXML
    public void chooseRole() {
        if (chooseRoleTeam.isSelected()) {
            userListView.setVisible(false);
            staffListView.setVisible(true);
            chooseRoleUserJoin.setSelected(false);
            setChooseTeamVisible(true);
            if (notFirst) {
                chooseWhichTeam();
            }
        }

        if (chooseRoleUserJoin.isSelected()) {
            userListView.setVisible(true);
            staffListView.setVisible(false);
            chooseRoleTeam.setSelected(false);
            setChooseTeamVisible(false);
            clearInfo();
            showUserJoin();

        }
    }

    public void updateData(){
        accountListDatasource = new UserEventListFileDatasource("data", "user-joined-event.csv");
    }

    public void setChooseTeamVisible(boolean bool) {
        chooseTeam.setVisible(bool);
        constantTeamLabel.setVisible(bool);
    }

    public void chooseWhichTeam() {
        team = (Team) chooseTeam.getSelectionModel().getSelectedItem();
        notFirst = true;
        showStaff();
    }

    public void showStaff() {
        staffListView.getItems().clear();
        staffListView.getItems().addAll(team.getStaffThatNotBan().getStaffList());
    }

    public void showUserJoin() {
        userListView.getItems().clear();
        for(Account account: accountList.getAccount()) {
            if(account.isEventName(selectedEvent.getEventName())) {
                userListView.getItems().add(account);
            }
        }
    }

    public void selectedTeam(){
        chooseRoleTeam.setSelected(true);
        chooseRoleUserJoin.setSelected(false);
        chooseRole();

        staffListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Staff>() {
            @Override
            public void changed(ObservableValue<? extends Staff> observable, Staff oldValue, Staff newValue) {
                if (newValue == null) {
                    clearInfo();
                    selectedStaff = null;
                } else {
                    nameLabel.setText(newValue.getName());
                    selectedStaff =  newValue;
                }
            }
        });
    }

    public void setSelectedUser(){
        chooseRoleUserJoin.setSelected(true);
        chooseRoleTeam.setSelected(false);
        chooseRole();
        userListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
            @Override
            public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account newValue) {
                if (newValue == null) {
                    clearInfo();
                    selectedUser = null;
                } else {
                    nameLabel.setText(newValue.getName());
                    selectedUser =  newValue;
                }
            }
        });
    }

    public void banTarget() {
        if (team != null && selectedStaff != null) {
            updateData();
            TeamListFileDatasource data = new TeamListFileDatasource("data","team.csv");
            BanListFileDatasource banPath = new BanListFileDatasource("data", "ban-staff-list.csv");
            team.banStaffInTeam(selectedStaff.getId());
            data.updateStaffInTeam(selectedEvent.getEventName(),team.getTeamName(),selectedStaff,"-");
            banPath.writeData(selectedStaff);
            banPath.updateEventToId(selectedStaff.getId(),selectedEvent.getEventName(),"+");
            showStaff();
        } else if (selectedUser != null) {
            updateData();
            EventListFileDatasource eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
            UserEventListFileDatasource banUserDatasource = new UserEventListFileDatasource("data", "ban-user.csv");
            AccountList banUserList = banUserDatasource.readData();
            EventList eventList = eventListDatasource.readData();
            selectedEvent = eventList.findEventByEventName(selectedEvent.getEventName());
            selectedUser.deleteUserEventName(selectedEvent.getEventName());
            banUserList.addUserEvent(selectedUser.getId(),selectedEvent.getEventName());
            selectedEvent.addTicket();
            eventListDatasource.writeData(eventList);
            banUserDatasource.writeData(banUserList);
            accountListDatasource.writeData(accountList);
            showUserJoin();
        }
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
