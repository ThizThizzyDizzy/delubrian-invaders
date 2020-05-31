package delubrian.invaders.menu;
import delubrian.invaders.Dropship;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.Core;
import delubrian.invaders.menu.component.MenuComponentDropship;
import delubrian.invaders.menu.component.MenuComponentFactory;
import delubrian.invaders.menu.component.MenuComponentHangar;
import delubrian.invaders.menu.component.MenuComponentMine;
import delubrian.invaders.menu.component.MenuComponentParkedDropship;
import delubrian.invaders.menu.component.MenuComponentPlot;
import delubrian.invaders.menu.component.MenuComponentRefinery;
import delubrian.invaders.menu.component.MenuComponentTruck;
import delubrian.invaders.menu.component.MenuComponentWarehouse;
import delubrian.invaders.planet.Building;
import delubrian.invaders.planet.Planet;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibraryextended.opengl.gui.LayoutMenu;
public class MenuSurface extends LayoutMenu{
    private final Galaxy galaxy;
    public final Planet planet;
    private final MenuComponentButton exit;
    private final MenuComponentButton edit;
    private MenuComponentFactoryGUI factoryGUI;
    private MenuComponentRefineryGUI refineryGUI;
    public MenuSurface(GUI gui, Menu parent, Galaxy galaxy, Planet planet){
        super(gui, parent, 0, 0, 200, 50, 50, false, LayoutMenu.SNAP_LEFT, 25);
        if(galaxy==null)galaxy = planet.parent.parent;
        this.galaxy = galaxy;
        this.planet = planet;
        for(int x = 0; x<planet.size; x++){
            for(int y = 0; y<planet.size; y++){
                addWithoutLayout(new MenuComponentPlot(planet,x*50,y*50,50,50));
            }
        }
        for(Building building : planet.buildings){
            addWithoutLayout(building.getComponent(galaxy, building));
        }
        exit = addWithoutScroller(new MenuComponentButton(0, Display.getHeight()-50, 200, 50, "Exit to Menu", true, true, "/textures/gui/button"));
        edit = addWithoutScroller(new MenuComponentButton(0, Display.getHeight()-100, 200, 50, "Edit", true, true, "/textures/gui/button"));
        exit.textInset += 14;
        if(planet.dropship!=null){
            MenuComponentParkedDropship ship = new MenuComponentParkedDropship(planet.dropship.x*50, planet.dropship.y*50, planet.dropship);
            addWithoutLayout(ship);
            planet.dropship = new Dropship(ship);
        }
        factoryGUI = addWithoutLayout(new MenuComponentFactoryGUI(galaxy));
        refineryGUI = addWithoutLayout(new MenuComponentRefineryGUI(galaxy));
    }
    public MenuSurface(GUI gui, Menu parent, Galaxy galaxy, Planet planet, MenuComponentPlot plot, MenuComponentDropship dropship){
        this(gui, parent, galaxy, planet);
        MenuComponentParkedDropship ship = new MenuComponentParkedDropship(plot.x, plot.y, dropship);
        addWithoutLayout(ship);
        planet.dropship = new Dropship(ship);
    }
    @Override
    public void onGUIClosed(){
        for(MenuComponent c : components){
            if(c instanceof MenuComponentTruck){
                for(MenuComponent component : scroller.components){
                    if(component instanceof MenuComponentWarehouse){
                        ((MenuComponentWarehouse)component).getWarehouse().addCargo(((MenuComponentTruck)c).cargo);
                    }
                }
            }
        }
    }
    @Override
    public void renderBackground(){
        if(factoryGUI.factory!=null||refineryGUI.refinery!=null){
            exit.y = edit.y = Display.getHeight();
        }else{
            exit.y = Display.getHeight()-exit.height;
            edit.y = exit.y-edit.height;
        }
    }
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        factoryGUI.renderForeground();
        refineryGUI.renderForeground();
    }
    @Override
    public void tick(){
        scroller.parent = this;
        for(MenuComponent component : scroller.components){
            if(component instanceof MenuComponentFactory){
                component.parent.parent = this;
            }
            component.tick();
        }
        super.tick();
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(factoryGUI.factory!=null||refineryGUI.refinery!=null){
            return;
        }
        if(button==exit){
            galaxy.system = planet.parent;
            galaxy.planet = planet;
            Core.save(galaxy);
            gui.open(new MenuMain(gui, parent));
        }
        if(button==edit){
            gui.open(new MenuEditSurface(gui, parent, galaxy, planet));
        }
        if(button instanceof MenuComponentHangar){
            MenuComponentHangar hangar = (MenuComponentHangar) button;
            gui.open(new MenuHangar(gui, this, galaxy, planet, hangar.getHangar()));
        }
        if(button instanceof MenuComponentParkedDropship){
            MenuComponentParkedDropship dropship = (MenuComponentParkedDropship) button;
            dropship.dropship.ship.addCargo(dropship.dropship.cargo);
            dropship.dropship.cargo.clear();
            planet.dropship = null;
            gui.open(new MenuStarSystem(gui, this, galaxy, planet, dropship.dropship.ship));
        }
        if(button instanceof MenuComponentFactory){
            factoryGUI.factory = (MenuComponentFactory) button;
            factoryGUI.refreshAll();
            lockScroll();
        }
        if(button instanceof MenuComponentRefinery){
            refineryGUI.refinery = (MenuComponentRefinery) button;
            refineryGUI.refreshAll();
            lockScroll();
        }
        if(button instanceof MenuComponentMine){
            gui.open(new MenuSelectCargo(gui, parent, galaxy, planet, ((MenuComponentMine)button).getMine()));
        }
        if(button instanceof MenuComponentWarehouse){
            gui.open(new MenuSelectCargo(gui, parent, galaxy, planet, ((MenuComponentWarehouse)button).getWarehouse()));
        }
    }
}