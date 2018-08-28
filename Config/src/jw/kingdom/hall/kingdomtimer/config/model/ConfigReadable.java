package jw.kingdom.hall.kingdomtimer.config.model;

/**
 * All rights reserved & copyright ©
 */
public interface ConfigReadable {
    String getSpeakerScreen();
    boolean isEnabledGleaming();
    boolean isEnabledShowMultimedia();

    String getRecordDestPath();
    boolean isEnabledAutopilot();

    String getMultimediaScreen();
    int getMinRefreshRate();
    int getWarningRefreshRate();
    int getDefaultRefreshRate();

    boolean isDirectDown();
}