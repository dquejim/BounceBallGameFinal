package com.bounce.cp.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bounce.cp.MainGame;
import com.bounce.cp.actors.Ball;
import com.bounce.cp.actors.Plane;
import com.bounce.cp.extra.Utils;

//Clase GameScreen que contiene el juego principal
public class GameScreen extends BaseScreen implements ContactListener {

    //Definimos las variables necesarias
    private Image background;
    private Animation<TextureRegion> animation;
    private Sound crashSound;
    private int score;

    private OrthographicCamera fontCamera;
    private BitmapFont scoreView;

    private float creationTime;
    private Stage stage;
    private World world;
    private Ball ball;
    private Music backgroundMusic;
    private Sound ballSound;
    private int STATE = 1;

    private Array<Plane> planeArray;
    private static OrthographicCamera ortCamera;

    //Constructor de clase
    public GameScreen(MainGame mainGame) {
        super(mainGame);
        //Creamos un nuevo escenario, indicando que ocupará toda la pantalla
        this.stage = new Stage(new FillViewport(Utils.WORLD_WIDTH,Utils.WORLD_WIDTH));
        //Creamos un nuevo objeto World que contendrá la parte física
        //Indicamos la gravedad del mismo
        this.world = new World(new Vector2(0,-10),true);
        //Añadimos el contacListener para detectar colisiones
        this.world.setContactListener(this);

        //Obtenemos la musica y lo sonidos del assetManager
        this.backgroundMusic = this.mainGame.assetManager.getMusic();
        this.ballSound = this.mainGame.assetManager.getBallSound();
        this.crashSound = this.mainGame.assetManager.getCrashSound();

        this.planeArray = new Array();

        this.ortCamera = (OrthographicCamera) this.stage.getCamera();

        prepareScore();
    }

    //Indiamos lo que se mostrará en pantalla
    @Override
    public void show() {
        //Ponemos en bucle la musica y la reproducimos
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.play();
        //Indicamos su volumen
        this.backgroundMusic.setVolume(0.2f);
        //Añadimos el fondo
        addBackground(this.stage);
        //Creamos un nuevo objeto Ball, que será nuestro actor principal
        ball = new Ball(world,mainGame.assetManager.getBall(),new Vector2(2.35f,2.2f),ortCamera,ballSound);
        //Lo añadimos al stage
        stage.addActor(ball);
    }

    //Método que añade el fondo
    public void addBackground(Stage stage){
        //Cargamos la imagen del assetManager
        background = new Image(mainGame.assetManager.getBackground());
        //Indicamos la posición
        background.setPosition(0,0);
        //Indicamos su tamaño
        background.setSize(Utils.WORLD_WIDTH,Utils.WORLD_HEIGTH);
        //Lo añadimos al stage
        stage.addActor(this.background);
    }

    //Método que crea el marcador de puntuacion
    private void prepareScore(){
        //Establecemos un valor inicial
        this.score = 0;
        //Obtenemos la fuente del assetManager (ScoreView es de tipo BitmapFont.class)
        this.scoreView = this.mainGame.assetManager.getFont();
        //Indicamos la escala de la fuente
        this.scoreView.getData().scale(1f);

        //Creamos una cámara, indicando que el marcador deberá ir actualizandose
        this.fontCamera = new OrthographicCamera();
        this.fontCamera.setToOrtho(false, Utils.SCREEN_WIDTH,Utils.SCREEN_HEIGHT);
        this.fontCamera.update();
    }

    //Método que añade los aviones progresivamente
    private void addPlanes(float delta){
        //Si el jugador no ha perdido
        if(STATE == 1) {
            //Obtenemos la textura de los dos tipos de aviones con el assetManager
            TextureRegion rightPlane = mainGame.assetManager.getRightPlane();
            TextureRegion leftPlane = mainGame.assetManager.getLeftPlane();

            //Si la pelota no ha chocado
            if (ball.state == 1) {

                creationTime += delta;
                //Indicamos el tiempo cada cuanto se generará un nuevo avión
                if (creationTime >= 2.5f) {

                    creationTime -= 2.5f;

                    //Generamos posiciones aleatorias para ambos aviones
                    float posRandomY1 = MathUtils.random(0.5f, 4f);
                    float posRandomY2 = MathUtils.random(0.5f, 4f);

                    //Creamos dos tipos de aviones, de tipo 1 y 2
                    Plane plane = new Plane(this.world, rightPlane, new Vector2(4f, posRandomY1), this.ortCamera, 1,mainGame.assetManager.getExplosion());
                    Plane plane2 = new Plane(this.world, leftPlane, new Vector2(-1f, posRandomY2), this.ortCamera, 2,mainGame.assetManager.getExplosion());

                    //Los añadimos al array de aviones
                    planeArray.add(plane);
                    planeArray.add(plane2);

                    //Los añadimos al stage
                    stage.addActor(plane);
                    stage.addActor(plane2);
                }
            }
        }
    }

    //Método para eliminar los aviones de la memoria
    private void removePlane(){
        //Para cada avion en nuestro array
        for (Plane plane : this.planeArray) {
            //Si el mundo no se ha bloqueado
            if(!world.isLocked()) {
                //Si nuestro avion se ha salido de la pantalla o ha cruzado el limite por debajo
                if(plane.isOutOfScreen() || plane.getY() == -1f) {
                    //Si no ha caido
                    if(plane.isOutOfScreen() && STATE == 1){
                        //Añadimos puntos al marcador
                        this.score += 1;
                    }
                    //Eliminamos los recursos
                    plane.detach();
                    plane.remove();
                    planeArray.removeValue(plane, false);
                }
            }
        }
    }

    //Método para indicar una nueva puntuacion máxima
    private void newHighScore(int lastScore){
        //Obtenemos la puntuación maxima actual
        int bestScore = Utils.getHighScore();

        //Si la puntuacion actual es mayor que la maxima
        if(bestScore < lastScore){
            //Añadimos una nueva puntuacion a nuestro fichero de preferencias
            Utils.setHighScore(lastScore);
        }
    }

    //Método que gestiona los recursos de la tarjeta  y se ejecuta infinitamente
    @Override
    public void render(float delta) {
        //Limpiamos memoria
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Añadimos los aviones
        addPlanes(delta);

        //Indicamos al escenario que "actue", mostrando y gestionando los recursos que contiene
        stage.getBatch().setProjectionMatrix(ortCamera.combined);
        stage.act();
        world.step(delta,6,2);
        stage.draw();

        //Eliminamos los aviones (Si fuera necesario)
        removePlane();

        //Mostramos el marcador de puntuacion
        this.stage.getBatch().setProjectionMatrix(this.fontCamera.combined);
        this.stage.getBatch().begin();
        this.scoreView.draw(this.stage.getBatch(), ""+this.score,1.6f * 90, 1.1f * 90);
        this.stage.getBatch().end();
    }

    //SI la pantalla pasa a segundo plano...
    @Override
    public void hide() {
        super.hide();

        //Eliminamos los recursos del actor principal
        ball.detach();
        ball.remove();

        //Eliminamos los recursos de todos los aviones de nuestro arrau
        for(Plane plane:planeArray){
            plane.detach();
            plane.remove();
        }

        //Paramos la musica de fondo
        this.backgroundMusic.stop();
    }

    //Si se cambia de pantalla...
    @Override
    public void dispose() {
        super.dispose();
    }

    //Método para detectar colisiones
    private boolean areColider(Contact contact, Object objA, Object objB){
        return (contact.getFixtureA().getUserData().equals(objA) && contact.getFixtureB().getUserData().equals(objB)) ||
                (contact.getFixtureA().getUserData().equals(objB) && contact.getFixtureB().getUserData().equals(objA));
    }


    //Como nuestra pantalla implementa la clase ContactListener,tenemos los métodos para gestionar las colisiones

    //Método que se ejecuta cuando la colisión empieza
    @Override
    public void beginContact(Contact contact) {
        //Si nuestro actor principal (ball) choca con un avion
        if(areColider(contact,Utils.BALL,Utils.RIGHTPLANE)) {
            //States a 0, indicando que hemos perdido
            STATE = 0;
            ball.state = 0;

            //Hacemos que todos los aviones de nuestro array caigan
            for(Plane plane:planeArray){
                plane.fall();
            }

            //Paramos la musica
            this.backgroundMusic.stop();
            //Emitimos un sonido de que nuestra pelota ha cochado
            this.crashSound.play(0.5f);

            //Comparamos la puntuacion con la máxima con nuestro método newHighScore
            newHighScore(this.score);

            //Tras 2 segundos, pasamos a la pantalla GameOver
            this.stage.addAction(Actions.sequence(
                    Actions.delay(2f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            mainGame.setScreen(new GameOverScreen(mainGame));
                        }
                    })
            ));

        }
    }

    //Método que se ejecuta cuando la colisión termina
    @Override
    public void endContact(Contact contact) {

    }

    //Método que se ejecuta antes de que la colision empiece
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    //Método que se ejecuta antes de que la colision termine
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
