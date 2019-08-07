package delubrian.invaders.menu;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.Core;
import delubrian.invaders.Main;
import java.io.File;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentList;
public class MenuChooseGame extends Menu{
    private final MenuComponentButton back;
    private final MenuComponentButton newSave;
    private final MenuComponentButton play;
    private final MenuComponentButton rename;
    private final MenuComponentButton delete;
    private final MenuComponentList saveList;
    public MenuChooseGame(GUI gui, Menu parent){
        super(gui, parent);
        back = add(new MenuComponentButton(Display.getWidth()/4-100, Display.getHeight()-80, 200, 40, "Exit", true, true, "/textures/gui/button"));
        newSave = add(new MenuComponentButton(Display.getWidth()/2-200, 440, 400, 40, "New Game", true, true, "/textures/gui/button"));
        play = add(new MenuComponentButton(Display.getWidth()/4-100, Display.getHeight()-160, 200, 40, "Play", false, true, "/textures/gui/button"));
        rename = add(new MenuComponentButton(Display.getWidth()/2+200, Display.getHeight()-160, 200, 40, "Rename", true, true, "/textures/gui/button"));
        delete = add(new MenuComponentButton(Display.getWidth()/2+200, Display.getHeight()-80, 200, 40, "Delete", true, true, "/textures/gui/button"));
        saveList = add(new MenuComponentList(Display.getWidth()/2-200, 120+400, 420, Display.getHeight()-360-400));
        File file = new File(Main.getAppdataRoot()+"\\saves");
        if(!file.exists()){
            file.mkdirs();
        }
        String[] filepaths = file.list();
        for(int i = 0; i<filepaths.length; i++){
            saveList.add(new MenuComponentButton(0, 0, 400, 40, filepaths[i].replace(".dat", ""), true, true, "/textures/gui/button"));
        }
    }
    @Override
    public void renderBackground(){
        if(saveList.getSelectedIndex()>-1){
            play.enabled=true;
            rename.enabled=true;
            delete.enabled=true;
        }else{
            play.enabled=false;
            rename.enabled=false;
            delete.enabled=false;
        }
        for(MenuComponent c : saveList.components){
            if(saveList.getSelectedIndex()<0||saveList.getSelectedIndex()>saveList.components.size()-1){
                break;
            }
            if(c instanceof MenuComponentButton){
                MenuComponentButton b = (MenuComponentButton)c;
                b.enabled = saveList.components.get(saveList.getSelectedIndex()) != b;
            }
        }
        back.x = Display.getWidth()/4-100;
        back.y = Display.getHeight()-80;
        delete.x = (Display.getWidth()-Display.getWidth()/4)-100;
        delete.y = back.y;
        play.x = back.x;
        play.y = back.y-80;
        rename.x = delete.x;
        rename.y = delete.y-80;
        newSave.x = Display.getWidth()/2-200;
        newSave.y = 40+300;
        saveList.x = newSave.x;
        saveList.y = newSave.y+80;
        saveList.height = (play.y-80)-(newSave.y+80);
        Core.renderMenuBackground(false);
    }
    @Override
    public void tick(){
        Core.tickMenuBackground(false);
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            gui.open(parent);
        }
        if(button==newSave){
            newGame();
        }
        if(button==delete&&saveList.components.size()>0){
            delete((MenuComponentButton)saveList.components.get(saveList.getSelectedIndex()));
        }
        if(button==rename&&saveList.components.size()>0){
            rename((MenuComponentButton)saveList.components.get(saveList.getSelectedIndex()));
        }
        if(button==play&&saveList.components.size()>0){
            play((MenuComponentButton)saveList.components.get(saveList.getSelectedIndex()));
        }
    }
    private void newGame(){
        gui.open(new MenuNewGame(gui, this));
    }
    private void delete(MenuComponentButton save){
        Core.deleteSave(save.label);
        gui.open(new MenuChooseGame(gui, parent));
    }
    private void rename(MenuComponentButton save){
        gui.open(new MenuRename(gui, this, save));
    }
    private void play(MenuComponentButton save){
        Galaxy galaxy = Core.load(save.label);
        gui.open(new MenuSurface(gui, this, galaxy, galaxy.planet));
    }
}