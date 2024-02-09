package cs211.project.services;

import cs211.project.models.Activity;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivityList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ActivityListFileDatasource implements Datasource<ActivityList>{
    private String directoryName;
    private String fileName;

    public ActivityListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ActivityList readData() {
        ActivityList activities = new ActivityList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            while ( (line = buffer.readLine()) != null ){
                if (line.isEmpty()) continue;

                String[] data = line.split(",");

                String activityName = data[0].trim();
                LocalDate startDate = LocalDate.parse(data[1].trim());
                LocalDate endDate = LocalDate.parse(data[2].trim());
                LocalTime startTimeActivity = LocalTime.parse(data[3].trim());
                LocalTime endTimeActivity = LocalTime.parse(data[4].trim());
                String teamName = data[5].trim();
                String participantName = data[6].trim();
                String status = data[7].trim();
                String eventName = data[8].trim();
                String infoActivity = data[9].trim();
                activities.addActivity(activityName, startDate, endDate, startTimeActivity, endTimeActivity, teamName, participantName, status, eventName, infoActivity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return activities;
    }

    @Override
    public void writeData(ActivityList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);

        try {
            for (Activity activity : data.getAllActivities()) {
                if(!activity.getActivityName().equals("")) {
                    String line = activity.getActivityName() + "," + activity.getStartDate()+","+activity.getEndDate() + "," + activity.getStartTimeActivity() + "," + activity.getEndTimeActivity() + "," + activity.getTeamName() + "," + activity.getParticipantName() + "," + activity.getStatus() + "," + activity.getEventName() + "," + activity.getInfoActivity();
                    buffer.append(line);
                    buffer.append("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    public void updateTeamInActivity(String activityName,Team team){
        ArrayList<String[]> allInfo = new ArrayList<>();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            while ( (line = buffer.readLine()) != null ){
                if (line.equals("")) continue;
                String[] data = line.split(",");

                if(data[0].equals(activityName)){
                    if(team != null) data[5] = team.getTeamName();
                    else data[5] = "";
                }

                allInfo.add(data);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);


        try {
            bufferedWriter.write("");
            for(int i = 0;i<allInfo.size();i++){
                String[] newLine = allInfo.get(i);
                String writeLine = newLine[0]+","+newLine[1]+","+newLine[2]+","+newLine[3]+","+newLine[4]+","+newLine[5]+","+newLine[6]+","+newLine[7]+","+newLine[8]+","+newLine[9];
                bufferedWriter.append(writeLine);
                bufferedWriter.append("\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bufferedWriter.flush();
                bufferedWriter.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }

    }

}
