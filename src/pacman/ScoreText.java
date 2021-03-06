package pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Text object for showing score for eating a ghost; disappears after 2 seconds.
 * <p>
 * ScoreText.fx created on 2009-2-6, 17:52:42 <br>
 * ScoreText.java created October 2011
 *
 * @author Henry Zhang
 * @author Patrick Webster
 */
public class ScoreText extends Parent {
    private static final Font SCORE_FONT = new Font(11);
    private static final Color SCORE_FILL = Color.YELLOW;
    private static final int DISPLAY_TIME = 2;
    private final Text text;

    private Timeline timeline;

    public ScoreText(String s, boolean isVisible) {
        text = new Text(s);
        text.setFont(SCORE_FONT);
        text.setFill(SCORE_FILL);
        createTimeline();
        getChildren().add(text);
        setVisible(isVisible);
    }

    private void createTimeline() {
        timeline = new Timeline();
        timeline.setCycleCount(1);
        KeyFrame kf = new KeyFrame(Duration.seconds(DISPLAY_TIME), event -> setVisible(false));
        timeline.getKeyFrames().add(kf);
    }

    public void showText() {
        setVisible(true);
        timeline.playFromStart();
    }

    public void setX(int x) {
        text.setX(x);
    }

    public void setY(int y) {
        text.setY(y);
    }
}
