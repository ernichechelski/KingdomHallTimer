package jw.kingdom.hall.kingdomtimer.javafx.loader;

import javafx.scene.Node;
import jw.kingdom.hall.kingdomtimer.javafx.entity.view.AppWindow;

/**
 * All rights reserved & copyright ©
 */
public interface ViewManager {
    void setWindow(AppWindow window);

    void addScreen(String name, Node screen);
    Node getScreen(String name);
    boolean loadScreen(String name, String path);
    boolean unloadScreen(String name);
    boolean setScreen(final String name);
}
