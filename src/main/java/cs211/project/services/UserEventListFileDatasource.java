package cs211.project.services;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UserEventListFileDatasource implements Datasource<AccountList> {
    private String directoryName;
    private String fileName;


    public UserEventListFileDatasource(String directoryName, String fileName) {
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
    public AccountList readData() {
        Datasource<AccountList> accountList = new AccountListDatasource("data", "user-info.csv");
        AccountList users = accountList.readData();
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
                int id = Integer.parseInt(data[0]);

                for (int i = 1; i < data.length; i++) {
                    String eventName = data[i].trim();
                    users.addUserEvent(id,eventName);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public void writeData(AccountList data) {
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
            for (Account user : data.getAccount()) {
                String line = String.valueOf(user.getId());
                for (String event : user.getAllEventUser()) {
                    line += "," + event;
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

}