package cs211.project.models.collections;

import cs211.project.models.Activity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class ActivityList {
    private ArrayList<Activity> allActivities;
    private ArrayList<Activity> activities;

    public ActivityList(){
        allActivities = new ArrayList<>();
        activities = new ArrayList<>();
    }
    public void addActivity(String activityName, LocalDate startDate, LocalDate endDate, LocalTime startTimeActivity, LocalTime endTimeActivity, String teamName, String participantName, String status, String eventName, String infoActivity){
        activityName = activityName.trim();
        allActivities.add(new Activity(activityName, startDate, endDate, startTimeActivity, endTimeActivity, teamName, participantName, status, eventName, infoActivity));
        if(!activities.isEmpty()){
            activities.add(new Activity(activityName, startDate, endDate, startTimeActivity, endTimeActivity, teamName, participantName, status, eventName, infoActivity));
        }
    }

    public void findActivityInEvent(String eventName){
        for(Activity activity: allActivities){
            if(activity.getEventName().equals((eventName))){
                activities.add(activity);
            }
        }
    }

    public boolean checkActivity(LocalDateTime startActivityTime, LocalDateTime endActivityTime){
        if(!activities.isEmpty()) {
            for (Activity activity : activities) {
                 if (!activity.checkTimeActivity(startActivityTime,endActivityTime)) {
                    return false;
                }
            }
        }
        if(startActivityTime.isBefore(endActivityTime)) {
            return true;
        }
        return false;
    }

    public boolean checkActivityName(String activityName){
        for(Activity activity: activities){
            if (activity.isActivity(activityName)) {
                return false;
            }
        }
        return true;
    }

    public void addParticipant(String id) {
        for(Activity activity : activities){
            if(activity.getParticipantName().isEmpty()){
                activity.setParticipantName(id);
                break;
            }
        }
    }

    public void setActivityStatus(String activityName, String status) {
        for(Activity activity : activities){
            if(activity.getActivityName().equals(activityName)){
                activity.setActivityStatus(status);
                break;
            }
        }
        for (Activity activity : allActivities) {
            for (Activity activity2 : activities) {
                if(activity.getEventName().equals(activity2.getEventName()) && activity.getActivityName().equals(activity2.getActivityName())){
                    if(!activity.getActivityName().equals(activity2.getActivityName())){
                        activity.setActivityStatus(activity2.getStatus());
                        break;
                    }
                }
            }
        }
    }

    public boolean userIsParticipant(String participantName) {
        for (Activity activity : activities) {
            if(activity.getParticipantName().equals(participantName)){
                return true;
            }
        }
        return false;
    }

    public void changeNameEvent(String  eventName,String changeName){
        for(Activity activity : allActivities){
            if(activity.getEventName().equals(eventName)){
                activity.setEventName(changeName);
            }
        }

    }

    public ArrayList<String> getParticipantInEvent(){
        ArrayList<String> participants = new ArrayList<>();
        for(Activity activity: activities){
            if(!activity.getParticipantName().isEmpty()) {
                participants.add(activity.getParticipantName());
            }
        }
        return participants;
    }

    public ArrayList<Activity> getAllActivities(){
        return allActivities;
    }

    public ArrayList<Activity> getActivities(){
        return activities;
    }
}
