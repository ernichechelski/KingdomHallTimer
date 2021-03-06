package jw.kingdom.hall.kingdomtimer.domain.countdown;

import jw.kingdom.hall.kingdomtimer.app.view.common.controller.TimeDisplayController;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * This file is part of KingdomHallTimer which is released under "no licence".
 */

abstract class TimerCountdownBase {
    private List<TimeDisplayController> controllers = new ArrayList<>();
    private boolean pause = true;
    private boolean stop = true;
    private int time = 0;
    private Thread countdown;

    private void countDown() {
        if(!pause) {
            time--;
            onTimeChange(time);
            notifyControllers();
        }
    }

    protected abstract void onTimeChange(int time);

    private void notifyControllers() {
        int timeToSet = getTimeToSet();
        for(TimeDisplayController display:controllers){
            display.setTime(timeToSet);
            if(stop) {
                display.setColorCode(TimerColor.getDefaultColorCode());
            } else {
                display.setColorCode(TimerColor.getColorCode(getStartTime(), time));
            }
        }
    }

    private int getTimeToSet() {
        if(isDirectDown()) {
            return time;
        } else {
            if(time<0){
                return time;
            } else {
                return getAddedTime()+getStartTime()-time;
            }
        }
    }

    protected abstract int getAddedTime();
    protected abstract int getStartTime();
    protected abstract boolean isDirectDown();

    protected void stop() {
        setPause(true);
        stop = true;
        setTime(0);
    }

    protected void startTime(int time) {
        setTime(time);
        setPause(false);
        stop = false;
    }

    protected boolean isStop() {
        return stop;
    }

    protected boolean isPause() {
        return pause;
    }

    protected void setPause(boolean pause) {
        this.pause = pause;
        if(!pause) {
            reloadThread();
            countdown.start();
        }
    }

    protected int getTime() {
        return time;
    }

    protected void setTime(int time) {
        this.time = time;
        notifyControllers();
    }

    protected void addController(TimeDisplayController controller) {
        if(!isOnList(controller)) {
            controllers.add(controller);
        }
    }

    protected void removeController(TimeDisplayController controller) {
        TimeDisplayController display = getControllerById(controller.getId());
        if(display!=null) {
            controllers.remove(display);
        }
    }

    private boolean isOnList(TimeDisplayController controller) {
        return getControllerById(controller.getId()) != null;
    }

    @Nullable
    private TimeDisplayController getControllerById(String id) {
        for(TimeDisplayController display: controllers){
            if(display.getId().equals(id)) {
                return display;
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    void reloadThread() {
        if(countdown!=null) {
            countdown.stop();
        }
        countdown = null;
        countdown = getThread();
    }

    private Thread getThread() {
        return new Thread(() -> {
            while (!pause) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDown();
            }
        });
    }
}
