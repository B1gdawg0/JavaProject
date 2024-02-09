package cs211.project.models;

import cs211.project.services.ActivityListFileDatasource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Activity {
    private String activityName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTimeActivity;
    private LocalTime endTimeActivity;
    private String teamName;
    private String participantName;
    private String status;
    private String eventName;
    private Team team;
    private String infoActivity;

    public Activity(String activityName, LocalDate startDate, LocalDate endDate, LocalTime startTimeActivity, LocalTime endTimeActivity, String teamName,String participantName, String status, String eventName,String infoActivity) {
        this.activityName = activityName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTimeActivity = startTimeActivity;
        this.endTimeActivity = endTimeActivity;
        this.teamName = teamName;
        this.participantName = participantName;
        this.status = status;
        this.eventName = eventName;
        this.infoActivity = infoActivity;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getStartDate(){return startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));}

    public String getEndDate(){return endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));}

    public String getDate(){return startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+ "      " +endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));}

    public String getStartTimeActivity() {
        return startTimeActivity.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getEndTimeActivity() {
        return endTimeActivity.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getTeamName() {
        return teamName;
    }

    public String getParticipantName() {
        return participantName;
    }

    public String getStatus() {
        return status;
    }

    public String getEventName() {
        return eventName;
    }

    public Team getTeam(){return team;}

    public String getInfoActivity(){return infoActivity;}

    public void setActivityStatus(String status){
        this.status = status;
    }

    public void setParticipantName(String participantName){
        this.participantName = participantName;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public boolean isActivity(String activityName) {
        return this.activityName.equals(activityName);
    }

    public boolean checkTimeActivity(LocalDateTime startActivityTime, LocalDateTime endActivityTime){
        LocalDateTime endLocalDateTime = LocalDateTime.of(endDate,endTimeActivity);
        LocalDateTime startLocalDateTime = LocalDateTime.of(startDate,startTimeActivity);
        if(startLocalDateTime.isBefore(endActivityTime) && !endLocalDateTime.isBefore(endActivityTime)){
            return false;
        }
        else if (!startLocalDateTime.isAfter(startActivityTime) && endLocalDateTime.isAfter(startActivityTime)){
            return false;
        }
        else if(!startLocalDateTime.isBefore(startActivityTime) && startLocalDateTime.isBefore(endActivityTime)){
            return false;
        }
        return true;
    }

    public boolean userIsParticipant(String participantName){
        return this.participantName.equals(participantName);
    }

    public void updateTeamInActivity(Team team){
        this.team = team;
        ActivityListFileDatasource activityListFileDatasource = new ActivityListFileDatasource("data","activity-list.csv");
        activityListFileDatasource.updateTeamInActivity(activityName,team);
    }

    public void deleteActivity(){
        this.activityName = "";
    }
}
