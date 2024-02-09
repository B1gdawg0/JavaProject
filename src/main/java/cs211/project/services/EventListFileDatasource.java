package cs211.project.services;

import cs211.project.models.Event;
import cs211.project.models.collections.EventList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EventListFileDatasource implements Datasource<EventList>{
    private String directoryName;
    private String fileName;
    public EventListFileDatasource(String directoryName, String fileName){
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
    public EventList readData() {
        EventList eventList = new EventList();
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String eventName = data[0].trim();
                LocalDate startDate = LocalDate.parse(data[1], formatter);
                LocalDate endDate = LocalDate.parse(data[2], formatter);
                String startTime = data[3].trim();
                String endTime = data[4].trim();
                int ticket = Integer.parseInt(data[5].trim());
                String detail = data[6].trim();
                LocalDate startJoinDate = LocalDate.parse(data[7], formatter);
                LocalDate endJoinDate = LocalDate.parse(data[8], formatter);
                int ticketBuy = Integer.parseInt(data[9].trim());
                String picURL = data[10].trim();
                String eventManager = data[11].trim();
                LocalDate teamStartDate = LocalDate.parse(data[12],formatter);
                LocalDate teamEndDate = LocalDate.parse(data[13], formatter);
                eventList.addNewEvent(eventName, startDate, endDate, startTime, endTime, ticket,
                        detail, startJoinDate, endJoinDate, ticketBuy, picURL,eventManager, teamStartDate, teamEndDate);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                buffer.close();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        return eventList;
    }

    @Override
    public void writeData(EventList data) {
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
            for(Event event:data.getEvents()){
                String line = event.getEventName()+","+event.getStartDate()+","+event.getEndDate()+
                        ","+event.getStartTime()+","+event.getEndTime()+","+event.getTicket()
                        +","+event.getDetail()+","+event.getStartJoinDate()
                        +","+event.getEndJoinDate()+","+event.getTicketBuy()
                        +","+event.getPicURL()+","+event.getEventManager()+","+event.getTeamStartDate()
                        +","+event.getTeamEndDate();
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
}
