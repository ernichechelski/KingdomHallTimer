package jw.kingdom.hall.kingdomtimer.app.javafx.view.head.tab.time;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import jw.kingdom.hall.kingdomtimer.app.javafx.common.controller.TimeDisplayController;
import jw.kingdom.hall.kingdomtimer.app.javafx.common.sps.StartPauseStopView;
import jw.kingdom.hall.kingdomtimer.app.javafx.view.head.tab.TabPresenter;
import jw.kingdom.hall.kingdomtimer.app.javafx.view.head.tab.time.table.TaskTableController;
import jw.kingdom.hall.kingdomtimer.app.javafx.view.head.tab.time.timedirect.BtnTimeDirectBase;
import jw.kingdom.hall.kingdomtimer.app.javafx.view.head.tab.time.timedirect.BtnTimeDirectForInstantController;
import jw.kingdom.hall.kingdomtimer.app.javafx.view.head.tab.time.timedirect.BtnTimeDirectForPanel;
import jw.kingdom.hall.kingdomtimer.domain.countdown.TimerCountdownListener;
import jw.kingdom.hall.kingdomtimer.domain.model.MeetingTask;
import jw.kingdom.hall.kingdomtimer.domain.schedule.NotEnoughTasksException;
import jw.kingdom.hall.kingdomtimer.javafx.custom.TimeField;

/**
 * This file is part of KingdomHallTimer which is released under "no licence".
 */
public class TimeControlController extends TabPresenter implements Initializable, StartPauseStopView.Listener,
StartPauseStopView.Controller {

    @FXML
    private Label lblTime;

    @FXML
    private Button btnFastDirect;

    @FXML
    private Button btnInstantDirect;

    @FXML
    private HBox hbTimeControlsContainer;

    @FXML
    private Button btnBuzzer;

    @FXML
    private TimeField tfFastTime;

    @FXML
    private TextField tfName;

    @FXML
    private TimeField atfTime;

    @FXML
    private CheckBox cbBuzzer;

    @FXML
    private Button btnCountdownDirect;

    @FXML
    private Button btnWidgetVisibility;

    @FXML
    private CheckBox cbTimeToEvaluate;

    @FXML
    private TableView<MeetingTask> tvList;

    @FXML
    private TableColumn<MeetingTask, String> tcDelete;

    @FXML
    private TableColumn<MeetingTask, String> tcBuzzer;

    @FXML
    private TableColumn<MeetingTask, String> tcDirect;

    @FXML
    private TableColumn<MeetingTask, String> tcName;

    @FXML
    private TableColumn<MeetingTask, TimeField> tcTime;

    @FXML
    private TableColumn<MeetingTask, String> tcType;

    private TimeDisplayController timeDisplay;
    private TaskTableController tableController;
    private BtnBuzzerController buzzerController;
    private BtnTimeDirectForPanel timeDirectController;
    private BtnTimeDirectForInstantController instantDirectController;
    private BtnTimeDirectForPanel fastDirectController;
    private StartPauseStopView spsView;

    @Override
    public void onSetup() {
        super.onSetup();
        spsView = new StartPauseStopView();
        hbTimeControlsContainer.getChildren().add(spsView);
        spsView.addListener(this);
        spsView.setController(this);

        buzzerController = new BtnBuzzerController(getCountdown(), btnBuzzer);

        timeDirectController = new BtnTimeDirectForPanel(btnCountdownDirect, getConfig());
        timeDirectController.setMedium(false);

        fastDirectController = new BtnTimeDirectForPanel(btnFastDirect, getConfig());
        fastDirectController.setMedium(false);

        instantDirectController = new BtnTimeDirectForInstantController(getCountdown(), getConfig(), btnInstantDirect);
        instantDirectController.setMedium(true);

        timeDisplay = new TimeDisplayController(lblTime);
        timeDisplay.setTime(0);
        getCountdown().addController(timeDisplay);
        tableController = new TaskTableController(
                getSchedule(),
                getConfig(),
                tvList,
                tcDelete,
                tcBuzzer,
                tcDirect,
                tcName,
                tcTime,
                tcType
        );

        setupInstantDirectController();
        getCountdown().addListener(new TimerCountdownListener() {
            @Override
            public void onStart(MeetingTask task) {
                super.onStart(task);
                spsView.setupForStart();
            }

            @Override
            public void onPause() {
                super.onPause();
                spsView.setupForPause();
            }

            @Override
            public void onResume() {
                super.onPause();
                spsView.setupForUnPause();
            }

            @Override
            public void onStop() {
                super.onStop();
                spsView.setupForStop();
            }
        });

        Platform.runLater(()-> new BackupPresenter().run());
        new WidgetVisibilityController(btnWidgetVisibility);
        setupTimeToEvaluate();
    }

    private void setupTimeToEvaluate() {
        cbTimeToEvaluate.setSelected(getConfig().getTimeToEvaluate()>=0);
        cbTimeToEvaluate.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                getConfig().setTimeToEvaluate(60);
            } else {
                getConfig().setTimeToEvaluate(-1);
            }
        });
    }

    private void setupInstantDirectController() {
        instantDirectController.addListener(new BtnTimeDirectBase.ListenerImpl() {
            @Override
            public void onDirectChange(boolean isDirectDown) {
//                getCountdown().setDirectTimeDown(isDirectDown);
            }
        });
    }

    @FXML
    private void handleAddTask(ActionEvent event) {
        MeetingTask task = new MeetingTask();

        task.setName(tfName.getText());
        tfName.clear();

        task.setTimeInSeconds(atfTime.getAllSeconds());
        atfTime.clear();

        task.setUseBuzzer(cbBuzzer.isSelected());
        cbBuzzer.setSelected(false);

        task.setCountdownDown(timeDirectController.isDirectDown());
        timeDirectController.reset();

        getSchedule().addTask(task);
    }

    @FXML
    private void handleLoadTimeAction(ActionEvent event) {
        FastPanelController.executeIfSave(tfFastTime.getAllSeconds(), ()->{
            MeetingTask task = new MeetingTask();
            task.setTimeInSeconds(tfFastTime.getAllSeconds());
            tfFastTime.setSeconds(0);
            task.setCountdownDown(fastDirectController.isDirectDown());
            fastDirectController.reset();
            getCountdown().start(task);
        });
    }

    @FXML
    private void loadOverseerTasksOnline(ActionEvent event) {
        getSchedule().setTasksOnline(getConfig(), true);
    }

    @FXML
    private void loadTasksOnline(ActionEvent event) {
        getSchedule().setTasksOnline(getConfig(), false);
    }

    @FXML
    private void handleAddTime(ActionEvent event) {
        FastPanelController.executeIfSave(tfFastTime.getAllSeconds(), ()->{
            getCountdown().addTime(tfFastTime.getAllSeconds());
        });
    }

    @FXML
    private void handleRemoveTime(ActionEvent event) {
        FastPanelController.executeIfSave(tfFastTime.getAllSeconds(), ()->{
            getCountdown().removeTime(tfFastTime.getAllSeconds());
        });
    }

    @Override
    public void onStart() {
        try {
            MeetingTask task = getSchedule().bringOutFirstTask();
            getCountdown().start(task);
        } catch (NotEnoughTasksException ignore) {}
    }

    @Override
    public void onPause() {
        getCountdown().pause();
    }

    @Override
    public void onResume() {
        getCountdown().resume();
    }

    @Override
    public void onStop() {
        getCountdown().stop();
    }

    @Override
    public boolean isToExecuteSPSAction(StartPauseStopView.ActionType type) {
        if(type==StartPauseStopView.ActionType.START) {
            return getSchedule().getList().size()!=0;
        }
        return true;
    }
}
