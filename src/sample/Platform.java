package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class Platform extends GameEntity {

    Platform(double px, double py) {
        sprite = new ImageView(Settings.pt);
        sprite.setX(px);
        sprite.setY(py);

        velocity = new Point2D(-2 + Settings.RNG.nextInt(5), 0);
    }

    public void chaoticMovement(){
        if (sprite.getBoundsInParent().getMinX() <= 2 || sprite.getBoundsInParent().getMinX() + Settings.platformWidth >= Settings.viewAreaWidth - 2)
            velocity = velocity.add(-2 * velocity.getX(), 0);
        sprite.setTranslateX(sprite.getTranslateX() + velocity.getX());
    }

}
