package pl.bugajsky.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.bugajsky.UWar;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.fullscreen = false;
        config.width = 1280;
        config.height = 720;
        config.resizable = false;
        new LwjglApplication(new UWar(), config);
    }
}
