package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class AboutUsController extends ThemeController {
    @FXML
    private ImageView bestImage;
    @FXML
    private ImageView winImage;
    @FXML
    private ImageView aiImage;
    @FXML
    private ImageView jimImage;

    public void initialize() {
        Boolean isLightTheme = (Boolean) FXRouter.getData();
        loadTheme(isLightTheme);
        bestImage.setImage(new Image(getClass().getResource("/images/best-image.jpg").toExternalForm()));
        winImage.setImage(new Image(getClass().getResource("/images/win-image.jpg").toExternalForm()));
        aiImage.setImage(new Image(getClass().getResource("/images/ai-image.jpg").toExternalForm()));
        jimImage.setImage(new Image(getClass().getResource("/images/jim-image.jpg").toExternalForm()));
    }

    @FXML
    private void onBackClick() throws IOException {
        FXRouter.goTo("login-page");
    }
}
