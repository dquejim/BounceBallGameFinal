package com.bounce.cp.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bounce.cp.extra.Utils;

//Clase Ball que extiende de actor
public class Ball extends Actor {

    //Definicion de variables
    private TextureRegion texture;
    private OrthographicCamera ortCamera;
    private Stage stage;
    private Sound sound;
    private World world;
    private Vector2 position;
    private Body body;
    private Fixture fixture;
    private float stateTime;

    //Indicamos que su estado actual es 1, es decir, el jugador no ha perdido
    public int state = 1;


    //Contructor
    public Ball(World world, TextureRegion texture, Vector2 position, OrthographicCamera ortCamera,Sound sound){
        //Le indicamos el world al que pertenece,así como su textura, la posicion ...
        this.world = world;
        this.texture = texture;
        this.position = position;
        this.ortCamera = ortCamera;
        stateTime = 0f;
        this.sound = sound;

        //Creamos su cuerpo
        createBody();

        //Creamos su fixture (forma del cuerpo)
        createFixture();
    }


    //Método para crear el cuerpo
    private void createBody(){
        //Definimos un nuevo cuerpo
        BodyDef bodyDef = new BodyDef();
        //Le asignamos su posicion
        bodyDef.position.set(this.position);
        //Indicamos que el cuerpo será dinámico
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //Creamos el cuerpo dentro del world al que pertenece
        this.body = this.world.createBody(bodyDef);
    }

    //Método para crear la fixture
    private void createFixture(){
        //Creamos un nuevo objeto, con el que definiremos su forma más tarde
        CircleShape circle = new CircleShape();
        //Como se trata de un circulo, indicamos su radio
        circle.setRadius(0.15f);
        //Añadimos la fixture al cuerpo
        this.fixture = this.body.createFixture(circle,8);
        //Le añadimos un identificador, que usaremos con las colisiones
        this.fixture.setUserData(Utils.BALL);

        circle.dispose();
    }

    //Indicamos a nuestro actor lo que debe hacer mientras se ejecute el juego
    @Override
    public void act(float delta) {

        //Con el método  Gdx.input.getX() y  Gdx.input.getY() obtenemos las coordenadas donde a pulsado el jugador
        float xPixelPulsado = Gdx.input.getX();
        float yPixelPulsado = Gdx.input.getY();
        //Creamos una variable para detectar si el jugador ha hecho click o no
        boolean touch = Gdx.input.isTouched();

        Vector3 vector3 = new Vector3(xPixelPulsado,yPixelPulsado,0);
        vector3 = ortCamera.unproject(vector3);

        //Si el jugador no ha perdido y toca la pantalla...
        if(this.state == 1){
            if(touch){
                //Si toca en la parte derecha de la pantalla, impulsará la pelota a la derecha
                if(vector3.x > Utils.WORLD_WIDTH/2) {
                    this.body.applyForceToCenter(6f,0f,true);
                }else{
                    //Si toca en la parte izquierda de la pantalla, impulsará la pelota a la izquierda
                    if(vector3.x < Utils.WORLD_WIDTH/2) {
                        this.body.applyForceToCenter(-6f,0f,true);
                    }
                }
            }

            //Para controlar que no se salga del borde, obtenemos la posicion del cuerpo
            //Si se sale por el borde derecho, la pelota aparecerá en la parte izquierda
            if(this.body.getPosition().x >= 4f){
                this.body.setTransform(1.15f,this.body.getPosition().y,0f);
            }

            //Si se sale por el borde izquierdo, la pelota aparecerá en la parte derecha
            if(this.body.getPosition().x <= 1f){
                this.body.setTransform(3.85f,this.body.getPosition().y,0f);
            }

            /*Fuerza que se aplica cuando el actor cae al suelo.
            He optado por usar un rebote de este tipo dado que usando el método setRestitution() y un suelo fisico
            el actor dejaba de rebotar en un momento dado debido a la gravedad*/

            if(this.body.getPosition().y <= 0f){
                //Cuando la pelota toque el suelo donde rebota, emitirá un sonido y será lanzada con un impulso hacia arriba
                this.body.setLinearVelocity(0f,9.5f);
                this.sound.play(0.3f);
            }
        }

    }

    //Indicamos como se debe dibujar el actor
    @Override
    public void draw(Batch batch, float parentAlpha) {
        //Aquí alineamos el cuerpo con su textura
        //Ambos se moverán a la vez ya que hace uso de delta para sincronizar ambos movimientos
        setPosition(body.getPosition().x, body.getPosition().y);
        batch.draw(this.texture,getX()-0.15f,getY()-0.15f,0.3f,0.3f);
        stateTime += Gdx.graphics.getDeltaTime();
    }

    //Destruimos el cuerpo y la fixture, tambien paramos el sonido de la pelota en caso necesario
    public void detach(){
        this.body.destroyFixture(this.fixture);
        this.sound.stop();
        this.world.destroyBody(this.body);
    }
}
