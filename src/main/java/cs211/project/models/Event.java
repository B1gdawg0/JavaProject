package cs211.project.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;

public class Event {
    private String eventName;
    private LocalDate startDate ;
    private LocalDate endDate;
    private String startTime;
    private String endTime;
    private int ticket;
    private String detail;
    private LocalDate startJoinDate;
    private LocalDate endJoinDate;
    private LocalDate teamStartDate;
    private LocalDate teamEndDate;
    private int ticketBuy;
    private String eventManager;
    private String picURL;
    private ActivityList activities;
    private TeamList teams;
public Event(String eventName, LocalDate startDate, LocalDate endDate, String startTime, String endTime,
             int ticket, String detail, LocalDate startJoinDate, LocalDate endJoinDate,int ticketBuy, String picURL, String eventManager,LocalDate teamStartDate, LocalDate teamEndDate){
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime =startTime;
        this.endTime = endTime;
        this.ticket = ticket;
        this.detail = detail;
        this.startJoinDate = startJoinDate;
        this.endJoinDate = endJoinDate;
        this.ticketBuy = ticketBuy;
        this.eventManager = eventManager;
        this.picURL = picURL;
        this.teamStartDate = teamStartDate;
        this.teamEndDate = teamEndDate;
    }

    public Event(String eventName){
        this.eventName = eventName;
        loadEventInfo();
    }

    public String getEventName() {
        return eventName;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public int getTicket() {
        return ticket;
    }
    public String getDetail() {
        return detail;
    }
    public LocalDate getStartJoinDate(){
        return startJoinDate;
    }
    public LocalDate getEndJoinDate(){
        return endJoinDate;
    }
    public int getTicketBuy() {
        return ticketBuy;
    }
    public String getEventManager(){return eventManager;}
    public String getPicURL() {
        return picURL;
    }
    public LocalDate getTeamStartDate(){return teamStartDate;};
    public LocalDate getTeamEndDate() {
        return teamEndDate;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public void setTicket(int ticket) {
        this.ticket = ticket;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setStartJoinDate(LocalDate startJoinDate) {
        this.startJoinDate = startJoinDate;
    }

    public void setEndJoinDate(LocalDate endJoinDate) {
        this.endJoinDate = endJoinDate;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public void setTeamEndDate(LocalDate teamEndDate) {
        this.teamEndDate = teamEndDate;
    }

    public void setTeamStartDate(LocalDate teamStartDate) {
        this.teamStartDate = teamStartDate;
    }

    public void ticketBuy() {
        ticketBuy += 1;
    }

    public void ticketCancel(){
        ticketBuy -= 1;
    }

    public int getTicketLeft() {
        return ticket - ticketBuy;
    }

    public ArrayList<String> getArrayHour() {
        ArrayList<String> arrayStartTimeActivity = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            arrayStartTimeActivity.add(String.valueOf(i));
        }
        return arrayStartTimeActivity;
    }

    public ArrayList<String> getArrayMinute() {
        ArrayList<String> arrayMinute = new ArrayList<>();
        for(int i = 0; i < 60; i++){
            arrayMinute.add(String.valueOf(i));
        }
        
        return arrayMinute;
    }

    public ArrayList<Activity> getArrayListActivities(){
        return activities.getActivities();
    }

    public ActivityList getActivities(){
        return activities;
    }

    public TeamList getTeams() {
        return teams;
    }
    public void addTicket() {
        ticket += 1;
    }

    public boolean isEvent(String eventName) {
        return this.eventName.equals(eventName);
    }
    public boolean isEventManager(String username) {
        return this.eventManager.equals(username);
    }

    public Event loadEventInfo() {
        EventListFileDatasource eventListFileDatasource = new EventListFileDatasource("data", "event-list.csv");
        for(Event event : eventListFileDatasource.readData().getEvents()){
            if(event.getEventName().equals(eventName)){
                this.eventName = event.getEventName();
                this.startDate = event.getStartDate();
                this.endDate = event.getEndDate();
                this.startTime = event.getStartTime();
                this.endTime = event.getEndTime();
                this.ticket = event.getTicket();
                this.detail = event.getDetail();
                this.startJoinDate = event.getStartJoinDate();
                this.endJoinDate = event.getEndJoinDate();
                ticketBuy = event.getTicketLeft();
            }
        }
        return this;
    }

    public ActivityList loadActivityInEvent() {
        ActivityListFileDatasource activityListFileDatasource = new ActivityListFileDatasource("data","activity-list.csv");
        activities = activityListFileDatasource.readData();
        activities.findActivityInEvent(eventName);

        return activities;
    }

    public TeamList loadTeamInEvent() {
        loadActivityInEvent();
        TeamListFileDatasource data = new TeamListFileDatasource("data", "team.csv");
        TeamList list = data.readData();
        teams = new TeamList();
        for(Team team : list.getTeams()){
            if(team.getEvent().getEventName().equals(eventName)){
                teams.addTeam(team);
            }
        }

        return  teams;
    }
    public Boolean checkParticipantIsFull(){
        loadActivityInEvent();
        for(Activity activity: activities.getActivities()){
            if(activity.getParticipantName().equals("")){
                return true;
            }
        }
        return false;
    }
    public Boolean checkTimeActivity(LocalDateTime startActivityTime, LocalDateTime endActivityTime) {
        LocalDateTime endLocalDateTime = null;
        LocalTime startLocalTime = LocalTime.parse(startTime);
        LocalDate localDate = startDate;
        LocalTime endLocalTime = LocalTime.parse(endTime);
        LocalDateTime end = LocalDateTime.of(endDate, endLocalTime);

        while (!localDate.isAfter(endDate)) {
            LocalDateTime startLocalDateTime = LocalDateTime.of(localDate, startLocalTime);
            if(startLocalDateTime.getHour() > end.getHour()){
                endLocalDateTime = LocalDateTime.of(LocalDate.from(startLocalDateTime).plusDays(1), endLocalTime);
            }
            else{
                endLocalDateTime = LocalDateTime.of(LocalDate.from(startLocalDateTime), endLocalTime);
            }
            if (!startLocalDateTime.isAfter(startActivityTime) && endLocalDateTime.isAfter(startActivityTime) && startLocalDateTime.isBefore(endActivityTime) && !endLocalDateTime.isBefore(endActivityTime)) {
                return true;
            }
            localDate = localDate.plusDays(1);
        }
        return false;
    }
    @Override
    public String toString() {
        return eventName;
    }
}
