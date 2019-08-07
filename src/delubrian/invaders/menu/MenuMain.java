package delubrian.invaders.menu;
import delubrian.invaders.Core;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuMain extends Menu{
    private final MenuComponentButton play;
    private final MenuComponentButton exit;
    private final MenuComponentButton credits;
    public MenuMain(GUI gui, Menu parent){
        super(gui, parent);
        play = add(new MenuComponentButton(Display.getWidth()/2-200, 300, 400, 100, "Play", true, true, "/textures/gui/button"));
        exit = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-100, 400, 100, "Exit", true, true, "/textures/gui/button"));
        credits = add(new MenuComponentButton(Display.getWidth()-200, Display.getHeight()-50, 200, 50, "Credits", true, true, "/textures/gui/button"));
    }
    @Override
    public void tick(){
        super.tick();
        Core.tickMenuBackground(true);
    }
    @Override
    public void renderBackground(){
        play.x = Display.getWidth()/2-200;
        exit.x = Display.getWidth()/2-200;
        credits.x = Display.getWidth()-200;
        credits.y = Display.getHeight()-50;
        exit.y = Display.getHeight()-100;
        Core.renderMenuBackground(true);
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==play){
            gui.open(new MenuChooseGame(gui, this));
        }
        if(button==exit){
            Core.helper.running = false;
        }
        if(button==credits){
            gui.open(new MenuCredits(gui));
        }
    }
}