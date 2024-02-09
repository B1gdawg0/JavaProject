package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class EventDetailsController extends MenuBarController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label ticketLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane slide;
    @FXML
    private BorderPane bPane;
    @FXML
    private HBox hBox;
    @FXML
    private Label bookDateLabel;
    @FXML
    private ScrollPane descriptionScroll;
    private Event event;
    private Account account;
    private Object[] objectsSend;
    private Object[] objects;
    public void initialize(){

        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        event = (Event) objects[1];
        Boolean isLightTheme = (Boolean) objects[2];

        objectsSend = new Object[2];
        objectsSend[0] = account;
        objectsSend[1] = isLightTheme;
        setObject(objectsSend);

        loadTheme(isLightTheme);
        changeLogoImage(isLightTheme);

        if (!event.getPicURL().equals("/images/default-event.png")) {
            imageView.setImage(new Image("file:"+event.getPicURL(), true));
        } else {
            imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
        }
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        nameLabel.setText(event.getEventName());
        dateLabel.setText(event.getStartDate() + " - " + event.getEndDate());
        bookDateLabel.setText(event.getStartJoinDate() + " - " + event.getEndJoinDate());
        timeLabel.setText(event.getStartTime() + " - " + event.getEndTime());
        ticketLabel.setText(event.getTicketLeft() + "/" + event.getTicket());
        descriptionLabel.setPrefWidth(667);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setText(event.getDetail());
        descriptionScroll.setContent(descriptionLabel);
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("events-list", objectsSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
