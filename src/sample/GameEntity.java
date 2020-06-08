package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GameEntity extends Pane {

    public ImageView sprite;
    public Point2D velocity;

    GameEntity(){
    }

    GameEntity(double px, double py){
        sprite = new ImageView(Settings.cl);

        sprite.setX(px);
        sprite.setY(py);
    }

}
