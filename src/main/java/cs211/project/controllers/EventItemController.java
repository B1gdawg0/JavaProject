package cs211.project.controllers;

import cs211.project.models.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EventItemController {
    @FXML
    private ImageView eventImageView;
    @FXML
    private Label eventItemNameLabel;

    public void setData(Event event) {
        eventItemNameLabel.setText(event.getEventName());
        if(!event.getPicURL().equals("/images/default-event.png")) {
            Image image = new Image("file:" + event.getPicURL(), true);
            eventImageView.setImage(image);
        } else {
            Image image = new Image(getClass().getResource("/images/default-event.png").toExternalForm());
            eventImageView.setImage(image);
        }
    }
}
