package jw.kingdom.hall.kingdomtimer.javafx.view.head.tab;

import jw.kingdom.hall.kingdomtimer.javafx.entity.view.window.WindowInput;
import jw.kingdom.hall.kingdomtimer.javafx.entity.view.window.container.WindowsContainer;

/**
 * All rights reserved & copyright ©
 */
public interface TabPresentable {

    void setWindowsContainer(WindowsContainer windowsContainer);
    WindowsContainer getWindowsContainer();

    void setWindowData(WindowInput input);
    WindowInput getWindowData();
}
