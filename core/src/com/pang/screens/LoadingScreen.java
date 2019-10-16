package com.pang.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.pang.MyGdxGame;

public class LoadingScreen implements Screen{

	final MyGdxGame game;
	OrthographicCamera camera;
	private Texture textureLogo;
	private Image imageLogo;
	private Stage stage;
	
	//Progress Bar
	private ProgressBar progressBar;
	private ProgressBarStyle progressBarStyle;
	private NinePatchDrawable ninePatchKnob, ninePatchKnobBefore, ninePatchBar;
	private float delay = 0;
	
	public LoadingScreen(final MyGdxGame game){
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		textureLogo = new Texture(Gdx.files.internal("Images/Logo.png"), true);
		textureLogo.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearNearest);
		imageLogo = new Image(textureLogo);
		imageLogo.setWidth(0.4f * Gdx.graphics.getWidth());
		imageLogo.setHeight(textureLogo.getHeight() * imageLogo.getWidth()/textureLogo.getWidth());
		imageLogo.setX(Gdx.graphics.getWidth()/2 - imageLogo.getWidth()/2);
		imageLogo.setY(Gdx.graphics.getHeight()/2 - imageLogo.getHeight()/2);
		stage = new Stage();
		

		//Progress Bar
		ninePatchBar = new NinePatchDrawable(
												new NinePatch(
																new Texture(Gdx.files.internal("Images/Bar.png"), true), 7, 7, 7, 7));
		ninePatchKnob = new NinePatchDrawable(
												new NinePatch(
																new Texture(Gdx.files.internal("Images/Knob.png"), true), 1, 7, 9, 9));
		ninePatchKnobBefore = new NinePatchDrawable(
													new NinePatch(
																new Texture(Gdx.files.internal("Images/KnobBefore.png"), true), 1, 1, 9, 9));	
		
		progressBarStyle = new ProgressBarStyle(ninePatchBar, ninePatchKnob);
		progressBarStyle.knobBefore = ninePatchKnobBefore;
		//for the knobBefore
		progressBarStyle.knobBefore.setLeftWidth(0);
		progressBarStyle.knobBefore.setRightWidth(0);
		//for the background
		progressBarStyle.background.setLeftWidth(2);
		progressBarStyle.background.setRightWidth(0);
		
		progressBar = new ProgressBar(0, 100, .1f, false, progressBarStyle);
		progressBar.setWidth(Gdx.graphics.getWidth()/3);
		progressBar.setHeight(3*Gdx.graphics.getHeight()/100);
		progressBar.setX(Gdx.graphics.getWidth()/2 - progressBar.getWidth()/2);
		progressBar.setY(Gdx.graphics.getHeight()/5);
		
		//Loading of the sounds
		//game.assets.load("Sounds/Alarm.ogg", Sound.class);
		//game.assets.load("Sounds/Background.ogg", Music.class);
		
		//Textures
		//game.assets.load("Images/Stars.jpg", Texture.class);
		game.assets.load("Images/Corde.png", Texture.class);
		game.assets.load("Images/Corde_Grappin.png", Texture.class);
		
		//Loading of the TextureAtlas
		game.assets.load("Images/Images.pack", TextureAtlas.class);
		
		//Loading of the Freetype Fonts
		FileHandleResolver resolver = new InternalFileHandleResolver();
		game.assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		game.assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		FreeTypeFontLoaderParameter size1Params = new FreeTypeFontLoaderParameter();
		size1Params.fontFileName = "Fonts/calibri.ttf";			
		size1Params.fontParameters.genMipMaps = true;					
		size1Params.fontParameters.minFilter = TextureFilter.Linear;
		size1Params.fontParameters.magFilter = TextureFilter.Linear;
		size1Params.fontParameters.size = (int)(0.04f*Gdx.graphics.getWidth());
		game.assets.load("fontMenu.ttf", BitmapFont.class, size1Params);

		stage.addActor(imageLogo);		
		imageLogo.addAction(Actions.sequence(Actions.alpha(0)
                ,Actions.fadeIn(0.1f),Actions.delay(1.5f)));
		
		stage.addActor(progressBar);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    camera.update();
		game.batch.setProjectionMatrix(camera.combined);
	
	    stage.act();
	    stage.draw();
	    
	    progressBar.setValue(100*game.assets.getProgress());
	    
		if(game.assets.update()){
			delay += Gdx.graphics.getDeltaTime();
			
			if(delay > 0.5f){
				dispose();
				((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));	
			}    		
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		textureLogo.dispose();
	}

}
