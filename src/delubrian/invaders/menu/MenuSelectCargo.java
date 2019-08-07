package delubrian.invaders.menu;
import delubrian.invaders.ItemStack;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.menu.component.MenuComponentTruck;
import delubrian.invaders.menu.component.MenuComponentItem;
import delubrian.invaders.planet.Building;
import delubrian.invaders.planet.Planet;
import delubrian.invaders.planet.Ship;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentMulticolumnList;
public class MenuSelectCargo extends Menu{
    private final MenuComponentMulticolumnList list;
    private final MenuComponentButton done;
    private Galaxy galaxy;
    private final Planet planet;
    private final Ship ship;
    private Building building = null;
    public MenuSelectCargo(GUI gui, Menu parent, Galaxy galaxy, Planet planet, Ship ship){
        super(gui, parent);
        list = add(new MenuComponentMulticolumnList(0, 0, Display.getWidth(), Display.getHeight()-50, 200, 50, 25, false));
        for(ItemStack stack : ship.getCargo()){
            list.add(new MenuComponentItem(stack));
        }
        done = add(new MenuComponentButton(Display.getWidth()/2-100, Display.getHeight()-50, 200, 50, "Done", true, true, "/textures/gui/button"));
        this.galaxy = galaxy;
        this.planet = planet;
        this.ship = ship;
    }
    public MenuSelectCargo(GUI gui, Menu parent, Galaxy galaxy, Planet planet, Building b){
        super(gui, parent);
        list = add(new MenuComponentMulticolumnList(0, 0, Display.getWidth(), Display.getHeight()-50, 200, 50, 25, false));
        for(ItemStack stack : b.getCargo()){
            list.add(new MenuComponentItem(stack));
        }
        done = add(new MenuComponentButton(Display.getWidth()/2-100, Display.getHeight()-50, 200, 50, "Done", true, true, "/textures/gui/button"));
        this.galaxy = galaxy;
        this.planet = planet;
        ship = null;
        building = b;
    }
    @Override
    public void renderBackground(){
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/textures/gui/background/cargo.png"));
        int amount = 0;
        for(MenuComponent component : list.components){
            MenuComponentItem item = (MenuComponentItem) component;
            amount += item.selectedAmount;
        }
        done.enabled = amount<=MenuComponentTruck.capacity;
    }
    @Override
    public void buttonClicked(MenuComponentButton button) {
        if(button==done){
            if(ship==null){
                building.removeCargo(getSelectedCargo());
            }else{
                ship.removeCargo(getSelectedCargo());
            }
            if(building!=null){
                //TODO null pointer on next line sometimes.
                MenuSurface surface = new MenuSurface(gui, parent.parent, galaxy, planet);
                gui.open(surface);
                if(!getSelectedCargo().isEmpty()){
                    surface.addWithoutLayout(new MenuComponentTruck(building, getSelectedCargo()));
                }
                return;
            }
            gui.open(new MenuLandDropship(gui, parent, galaxy, planet, ship, getSelectedCargo()));
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