package jw.kingdom.hall.kingdomtimer.app.javafx.domain.screen;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.Region;
import jw.kingdom.hall.kingdomtimer.app.javafx.domain.loader.ViewManager;
import jw.kingdom.hall.kingdomtimer.app.javafx.domain.window.AppWindow;
import jw.kingdom.hall.kingdomtimer.app.javafx.domain.window.WindowInput;
import jw.kingdom.hall.kingdomtimer.app.javafx.domain.window.container.WindowsContainer;
import jw.kingdom.hall.kingdomtimer.data.config.AppConfig;
import jw.kingdom.hall.kingdomtimer.domain.countdown.Countdown;
import jw.kingdom.hall.kingdomtimer.domain.record.voice.RecordControl;
import jw.kingdom.hall.kingdomtimer.domain.schedule.Schedule;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * All rights reserved & copyright ©
 */
public abstract class ControlledScreenBase implements ControlledScreen, Initializable {
    protected AppWindow window;
    protected ViewManager viewManager;
    protected WindowsContainer container;
    protected WindowInput data;

    @Override
    public void setWindow(AppWindow window) {
        this.window = window;
        setupAutoMaxSize();
        runSetup();
    }

    @Override
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
        runSetup();
    }

    @Override
    public void setWindowsContainer(WindowsContainer container) {
        this.container = container;
        runSetup();
    }

    @Override
    public void setWindowData(WindowInput input) {
        this.data = input;
        runSetup();
    }

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        onPreCreate();
        onCreate(location, resources);
        runSetup();
    }

    private void runSetup() {
        if(window!=null && viewManager!=null && data!=null && container!=null) {
            Platform.runLater(this::setup);
        }
    }

    private void setup() {
        onSetup();
    }

    protected void onPreCreate() {

    }

    protected void onCreate(URL location, ResourceBundle resources) {

    }

    protected void onSetup() {

    }

    private void setupAutoMaxSize() {
        if(getMainContainer()==null) {
            return;
        }
        getMainContainer().minHeightProperty().bind(
                window.getStage().heightProperty()
        );
        getMainContainer().maxHeightProperty().bind(getMainContainer().minHeightProperty());
        getMainContainer().prefHeightProperty().bind(getMainContainer().minHeightProperty());

        getMainContainer().minWidthProperty().bind(
                window.getStage().widthProperty()
        );
        getMainContainer().maxWidthProperty().bind(getMainContainer().minWidthProperty());
        getMainContainer().prefWidthProperty().bind(getMainContainer().minWidthProperty());
    }

    protected abstract Region getMainContainer();

    @Override
    public WindowsContainer getWindowsContainer() {
        return container;
    }

    @Override
    public WindowInput getWindowData() {
        return data;
    }

    protected final AppConfig getConfig() {
        return getWindowData().getConfig();
    }

    protected final RecordControl getRecorder(){
        return getWindowData().getRecorder();
    }

    protected final Schedule getSchedule(){
        return getWindowData().getSchedule();
    }

    protected final Countdown getCountdown(){
        return getWindowData().getCountdown();
    }
}
