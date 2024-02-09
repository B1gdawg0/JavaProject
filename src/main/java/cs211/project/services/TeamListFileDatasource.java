package cs211.project.services;

import cs211.project.models.Staff;
import cs211.project.models.Team;
import cs211.project.models.collections.StaffList;
import cs211.project.models.collections.TeamList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TeamListFileDatasource implements Datasource<TeamList>{
    private String directoryName;
    private String fileName;
    public TeamListFileDatasource(String directoryName, String fileName){
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
    public TeamList readData() {
        TeamList teams = new TeamList();
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

                StaffList list =new StaffList();
                String eventName = data[0].trim();
                String teamName = data[1].trim();
                int numberAll = Integer.parseInt(data[2].trim());
                int numberLeft = Integer.parseInt(data[3].trim());

                Team newTeam = new Team(teamName, numberAll,numberLeft,eventName);

                for(int i = 4;i<data.length;i++){
                    newTeam.addStaffInTeam(data[i]);
                }

                teams.addTeam(newTeam);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return teams;
    }

    @Override
    public void writeData(TeamList data) {
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
            for(Team team:data.getTeams()){
                String line = team.getEvent().getEventName()+","+team.getTeamName()+","+team.getNumberOfStaff()+","+team.getNumberOfStaffLeft();
                for(Staff staff:team.getStaffThatNotBan().getStaffList()){
                    line += ","+staff.getId();
                }
                buffer.append(line);
                buffer.append("\n");
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

    public void writeData(Team team) {
        TeamList list = readData();
        list.addTeam(team);
        writeData(list);
    }

    public void  updateStaffInTeam(String eventName, String teamName, Staff staff, String op){
        TeamList list = this.readData();
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
            buffer.write("");
            for(Team team : list.getTeams()){
                if(team.getTeamName().equals(teamName) && team.getEvent().getEventName().equals(eventName)){
                    if(op.equals("+")) team.addStaffInTeam(staff);
                    else if(op.equals("-")) team.banStaffInTeam(staff.getId());
                }
            }
            writeData(list);
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
}
