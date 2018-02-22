package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.gson.annotations.SerializedName;
import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import java.awt.Font;
import java.awt.Image;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

class PlayerGlobals
{
	public static SaveInfo loadedData = null;
	public static boolean skip = false;
}

class AllStat
{
	AllStat()
	{
		stat = new ArrayList<Stat>();
	}

	@SerializedName("stats") public java.util.List<Stat> stat;
}
class Stat
{
	public int hp,damage,price;
	public String name;
}

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


class Cooldown
{

	boolean isCooldown;
	float cooldown;
	Cooldown(float iMax)
	{
		iMax = max;
	}
	void update(float delta)
	{

		if ( isCooldown ) {
			cooldown += delta;
			if (cooldown >= max) {
				Utils.makeLog(Float.toString(cooldown));
				cooldown = 0.0f;
				isCooldown = false;
			}
		}
	}

	@SerializedName("cooldownmax")
	float max;
	void makeCooldown()
	{
		isCooldown = true;
	}
}

class CooldownList
{

	CooldownList()
	{
		cooldowns = new ArrayList<Cooldown>();
	}
	void reset()
	{
		for ( int a = 0; a < cooldowns.size(); a++ )
		{
			cooldowns.get(a).cooldown = 0.0f;
			cooldowns.get(a).isCooldown = false;
		}
	}
	List<Cooldown> cooldowns;

}

class Factory
{
	static int get(String str)
	{
		return Integer.parseInt(str);
	}

	static AnimationModel createAnimationModelFromParsedJSON(AnimationInfo info)
	{
		AnimationModel model = new AnimationModel(info.animation_per_face, info.num_face);

		for ( int a = 0; a < info.bounds.size(); a++ )
		{
			model.addBound(info.bounds.get(a));
		}

		return model;
	}
	static BitmapFont createdBitmapFrontFromFile(FileHandle file, int fontSize)
	{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = fontSize;
		parameter.color = Color.RED;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
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
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = fontSize;
		return createdBitmapFrontFromFile(file, parameter);
	}
	static BitmapFont createdBitmapFrontFromFile(FileHandle file, FreeTypeFontGenerator.FreeTypeFontParameter param)
	{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
		BitmapFont font = generator.generateFont(param);
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
			Texture tileset = (Texture) ResourceManager.getObjectFromResource("map1");
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
	public static boolean isFinishedGame;
	List<EntityModel> sharedModels;
	List<AnimationModel> sharedAnimationModels;
	List<String> sharedAnimationModelsName;
	static int level = 1;

	List<GameState> gameStates;
	PlayerInput worldInput;
	HUD hud;
	Skin skins;
	Tower currentHoldingTower;
	InputMultiplexer multiplexer;
	MainMenu menu;
	LevelStructure lvls;

	Sprite[] sprite = new Sprite[3];
	Texture getTextureFromAnimation(Texture texture, Rectangle rect)
	{
		TextureRegion reg = new TextureRegion(texture, rect.x, rect.y, rect.width, rect.height);
		return reg.getTexture();
	}
	CooldownList cdList;
	AllStat stats;
	List<Cooldown> cooldowns;
	@Override
	public void create () {
		ResourceManager.load();
		isFinishedGame = false;
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
		BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);



		Texture spriteSheet = (Texture) ResourceManager.getObjectFromResource("spritesheet2");
		Texture spriteSheet_ = (Texture) ResourceManager.getObjectFromResource("spritesheet1");
		AnimationModel spriteSheet2 = Utils.getFromJson("jsons/animations/spritesheet2.json", AnimationModel.class);
		Animation spriteSheet2Animator = new Animation(1, 1, spriteSheet2);
		AnimationModel spriteSheet1 = Utils.getFromJson("jsons/animations/spritesheet1.json", AnimationModel.class);
		Animation spriteSheet1Animator = new Animation(1, 1, spriteSheet1);


		spriteSheet1Animator.getNextAnimation();
		spriteSheet1Animator.getNextAnimation();


		Rectangle pandaRect = spriteSheet1Animator.getNextAnimation();
		Sprite pandaSprite = new Sprite();
		Texture panda = (Texture)ResourceManager.getObjectFromResource("panda");
		Texture drweb =  (Texture)ResourceManager.getObjectFromResource("drweb");
		Texture k7 = (Texture)ResourceManager.getObjectFromResource("k7");
		Texture avira = (Texture)ResourceManager.getObjectFromResource("avira");
		Texture avast = (Texture)ResourceManager.getObjectFromResource("avast");
		Texture norton = (Texture)ResourceManager.getObjectFromResource("norton");
		Texture kaspersky = (Texture)ResourceManager.getObjectFromResource("kaspersky");
		Texture mcafee = (Texture)ResourceManager.getObjectFromResource("mcafee");
		cooldowns = new ArrayList<Cooldown>();




		ImageTextButton.ImageTextButtonStyle aviraStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
				avira)), null, null, font);
		ImageTextButton.ImageTextButtonStyle avastStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
				avast)), null, null, font);
		ImageTextButton.ImageTextButtonStyle nortonStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
				norton)), null, null, font);
		ImageTextButton.ImageTextButtonStyle kasperskyStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
				kaspersky)), null, null, font);
		ImageTextButton.ImageTextButtonStyle mcafeeStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
				mcafee)), null, null, font);
		ImageTextButton.ImageTextButtonStyle drwebStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
				drweb)), null, null, font);
		ImageTextButton.ImageTextButtonStyle k7Style = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
				k7)), null, null, font);
		ImageTextButton.ImageTextButtonStyle pandaStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
				panda)), null, null, font);


		aviraStyle.fontColor = Color.RED;
		avastStyle.fontColor = Color.RED;
		nortonStyle.fontColor = Color.RED;
		kasperskyStyle.fontColor = Color.RED;
		mcafeeStyle.fontColor = Color.RED;
		drwebStyle.fontColor = Color.RED;
		k7Style.fontColor = Color.RED;
		pandaStyle.fontColor = Color.RED;

		cdList = new CooldownList();

		cdList = Utils.getFromJson("cooldowns.json", CooldownList.class);


		//Utils.makeLog("cdlist" + Integer.toString(cdList.cooldowns.size()));






		TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(new SpriteDrawable(new Sprite(
				(Texture) ResourceManager.getObjectFromResource("button"))), null, null, font);

		skins.add("default1", style);

		currentWorld = WorldFactory.createWorldFromJSON("jsons/levels/first/map1.json");


		worldInput = new PlayerInput(worldCam);

		stats = Utils.getFromJson("stats.json", AllStat.class);
		Utils.makeLog(Integer.toString(stats.stat.size()) + " sizer");
		AnimationInfo virus1AnimationInfo = Utils.getFromJson("jsons/animations/virus1.json", AnimationInfo.class);
		AnimationModel virus1AnimationModel = AnimationFactory.createAnimationModelFromParsedJSON(virus1AnimationInfo);
		AnimationInfo logicBombAnimationInfo = Utils.getFromJson("jsons/animations/logicbomb.json", AnimationInfo.class);
		AnimationModel logicBombAnimationModel = AnimationFactory.createAnimationModelFromParsedJSON(logicBombAnimationInfo);
		AnimationInfo trojanHorseAnimationInfo = Utils.getFromJson("jsons/animations/trojan.json", AnimationInfo.class);
		AnimationModel trojanHorseAnimationModel = AnimationFactory.createAnimationModelFromParsedJSON(trojanHorseAnimationInfo);
		AnimationInfo virusAnimationInfo = Utils.getFromJson("jsons/animations/virus.json", AnimationInfo.class);
		AnimationModel virusAnimationModel = AnimationFactory.createAnimationModelFromParsedJSON(virusAnimationInfo);

		AnimationInfo shortcutAnimationInfo = Utils.getFromJson("jsons/animations/shortcut.json", AnimationInfo.class);
		AnimationModel shortcutAnimationModel = AnimationFactory.createAnimationModelFromParsedJSON(virusAnimationInfo);


		AnimationInfo ransomwareAnimationInfo = Utils.getFromJson("jsons/animations/ransomware.json", AnimationInfo.class);
		AnimationModel ransomwareAnimationModel = AnimationFactory.createAnimationModelFromParsedJSON(virusAnimationInfo);


		AnimationInfo adwareAnimationInfo = Utils.getFromJson("jsons/animations/adware.json", AnimationInfo.class);
		AnimationModel adwareAnimationModel = AnimationFactory.createAnimationModelFromParsedJSON(virusAnimationInfo);
		sharedAnimationModels.add(trojanHorseAnimationModel);
		sharedAnimationModels.add(logicBombAnimationModel);
		sharedAnimationModels.add(virus1AnimationModel);
		sharedAnimationModels.add(virusAnimationModel);
		sharedAnimationModels.add(shortcutAnimationModel);
		sharedAnimationModels.add(ransomwareAnimationModel);
		sharedAnimationModels.add(adwareAnimationModel);
		sharedAnimationModelsName = new ArrayList<String>();
		sharedAnimationModelsName.add("trojan");
		sharedAnimationModelsName.add("logicbomb");
		sharedAnimationModelsName.add("worm");
		sharedAnimationModelsName.add("virus");
		sharedAnimationModelsName.add("shortcut");
		sharedAnimationModelsName.add("ransomware");
		sharedAnimationModelsName.add("adware");



		EntityModel trojanHorseModel = new EntityModel(new Texture(Gdx.files.internal("graphics/textures/sprites/viruses/trojan.png")), "trojan");
		EntityModel model = new EntityModel((Texture)ResourceManager.getObjectFromResource("worm"), "worm");
		EntityModel logicBombModel = new EntityModel((Texture)ResourceManager.getObjectFromResource("logicbomb"), "logicbomb");
		EntityModel virusModel = new EntityModel((Texture)ResourceManager.getObjectFromResource("virus"), "virus");
		EntityModel shortcutModel = new EntityModel((Texture)ResourceManager.getObjectFromResource("shortcut"), "shortcut");
		EntityModel ransomwareModel = new EntityModel((Texture)ResourceManager.getObjectFromResource("ransomware"), "ransomware");
		EntityModel adwareModel = new EntityModel((Texture)ResourceManager.getObjectFromResource("adware"), "adware");
		sharedModels.add(trojanHorseModel);
		sharedModels.add(model);
		sharedModels.add(logicBombModel);
		sharedModels.add(virusModel);
		sharedModels.add(shortcutModel);
		sharedModels.add(ransomwareModel);
		sharedModels.add(adwareModel);



		sharedModels.add(createModel(panda, "panda"));
		sharedModels.add(createModel(drweb, "drweb"));
		sharedModels.add(createModel(k7, "k7"));
		sharedModels.add(createModel(avira, "avira"));
		sharedModels.add(createModel(avast, "avast"));
		sharedModels.add(createModel(norton, "norton"));
		sharedModels.add(createModel(kaspersky, "kaspersky"));
		sharedModels.add(createModel(mcafee, "mcafee"));

		for ( int a = 0; a < sharedModels.size(); a++ )
		{
			for ( int b = 0; b < stats.stat.size(); b++) {
				if ( sharedModels.get(a).getName().equals(stats.stat.get(b).name))
				{
					EntityModel currModel = sharedModels.get(a);
					currModel.damage = stats.stat.get(a).damage;
					currModel.money = stats.stat.get(a).price;
				}
			}
		}
		Texture bullet = (Texture) ResourceManager.getObjectFromResource("bullet");
		Texture bullet1 = (Texture) ResourceManager.getObjectFromResource("bullet1");
		Texture bullet2 = (Texture) ResourceManager.getObjectFromResource("bullet2");
		Texture bullet3 = (Texture) ResourceManager.getObjectFromResource("bullet3");
		Texture bullet4 = (Texture) ResourceManager.getObjectFromResource("bullet4");
		sharedModels.add(createModel(bullet, "tower1Bullet"));
		sharedModels.add(createModel(bullet1, "bullet1"));
		sharedModels.add(createModel(bullet2, "bullet2"));
		sharedModels.add(createModel(bullet3, "bullet3"));
		sharedModels.add(createModel(bullet4, "bullet4"));
		//sharedModels.add(createModel(spriteSheet, "spritesheet2"));
		//sharedModels.add(createModel(spriteSheet_, "spritesheet1"));


		currentHoldingTower = new Tower(null, null, 0, null, null, null, ENTITYTYPE.TOWER);



		/////// hud creation
		Stage stage = new Stage(new ScreenViewport(), batch);

		menuButton = new TextButton("menu", skins, "default1");
		menuButton.setSize(128, 32);
		menuButton.setPosition(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH - (menuButton.getWidth()), GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - (menuButton.getHeight()));


		multiplexer = new InputMultiplexer(worldInput, stage);
		Gdx.input.setInputProcessor(multiplexer);
		List<ShopUnit> shopUnits = new ArrayList<ShopUnit>();


		Bullet bbullet = new Bullet(getModel("tower1Bullet"), null, 50, new Vector2(), new Vector2(0.3f, 0.3f), DIRECTION.RIGHT, ENTITYTYPE.BULLET);

		Bullet bbullet1 = new Bullet(getModel("bullet1"), null, 50, new Vector2(), new Vector2(0.3f, 0.3f), DIRECTION.RIGHT, ENTITYTYPE.BULLET);

		Bullet bbullet2 = new Bullet(getModel("bullet2"), null, 50, new Vector2(), new Vector2(0.3f, 0.3f), DIRECTION.RIGHT, ENTITYTYPE.BULLET);

		Bullet bbullet3 = new Bullet(getModel("bullet3"), null, 50, new Vector2(), new Vector2(0.3f, 0.3f), DIRECTION.RIGHT, ENTITYTYPE.BULLET);

		Bullet bbullet4 = new Bullet(getModel("bullet4"), null, 50, new Vector2(), new Vector2(0.3f, 0.3f), DIRECTION.RIGHT, ENTITYTYPE.BULLET);

		Animation pandaAnimation = new Animation(1, 1, Utils.getFromJson("jsons/animations/panda.json", AnimationModel.class));

		Tower[] arrTowers = new Tower[8];
		arrTowers[0] = new Tower(getModel("panda"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER);
		arrTowers[1] = new Tower(getModel("drweb"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER);
		arrTowers[2] = new Tower(getModel("k7"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER);
		arrTowers[3] = new Tower(getModel("avira"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER);
		arrTowers[4] = new Tower(getModel("norton"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER);
		arrTowers[5] = new Tower(getModel("kaspersky"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER);
		arrTowers[6] = new Tower(getModel("mcafee"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER);
		arrTowers[7] = new Tower(getModel("avast"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER);
		for ( int a = 0; a < stats.stat.size(); a++ )
		{
			for ( int b = 0; b < arrTowers.length; b++ )
			{
				if ( arrTowers[b].getModel().getName().equals(stats.stat.get(a)))
				{
					arrTowers[b].hp = stats.stat.get(a).hp;
					break;
				}
			}
		}



		ShopUnit image1 = new ShopUnit(Integer.toString(arrTowers[0].getModel().getMoney()), pandaStyle, new Tower(getModel("panda"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER));
		image1.setPrice(arrTowers[0].getModel().getMoney());
		Animation drwebAnimation = new Animation(1, 1, Utils.getFromJson("jsons/animations/drweb.json", AnimationModel.class));
		ShopUnit image2 = new ShopUnit(Integer.toString(arrTowers[1].getModel().getMoney()), drwebStyle, new Tower(getModel("drweb"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet1, ENTITYTYPE.TOWER));
		image2.setPrice(arrTowers[1].getModel().getMoney());
		Animation k7Animation = new Animation(1, 1, Utils.getFromJson("jsons/animations/k7.json", AnimationModel.class));
		ShopUnit image3 = new ShopUnit(Integer.toString(arrTowers[2].getModel().getMoney()), k7Style, new Tower(getModel("k7"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet2, ENTITYTYPE.TOWER));
		image3.setPrice(arrTowers[2].getModel().getMoney());
		Animation aviraAnimation = new Animation(1, 1, Utils.getFromJson("jsons/animations/avira.json", AnimationModel.class));
		ShopUnit image4 = new ShopUnit(Integer.toString(arrTowers[3].getModel().getMoney()), aviraStyle, new Tower(getModel("avira"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet3, ENTITYTYPE.TOWER));
		image4.setPrice(arrTowers[3].getModel().getMoney());
		Animation nortonAnimation = new Animation(1, 1, Utils.getFromJson("jsons/animations/norton.json", AnimationModel.class));
		ShopUnit image5 = new ShopUnit(Integer.toString(arrTowers[4].getModel().getMoney()), nortonStyle, new Tower(getModel("norton"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet4, ENTITYTYPE.TOWER));
		image5.setPrice(arrTowers[4].getModel().getMoney());
		Animation kasperskyAnimation = new Animation(1, 1, Utils.getFromJson("jsons/animations/kaspersky.json", AnimationModel.class));
		ShopUnit image6 = new ShopUnit(Integer.toString(arrTowers[5].getModel().getMoney()), kasperskyStyle, new Tower(getModel("kaspersky"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet4, ENTITYTYPE.TOWER));
		image6.setPrice(arrTowers[5].getModel().getMoney());
		ShopUnit image7 = new ShopUnit(Integer.toString(arrTowers[6].getModel().getMoney()), mcafeeStyle, new Tower(getModel("mcafee"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet3, ENTITYTYPE.TOWER));
		image7.setPrice(arrTowers[6].getModel().getMoney());
		Animation avastAnimation = new Animation(1, 1, Utils.getFromJson("jsons/animations/avast.json", AnimationModel.class));
		ShopUnit image8 = new ShopUnit(Integer.toString(arrTowers[7].getModel().getMoney()), avastStyle, new Tower(getModel("avast"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet2, ENTITYTYPE.TOWER));
		image8.setPrice(arrTowers[7].getModel().getMoney());
		image1.addListener(new ShopUnitClick(image1));
		image2.addListener(new ShopUnitClick(image2));
		image3.addListener(new ShopUnitClick(image3));
		image4.addListener(new ShopUnitClick(image4));
		image5.addListener(new ShopUnitClick(image5));
		image6.addListener(new ShopUnitClick(image6));
		image7.addListener(new ShopUnitClick(image7));
		image8.addListener(new ShopUnitClick(image8));



		image1.setSize(100,100);
		image2.setSize(100,100);
		image3.setSize(100,100);
		image4.setSize(100,100);
		image5.setSize(100,100);
		image6.setSize(100,100);
		image7.setSize(100,100);
		image8.setSize(100,100);
		image1.setPosition(0, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image1.getHeight());
		image2.setPosition(100, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image2.getHeight());
		image3.setPosition(200, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image3.getHeight());
		image4.setPosition(300, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image4.getHeight());
		image5.setPosition(400, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image5.getHeight());
		image6.setPosition(500, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image6.getHeight());
		image7.setPosition(600, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image7.getHeight());
		image8.setPosition(700, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image8.getHeight());
		shopUnits.add(image1);
		shopUnits.add(image2);
		shopUnits.add(image3);
		shopUnits.add(image4);
		shopUnits.add(image5);
		shopUnits.add(image6);
		shopUnits.add(image7);
		shopUnits.add(image8);

		for ( int a = 0; a < shopUnits.size(); a++ )
		{
			shopUnits.get(a).setCooldown(cdList.cooldowns.get(a));
		}
		hud = new HUD(stage, menuButton, shopUnits);
		/////end of hud creation
		Label.LabelStyle lblStyle = new Label.LabelStyle((BitmapFont) ResourceManager.getObjectFromResource("font"), Color.RED);
		lblmoney = new Label(Integer.toString(money), lblStyle);
		lblmoney.setPosition(menuButton.getX(), menuButton.getY() - menuButton.getHeight());
		wave = new Label("wave 1", lblStyle);
		infectionRateLabel = new Label("infection rate: 0", lblStyle );

		bitcoinImage = new ImageTextButton("",new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("bitcoin"))), null, null, font));

		bitcoinImage.setSize(32, 32);
		bitcoinImage.setPosition(lblmoney.getX() - 32, lblmoney.getY());
		;


		GlyphLayout layout = new GlyphLayout((BitmapFont)ResourceManager.getObjectFromResource("font")," infection rate: xxx");

		infectionRateLabel.setPosition(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH - layout.width, lblmoney.getY() - lblmoney.getHeight());
		infectionImage = new ImageTextButton("",new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("infection"))), null, null, font));

		infectionImage.setSize(32, 32);
		infectionImage.setPosition(infectionRateLabel.getX() - 32, infectionRateLabel.getY());
        stage.addActor(bitcoinImage);
		stage.addActor(infectionImage);
		stage.addActor(infectionRateLabel);
		stage.addActor(wave);
		wave.setPosition(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH/2, (GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT / 2) -  100);
		entities = new ArrayList<Entity>();
		//loadStage(1);
		//gameStates.add(new MenuState(gameStates));
		Label.LabelStyle lblStylemoney = new Label.LabelStyle(font, Color.WHITE);
		stage.addActor(lblmoney);
		menuButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				event.getStage().addActor(pause);
				}
		});

		Window.WindowStyle windowStyle = new Window.WindowStyle((BitmapFont)ResourceManager.getObjectFromResource("font"), Color.WHITE, new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("splash"))));
		pause = new PauseView("", windowStyle);
		pause.setFillParent(true);
		pause.setPosition(0, 0);
		pause.quit.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				gameStates.clear();
				gameStates.add(new PlayerProfileState(gameStates));
			}
		});


		menuExplanation = Gdx.audio.newMusic(Gdx.files.internal("audios/menuButtonExplanation.mp3"));
		infectionRateExplanation = Gdx.audio.newMusic(Gdx.files.internal("audios/infectionRateExplanation.mp3"));
		moneyExplanation = Gdx.audio.newMusic(Gdx.files.internal("audios/moneyExplanation.mp3"));
		towerExplanation = Gdx.audio.newMusic(Gdx.files.internal("audios/moneyExplanation.mp3"));



		sprite[2] = new Sprite((Texture)ResourceManager.getObjectFromResource("stage1background"));
		sprite[1] = new Sprite((Texture)ResourceManager.getObjectFromResource("stage2background"));
		sprite[0] = new Sprite((Texture)ResourceManager.getObjectFromResource("stage3background"));
		sprite[2].setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
		sprite[1].setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
		sprite[0].setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);


		gameStates.add(new SplashState(gameStates));

		field = new Sprite((Texture) ResourceManager.getObjectFromResource("field"));
		field.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, 5);
	}
	Label infectionRateLabel;

	ImageTextButton bitcoinImage;
	ImageTextButton infectionImage;
	PauseView pause;

	Label lblmoney;
	List<Entity> entities;
	boolean wavemode = true;

	EntityModel createModel(Texture texture, String modelName)
	{
		//no checking for now
		EntityModel newModel = new EntityModel(texture, modelName);
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
		return null;
	}
	void setInfectionRate(int newrate)
	{
		infection_rate = newrate;
		infectionRateLabel.setText("infection rate: " + Integer.toString(newrate));
	}
	public void update(float delta)
	{
		hud.update(delta);
		for ( int a = 0; a < cooldowns.size(); a++ )
		{
			cooldowns.get(a).update(delta);
		}
		for ( int a = 0; a < entities.size(); a++ )
		{
			Entity enemy = entities.get(a);
			if ( enemy.getType() == ENTITYTYPE.VIRUS && enemy.getPostiion().x < -1 )
			{
				Virus v = (Virus) enemy;
				setInfectionRate(infection_rate + enemy.getModel().damage);
				if ( !Core.isTutorial ) {
					if (Utils.isObjectNull(PlayerGlobals.loadedData.leaked))
						PlayerGlobals.loadedData.leaked = new ArrayList<leakedvirus>();

					PlayerGlobals.loadedData.leaked.add(new leakedvirus(enemy.getModel().getName(), Core.stageplaying));
					Utils.makeLog(enemy.getModel().getName());
				}
				entities.remove(enemy);
				a--;
			}
		}
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
						if ( enemy.getType() == ENTITYTYPE.VIRUS && Utils.isAligned(enemy.getAlignment(), bullet.getAlignment()) )
						{
							if ( Utils.has2DCollision(bullet.getSprite(), enemy.getSprite()) )
							{
								enemy.hp -= bullet.damage;
								if ( enemy.hp <= 0 ) {
									setMoney(money + enemy.getModel().money);
									entities.remove(enemy);
									a--;
									break;
								};
								refbullets.remove(bullet);
							}

						}
					}
				}
			}
			boolean enemyAttackDontMove = false;
			if ( currentEntity.getType() == ENTITYTYPE.VIRUS ) {
				for (int c = 0; c < entities.size(); c++) {
					if (entities.get(c) != currentEntity) {
						if (entities.get(c).getType() == ENTITYTYPE.TOWER) {
							if (Utils.has2DCollision(entities.get(c).getSprite(), currentEntity.getSprite())) {
								Virus currV = (Virus)currentEntity;
								currV.accumulated_time_attack += delta;
								if ( currV.accumulated_time_attack >= currV.attack_trigger_time )
								{
									currV.accumulated_time_attack = 0;
									entities.get(c).hp -= currV.getModel().damage;
									if ( entities.get(c).hp <= 0 )
									{
										entities.remove(entities.get(c));
										c--;
									}
								}
								enemyAttackDontMove = true;

							}
						}
					}
				}
			}
			if ( !enemyAttackDontMove )
				currentEntity.update(delta);
		}

		Vector3 worldUnitCoord;

		if (!Utils.isObjectNull(ShopUnitClick.getCurrentClickedUnit()) && cdList.cooldowns.get(ShopUnitClick.getCurrentClickedUnit().id).isCooldown)
			ShopUnitClick.resetClick();
		if ( !Utils.isObjectNull(ShopUnitClick.getCurrentClickedUnit()) )
		{
			///cfor cooldown
			if ( currentHoldingTower != ShopUnitClick.getCurrentClickedUnit().getUnit() ) {
					currentHoldingTower = ShopUnitClick.getCurrentClickedUnit().getUnit();
			}
			worldUnitCoord = worldCam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			currentHoldingTower.getSprite().setPosition(worldUnitCoord.x - 0.5f, worldUnitCoord.y - 0.5f);
		}


		worldUnitCoord = worldInput.getUnprojectedCoord();
		Vector2 coord = new Vector2((int) worldUnitCoord.x, (int)worldUnitCoord.y);

		if ( coord.x >= 0 && coord.y >= 0 && coord.x < worldCam.viewportWidth && coord.y < worldCam.viewportHeight - 1 )
		{
			if ( !Utils.isObjectNull(ShopUnitClick.getCurrentClickedUnit()) )
			{
				for ( int a = 0; a < entities.size(); a++ )
				{

					if ( entities.get(a).getType() == ENTITYTYPE.TOWER )
					{
						if ( entities.get(a).getPostiion().x == coord.x && entities.get(a).getPostiion().y == coord.y )
						{
							return;
						}
					}
				}
				int price = ShopUnitClick.getCurrentClickedUnit().price;
				if ( money < price )
				{
					return;
				}
				else {
					Utils.makeLog("else else");
					setMoney(money - price);
					currentHoldingTower.getSprite().setPosition(coord.x, coord.y);
					//ShopUnitClick.getCurrentClickedUnit().makeCooldown();
					//ShopUnitClick.getCurrentClickedUnit().isCooldown = true;
					cdList.cooldowns.get(ShopUnitClick.getCurrentClickedUnit().id).makeCooldown();
					entities.add(new Tower(currentHoldingTower));
					ShopUnitClick.resetClick();
				}
			}
		}

	}
	void setMoney(int newmoney)
	{
		lblmoney.setText(Integer.toString(newmoney));
		money = newmoney;
	}

	float accumulated_data = 0;
	void loadStage(int stage)
	{
		lvls =  Utils.deserialize("levels.json", LevelStructure.class);
		StageInfo currStage = lvls.stages.get(stage);

		currStageInfo = currStage;
		stageCounter = new StageInfo();
	}
	void loadTutorial()
	{
		lvls =  Utils.deserialize("tutorial.json", LevelStructure.class);
		StageInfo currStage = lvls.stages.get(0);

		currStageInfo = currStage;
		stageCounter = new StageInfo();
	}

	static int tutorial_step = 0;
static	boolean isTutorial = false;
	StageInfo currStageInfo;
	StageInfo stageCounter;
	Label wave;
	boolean finishedwave = false;
	int infection_rate = 0;
	int money = 20;
	TextButton menuButton;
	void reset()
	{
		cdList.reset();
		money = 20 * Core.level;
		infection_rate = 0;
		finishedwave = false;
		accumulated_data = 0;
		stageCounter = new StageInfo();
		wavemode = true;
		setInfectionRate(0);
		setMoney(money);
		entities.clear();

	}
	boolean firstexecute = true;
	static int stageplaying = 0;
	int tutorial_time = 25;
	int explanation_step = 0;
	Music menuExplanation;
	Music moneyExplanation;
	Music towerExplanation;
	Music infectionRateExplanation;
	Sprite field;
	boolean infectionExplained = false;
	boolean moneyExplained = false;
	boolean menuExplained = false;
	boolean towerExplained = false;
	boolean firstexplanation = true;
	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(worldCam.combined);
		if (gameStates.size() > 0)
		{
			firstexecute = true;
			for ( int a = 0; a < gameStates.size(); a++ )
				if ( gameStates.get(a).isActiveState() )
					gameStates.get(a).draw(batch);
			for ( int a = 0; a < gameStates.size(); a++ )
				if ( gameStates.get(a).isActiveState() ) {
					GameState curr = gameStates.get(a);
					gameStates.get(a).update(delta);
					if ( curr instanceof PlayerSkillTreeState )
					{
						PlayerSkillTreeState temp = (PlayerSkillTreeState) curr;
						// TF i dont remember why i coded this one
					}
				}
		}
		else {
			if ( firstexecute )
			{
				reset();


				if ( isTutorial )
				{
					if ( firstexplanation )
					{
						menuExplanation.play();
						menuButton.setDebug(true);
						firstexplanation = false;
						Utils.makeLog("money money money");
					}
/*
					infectionRateExplanation = Gdx.audio.newMusic(Gdx.files.internal("audios/infectionRateExplanation.mp3"));
					moneyExplanation = Gdx.audio.newMusic(Gdx.files.internal("audios/moneyExplanation.mp3"));
					towerExplanation = Gdx.audio.newMusic(Gdx.files.internal("audios/moneyExplanation.mp3"));*/
					if ( !menuExplanation.isPlaying() && !menuExplained)
					{
						menuExplanation.dispose();
						moneyExplanation.play();
						lblmoney.setDebug(true);
						menuButton.setDebug(false);
						menuExplained = true;
					}
					else if ( !moneyExplanation.isPlaying() && menuExplained && !moneyExplained)
					{
						moneyExplanation.dispose();
						infectionRateExplanation.play();
						lblmoney.setDebug(false);
						infectionRateLabel.setDebug(true);
						moneyExplained = true;
					}
					else if ( !infectionRateExplanation.isPlaying() && moneyExplained && !infectionExplained)
					{
						infectionRateLabel.setDebug(false);
						infectionRateExplanation.dispose();
						towerExplanation.play();
						infectionExplained = true;
					}
					else if ( !towerExplanation.isPlaying() && infectionExplained )
					{
						explanation_step = 3;
						towerExplanation.dispose();
						towerExplanation = null;
					}
					if (Utils.isObjectNull(towerExplanation))
					{
						firstexecute = false;
						loadTutorial();
					}
				}
				else
				{
					firstexecute = false;
					loadStage(Core.stageplaying - 1);
					((Music)ResourceManager.getObjectFromResource("menuBackgroundMusic")).stop();
					if ( !Core.isTutorial ) {
						Music music = ((Music) ResourceManager.getObjectFromResource("backgroundGameplay"));
						music.play();
						music.setLooping(true);
					}
					else
					{
						Music music = ((Music) ResourceManager.getObjectFromResource("backgroundGameplay"));
						music.stop();
					}
				}
			}

			else {
				if (!finishedwave) {
					if (infection_rate > 0)
					{
						SaveList lists = Utils.getFromJson("savestate.json", SaveList.class);
						for ( int a = 0; a < lists.list.size(); a++ )
						{
							if ( lists.list.get(a).name.equals(PlayerGlobals.loadedData.name))
							{
								lists.list.get(a).leaked = PlayerGlobals.loadedData.leaked;
							}
						}
						Utils.serialize(lists);
					}
					if (infection_rate >= 100) {

						((Music)ResourceManager.getObjectFromResource("backgroundGameplay")).stop();

						if ( !isTutorial ) {
							Music gameover = Gdx.audio.newMusic(Gdx.files.internal("audios/gameover.mp3"));
							gameover.play();
							gameover.setOnCompletionListener(new Music.OnCompletionListener() {
								@Override
								public void onCompletion(Music music) {
									music.dispose();
								}
							});
						}
						finishedwave = true;
						BitmapFont font = (BitmapFont) ResourceManager.getObjectFromResource("font");
						Window.WindowStyle winStyle = new Window.WindowStyle(font, Color.WHITE, new SpriteDrawable(new Sprite((Texture) ResourceManager.getObjectFromResource("splash"))));
						TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle(new SpriteDrawable(new Sprite((Texture) ResourceManager.getObjectFromResource("button"))), null, null, font);
						TextButton button = new TextButton("quit", btnStyle);
						d = new Dialog("", winStyle);
						button.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								super.clicked(event, x, y);
								reset();
								d.remove();
								Core.isTutorial = false;
								gameStates.add(new PlayerProfileState(gameStates));
								////// go to player
								////// because you die
							}
						});
						d.setFillParent(true);
						d.add(button).bottom();
						String lbl = "you have been infected completely try again!";
						if ( Core.isTutorial )
							lbl = "You have finished the tutorial. it shows you how to get infected";
						d.getContentTable().add(new Label(lbl, new Label.LabelStyle(font, Color.WHITE)));
						hud.getStage().addActor(d);
					}
					update(delta);
					Gdx.input.setInputProcessor(multiplexer);
					accumulated_data += delta;
					if (wavemode) {
						if (accumulated_data >= 5.0f) {
							wave.setVisible(false);
							wavemode = false;
							accumulated_data = 0;
						}
					} else {

						boolean found = false;
						for (int a = 0; a < entities.size(); a++) {
							if (entities.get(a).getType() == ENTITYTYPE.VIRUS) {
								found = true;
								break;
							}
						}
						if (stageCounter.wave_num >= currStageInfo.wave_num && !found && stageCounter.enemy_per_wave >= currStageInfo.enemy_per_wave) {
							finishedwave = true;

							BitmapFont font = (BitmapFont) ResourceManager.getObjectFromResource("font");
							Window.WindowStyle winStyle = new Window.WindowStyle(font, Color.WHITE, new SpriteDrawable(new Sprite((Texture) ResourceManager.getObjectFromResource("splash"))));
							TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle(new SpriteDrawable(new Sprite((Texture) ResourceManager.getObjectFromResource("button"))), null, null, font);
							TextButton button = new TextButton("finish", btnStyle);
							button.addListener(new ClickListener() {
								@Override
								public void clicked(InputEvent event, float x, float y) {
									super.clicked(event, x, y);
									if ( !Core.isTutorial ) {
										SaveList list = Utils.deserialize("savestate.json", SaveList.class);
										for (int a = 0; a < list.list.size(); a++) {
											if (list.list.get(a).name.equals(PlayerGlobals.loadedData.name)) {
												int star = -1;
												if (infection_rate >= 75) {
													star = 1;
												} else if (infection_rate >= 50) {
													star = 2;
												} else if (infection_rate < 50) {
													star = 3;
												}
												if (stageplaying == 1)
													PlayerGlobals.loadedData.stage1star = star;
												else if (stageplaying == 2)
													PlayerGlobals.loadedData.stage2star = star;
												else if (stageplaying == 3)
													PlayerGlobals.loadedData.stage3star = star;
												list.list.remove(a);
												if (PlayerGlobals.loadedData.skilltree < 7)
													PlayerGlobals.loadedData.skilltree += 1;
												if (PlayerGlobals.loadedData.stage == 1)
													PlayerGlobals.loadedData.stage = 2;
												else if (PlayerGlobals.loadedData.stage == 2)
													PlayerGlobals.loadedData.stage = 3;

												list.list.add(PlayerGlobals.loadedData);

												Utils.serialize(list);

												break;
											}

										}
										reset();
									}
									//50% infection rate = 2 stars
									//75% infection rate = 1 star
									//0% infection rate = 3 stars
									d.remove();
									if ( !Core.isTutorial )
									{
										if ( Core.stageplaying >= 3 )
										{
											Core.isFinishedGame = true;
											java.util.List<Scene> scenes = new ArrayList<Scene>();

											((Music) ResourceManager.getObjectFromResource("backgroundGameplay")).stop();
											for ( int a = 9; a > 0; a-- )
											{
												Sprite sprite = new Sprite(new Texture(Gdx.files.internal("graphics/textures/CUTSCENE_ENDING/" + Integer.toString(a) + ".png")));
												sprite.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
												scenes.add(new Scene(gameStates, sprite, scenes));
											}
											gameStates.add(new CutsceneState(gameStates, scenes, Gdx.audio.newMusic(Gdx.files.internal("audios/victorious.mp3"))));
										}
										else
										{
											gameStates.add(new PlayerProfileState(gameStates));
										}
									}
									else {
										Core.isTutorial = false;
										reset();
										gameStates.add(new PlayerProfileState(gameStates));
									}
									////// go to player
									////// save the shit
								}
							});
							d = new Dialog("", winStyle);
							d.setFillParent(true);
							d.add(button).bottom();
							String lbl = "you just finished the stage congrats";
							if ( Core.isTutorial )
							{
								lbl = "You have finished the tutorial congrats! it shows you how to dominate them!";

							}
							d.getContentTable().add(new Label(lbl, new Label.LabelStyle(font, Color.WHITE)));
							hud.getStage().addActor(d);
						}
						if (!finishedwave) {
							if (accumulated_data >= 1.0f) {
								accumulated_data = 0;
								if (stageCounter.enemy_per_wave < currStageInfo.enemy_per_wave) {
									stageCounter.enemy_per_wave++;
									String virusName = currStageInfo.enemies.get(MathUtils.random(0, currStageInfo.enemies.size() - 1));
									int animIndex = 0;
									for (int a = 0; a < sharedAnimationModelsName.size(); a++) {
										if (sharedAnimationModelsName.get(a).equals(virusName)) {
											animIndex = a;
											break;
										}
									}
									Virus virus = new Virus(getModel(virusName), new Animation(1, 1, sharedAnimationModels.get(animIndex)), 5, new Vector2(GAME_GLOBALS.GAME_CAMERA_WIDTH - 1, Utils.getRand(5)), new Vector2(1, 1), DIRECTION.LEFT, ENTITYTYPE.VIRUS);

									for ( int a = 0; a < stats.stat.size(); a++ )
									{
										if ( stats.stat.get(a).name.equals(virusName) )
										{
											Utils.makeLog(Integer.toString(virus.hp));
											virus.hp = stats.stat.get(a).hp;
											virus.getModel().money = stats.stat.get(a).price;
											virus.getModel().damage = stats.stat.get(a).damage;
											break;
										}
									}
									entities.add(virus);
								} else if (stageCounter.wave_num < currStageInfo.wave_num) {

									wave.setVisible(true);
									wave.setText("wave " + Integer.toString(stageCounter.wave_num + 1));
									wavemode = true;
									stageCounter.wave_num++;
									currStageInfo.enemy_per_wave += 5;
									stageCounter.enemy_per_wave = 0;
								}
							}
						}
					}
				}
			}
				batch.setProjectionMatrix(worldCam.combined);
				batch.begin();
				//currentWorld.draw(batch);
			sprite[Core.stageplaying-1].draw(batch);
			//sprite[0].draw(batch);
				field.draw(batch);
				for (int a = 0; a < entities.size(); a++) {
					if (entities.get(a).isVisible())
						entities.get(a).draw(batch);
				}


				if (!Utils.isObjectNull(ShopUnitClick.getCurrentClickedUnit()))
					currentHoldingTower.draw(batch);
				batch.end();

			hud.draw(null);
		}

	}
	@Override
	public void dispose () {
		batch.dispose();
	}


	Dialog d;
}
