package cs211.project.services;

import cs211.project.models.Staff;
import cs211.project.models.collections.StaffList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class BanListFileDatasource implements Datasource<StaffList>{
    private String directoryName;
    private String fileName;
    public BanListFileDatasource(String directoryName, String fileName){
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
    public StaffList readData() {
        StaffList list = new StaffList();
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
                Staff staff = new Staff(data[0],"notnullja");
                for(int i = 1;i< data.length;i++){
                    staff.addEventThatGetBanned(data[i]);
                }
                list.addStaff(staff);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void writeData(StaffList data) {
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
            for(Staff staff : data.getStaffList()){
                String line = staff.getId();
                for(String name : staff.getBannedEvent()){
                    line += ","+name;
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

    public void writeData(Staff staff){
        StaffList list = readData();
        list.addStaff(staff);
        writeData(list);
    }


    public void updateEventToId(String id, String eventName, String op){
        StaffList list = readData();
        for(Staff staff : list.getStaffList()){
            if(staff.isId(id)){
                if(op.equals("+")) staff.addEventThatGetBanned(eventName);
                else if(op.equals("-")) staff.removeEventThatGetBanned(eventName);
            }
            list.addStaff(staff);
        }
        writeData(list);
    }
}
