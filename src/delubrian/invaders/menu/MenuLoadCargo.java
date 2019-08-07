package delubrian.invaders.menu;
import delubrian.invaders.ItemStack;
import delubrian.invaders.menu.component.MenuComponentItem;
import delubrian.invaders.planet.Hangar;
import delubrian.invaders.planet.Ship;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentMulticolumnList;
public class MenuLoadCargo extends Menu{
    private final MenuComponentMulticolumnList list;
    private final MenuComponentButton done;
    private final Ship ship;
    private final Hangar hangar;
    private final boolean unload;
    public MenuLoadCargo(GUI gui, Menu parent, Hangar hangar, boolean unload){
        super(gui, parent);
        ship = hangar.ship;
        list = add(new MenuComponentMulticolumnList(0, 0, Display.getWidth(), Display.getHeight()-50, 200, 50, 25, false));
        if(unload){
            for(ItemStack stack : ship.getCargo()){
                list.add(new MenuComponentItem(stack));
            }
        }else{
            for(ItemStack stack : hangar.getCargo()){
                list.add(new MenuComponentItem(stack));
            }
        }
        done = add(new MenuComponentButton(Display.getWidth()/2-100, Display.getHeight()-50, 200, 50, "Done", true, true, "/textures/gui/button"));
        this.unload = unload;
        this.hangar = hangar;
    }
    @Override
    public void renderBackground(){
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/textures/gui/background/cargo.png"));
        int amount = 0;
        for(MenuComponent component : list.components){
            MenuComponentItem item = (MenuComponentItem) component;
            amount += item.selectedAmount;
        }
        if(hangar==null){
            return;
        }
        if(unload){
            done.enabled = amount<=hangar.cargoSpace();
        }else{
            done.enabled = amount<=hangar.ship.cargoSpace();
        }
    }
    @Override
    public void buttonClicked(MenuComponentButton button) {
        if(button==done){
            if(unload){
                hangar.addCargo(getSelectedCargo());
                ship.removeCargo(getSelectedCargo());
            }else{
                hangar.removeCargo(getSelectedCargo());
                ship.addCargo(getSelectedCargo());
            }
            gui.open(parent);
        }
    }
    private ArrayList<ItemStack> getSelectedCargo(){
        ArrayList<ItemStack> cargo = new ArrayList<>();
        for(MenuComponent component : list.components){
            MenuComponentItem item = (MenuComponentItem) component;
            cargo.add(new ItemStack(item.stack.item, item.selectedAmount));
        }
        return cargo;
    }
}