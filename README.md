# CS211-661-Project
> Project ภาคต้น 2566

## Project detail
### รายชื่อสมาชิก
* ### 6510450160 กวินท์ สินธพสิริพร: วิน (Kawin Sinthopsiriporn)
  * 17 ระบบของผู้ใช้ทั่วไป > การร่วมอีเวนต์
    * a. มีหน้าแสดงรายการอีเวนต์ที่อยู่ระหว่างการจัดงานทั้งหมดให้ผู้ใช้ทั่วไปดู โดยแสดงภาพอีเวนต์ ชื่ออีเวนต์ และจำนวนที่ว่างเหลือสำหรับเข้าร่วมอีเวนต์
    * b. มีหน้าแสดงประวัติรายการอีเวนต์ที่ตนเองเข้าร่วม โดยแยกอีเวนต์ที่อยู่ระหว่างการจัดงาน และอีเวนต์ที่สิ้นสุดแล้ว
    * c. มีส่วนให้ค้นหาอีเวนต์ด้วยบางส่วนของชื่ออีเวนต์
  * 18 ระบบของผู้ใช้ทั่วไป > การสร้างอีเวนต์
    * b. มีหน้าแสดงรายการอีเวนต์ที่ตนเองเป็นผู้จัดอีเวนต์ ซึ่งสามารถแสดงหน้าจัดการอีเวนต์ได้
  * 20 ระบบของผู้ใช้ทั่วไป > ผู้จัดอีเวนต์ > การจัดการผู้เข้าร่วมอีเวนต์

* ### 6510450607 ปริยานุช มั่งคั่ง: อัย (Pariyanuch)
  * 16 การสร้างบัญชีของผู้ใช้ทั่วไป
  * 17 ระบบของผู้ใช้ทั่วไป > การร่วมอีเวนต์
    * d. มีหน้าแสดงรายละเอียดของอีเวนต์ที่ผู้ใช้เลือกจากข้อ 17a. แสดงรายละเอียดที่เหมาะสม และผู้ใช้สามารถเข้าร่วมอีเวนต์ได้หากมีที่ว่างเหลืออยู่ (หากไม่มีที่ว่างเหลือต้องไม่ให้เข้าร่วม) - หากเข้าร่วมอีเวนต์แล้ว ให้แสดง “ตารางกิจกรรมของอีเวนต์”
  * 18 ระบบของผู้ใช้ทั่วไป > การสร้างอีเวนต์
    * a. มีส่วนให้ผู้ใช้ทั่วไปสร้างอีเวนต์ เพื่อเป็น “ผู้จัดอีเวนต์” โดยต้องระบุข้อมูลต่อไปนี้
  * 19 ระบบของผู้ใช้ทั่วไป > การจัดการอีเวนต์
    * c. มีส่วนจัดการ “การเปิดรับผู้เข้าร่วมอีเวนต์” (เช่น ผู้ร่วมสัมมนา ผู้เข้าแข่งขัน) โดยระบุจำนวนผู้เข้าร่วมอีเวนต์สูงสุด และช่วงเวลาเริ่มต้น-สิ้นสุดที่จะเปิดรับผู้เข้าร่วมอีเวนต์

* ### 6510450763 ภัควัฒน์ ปานกลาง: จิม (JIMpk2w)
  * 15 ระบบของผู้ดูแลระบบ
  * 19 ระบบของผู้ใช้ทั่วไป > การจัดการอีเวนต์
    * a. ต้องเป็น ”ผู้จัดอีเวนต์” นี้เท่านั้นจึงจะจัดการอีเวนต์นี้ได้ 
    * b. มีหน้าแก้ไขข้อมูลอีเวนต์
  * 22 ระบบของผู้ใช้ทั่วไป > ผู้ร่วมทีม > การสื่อสารภายในทีม

* ### 6510450917 เลิศพิพัฒน์ กาญจนเรืองโรจน์: เบสท์ (B1gdawg0)
  * 19 ระบบของผู้ใช้ทั่วไป > การจัดการอีเวนต์
   * e.  มีส่วนจัดการ “การเปิดรับทีมผู้ร่วมจัดอีเวนต์” (เช่น ทีมอาสาสมัคร) โดยระบุชื่อทีม จำนวนผู้ร่วมทีมสูงสุด และช่วงเวลาเริ่มต้น-สิ้นสุดที่จะเปิดรับผู้ร่วมทีม ซึ่งสร้างได้หลายทีม และชื่อทีมต้องไม่ซ้ำกันในอีเวนต์เดียวกัน
  * 21 ระบบของผู้ใช้ทั่วไป > ผู้จัดอีเวนต์ > การจัดการทีมผู้ร่วมจัดอีเวนต์
**หากทำครบทั้งข้อจะไม่เขียนข้อย่อย**


### วิธีการติดตั้งหรือรันโปรแกรม
* เข้าไปยัง github reposity นี้:
            https://github.com/CS211-661/cs211-661-project-java-best-win-ai-jim.git

โดยให้กดเข้าไปที่ tag แล้วเลือก release 1.0.0 จะสังเกตุเห็นไฟล์ชื่อ java-best-win-ai-jim.zip ให้Downloadและแตกไฟล์(Extract file) โดยถ้า

    (1)ท่านใช้ระบบปฏิบัติการ window ท่านสามารถ double-clickเพื่อเข้าใช้งานได้เลย

    (2)ท่านใช้ระบบปฏิบัติการ macOS ท่านต้องแตกไฟล์.zipของท่านที่หน้า Desktop แล้วจึง 
         เปิด Terminal หรือ Iterm หลังจากนั้นให้ใช้คำสั่ง 

                   cd Desktop/

        หลังจากนั้นให้ใช้คำสั่ง 

                   java -jar cs211-661-project-1.0-SNAPSHOT-shaded.jar


        ท่านจึงจะสามารถใช้งานโปรแกรมได้


### การวางโครงสร้างไฟล์
```
cs211-661-project-java-best-win-ai-jim
ª   .idea
ª   .mvn
ª   .target
ª   .gitignore
ª   mvnw
ª   mvnw.cmd
ª   pom.xml
ª   README.md
ª   
ª           
+---data
ª       activity-list.csv
ª       ban-staff-list.csv
ª       ban-user.csv
ª       event-list.csv
ª       team-comment.csv
ª       team.csv
ª       user-info.csv
ª       user-joined-event.csv
ª       
+---images
ª       ...
ª       
+---src
ª   +---main
ª       +---java
ª       ª   ª   module-info.java
ª       ª   ª   
ª       ª   +---cs211
ª       ª       +---project
ª       ª           +---controllers
ª       ª           ª       AboutUsController.java
ª       ª           ª       BanAllController.java
ª       ª           ª       CommentTeamController.java
ª       ª           ª       CreateEventController.java
ª       ª           ª       CreateScheduleController.java
ª       ª           ª       CreateTeamController.java
ª       ª           ª       EditEventController.java
ª       ª           ª       EventDetailsController.java
ª       ª           ª       EventHistoryController.java
ª       ª           ª       EventItemController.java
ª       ª           ª       EventScheduleController.java
ª       ª           ª       EventsListController.java
ª       ª           ª       FinishActivityController.java
ª       ª           ª       FixScheduleController.java
ª       ª           ª       JoinedHistoryController.java
ª       ª           ª       LoginPageController.java
ª       ª           ª       ParticipantScheduleController.java
ª       ª           ª       ProfileSetPageController.java
ª       ª           ª       RegisterPageController.java
ª       ª           ª       RePassPageController.java
ª       ª           ª       TeamScheduleController.java
ª       ª           ª       UserStatusController.java
ª       ª           ª       
ª       ª           +---cs211661project
ª       ª           ª       HelloApplication.java
ª       ª           ª       
ª       ª           +---models
ª       ª           ª   ª   Account.java
ª       ª           ª   ª   Activity.java
ª       ª           ª   ª   Event.java
ª       ª           ª   ª   Staff.java
ª       ª           ª   ª   Team.java
ª       ª           ª   ª   
ª       ª           ª   +---collections
ª       ª           ª           AccountList.java
ª       ª           ª           ActivityList.java
ª       ª           ª           EventList.java
ª       ª           ª           StaffList.java
ª       ª           ª           TeamList.java
ª       ª           ª           
ª       ª           +---services
ª       ª                   AccountListDatasource.java
ª       ª                   ActivityListFileDatasource.java
ª       ª                   BanListFileDatasource.java
ª       ª                   CommentTeamListDatasource.java
ª       ª                   Datasource.java
ª       ª                   EventListFileDatasource.java
ª       ª                   FXRouter.java
ª       ª                   TeamListFileDatasource.java
ª       ª                   UserEventListFileDatasource.java
ª       ª                   
ª       +---resources
ª           +---cs211
ª           ª   +---project
ª           ª       +---views
ª           ª               about-us.fxml
ª           ª               ban-all.fxml
ª           ª               comment-activity.fxml
ª           ª               create-event.fxml
ª           ª               create-schedule.fxml
ª           ª               create-team.fxml
ª           ª               dark-theme.css
ª           ª               edit-event.fxml
ª           ª               event-details.fxml
ª           ª               event-history.fxml
ª           ª               event-item.fxml
ª           ª               event-schedule.fxml
ª           ª               events-list.fxml
ª           ª               finish-activity.fxml
ª           ª               fix-schedule.fxml
ª           ª               joined-history.fxml
ª           ª               login-page.fxml
ª           ª               participant-schedule.fxml
ª           ª               profile-setting.fxml
ª           ª               re-password.fxml
ª           ª               register-page.fxml
ª           ª               st-theme.css
ª           ª               team-schedule.fxml
ª           ª               user-status.fxml
ª           ª               
ª           +---images
ª                   ai-image.jpg
ª                   best-image.jpg
ª                   default-event.png
ª                   default-profile.png
ª                   jim-image.jpg
ª                   logo-dark-theme.png
ª                   logo-light-theme.png
ª                   win-image.jpg
```

### ตัวอย่างข้อมูลผู้ใช้ระบบ
* (Admin) Username : admin, Password : 123
* (User)  Username : best, Password : 123
* (User)  Username : win, Password : 123
* (User)  Username : ai, Password : 123
* (User)  Username : jim, Password : 123
* (User)  Username : testjoin, Password : 123
* (User)  Username : testcreate, Password : 123

### สรุปสิ่งที่พัฒนาแต่ละครั้งที่นำเสนอความก้าวหน้าของระบบ

* ครั้งที่ 1 (11/08/2023)
  * ทำ figma วางโครงสร้างโปรแกรม (ทุกคนในกลุ่ม)
  * กำหนด class พื้นฐานต่างๆ เช่น Account, AccountList (ทุกคนในกลุ่ม)
  * กำหนดหน้า fxml ที่จะทำในโปรแกรมแบ่งงานและทำ fxml ในส่วนของตนเอง (ทุกคนในกลุ่ม)

* ครั้งที่ 2 (01/09/2023)
  * เบสท์: 
   * ทำโมเดลและ Collection ของ Staff, ทำโมเดลและ Collection ของ Team, เริ่มทำระบบการแบน, ทำข้อมูล Hardcode สำหรับทดสอบ
  * วิน: 
   *  เริ่มทำระบบการแสดงผลในหน้า EventList, สามารถหา event จากการ serch ได้, เพิ่มเติมโมเดล Event และ EventList, เริ่มทำระบบของหน้า Join History
  * อัย: 
   * ทำโมเดลและ Collection ของ Account, ทำโมเดลและ Collection ของ User, ทำโมเดลและ Collection ของ Event, เริ่มทำระบบ Log in และ Change Password
  * จิม: 
   * ทำโมเดลและ Collection ของ Admin, ทำโมเดลและ Collection ของ Manager, เริ่มทำระบบในหน้า Edit Event, เริ่มทำระบบการสื่อสารของทีมในหน้า Comment
  * แก้ไขหน้า fxml (ทุกคนในกลุ่ม)

* ครั้งที่ 3 (22/09/2023)
  * เบสท์: 
   * ทำระบบการสร้างทีมใน Create Team, ทำตัวอ่านและเขียนไฟล์ของ Activity และ Team และ BanList, เพิ่มเติมโมเดล Activity และ Team, ทำระบบหน้า Finish Activity, พัฒนาระบบการแบน, ทำระบบการแก้ไขตารางของทีมใน FixTeamSchedule, พัฒนาระบบในหน้า Event List, เพิ่มเติมโมเดล Event
  * วิน: 
   *  ทำไฟล์ CSV, ทำระบบการสร้างตาราง(CreateSchedule), พัฒนาระบบหน้า EventSchedule, ทำโมเดลและ Collection ของ Activity, พัฒนาระบบการแสดงผลในหน้า EventList, ทำตารางสำหรับ Participant, พัฒนาระบบการแบน, เพิ่มเติมโมเดล Event, เพิ่มเติม AccountList
  * อัย: 
   * พัฒนาระบบการ Login และ Register, พัฒนาระบบหน้า profileSetting และ Change Password, พัฒนาระบบหน้า Create Event, แก้ไขโมเดลและ Collection ของ Account และ User, ทำตัวอ่านและเขียนไฟล์ของ Account และ Event, เพิ่มเติมโมเดลและ Collection ของ Event, ทำเรื่องการเพิ่มรูปภาพ
  * จิม: 
   * พัฒนาระบบของหน้า Join History, พัฒนาระบบการแสดงผลในหน้า Event List, แก้ไขโมเดลและ Collection ของ User และ Account และ Event และ Team, ทำตัวอ่านและเขียนไฟล์ของ User และ Comment
  * ทำข้อมูล Hardcode สำหรับทดสอบ (ทุกคนในกลุ่ม)
  * ลบโมเดลและไฟล์ที่ไม่จำเป็นออก(User, Admin, Manager, Hardcode)
  * แก้บัคและเก็บรายละเอียดของระบบ (ทุกคนในกลุ่ม)
  * แก้ไขหน้า fxml (ทุกคนในกลุ่ม)

* ครั้งที่ 4 (13/10/2023)
  * เบสท์: 
   * ตรวจสอบและทดสอบระบบ, เขียนคำแนะนำใน Tips, Effect เคลื่อนไหวสำหรับการเปลี่ยนหน้า, ทำรูป Logo, ทำไฟล์ pom สำหรับการรองรับ window และ mac, ทำระบบแก้ไขชื่อ Event ใน EditSchedule ให้สมบูรณ์, เพิ่มวันสำหรับเปิดรับทีมในหน้า Create Event และ Edit Event, แก้ไขจุดผิดพลาดในการเขียนไฟล์ Ban Staff และการตรวจสอบการถูกแบนจาก Event ต่างๆ
  * วิน: 
   * ตรวจสอบและทดสอบระบบ, แก้ Font ใน css เพื่อให้รองรับทั้งภาษาไทยและอังกฤษ ในทั้ง MAC และ Window, ทำระบบ join ให้สมบูรณ์มากขึ้น, แก้ไขเพิ่มเติมในส่วนการ add/delete participant และ Ban user ให้เสร็จสมบูรณ์, แก้ไขและเพิ่มเติมส่วน Team Schedule และ Participant Schedule ให้เสร็จสมบูรณ์, แก้ไขและเพิ่มเติมระบบในหน้า Event List ให้เสร็จสมบูรณ์, แก้ไขโมเดล Activity และเพิ่มรายละเอียด
  * อัย: 
   * แก้ไขและเพิ่มเติมเกี่ยวกับความสวยงามสำหรับการแสดงผล, ทำ Tips และ About Us เพิ่ม, Effect เคลื่อนไหวตรงส่วนเมนู, จัดการไฟล์รูปภาพให้ทำงานได้อย่างถูกต้อง, จัดการด้านการแสดงผลของไฟล์รูปภาพ, เก็บรายละเอียดและตกแต่งความสวยงามต่างๆ, จัดระเบียบหน้า fxml, ทำไฟล์ css, เพิ่มเติมหน้า Change Password, แก้ไขโมเดลและ Collection ของ Event, แก้ไขและเพิ่มเติมส่วน Create Event และ Profile Setting ให้เสร็จสมบูรณ์
  * จิม: 
   * ตรวจสอบและทดสอบระบบ, ทำระบบการ sort, แก้ไขและเพิ่มเติมเกี่ยวกับความสวยงามสำหรับการแสดงผล, เก็บรายละเอียดและตกแต่งความสวยงามต่างๆ, จัดระเบียบหน้า fxml, ทำไฟล์ css, จัดการเกี่ยบกับการส่ง Object ในทุก ๆ หน้า, ทำระบบการเปลี่ยน Theme, แก้ไขและเพิ่มเติมส่วน Edit Event และ Event History ให้เสร็จสมบูรณ์, แก้ไขและเพิ่มเติมส่วน Event List และ Join History 
  * จัดระเบียบและ Clean code (ทุกคนในกลุ่ม)
  * นำส่วนที่ไม่จำเป็นออกจากระบบ (ทุกคนในกลุ่ม)
  * ทำเพิ่มเติมในส่วนของ User Experience เช่น การเปลี่ยนไปใช้ text area สำหรับการรับข้อมูลแทนในบางส่วน, แก้ไขคำให้เข้าใจได้ง่ายขึ้น (ทุกคนในกลุ่ม)
  * แก้บัคและเก็บรายละเอียดของระบบ (ทุกคนในกลุ่ม)
  * สร้าง pdf, readme, UML diagram (ทุกคนในกลุ่ม)