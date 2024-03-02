package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.assets.AssetManager;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.mygdx.game.component.PlayerComponent;
import com.mygdx.game.component.PlayerSpriteAnimationComponent;
import com.mygdx.game.script.PlayerScript;
import com.mygdx.game.system.CameraSystem;
import com.mygdx.game.system.PlayerAnimationSystem;
import games.rednblack.editor.renderer.SceneConfiguration;
import games.rednblack.editor.renderer.SceneLoader;

import games.rednblack.editor.renderer.resources.AsyncResourceManager;
import games.rednblack.editor.renderer.resources.ResourceManagerLoader;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.ItemWrapper;

public class MyGdxGame extends ApplicationAdapter {
	private AssetManager mAssetManager;

	private SceneLoader mSceneLoader;
	private AsyncResourceManager mAsyncResourceManager;

	private Viewport mViewport;
	private OrthographicCamera mCamera;

	private com.artemis.World mEngine;

	@Override
	public void create() {
		mAssetManager = new AssetManager();
		mAssetManager.setLoader(
				AsyncResourceManager.class,
				new ResourceManagerLoader(mAssetManager.getFileHandleResolver())
		);

		mAssetManager.load("project.dt", AsyncResourceManager.class);

		mAssetManager.finishLoading();

		mAsyncResourceManager = mAssetManager.get("project.dt", AsyncResourceManager.class);
		SceneConfiguration config = new SceneConfiguration();
		config.setResourceRetriever(mAsyncResourceManager);

		CameraSystem cameraSystem = new CameraSystem(0, 5000, 0, 5000);

		//добавление кастомных систем в конфиг сцены
		config.addSystem(cameraSystem);
		config.addSystem(new PlayerAnimationSystem()); // система для анимаций главного персонажа

		mSceneLoader = new SceneLoader(config);
		mEngine = mSceneLoader.getEngine();

		//создание маппера игрока компонента
		ComponentRetriever.addMapper(PlayerComponent.class);
		ComponentRetriever.addMapper(PlayerSpriteAnimationComponent.class);

		mCamera = new OrthographicCamera();
		mViewport = new ExtendViewport(450, 450, mCamera);

		mSceneLoader.loadScene("MainScene", mViewport);

		ItemWrapper root = new ItemWrapper(mSceneLoader.getRoot(), mEngine);
		ItemWrapper player = root.getChild("player");

		//добавляем компонент анимации главного персонажа
		ComponentRetriever.create(player.getChildEntity("player-anim"), PlayerComponent.class, mEngine);

		player.addScript(new PlayerScript());
		cameraSystem.setFocus(player.getEntity());
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mViewport.apply();
		mEngine.process();
	}

	@Override
	public void resize(int width, int height) {
		mViewport.update(width, height);

		if(width != 0 && height != 0){
			mSceneLoader.resize(width, height);
		}
	}

	@Override
	public void dispose() {
		mAssetManager.dispose();
		mSceneLoader.dispose();
	}
}
