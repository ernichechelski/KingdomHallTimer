package jw.kingdom.hall.kingdomtimer.app.view.panel;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jw.kingdom.hall.kingdomtimer.app.CrashMaker;
import jw.kingdom.hall.kingdomtimer.app.view.loader.Screens;
import jw.kingdom.hall.kingdomtimer.app.view.loader.StageWindow;
import jw.kingdom.hall.kingdomtimer.app.view.loader.WindowController;
import jw.kingdom.hall.kingdomtimer.app.view.loader.WindowSettings;
import jw.kingdom.hall.kingdomtimer.domain.backup.BackupManager;

/**
 * This file is part of KingdomHallTimer which is released under "no licence".
 */
public class PanelWindow implements StageWindow {
    public final WindowController CONTROLLER;

    private static Stage stage;
    private static Scene scene;
    private static Group root;

    public PanelWindow() {
        CONTROLLER = new WindowController(this);
    }

    public void build(Stage primaryStage){
        stage = primaryStage;
        root = new Group();

        stage.setTitle(WindowSettings.TITLE);

        loadScreens();
        root.getChildren().addAll(CONTROLLER.getScreensPane());
        CONTROLLER.setScreen(Screens.PANEL_CONTROLLING);

        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        stage.setOnCloseRequest(event -> {
            BackupManager.delete();
            System.exit(0);
        });

//        crashOnDemand();
    }

    /**
     * Useful for testing backup system
     */
    private void crashOnDemand() {
        new Thread(()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CrashMaker.crashApp();
        }).start();
    }

    public void loadScreens() {
        CONTROLLER.loadScreen(Screens.PANEL_CONTROLLING);
    }

    @Override
    public Stage getStage() {
        return stage;
    }
}
