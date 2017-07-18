package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.gson.annotations.SerializedName;


import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import java.awt.Font;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


class Tileset
{
	Tileset()
	{
		properties = new ArrayList<List<Map<String, String>>>();
		file = new String();
	}
	@SerializedName("file") String file;
	@SerializedName("properties") List<List<Map<String, String>>> properties;
	String column;
	String tile_height;
	String tile_width;
	String margin;
	String spacing;
	String row;
}
class WorldInfo
{
	WorldInfo()
	{
		tileIds = new ArrayList<String>();
		tilesets = new ArrayList<Tileset>();
	}
	String column;
	String row;
	@SerializedName("tiles") List<String> tileIds;
	@SerializedName("tileset") List<Tileset> tilesets;
}


class TileType
{
	TileType()
	{
		properties = new ArrayList<Map<String, String>>();
	}

	List<Map<String, String>> properties;
}
class Tile
{
	Tile(TileType initSharedProperties)
	{
		sharedProperties = initSharedProperties;
	}
	Sprite getSprite() { return sprite; }
	void setSprite(Sprite newSprite) { sprite = newSprite; }
	Sprite sprite;
	TileType sharedProperties;
}




class AnimationInfo
{
	AnimationInfo()
	{
		bounds = new ArrayList<Rectangle>();
	}
	List<Rectangle> bounds;
	int default_face;
	int default_animation;
	int animation_per_face;
	int num_face;
}
class AnimationFactory
{

	static AnimationModel createAnimationModelFromParsedJSON(AnimationInfo info)
	{
		AnimationModel model = new AnimationModel(info.animation_per_face, info.num_face);

		for ( int a = 0; a < info.bounds.size(); a++ )
		{
			model.addBound(info.bounds.get(a));
		}

		return model;
	}


}
class World implements Drawable
{
	World(List<Tile> initTiles)
	{
		setTiles(initTiles);
	}
	void addTile(Tile tile)
	{
		tiles.add(tile);
	}
	void setTiles(List<Tile> newTiles) { tiles = newTiles; }
	List<Tile> tiles;
	@Override
	public void draw(SpriteBatch batch)
	{
		for(int a = 0; a < tiles.size(); a++)
		{
			tiles.get(a).getSprite().draw(batch);
		}
	}


	List<Entity> entities;
}
class BitmapFontFactory
{
	static BitmapFont createdBitmapFrontFromFile(FileHandle file, int fontSize)
	{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = fontSize;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}
}
class WorldFactory
{
	static int get(String str)
	{
		return Integer.parseInt(str);
	}

	static World createWorldFromJSON(String filepath)
	{
		WorldInfo worldInfo = Utils.getFromJson(filepath, WorldInfo.class);
		if ( worldInfo.tilesets.size() > 0 )
		{
			Tileset ref = worldInfo.tilesets.get(0);
			Texture tileset = new Texture("graphics/textures/" + ref.file);

			int spacing = get(ref.spacing);
			int margin = get(ref.margin);

			int width = get(ref.column);
			int mapheight = get(worldInfo.row);
			int mapwidth = get(worldInfo.column);
			int tilewidth = get(ref.tile_width);
			int tileheight = get(ref.tile_height);
			List<Tile> worldTiles = new ArrayList<Tile>();
			int x = 0, y = mapheight - 1;
			int offsetx = 0;
			int offsety = 0;
			for (int a = 0; a < worldInfo.tileIds.size(); a++) {
				int id = Integer.parseInt(worldInfo.tileIds.get(a));
				int row = 0;
				int column = id - 1;
				if (id > width) {
					row = id / width;
					column = (id % width) - 1;
				}
				offsetx = margin + ( column * spacing );
				offsety = margin + ( row * spacing );
				TextureRegion textureTile = new TextureRegion(tileset, (column * tilewidth) + offsetx, (row * tileheight) + offsety, tilewidth, tileheight);
				Sprite spriteTile = new Sprite(textureTile);
				spriteTile.setSize(1, 1);
				spriteTile.setPosition(x++, y);
				Tile tile = new Tile(null);
				tile.setSprite(spriteTile);
				worldTiles.add(tile);
				if (x >= mapwidth) {
					x = 0;
					y--;
				}
			}
			return new World(worldTiles);
		}
		return null;
	}
	static World createWorldFromImageFile(String filepath, float width, float height)
	{
		Texture txture = new Texture(filepath);
		Tile tile = new Tile(null);
		Sprite sprite = new Sprite(txture);
		sprite.setSize(width - 1, height);
		tile.setSprite(sprite);
		List<Tile> tiles = new ArrayList<Tile>();
		tiles.add(tile);
		return new World(tiles);
	}
}


public class Core extends ApplicationAdapter {
	SpriteBatch batch;
	Camera worldCam;
	Camera HUDCam;
	World currentWorld;

	List<EntityModel> sharedModels;
	List<AnimationModel> sharedAnimationModels;


	List<GameState> gameStates;
	PlayerInput worldInput;
	HUD hud;
	Skin skins;
	Tower currentHoldingTower;
	InputMultiplexer multiplexer;
	MainMenu menu;
	@Override
	public void create () {
		menu = new MainMenu();
		batch = new SpriteBatch();
		worldCam = new OrthographicCamera(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.ASPECT_RATIO);
		worldCam.position.set(worldCam.viewportWidth / 2f, worldCam.viewportHeight / 2f, 0);
		worldCam.update();
		HUDCam = new OrthographicCamera(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.ASPECT_RATIO);
		HUDCam.position.set(HUDCam.viewportWidth / 2f, HUDCam.viewportHeight / 2f, 0);
		HUDCam.update();
		sharedModels = new ArrayList<EntityModel>();
		sharedAnimationModels = new ArrayList<AnimationModel>();
		skins = new Skin();

		gameStates = new ArrayList<GameState>();
		BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/textures/fonts/arial.ttf"), 32);

		TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(new SpriteDrawable(new Sprite(
				new Texture("graphics/textures/defaultskin.png"))), null, null, font);
		ImageTextButton.ImageTextButtonStyle style2 = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
				new Texture("graphics/textures/testtower1.png"))), null, null, font);

		ImageTextButton.ImageTextButtonStyle style3 = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
				new Texture("graphics/textures/testtower2.png"))), null, null, font);

		ImageTextButton.ImageTextButtonStyle style4 = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
				new Texture("graphics/textures/testtower3.png"))), null, null, font);

		skins.add("default1", style);
		skins.add("default2", style2);
		skins.add("default3", style3);
		skins.add("default4", style4);

		currentWorld = WorldFactory.createWorldFromJSON("jsons/levels/first/map1.json");


		worldInput = new PlayerInput(worldCam);


		AnimationInfo virus1AnimationInfo = Utils.getFromJson("jsons/animations/virus1.json", AnimationInfo.class);
		AnimationModel virus1AnimationModel = AnimationFactory.createAnimationModelFromParsedJSON(virus1AnimationInfo);
		sharedAnimationModels.add(virus1AnimationModel);

		sharedModels.add(loadModel("graphics/textures/testtower1.png", "tower1"));
		sharedModels.add(loadModel("graphics/textures/bullet.png", "tower1Bullet"));
		sharedModels.add(loadModel("graphics/textures/testtower2.png", "tower2"));
		sharedModels.add(loadModel("graphics/textures/bullet.png", "tower2Bullet"));
		sharedModels.add(loadModel("graphics/textures/testtower3.png", "tower5"));
		sharedModels.add(loadModel("graphics/textures/bullet.png", "tower5Bullet"));


		//sharedModels.add(loadModel("graphics/textures/virus1.png", "virus1"));

		currentHoldingTower = new Tower(null, null, 0, null, null, null, ENTITYTYPE.TOWER);



		/////// hud creation
		Stage stage = new Stage(new ScreenViewport(), batch);

	    TextButton menuButton = new TextButton("menu", skins, "default1");
		menuButton.setSize(128, 32);
		menuButton.setPosition(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH - (menuButton.getWidth()), GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - (menuButton.getHeight()));


		multiplexer = new InputMultiplexer(worldInput, stage);
		Gdx.input.setInputProcessor(multiplexer);
		List<ShopUnit> shopUnits = new ArrayList<ShopUnit>();

		Bullet bullet = new Bullet(getModel("bullet"), null, 50, new Vector2(), new Vector2(0.3f, 0.3f), DIRECTION.RIGHT, ENTITYTYPE.BULLET);
		ShopUnit image1 = new ShopUnit("500", style2, new Tower(getModel("tower1"), null, 1.0f, new Vector2(), new Vector2(1, 1), bullet, ENTITYTYPE.TOWER));
		ShopUnit image2 = new ShopUnit("500", style3, new Tower(getModel("tower2"), null, 1.0f, new Vector2(), new Vector2(1, 1), bullet, ENTITYTYPE.TOWER));
		ShopUnit image3 = new ShopUnit("500", style4, new Tower(getModel("tower5"), null, 1.0f, new Vector2(), new Vector2(1, 1), bullet, ENTITYTYPE.TOWER));
		image1.addListener(new ShopUnitClick(image1));
		image2.addListener(new ShopUnitClick(image2));
		image3.addListener(new ShopUnitClick(image3));

		image1.setSize(100,100);
		image2.setSize(100,100);
		image3.setSize(100,100);
		image1.setPosition(0, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image1.getHeight());
		image2.setPosition(100, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image2.getHeight());
		image3.setPosition(200, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image3.getHeight());
		shopUnits.add(image1);
		shopUnits.add(image2);
		shopUnits.add(image3);


		hud = new HUD(stage, menuButton, shopUnits);

		/////end of hud creation


		entities = new ArrayList<Entity>();
		Utils.makeLog(Integer.toString(sharedAnimationModels.size()));/*
		entities.add(new Virus(getModel("virus1"), new Animation(virus1AnimationInfo.default_face, virus1AnimationInfo.default_animation, sharedAnimationModels.get(0)), 7, new Vector2(GAME_GLOBALS.GAME_CAMERA_WIDTH - 1, Utils.getRand(5)), new Vector2(1, 1), DIRECTION.LEFT, ENTITYTYPE.VIRUS));
		entities.add(new Virus(getModel("virus1"), new Animation(virus1AnimationInfo.default_face, virus1AnimationInfo.default_animation, sharedAnimationModels.get(0)), 8, new Vector2(GAME_GLOBALS.GAME_CAMERA_WIDTH - 1, Utils.getRand(5)), new Vector2(1, 1), DIRECTION.LEFT, ENTITYTYPE.VIRUS));
		entities.add(new Virus(getModel("virus1"), new Animation(virus1AnimationInfo.default_face, virus1AnimationInfo.default_animation, sharedAnimationModels.get(0)), 9, new Vector2(GAME_GLOBALS.GAME_CAMERA_WIDTH - 1, Utils.getRand(5)), new Vector2(1, 1), DIRECTION.LEFT, ENTITYTYPE.VIRUS));
		entities.add(new Virus(getModel("virus1"), new Animation(virus1AnimationInfo.default_face, virus1AnimationInfo.default_animation, sharedAnimationModels.get(0)), 10, new Vector2(GAME_GLOBALS.GAME_CAMERA_WIDTH - 1, Utils.getRand(5)), new Vector2(1, 1), DIRECTION.LEFT, ENTITYTYPE.VIRUS));
		entities.add(new Virus(getModel("virus1"), new Animation(virus1AnimationInfo.default_face, virus1AnimationInfo.default_animation, sharedAnimationModels.get(0)), 11, new Vector2(GAME_GLOBALS.GAME_CAMERA_WIDTH - 1, Utils.getRand(5)), new Vector2(1, 1), DIRECTION.LEFT, ENTITYTYPE.VIRUS));
*/

		gameStates.add(new SplashState());
	}
	List<Entity> entities;


	EntityModel loadModel(String path, String modelName)
	{
		//no checking for now
		EntityModel newModel = new EntityModel(new Texture(path), modelName);
		return newModel;
	}
	EntityModel getModel(String modelToSearch)
	{
		for ( int a = 0; a < sharedModels.size(); a++ )
		{
			if ( sharedModels.get(a).getName().equals(modelToSearch) )
			{
				return sharedModels.get(a);
			}
		}
		return loadModel("graphics/textures/" + modelToSearch + ".png", modelToSearch);
	}
	public void update(float delta)
	{
		for ( int i = 0; i < entities.size(); i++ )
		{
			Entity currentEntity = entities.get(i);
			if ( currentEntity.getType() == ENTITYTYPE.TOWER )
			{
				Tower tower = (Tower) currentEntity;
				if ( tower.isReadyToFire() )
				{
					for ( int a = 0; a < entities.size(); a++ )
					{
						Entity enemy = entities.get(a);
						if ( enemy.getType() == ENTITYTYPE.VIRUS && enemy.getAlignment() == tower.getAlignment() && enemy.getPostiion().x > tower.getPostiion().x )
						{
							tower.fire();
							tower.reload();
							break;
						}
					}
				}
				List<Bullet> refbullets = tower.getBullets();
				for ( int a = 0; a < refbullets.size(); a++ )
				{
					Bullet bullet = refbullets.get(a);
					for ( int b = 0; b < entities.size(); b++ )
					{
						Entity enemy = entities.get(b);
						if ( enemy.getType() == ENTITYTYPE.VIRUS )
						{
							if ( Utils.isAligned(enemy.getAlignment(), bullet.getAlignment()) && Utils.has2DCollision(bullet.getSprite(), enemy.getSprite()))
							{
								entities.remove(enemy);
								refbullets.remove(bullet);
								a--;
								break;
							}
						}
					}
				}
			}
			currentEntity.update(delta);
		}

		Vector3 worldUnitCoord;

		if ( !Utils.isObjectNull(ShopUnitClick.getCurrentClickedUnit()) )
		{
			Utils.makeLog("1st");
			if ( currentHoldingTower != ShopUnitClick.getCurrentClickedUnit().getUnit() )
				currentHoldingTower = ShopUnitClick.getCurrentClickedUnit().getUnit();
			worldUnitCoord = worldCam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			currentHoldingTower.getSprite().setPosition(worldUnitCoord.x - 0.5f, worldUnitCoord.y - 0.5f);
		}

		worldUnitCoord = worldInput.getUnprojectedCoord();
		Vector2 coord = new Vector2((int) worldUnitCoord.x, (int)worldUnitCoord.y);

		if ( coord.x >= 0 && coord.y >= 0 && coord.x < worldCam.viewportWidth && coord.y < worldCam.viewportHeight - 1 )
		{
			if ( !Utils.isObjectNull(ShopUnitClick.getCurrentClickedUnit()) )
			{
				currentHoldingTower.getSprite().setPosition(coord.x, coord.y);
				entities.add(new Tower(currentHoldingTower));
				ShopUnitClick.resetClick();
			}
		}

	}
	float accumulated_data = 0; //proposal key
	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		update(delta);
		gameStates.get(gameStates.size() - 1).update(delta);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(worldCam.combined);
		SplashState splash = (SplashState) gameStates.get(gameStates.size() - 1);
		if (!splash.isFinished())
		{
			batch.begin();
			gameStates.get(gameStates.size() - 1).draw(batch);
			batch.end();
		}
		else {
			accumulated_data += delta;
			if (accumulated_data >= 3.0f) {
				accumulated_data = 0;
				entities.add(new Virus(getModel("virus1"), new Animation(1, 1, sharedAnimationModels.get(0)), 11, new Vector2(GAME_GLOBALS.GAME_CAMERA_WIDTH - 1, Utils.getRand(5)), new Vector2(1, 1), DIRECTION.LEFT, ENTITYTYPE.VIRUS));

			}
			hud.draw(null);
			batch.setProjectionMatrix(worldCam.combined);
			batch.begin();
			currentWorld.draw(batch);
			for ( int a = 0; a < entities.size(); a++ )
			{
				if ( entities.get(a).isVisible() )
					entities.get(a).draw(batch);
			}
			if ( !Utils.isObjectNull(ShopUnitClick.getCurrentClickedUnit()) )
				currentHoldingTower.draw(batch);
			batch.end();
		}

	}
	@Override
	public void dispose () {
		batch.dispose();
	}



}
