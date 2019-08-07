package delubrian.invaders.menu;
import delubrian.invaders.Core;
import delubrian.invaders.Main;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.menu.MenuSurface;
import java.io.File;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentTextBox;
import simplelibrary.opengl.gui.Menu;
public class MenuNewGame extends Menu{
    private final MenuComponentButton back;
    private final MenuComponentButton create;
    private final MenuComponentTextBox name;
    public MenuNewGame(GUI gui, Menu parent){
        super(gui, parent);
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Back", true, true, "/textures/gui/button"));
        create = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-160, 400, 40, "Create", false, true, "/textures/gui/button"));
        name = add(new MenuComponentTextBox(Display.getWidth()/2-200, 120, 400, 40, "", true));
    }
    @Override
    public void renderBackground(){
        File file = new File(Main.getAppdataRoot()+"\\saves\\"+name.text);
        if(!file.exists()){
            create.enabled = true;
        }else{
            create.enabled = false;
        }
        back.x = Display.getWidth()/2-200;
        back.y = Display.getHeight()-80;
        create.x = back.x;
        create.y = back.y-80;
        name.x = create.x;
        name.y = create.y-240;
        Core.renderMenuBackground(false);
    }
    @Override
    public void tick(){
        Core.tickMenuBackground(false);
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            back();
        }
        if(button==create){
            create(name.text);
        }
    }
    private void back(){
        gui.open(parent);
    }
    private void create(String name){
        Galaxy galaxy = Core.load(name);
        gui.open(new MenuSurface(gui, parent.parent, galaxy, galaxy.planet));
    }
}