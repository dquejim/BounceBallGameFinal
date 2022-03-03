package com.bounce.cp.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bounce.cp.MainGame;
import com.bounce.cp.extra.Utils;

//Pantalla de Game Over
public class GameOverScreen extends BaseScreen{

    //Declaracion de variables como el sonido, el stage ...
    private Stage stage;
    private Sound gameOverSound;
    private OrthographicCamera ortCamera;
    private Image gameOver;

    //Constructor de la clase GameOver
    public GameOverScreen(MainGame mainGame) {
        super(mainGame);

        //Indicamos que el escenario ocupará toda nuestra pantalla
        this.stage = new Stage(new FillViewport(Utils.WORLD_WIDTH,Utils.WORLD_WIDTH));
        //Obtenemos la cámara, que usaremos luego
        this.ortCamera = (OrthographicCamera) this.stage.getCamera();
        //Obtenemos el sonido de la pantalla GameOver mediante el AssetManager
        this.gameOverSound = mainGame.assetManager.getGameOverSound();

        //Tras 5 segundos,volveremos a la pantalla de inicio (GetReady)
        this.stage.addAction(Actions.sequence(
                Actions.delay(5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        mainGame.setScreen( new GetReadyScreen(mainGame));
                    }
                })
        ));

    }

    //Método donde añadimos el letrero de Game Over
    public void addGameOver(Stage stage){
        //Cargamos la imagen con el assetManager
        gameOver = new Image(mainGame.assetManager.getGameOver());
        //Indicamos su posicion
        gameOver.setPosition(1.6f,1.1f);
        //Indicamos su tamaño
        gameOver.setSize(Utils.WORLD_WIDTH/3f,Utils.WORLD_HEIGTH/3f);
        //Añadimos la imagen al stage
        stage.addActor(this.gameOver);
    }

    //Método donde indicamos lo que se mostrará en pantalla
    @Override
    public void show() {
        //Reproducimos el sonido Game Over
        this.gameOverSound.play(0.4f);
        //Añadimos el fondo, como ya lo tenemos en la pantalla gameScreen, hacemos uso de el
        mainGame.gameScreen.addBackground(this.stage);
        addGameOver(this.stage);
    }

    //Método que gestiona los recursos de la gráfica
    @Override
    public void render(float delta) {
        //Limpiamos la memória, por si quedara algo anterior
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Indicamos al stage que dibuje por pantalla lo que contiene
        stage.act();
        stage.draw();
        //Actualizamos la camara
        this.ortCamera.update();
    }

    //Cuando la pantalla pasa a segundo plano...
    @Override
    public void hide() {
        super.hide();
        this.gameOverSound.stop();
    }

    //Cuando la pantalla se elimina...
    @Override
    public void dispose() {
        super.dispose();
    }
}
