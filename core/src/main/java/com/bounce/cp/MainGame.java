package com.bounce.cp;

import com.badlogic.gdx.Game;
import com.bounce.cp.extra.AssetMan;
import com.bounce.cp.screens.GameOverScreen;
import com.bounce.cp.screens.GameScreen;
import com.bounce.cp.screens.GetReadyScreen;

//Clase MainGame que gestionará las pantallas
public class MainGame extends Game{

    //AssetManager del que obtendremos los recursos
    public AssetMan assetManager;
    //Pantallas que componen el juego
    public GameScreen gameScreen;
    public GameOverScreen gameOverScreen;
    public GetReadyScreen getReadyScreen;

    @Override
    public void create(){
        this.assetManager = new AssetMan();
        this.getReadyScreen = new GetReadyScreen(this);
        this.gameScreen = new GameScreen(this);
        this.gameOverScreen = new GameOverScreen(this);

        //Indicamos la pantalla inicial de nuestro juego
        setScreen(this.getReadyScreen);
    }



}