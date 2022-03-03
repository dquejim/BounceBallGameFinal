package com.bounce.cp.extra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class AssetMan {

    //Definimos nuestra clase AssetManager y los atlas que usaremos
    private AssetManager assetManager;
    private TextureAtlas myAtlas;
    private TextureAtlas explosionAtlas;

    public AssetMan() {
        this.assetManager = new AssetManager();

        //Cargamos los recursos que usaremos
        assetManager.load(Utils.INGAME_ATLAS, TextureAtlas.class);
        assetManager.load(Utils.EXPLOSION_ATLAS, TextureAtlas.class);
        assetManager.load(Utils.RIGHT_ARROW,Texture.class);
        assetManager.load(Utils.LEFT_ARROW, Texture.class);

        assetManager.load(Utils.BALL_SOUND, Sound.class);
        assetManager.load(Utils.BACKGROUND_MUSIC, Music.class);
        assetManager.load(Utils.GAMEOVER_SOUND, Sound.class);
        assetManager.load(Utils.CRASH_SOUND, Sound.class);
        assetManager.load(Utils.PRESS_START_SOUND, Sound.class);

        //Terminamos la carga
        assetManager.finishLoading();

        //Añadimos los atlas al assetManager
        myAtlas = assetManager.get(Utils.INGAME_ATLAS);
        explosionAtlas = assetManager.get(Utils.EXPLOSION_ATLAS);

    }

    //Métodos para obtener todas las Texturas necesarias y sonidos de nuetsra carpeta Assets

    //Texturas...
    public TextureRegion getBackground() { return this.myAtlas.findRegion(Utils.BACKGROUND);}

    public TextureRegion getBall() {
        return this.myAtlas.findRegion(Utils.BALL);
    }

    public TextureRegion getRightPlane() {
        return this.myAtlas.findRegion(Utils.RIGHTPLANE);
    }

    public TextureRegion getLeftPlane() {
        return this.myAtlas.findRegion(Utils.LEFTPLANE);
    }

    public TextureRegion getGameOver() {
        return this.myAtlas.findRegion(Utils.GAMEOVER);
    }

    public TextureRegion getGetReady() {
        return this.myAtlas.findRegion(Utils.GET_READY);
    }

    public Texture getRightArrow() {
        return new Texture(Utils.RIGHT_ARROW);
    }

    public Texture getLeftArrow() {
        return new Texture(Utils.LEFT_ARROW);
    }


    //Sonidos...
    public Sound getBallSound(){
        return this.assetManager.get(Utils.BALL_SOUND);
    }

    public Sound getCrashSound(){
        return this.assetManager.get(Utils.CRASH_SOUND);
    }

    public Sound getGameOverSound(){
        return this.assetManager.get(Utils.GAMEOVER_SOUND);
    }

    public Sound getPressToPlay(){
        return this.assetManager.get(Utils.PRESS_START_SOUND);
    }

    public Music getMusic(){
        return this.assetManager.get(Utils.BACKGROUND_MUSIC);
    }

    //Fuente para la letra....
    public BitmapFont getFont(){
        return new BitmapFont(Gdx.files.internal(Utils.FONT_FNT),Gdx.files.internal(Utils.FONT_PNG), false);
    }

    //Animaciond de explosion
    public Animation<TextureRegion> getExplosion(){
        return new Animation<TextureRegion>(0.083f,
                explosionAtlas.findRegion("explosion-0"),
                explosionAtlas.findRegion("explosion-1"),
                explosionAtlas.findRegion("explosion-2"),
                explosionAtlas.findRegion("explosion-3"),
                explosionAtlas.findRegion("explosion-4"),
                explosionAtlas.findRegion("explosion-5"),
                explosionAtlas.findRegion("explosion-6"),
                explosionAtlas.findRegion("explosion-7"),
                explosionAtlas.findRegion("explosion-8"),
                explosionAtlas.findRegion("explosion-0"),
                explosionAtlas.findRegion("explosion-10"),
                explosionAtlas.findRegion("explosion-11")
        );
    }

}
