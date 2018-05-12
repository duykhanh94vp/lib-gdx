package compopvn.gdxdemo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

public class GameMain extends Game {
	SpriteBatch batch;
	Texture img;
	BitmapFont bitmapFont;
	private Socket socket;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		bitmapFont = new BitmapFont();

		socketConnect();
		initSocketEvent();
	}

	private void initSocketEvent() {
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Conncted");
			}
		});
		socket.on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "Socket ID : " + id);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		socket.on("newUser", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "new user joined : " + id);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void socketConnect() {
		try {
			socket = IO.socket("http://localhost:3000");
			socket.connect();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.draw(img, 100, 100);
		batch.draw(img, 200, 200);
		bitmapFont.draw(batch, "tao là khánh", 100, 100);
		bitmapFont.setColor(Color.RED);
		bitmapFont.draw(batch, "tao là khánh", 200, 100);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
