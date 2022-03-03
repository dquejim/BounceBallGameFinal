package com.bounce.cp.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.bounce.cp.extra.Utils;

//Clase Plane que extiende de actor
public class Plane extends Actor {

    //Declaracion de variables
    private TextureRegion texture;
    private Animation<TextureRegion> animation;
    private OrthographicCamera ortCamera;
    private World world;
    private Vector2 position;
    private Body body;
    private int planeOption;
    private Fixture fixture;
    private float stateTime;
    private float state = 1;

    //Para el constructor...
    public Plane(World world, TextureRegion texture, Vector2 position, OrthographicCamera ortCamera, int planeOption, Animation<TextureRegion> animation){

        //Indicamos el mundo al que pertenece, su textura, animación, la opcion del avion...
        this.world = world;
        this.texture = texture;
        this.animation = animation;
        this.position = position;
        this.planeOption = planeOption;
        this.ortCamera = ortCamera;

        stateTime = 0f;

        //Creamos el cuerpo del actor
        createBody();

        //Creamos el fixture y le damos la forma,dimensiones e identificador al cuerpo
        createFixture();

        //Si el contructor tiene la opcion de avion 1 (planeOption, avion apuntando a la izquierda)
        //Crearemos un impulso hacia la izquierda para que avance
        if(this.planeOption == 1){
            this.body.setLinearVelocity(-2f,0f);
        }

        //Si el contructor tiene la opcion de avion 2 (planeOption, avion apuntando a la derecha)
        //Crearemos un impulso hacia la derecha para que avance
        if(this.planeOption == 2){
            this.body.setLinearVelocity(2f,0f);
        }
    }

    //Creamos el body del actor
    public void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        //Aquí indicamos que es un cuerpo kinematico (se mueve pero no le afecta la gravedad)
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        //Creamos el cuerpo dentro del world
        this.body = this.world.createBody(bodyDef);
    }

    //Creamos el fixture
    public void createFixture(){
        //En este caso es un polígono, por lo que creamos un nuevo objeto PolygonShape
        PolygonShape shape = new PolygonShape();
        //Indicamos que nuestro polígono es una caja, y le damos las medidas
        shape.setAsBox(0.1f,0.05f);
        //Creamos la fixture, asociandola al cuerpo
        this.fixture = this.body.createFixture(shape,8);
        //Le añadimos un identificador, que usaremos con las colisiones
        //Dado que tanto al avion 1 como el 2 realizan la misma accion al colisionar, usaremos el mismo identificador para los dos
        this.fixture.setUserData(Utils.RIGHTPLANE);

        shape.dispose();
    }

    //Como el avion solo tendrá un impulso al principio, solo le añadimos dicho impulso en el constructor
    @Override
    public void act(float delta) {

    }

    //Comparamos si el avión está fuera o dentro de la pantalla
    public boolean isOutOfScreen(){
        return (this.body.getPosition().x <= -1f || this.body.getPosition().x >= 4f);
    }

    //Método que usaremos en las colisiones, cuando la pelota choque contra un avion
    public void fall(){
        //Si choca con el avion 1, añadiremos un impulso a la izquierda para que parezca que cae
        if(this.planeOption == 1){
            this.body.setLinearVelocity(-1.5f,-2f);
        }
        //Si choca con el avion 2, añadiremos un impulso a la izquierda para que parezca que cae
        if(this.planeOption == 2){
            this.body.setLinearVelocity(1.5f,-2f);
        }

        //Indicamos que el jugador ha perdido poniendo el state a 0
        state = 0;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        //Indicamos la posicion de nuestro actor
        setPosition(body.getPosition().x, body.getPosition().y);
        //Si el jugador no ha perdido, el avión tendrá su textura normal
        if(state == 1){
            batch.draw(this.texture, getX() - 0.15f, getY() - 0.15f, 0.3f, 0.3f);
        }
        //Si el jugador pierde, el avión pasará a tener una animacion de explosión mientras cae
        else{
            batch.draw(this.animation.getKeyFrame(stateTime,true),getX() - 0.15f, getY() - 0.15f, 0.3f, 0.3f);
        }
        stateTime += Gdx.graphics.getDeltaTime();
    }

    //Eliminamos los recursos necesarios
    public void detach(){
        this.body.destroyFixture(this.fixture);
        this.world.destroyBody(this.body);
    }
}
