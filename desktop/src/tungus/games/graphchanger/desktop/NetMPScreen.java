package tungus.games.graphchanger.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import tungus.games.graphchanger.BaseScreen;
import tungus.games.graphchanger.game.GameScreen;
import tungus.games.graphchanger.game.players.Player;

public class NetMPScreen extends BaseScreen {
	
	private static final int MODE_LISTEN = 1;
	private static final int MODE_CONNECT = 2;
	
	public static int mode = MODE_LISTEN;
	public static int port = 8901;
	public static String IP = "25.???.??.???";
	
	public NetMPScreen(Game game) {
		super(game);
		if(IP.equals(""))
			mode=MODE_LISTEN;
		else
			mode=MODE_CONNECT;
	}
	
	@Override
	public void render(float deltaTime) {
		Socket s;
		if (mode == MODE_CONNECT) {
            while(true) {
                try {
                    Gdx.app.log("MODE", "CONNECT");
                    s = Gdx.net.newClientSocket(Net.Protocol.TCP, IP, port, new SocketHints());
                    game.setScreen(new GameScreen(game, Player.P2, s.getInputStream(), s.getOutputStream()));
                    break;
                } catch (Exception e) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }

		} else if (mode == MODE_LISTEN) {
			Gdx.app.log("MODE", "LISTEN");
			try {
				ServerSocketHints hints = new ServerSocketHints();
				hints.acceptTimeout = 0;
				ServerSocket ss = Gdx.net.newServerSocket(Net.Protocol.TCP, port, hints);
				s = ss.accept(new SocketHints());
				ss.dispose();
				game.setScreen(new GameScreen(game, Player.P1, s.getInputStream(), s.getOutputStream()));
			} catch (GdxRuntimeException e) {
				Gdx.app.log("Net MP", "Socket accept timed out. Retrying...");
				e.printStackTrace();
			}
			
		}
	}

}