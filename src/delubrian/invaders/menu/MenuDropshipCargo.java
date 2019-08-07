package delubrian.invaders.menu;
import delubrian.invaders.ItemStack;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.menu.component.MenuComponentDropship;
import delubrian.invaders.menu.component.MenuComponentItem;
import delubrian.invaders.menu.component.MenuComponentTruck;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentMulticolumnList;
public class MenuDropshipCargo extends Menu{
    private final MenuComponentMulticolumnList list;
    private final MenuComponentButton done;
    private final MenuComponentButton toShip;
    private final Galaxy galaxy;
    private final MenuComponentDropship dropship;
    public MenuDropshipCargo(GUI gui, Menu parent, Galaxy galaxy, MenuComponentDropship dropship){
        super(gui, parent);
        list = add(new MenuComponentMulticolumnList(0, 0, Display.getWidth(), Display.getHeight()-50, 200, 50, 25, false));
        for(ItemStack stack : dropship.cargo){
            list.add(new MenuComponentItem(stack));
        }
        done = add(new MenuComponentButton(Display.getWidth()/2-225, Display.getHeight()-50, 200, 50, "Done", true, true, "/textures/gui/button"));
        toShip = add(new MenuComponentButton(Display.getWidth()/2+25, Display.getHeight()-50, 200, 50, "Return to Ship", true, true, "/textures/gui/button"));
        this.galaxy = galaxy;
        this.dropship = dropship;
    }
    @Override
    public void renderBackground(){
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/textures/gui/background/cargo.png"));
        int amount = 0;
        for(MenuComponent component : list.components){
            MenuComponentItem item = (MenuComponentItem) component;
            amount += item.selectedAmount;
        }
        if(dropship==null){
            return;
        }
        done.enabled = amount<=MenuComponentTruck.capacity;
    }
    @Override
    public void buttonClicked(MenuComponentButton button) {
        if(button==done){
            dropship.removeCargo(getSelectedCargo());
            gui.open(parent);
            if(!getSelectedCargo().isEmpty()){
                parent.add(new MenuComponentTruck(dropship.x, dropship.y, getSelectedCargo()));
            }
        }
        if(button==toShip){
            dropship.ship.addCargo(dropship.cargo);
            dropship.cargo.clear();
            MenuSurface surface = (MenuSurface) parent;
            surface.planet.dropship = null;
            gui.open(new MenuStarSystem(gui, this, galaxy, surface.planet, dropship.ship));
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