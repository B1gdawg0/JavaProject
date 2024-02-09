package cs211.project.models.collections;

import java.util.ArrayList;
import cs211.project.models.*;

public class StaffList {
    private ArrayList<Staff> staffs;
    public StaffList(){
        staffs = new ArrayList<>();
    }
    private String name;
    private String id;

    public void addStaff(String id, String name){
        name = name.trim();
        id = id.trim();

        if(!id.equals("") && !name.equals("")){
            Staff exist = checkStaffInList(id);
            if(exist == null){
                staffs.add(new Staff(id.trim(),name.trim()));
            }
        }
    }

    public void addStaff(Staff staff){
        name = staff.getName().trim();
        id = staff.getId().trim();

        if(!id.equals("") && !name.equals("")){
            Staff exist = checkStaffInList(id);
            if(exist == null){
                staffs.add(staff);
            }
        }
    }

    public Staff checkStaffInList(String id){
        for(Staff staff: staffs){
            if(staff.isId(id)){
                return staff;
            }
        }
        return null;
    }

    public ArrayList<Staff> getStaffList(){
        return staffs;
    }
}
