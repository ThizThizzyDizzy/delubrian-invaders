package delubrian.invaders.menu;
import delubrian.invaders.planet.Galaxy;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
public class MenuGameOver extends Menu{

    private final Galaxy galaxy;
    public MenuGameOver(GUI gui, Galaxy galaxy){
        super(gui, null);
        this.galaxy = galaxy;
    }
    @Override
    public void render(int millisSinceLastTick){
        GL11.glColor4d(0, 0, 0, 1);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
        int i = 20;
        GL11.glColor4d(1, 1, 1, 1);
        drawCenteredText(0, Display.getHeight()/2-i, Display.getWidth(), Display.getHeight()/2, "Game Over");
        drawCenteredText(0, Display.getHeight()/2, Display.getWidth(), Display.getHeight()/2+i, "Click to continue");
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange){
        gui.open(new MenuSurface(gui, new MenuMain(gui, null), galaxy, galaxy.getStarSystem("Shaldon").getPlanet("Shielgic")));
    }
}