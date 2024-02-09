package cs211.project.models;

import java.util.ArrayList;

public class Staff{
    private String id;
    private String name;
    private ArrayList<String> bannedEvent;

    public Staff(String id, String name){
        this.id = id;
        this.name = name;
        bannedEvent = new ArrayList<>();
    }

    public Staff(Account account){
        this.id = Integer.toString(account.getId());
        this.name = account.getName();
        bannedEvent = new ArrayList<>();
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public boolean isId(String id){
        return this.getId().equals(id);
    }

    public void addEventThatGetBanned(String eventName){
        bannedEvent.add(eventName);
    }

    public void removeEventThatGetBanned(String eventName){
        bannedEvent.remove(eventName);
    }
    public ArrayList<String> getBannedEvent(){
        return bannedEvent;
    }

    @Override
    public String toString() {
        return name+" : "+id;
    }
}
