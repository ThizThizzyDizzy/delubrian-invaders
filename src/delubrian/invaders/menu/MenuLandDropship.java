package delubrian.invaders.menu;
import delubrian.invaders.ItemStack;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.menu.component.MenuComponentBuilding;
import delubrian.invaders.menu.component.MenuComponentDropship;
import delubrian.invaders.menu.component.MenuComponentPlot;
import delubrian.invaders.planet.Building;
import delubrian.invaders.planet.Planet;
import delubrian.invaders.planet.Ship;
import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibraryextended.opengl.gui.LayoutMenu;
public class MenuLandDropship extends LayoutMenu{
    private final Galaxy galaxy;
    private final Planet planet;
    private final MenuComponentButton exit;
    private final MenuComponentDropship dropship;
    public MenuLandDropship(GUI gui, Menu parent, Galaxy galaxy, Planet planet, Ship ship, ArrayList<ItemStack> cargo){
        super(gui, parent, 0, 0, 200, 50, 50, false, LayoutMenu.SNAP_LEFT, 25);
        this.galaxy = galaxy;
        this.planet = planet;
        for(int x = 0; x<planet.size; x++){
            THAT:for(int y = 0; y<planet.size; y++){
                for(Building budilding : planet.buildings){
                    if(isClickWithinBounds(x*50+25, y*50+25, budilding.x*50, budilding.y*50, (budilding.x+budilding.width)*50, (budilding.y+budilding.height)*50)){
                        addWithoutLayout(new MenuComponentPlot(planet,x*50,y*50,50,50, false));
                        continue THAT;
                    }
                }
                addWithoutLayout(new MenuComponentPlot(planet,x*50,y*50,50,50));
            }
        }
        for(Building building : planet.buildings){
            addWithoutLayout(building.getComponent(galaxy, building));
        }
        exit = addWithoutScroller(new MenuComponentButton(0, Display.getHeight()-50, 200, 50, "Back to System", true, true, "/textures/gui/button"));
        exit.textInset = 15;
        dropship = addWithoutScroller(new MenuComponentDropship(ship, planet, cargo));
    }
    @Override
    public void renderBackground(){}
    @Override
    public void render(int millisSinceLastTick){
        canLand();
        super.render(millisSinceLastTick);
        GL11.glColor4d(1, 0, 0, 0.5);
        if(canLand()){
            GL11.glColor4d(0, 1, 0, 0.5);
        }
        dropship.doRender();
        drawRect(dropship.x, dropship.y, dropship.x+dropship.width, dropship.y+dropship.height, 0);
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==exit){
            MenuStarSystem system = (MenuStarSystem) parent;
            system.snapToPlanet(planet);
            gui.open(parent);
        }
        if(!canLand()){
            return;
        }
        if(button instanceof MenuComponentPlot){
            MenuComponentPlot plot = (MenuComponentPlot) button;
            gui.open(new MenuSurface(gui, null, galaxy, planet, plot, dropship));
        }
    }
    private boolean canLand(){
        dropship.x = (Math.round((Mouse.getX()-25+(scroller.getHorizScroll()%50))/50D)-1/2)*50-scroller.getHorizScroll()%50;
        dropship.y = (Math.round((Display.getHeight()-Mouse.getY()-25+(scroller.getVertScroll()%50))/50D)-1/2)*50-scroller.getVertScroll()%50;
        for(MenuComponent component : scroller.components){
            if(component instanceof MenuComponentBuilding){
                MenuComponentButton button = (MenuComponentButton) component;
                if(button.isMouseOver){
                    return false;
                }
            }
        }
        return true;
    }
}