package com.bounce.cp.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bounce.cp.MainGame;
import com.bounce.cp.actors.Plane;
import com.bounce.cp.extra.Utils;

//Clase GetReadyScreen donde definiremos la pantalla de inicion y que extiende de BaseScreen como las demás pantallas
public class GetReadyScreen extends BaseScreen{
    //Declaración de variables
    private Stage stage;
    private int highScore;
    private Sound sound;
    private BitmapFont font;
    private OrthographicCamera ortCamera;
    private Image getReady,leftArrow,rightArrow;
    private Boolean touch;
    private World world;
    private Array<Plane> planeArray;
    private OrthographicCamera fontCamera;
    private float creationTime;

    //Constructor de la clase
    public GetReadyScreen(MainGame mainGame) {
        super(mainGame);

        //Indicamos que el jugador todavia no ha tocado la pantalla
        this.touch = false;
        //Creamos un nuevo escenario, indicando que ocupará toda la pantalla
        this.stage = new Stage(new FillViewport(Utils.WORLD_WIDTH,Utils.WORLD_WIDTH));

        //Creamos un nuevo objeto World que contendrá la parte física
        //Indicamos la gravedad del mismo
        this.world = new World(new Vector2(0,-10),true);

        //Cargamos el sonido de la pantalla con nuestro assetManager
        this.sound = mainGame.assetManager.getPressToPlay();

        //Creamos un array de aviones y obtenemos a cámara
        ortCamera = (OrthographicCamera) this.stage.getCamera();
        planeArray = new Array();

        //Obtenemos la puntuación más alta de nuestro fichero de preferencias
        highScore = Utils.getHighScore();

        //Preparamos nuestro texto para indicar la puntuación máxima actual (Reciclado de la pantalla GameScreen)
        prepareScore();

    }

    //Método que añade nuestra imagen de Start Game
    private void addGetReady(){
        //Cargamos la imagen del assetManager
        getReady = new Image(mainGame.assetManager.getGetReady());
        //Indicamos su posicion
        getReady.setPosition(1.6f,2.5f);
        //Indicamos su tamaño
        getReady.setSize(Utils.WORLD_WIDTH/3f,Utils.WORLD_HEIGTH/3f);
        //Añadimos la imagen al stage
        stage.addActor(this.getReady);
    }

    //Método que añade la flecha izquierda y derecha, que orienta al jugador de como jugar
    private void addArrows(Vector2 position, Image arrowOption, Texture texture){
        //Cargamos la textura de la imagen
        arrowOption = new Image(texture);
        //Indicamos su posicion
        arrowOption.setPosition(position.x,position.y);
        //Indicamos su tamaño
        arrowOption.setSize(0.5f,0.5f);
        //Añadimos la imagen al stage
        stage.addActor(arrowOption);
    }


    //Método donde iniciaremos nuestro marcador de puntuacion maxima
    private void prepareScore(){
        //Obtenemos la fuente del assetManager
        this.font = this.mainGame.assetManager.getFont();
        //Indicamos su escala
        this.font.getData().scale(0.2f);

        //Indicamos la cámara que usará nuestro marcador
        this.fontCamera = new OrthographicCamera();
        this.fontCamera.setToOrtho(false, Utils.SCREEN_WIDTH,Utils.SCREEN_HEIGHT);
        this.fontCamera.update();
    }

    //Método que añade los aviones al array y los va generando aleatoriamente(Explicado en la pantalla GameScreen)
    private void addPlanes(float delta){

        TextureRegion rightPlane = mainGame.assetManager.getRightPlane();
        TextureRegion leftPlane = mainGame.assetManager.getLeftPlane();
        creationTime += delta;

        if (creationTime >= 2.5f) {
            creationTime -= 2.5f;

            float posRandomY1 = MathUtils.random(0.5f, 4f);
            float posRandomY2 = MathUtils.random(0.5f, 4f);

            Plane plane = new Plane(this.world, rightPlane, new Vector2(4f, posRandomY1), this.ortCamera, 1,mainGame.assetManager.getExplosion());
            Plane plane2 = new Plane(this.world, leftPlane, new Vector2(-1f, posRandomY2), this.ortCamera, 2,mainGame.assetManager.getExplosion());

            planeArray.add(plane);
            planeArray.add(plane2);

            stage.addActor(plane);
            stage.addActor(plane2);
        }
    }

    //Método que elimina los recursos de los aviones de nuestro array (Explicado en el método de GameScreen)
    public void removePlane(){
        for (Plane plane : this.planeArray) {
            if(!world.isLocked()) {
                if(plane.isOutOfScreen() || plane.getY() == -1f) {
                    plane.detach();
                    plane.remove();
                    planeArray.removeValue(plane, false);
                }
            }
        }
    }

    //Indicamos lo que se mostrará por pantalla
    @Override
    public void show() {
        //Reproducimos el sonido de la pantalla
        this.sound.play(0.5f);
        //Añadimos el fondo
        mainGame.gameScreen.addBackground(this.stage);
        //Añadimos el titulo de Start Game
        addGetReady();
        //Añadimos las flechas de los lados pasandoles la posicion y la textura cargada del assetManager
        addArrows(new Vector2(1.2f,2f),this.rightArrow,mainGame.assetManager.getLeftArrow());
        addArrows(new Vector2(3.1f,2f),this.leftArrow,mainGame.assetManager.getRightArrow());
    }

    //Método que gestiona los recursos y se ejecuta infinitamente
    @Override
    public void render(float delta) {
        //Limpiamos la memória de la gráfica (por si acaso)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Añadimos los aviones
        addPlanes(delta);

        //Indicamos al escenario que "actue", mostrando y gestionando los recursos que contiene
        stage.getBatch().setProjectionMatrix(ortCamera.combined);
        stage.act();
        world.step(delta,6,2);
        stage.draw();

        //Mostramos el marcador de puntuacion
        this.stage.getBatch().setProjectionMatrix(this.fontCamera.combined);
        this.stage.getBatch().begin();
        //Dibujamos el marcador en pantalla
        //Cuando le damos la posicion, se multiplica por 90 ya que para nuestra pantalla 1 unidad de medida (1f) equivaldria a 90 pixeles
        this.font.draw(this.stage.getBatch(), "Record: "+this.highScore,2f * 90 ,1f * 90);
        this.stage.getBatch().end();

        //Eliminamos los aviones (Si fuera necesario)
        removePlane();

        //Guardamos en nuestra variable de tipo Boolean si el jugador ha tocado la pantalla
        touch = Gdx.input.justTouched();

        //Cuando el jugador toque la pantalla, se iniciará la pantalla GameScreem y el juego emprezará
        if(touch == true){
            this.stage.addAction(Actions.sequence(
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            mainGame.setScreen(new GameScreen(mainGame));
                        }
                    })
            ));
        }
    }

    //Cuando la pantalla pasa a segundo plano
    @Override
    public void hide() {
        super.hide();

        //Eliminamos todos los aviones
        for(Plane plane:planeArray){
            plane.detach();
            plane.remove();
        }
    }

    //Cuando la pantalla se elimina o se cambia
    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
    }
}

