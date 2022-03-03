package com.bounce.cp.extra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Utils {
    //Debemos crear variables que definan los tamaños de pantalla
    public static final int SCREEN_HEIGHT = 800;
    public static final int SCREEN_WIDTH = 480;

    //Debemos crear variables que definan los tamaños de nuestro mundo
    public static final float WORLD_HEIGTH = 8f ;
    public static final float WORLD_WIDTH = 4.8f;

    //Identificadores de assets
    //Atlas...
    public static final String INGAME_ATLAS = "Atlas/myAtlas.atlas";
    public static final String EXPLOSION_ATLAS = "Atlas/explosionAtlas.atlas";

    //Imagenes y texturas...
    public static final String BACKGROUND = "background";
    public static final String BALL = "ball";
    public static final String RIGHTPLANE = "plane";
    public static final String LEFTPLANE = "plane2";
    public static final String GAMEOVER = "gameOver";
    public static final String GET_READY = "start";
    public static final String RIGHT_ARROW = "rightArrow.png";
    public static final String LEFT_ARROW = "leftArrow.png";

    //Fuente...
    public static final String FONT_PNG = "fonts.png";
    public static final String FONT_FNT = "fonts.fnt";

    //Musica y sonidos...
    public static final String BACKGROUND_MUSIC = "Sounds&Music/backgroundMusic.mp3";
    public static final String BALL_SOUND = "Sounds&Music/ballSound.mp3";
    public static final String GAMEOVER_SOUND = "Sounds&Music/gameOverSound.mp3";
    public static final String CRASH_SOUND = "Sounds&Music/crashSound.mp3";
    public static final String PRESS_START_SOUND = "Sounds&Music/pressStart.mp3";

    //Con el uso de la clase Preferences guardaremos en un fichero interno la puntuacion maxima
    public static int getHighScore(){
        //Cargamos las preferencias
        Preferences pref = Gdx.app.getPreferences("preferences");

        int score;
        //Obtenemos el objeto Integer de las preferencias, este tiene como identificador score
        score = pref.getInteger("score");
        //Actualizamos el fichero por si hubiera algun problema
        pref.flush();
        return score;
    };

    public static void setHighScore(int score){
        //Cargamos las preferencias
        Preferences pref = Gdx.app.getPreferences("preferences");
        //Añadimos el numero entero con la puntuacion dentro del fichero
        pref.putInteger("score",score);
        //Actualizamos el fichero
        pref.flush();
    }

}
