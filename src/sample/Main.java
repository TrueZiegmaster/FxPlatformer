package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;


public class Main extends Application {

    private Player player;
    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private Button playButton = new Button("Play");

    private Pane initGame(){
        //Корневые элементы
        Pane appRoot = new Pane();
        Pane gameRoot = new Pane();
        Pane uiRoot = new Pane();

        //Интерфейс
        HBox header = new HBox();
        Label scoreLabel = new Label("Score:");
        header.setMinSize(Settings.viewAreaWidth, 50);
        header.setAlignment(Pos.CENTER);
        header.getChildren().add(scoreLabel);
        uiRoot.getChildren().add(header);

        //Устанавливаем бэкграунд
        ImageView bg = new ImageView(Settings.bg);
        bg.setFitWidth(Settings.viewAreaWidth);
        bg.setFitHeight(Settings.viewAreaHeight - 50);
        bg.setY(50);
        gameRoot.getChildren().add(bg);


        //Контрольная линия
        GameEntity controlLine = new GameEntity(0, Settings.viewAreaHeight);
        gameRoot.getChildren().add(controlLine.sprite);


        //Добавляем игрока
        player = new Player(Settings.playerStartX, Settings.playerStartY, Settings.playerWidth, Settings.playerHeight, controlLine);
        gameRoot.getChildren().add(player.sprite);


        //Добавляем платформы
        for (int i = 0; i < 7; i++){
            platforms.add(new Platform((Settings.viewAreaWidth - Settings.platformWidth) * Settings.RNG.nextDouble(), (Settings.viewAreaHeight - 350) - i * 252));
        }
        for (Platform platform : platforms){
            gameRoot.getChildren().add(platform.sprite);
        }


        //Упаковывем все в один контейнер
        appRoot.getChildren().addAll(uiRoot, gameRoot);

        return appRoot;
    }

    private Pane initMenu(){
        Pane mainMenu = new Pane();
        Label title = new Label("Doodle Hazbin");
        VBox header = new VBox();
        header.setMinSize(Settings.viewAreaWidth, Settings.viewAreaHeight);
        header.setAlignment(Pos.CENTER);
        header.getChildren().add(title);
        if (player != null){
            Label scored = new Label("Your score is: " + player.score);
            header.getChildren().add(scored);
        }
        header.getChildren().add(playButton);
        mainMenu.getChildren().add(header);

        return mainMenu;
    }


    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("Doodle Hazbin");
        primaryStage.setHeight(Settings.viewAreaHeight);
        primaryStage.setWidth(Settings.viewAreaWidth);
        primaryStage.setResizable(false);

        Scene menuScene = new Scene(new Pane(initMenu()));
        primaryStage.setScene(menuScene);
        primaryStage.show();

        //Вешаем на кнопку запуск игровой сессии и смену сцены
        playButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Инициализация процесса игры

                Pane gamePane = initGame();
                Scene gameScene = new Scene(gamePane);

                primaryStage.setScene(gameScene);

                //Цикл игры
                AnimationTimer timer = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        if (!player.isAlive) {
                            Settings.keys.put(KeyCode.A, false);
                            Settings.keys.put(KeyCode.D, false);
                            primaryStage.setScene(new Scene(initMenu()));
                            platforms.clear();
                            this.stop();
                        }
                        else{
                            for (Platform p: platforms) {
                                p.chaoticMovement();
                            }
                        }

                        Pane gui = (Pane) gamePane.getChildren().get(0);
                        HBox box = (HBox) gui.getChildren().get(0);
                        Label lb = (Label) box.getChildren().get(0);
                        lb.setText("Score: " + player.score);

                        player.Update(platforms);

                        if (player.needScrollDown){
                            double maxY = player.currentPlatform.sprite.getBoundsInParent().getMinY();
                            double step = 6;
                            if (Settings.lowestPlatformPos - maxY < step){
                                step = Settings.lowestPlatformPos - maxY;
                                player.needScrollDown = false;
                            }
                            for (Platform p : platforms){
                                p.sprite.setTranslateY(p.sprite.getTranslateY() + step);
                            }
                            player.sprite.setTranslateY(player.sprite.getTranslateY() + step);
                        }
                    }
                };
                timer.start();

                gameScene.setOnKeyPressed(event -> Settings.keys.put(event.getCode(), true));
                gameScene.setOnKeyReleased(event -> Settings.keys.put(event.getCode(), false));
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
