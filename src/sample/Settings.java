package sample;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Random;

public abstract class Settings {

    public static final Random RNG = new Random();

    public static final double viewAreaHeight = 800;
    public static final double viewAreaWidth = 600;
    public static final double platformWidth = 120;
    public static final double platformHeight = 20;
    public static final double playerWidth = 80;
    public static final double playerHeight = 120;
    public static final double playerStartX = viewAreaWidth/2 - playerWidth/2;
    public static final double playerStartY = viewAreaHeight - playerHeight;
    public static final double lowestPlatformPos = 702;

    public static final Image bg = new Image("file:res/img/Dust.png");
    public static final Image cl = new Image("file:res/img/controlLine.png");
    public static final Image pl = new Image("file:res/img/Alastor2.png");
    public static final Image pt = new Image("file:res/img/Platform.png");

    public static final HashMap<KeyCode, Boolean> keys = new HashMap<>();
}
