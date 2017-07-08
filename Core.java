package com.malaware.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.annotations.SerializedName;


import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;



class Tileset
{
	Tileset()
	{
		properties = new ArrayList<Map<String, String>>();
		file = new String();
	}
	@SerializedName("file") String file;
	@SerializedName("properties") List<Map<String, String>> properties;
	String column;
	String tile_height;
	String tile_width;
	String margin;
	String spacing;
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

			int width = get(ref.column);
			int mapheight = get(worldInfo.row);
			int mapwidth = get(worldInfo.column);
			int tilewidth = get(ref.tile_width);
			int tileheight = get(ref.tile_height);
			int spacing = get(ref.spacing);
			int margin = get(ref.margin);
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

	World currentWorld;
	List<EntityModel> sharedModels;
	List<AnimationModel> sharedAnimationModels;
	List<Virus> enemies;
	List<Tower> towers;
	@Override
	public void create () {
		towers = new ArrayList<Tower>();
		batch = new SpriteBatch();
		worldCam = new OrthographicCamera(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.ASPECT_RATIO);
		//Utils.makeLog(Float.toString(GAME_GLOBALS.ASPECT_RATIO));
		worldCam.position.set(worldCam.viewportWidth / 2f, worldCam.viewportHeight / 2f, 0);
		worldCam.update();
		sharedModels = new ArrayList<EntityModel>();
		sharedAnimationModels = new ArrayList<AnimationModel>();
		enemies = new ArrayList<Virus>();

		currentWorld = WorldFactory.createWorldFromJSON("jsons/levels/first/map1.json");


		AnimationInfo virus1AnimationInfo = Utils.getFromJson("jsons/animations/virus1.json", AnimationInfo.class);
		AnimationModel virus1AnimationModel = AnimationFactory.createAnimationModelFromParsedJSON(virus1AnimationInfo);


		sharedModels.add(loadModel("graphics/textures/virus1.png", "virus1"));
		sharedModels.add(loadModel("graphics/textures/tower.png", "tower"));

		sharedModels.add(loadModel("graphics/textures/bullet.png", "bullet"));


		Vector2 pos = new Vector2(0, 3.5f);

		Bullet bullet = new Bullet(getModel("bullet"), null, 15, pos, new Vector2(0.3f, 0.3f), DIRECTION.RIGHT);

		towers.add(new Tower(getModel("tower"), null, 30, new Vector2(0, 3), new Vector2(1, 1), bullet));

		enemies.add(new Virus(getModel("virus1"), new Animation(virus1AnimationInfo.default_face, virus1AnimationInfo.default_animation, virus1AnimationModel), 10, new Vector2(GAME_GLOBALS.GAME_CAMERA_WIDTH - 1, 3),
				new Vector2(1, 1), DIRECTION.LEFT));


	}

	EntityModel loadModel(String path, String modelName)
	{
		//no checking for now
		EntityModel newModel = new EntityModel(new Texture(path), modelName);
		sharedModels.add(newModel);
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
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(worldCam.combined);
		batch.begin();
		currentWorld.draw(batch);
		for ( int a = 0; a < enemies.size(); a++ )
		{
			enemies.get(a).update(Gdx.graphics.getDeltaTime());
			enemies.get(a).draw(batch);
		}
		for ( int a = 0; a < towers.size(); a++ )
		{
			towers.get(a).update(Gdx.graphics.getDeltaTime());

			towers.get(a).draw(batch);
			List<Bullet> refbullets = towers.get(a).getBullets();
			for ( int b = 0; b < refbullets.size(); b++ )
			{
				if ( Utils.has2DCollition(refbullets.get(a).getSprite().getBoundingRectangle(),
						enemies.get(0).getSprite().getBoundingRectangle()) ) {

				}
			}
		}
		batch.end();
	}
	@Override
	public void dispose () {
		batch.dispose();
	}



}
