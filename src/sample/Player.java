package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;

public class Player extends GameEntity {
    public long score = 0;

    public boolean isStanding = true;
    public Platform currentPlatform = null;
    public GameEntity controlLine;
    public boolean isAlive = true;
    public boolean needScrollDown = false;

    Player(double px, double py, double a, double b, GameEntity startsWith){
        sprite = new ImageView(Settings.pl);
        sprite.setFitWidth(a);
        sprite.setFitHeight(b);
        sprite.setX(px);
        sprite.setY(py);
        controlLine = startsWith;
        velocity = new Point2D(0, 10);
    }

    public void Update(ArrayList<Platform> platforms){
        if (sprite.getBoundsInParent().getMinY() > Settings.viewAreaHeight)
            isAlive = false;

        if (isPressed(KeyCode.A))
            moveX(-3);
        if (isPressed(KeyCode.D)){
            moveX(3);
        }
        if (isStanding) {
            velocity = velocity.add(0, -50);
            isStanding = false;
        }

        //Пассивное падение и прыжки
        if (velocity.getY() < 10)
            velocity = velocity.add(0, 1);
        moveY(velocity.getY(), platforms, controlLine);
    }

    public boolean isPressed(KeyCode key){
        return Settings.keys.getOrDefault(key, false);
    }

    public void moveX(double value){
        boolean movingRight = value > 0;
        for (int i = 0; i < Math.abs(value); i++){
            sprite.setTranslateX(sprite.getTranslateX() + (movingRight ? 1 : -1));
            if (sprite.getTranslateX() < -300 + 20){
                sprite.setTranslateX(2 * getTranslateX() + 300 - 20);
            }
            if (sprite.getTranslateX() > 300 - 20){
                sprite.setTranslateX(2 * getTranslateX() - 300 + 20);
            }
        }
    }

    public void moveY(double value, ArrayList<Platform> platforms, GameEntity controlLine){
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++){
            //Перемещение по ускорению на 1 пункт
            sprite.setTranslateY(sprite.getTranslateY() + (movingDown ? 0.5 : -0.5));
            //Проверка касание платформ + опора
            for (Platform platform : platforms) {
                if (sprite.getBoundsInParent().intersects(platform.sprite.getBoundsInParent())) {
                    //Ускорение положительное = падаем вниз
                    if (value > 0) {
                        //Исключаем возможность провалиться сквозь платформу при падении и опираемся на нее
                        if (sprite.getBoundsInParent().getMaxY() >= platform.sprite.getBoundsInParent().getMinY() && sprite.getBoundsInParent().getMaxY() <= platform.sprite.getBoundsInParent().getMinY() + 5) {
                            movePlatformsDown(platforms);
                            sprite.setTranslateY(sprite.getTranslateY() - 0.1);
                            isStanding = true;
                            break;
                        }
                        else
                            isStanding = false;
                    }
                }
            }
            //Опора на контрольную линию если не задеты платформы
            if (sprite.getBoundsInParent().intersects(controlLine.sprite.getBoundsInParent()) && currentPlatform == null){
                isStanding = true;
                break;
            }
            if (isStanding)
                break;
        }
    }

    public void movePlatformsDown(ArrayList<Platform> platforms){
        for (Platform platform: platforms) {
            if(sprite.getBoundsInParent().intersects(platform.sprite.getBoundsInParent())){
                if (currentPlatform != platform){
                    //Смещение всех элементов вниз при прыжке на новую платформу
                    currentPlatform = platform;
                    score++;

                    needScrollDown = true;

                    //Для платформ внизу экрана генерируем новые позиции сверху
                    for (Platform p : platforms){
                        if (p.sprite.getBoundsInParent().getMinY() > Settings.viewAreaHeight){
                            resetX(p);
                            generateNewY(p.sprite);
                        }
                    }
                }
                break;
            }
        }
    }


    public void resetX(Platform p){
        p.sprite.setTranslateX(0);
        p.velocity = new Point2D(-2 + Settings.RNG.nextInt(5), 0);
    }

    public void generateNewY(ImageView sprite){
        sprite.setTranslateY(sprite.getTranslateY() - (250 * 7));
    }
}
