package jw.kingdom.hall.kingdomtimer.app.javafx.view.speaker.screen;

import com.sun.javafx.tk.Toolkit;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jw.kingdom.hall.kingdomtimer.app.javafx.common.controller.MultimediaPreviewController;
import jw.kingdom.hall.kingdomtimer.app.javafx.utils.FontUtils;
import jw.kingdom.hall.kingdomtimer.app.javafx.utils.SizeBinder;
import jw.kingdom.hall.kingdomtimer.app.javafx.view.speaker.screen.coordinator.PreviewSizeController;

/**
 * This file is part of KingdomHallTimer which is released under "no licence".
 */
class PreviewAndTimeCoordinator {
    private Region mainContainer;
    private VBox timeContainer;
    private Label timeView;
    private VBox multimediaContainer;
    private ImageView multimediaView;
    private MultimediaPreviewController previewController;

    PreviewAndTimeCoordinator(ElementsContainer container) {
        this.mainContainer = container.getMainContainer();
        this.timeContainer = container.getTimeContainer();
        this.multimediaContainer = container.getMultimediaContainer();
        this.timeView = container.getTimeView();
        this.multimediaView = container.getView();
        this.previewController = container.getMultimediaPreviewController();
        init();
    }
    /*
            START INIT
     */
    private void init() {
        SizeBinder.bindSize(mainContainer, multimediaContainer);
        new PreviewSizeController(multimediaContainer, multimediaView);
        setupTimerSize();
        setupMultimediaSize();
    }

    private void setupTimerSize() {
        timeView.setMinWidth(Region.USE_COMPUTED_SIZE);
        timeView.setMaxWidth(Region.USE_COMPUTED_SIZE);

        mainContainer.heightProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(()->
                onMultimediaVisibilityChange(previewController.isShowing())));
        mainContainer.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(()->
                onMultimediaVisibilityChange(previewController.isShowing())));
        bindTimerContainerSize(previewController.isShowing());
    }

    private void setupMultimediaSize() {
        multimediaContainer.minWidthProperty().bind(mainContainer.widthProperty());
        multimediaContainer.minHeightProperty().bind(mainContainer.heightProperty());

        mainContainer.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(()->{
            double height = mainContainer.heightProperty().doubleValue();
            double width = mainContainer.widthProperty().doubleValue();

            double heightPropose = height/5*4;
            double widthPropose = heightPropose/9*16;

            double viewSize = heightPropose;

            if(widthPropose > width) {
                viewSize = width/16*9;
            }

            if(previewController.isShowing()) {
                double ratio = 1-(viewSize/height)+0.1;
                double freeSpace = ratio*height;
                setupFontForTimerText(freeSpace);
            }
        }));
    }
    /*
            START LOGIC
     */
    void onMultimediaVisibilityChange(boolean isMultimediaShowing) {
        updateTimerText(isMultimediaShowing,
                ()->bindTimerContainerSize(isMultimediaShowing)
        );
    }

    private void updateTimerText(boolean isShowing, Runnable callbackOnEnd) {
        Platform.runLater(()->{
            if(isShowing) {
                setupFontForTimerText(mainContainer.heightProperty().get()/100*18);
                timeView.setPadding(new Insets(0,(int)(mainContainer.widthProperty().get()*0.01),0,0));
                timeContainer.setAlignment(Pos.BOTTOM_CENTER);
            } else {
                setupFontForTimerText(mainContainer.heightProperty().get());
                timeView.setPadding(new Insets(0, 0,0,0));
                timeContainer.setAlignment(Pos.CENTER);
            }
            callbackOnEnd.run();
        });
    }

    private void setupFontForTimerText(double height) {
        int integerHeight = (int) height;
        Font font = timeView.getFont();
        double newSize = FontUtils.findFontSizeForNumberHeight(font, integerHeight);
        Font newFont = new Font(font.getName(), newSize);

        if(FontUtils.textWidth(newFont, timeView.getText())>mainContainer.widthProperty().getValue()*0.9) {
            newSize = FontUtils.findFontSizeForWidth(newFont, timeView.getText(), (int) (mainContainer.widthProperty().getValue()*0.9));
            newFont = new Font(font.getName(), newSize);
        }
        timeView.setFont(newFont);
    }

    private void bindTimerContainerSize(boolean isShowing) {
        if(isShowing) {
            timeContainer.minWidthProperty().bind(mainContainer.widthProperty());
            timeContainer.minHeightProperty().bind(mainContainer.heightProperty().add(getBottomBlankSpace(timeView.getFont())));
            timeContainer.maxHeightProperty().bind(timeContainer.minHeightProperty());
        } else {
            timeContainer.minWidthProperty().bind(mainContainer.widthProperty());
            timeContainer.minHeightProperty().bind(mainContainer.heightProperty());
        }
    }

    private DoubleProperty getBottomBlankSpace(Font font) {
        return new SimpleDoubleProperty(Toolkit.getToolkit().getFontLoader().getFontMetrics(font).getMaxDescent());
    }

    public interface ElementsContainer extends MultimediaPreviewContainer, TimeInfoContainer {
        MultimediaPreviewController getMultimediaPreviewController();
    }
}
