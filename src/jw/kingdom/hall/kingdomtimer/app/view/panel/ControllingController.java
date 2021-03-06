package jw.kingdom.hall.kingdomtimer.app.view.panel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import jw.kingdom.hall.kingdomtimer.app.view.common.ControlledScreenImpl;
import jw.kingdom.hall.kingdomtimer.app.view.panel.tabs.TabScreens;
import jw.kingdom.hall.kingdomtimer.app.view.loader.ScreenPaths;
import jw.kingdom.hall.kingdomtimer.app.view.loader.ScreenUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This file is part of KingdomHallTimer which is released under "no licence".
 */
public class ControllingController extends ControlledScreenImpl implements Initializable {

    @FXML
    public TabPane mainContainer;

    @FXML
    public Tab tabTimeControl;

    @FXML
    public HBox hbTimeControl;

    @FXML
    public Tab tabSpeakerScreen;

    @FXML
    public HBox hbSpeakerScreen;

    @FXML
    private Tab tabRecording;

    @FXML
    private HBox hbRecording;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setScreens();
    }

    private void setScreens() {
        setScreenToElement(hbTimeControl, TabScreens.TIME_CONTROL);
        setScreenToElement(hbSpeakerScreen, TabScreens.SPEAKER_SCREEN);
        setScreenToElement(hbRecording, TabScreens.RECORDING);
    }

    private void setScreenToElement(HBox container, TabScreens screen) {
        Node screenNode = getScreen(screen);
        container.getChildren().setAll(screenNode);
    }

    private Node getScreen(TabScreens screenName) {
        String screenPath = ScreenPaths.PANEL_LAYOUTS_TAB+screenName.NAME+"/"+screenName.NAME+".fxml";
        Node screen = new Label("Screen don't want load");
        try {
            screen = new ScreenUtils().getScreenAsNode(screenPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;
    }

    @Override
    protected Region getMainContainer() {
        return mainContainer;
    }
}
