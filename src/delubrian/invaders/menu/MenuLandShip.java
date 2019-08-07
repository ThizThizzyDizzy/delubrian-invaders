 package delubrian.invaders.menu;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.menu.component.MenuComponentBuilding;
import delubrian.invaders.menu.component.MenuComponentHangar;
import delubrian.invaders.menu.component.MenuComponentPlot;
import delubrian.invaders.menu.component.MenuComponentShip;
import delubrian.invaders.planet.Building;
import delubrian.invaders.planet.Planet;
import delubrian.invaders.planet.Ship;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibraryextended.opengl.gui.LayoutMenu;
public class MenuLandShip extends LayoutMenu{
    private final Galaxy galaxy;
    private final Planet planet;
    private final MenuComponentButton exit;
    private final Ship ship;
    private final MenuComponentShip landing;
    public MenuLandShip(GUI gui, Menu parent, Galaxy galaxy, Planet planet, Ship ship){
        super(gui, parent, 0, 0, 200, 50, 50, false, LayoutMenu.SNAP_LEFT, 25);
        this.galaxy = galaxy;
        this.planet = planet;
        for(int x = 0; x<planet.size; x++){
            THAT:for(int y = 0; y<planet.size; y++){
                for(Building building : planet.buildings){
                    if(isClickWithinBounds(x*50+25, y*50+25, building.x*50, building.y*50, (building.x+building.width)*50, (building.y+building.height)*50)){
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
        this.ship = ship;
        landing = addWithoutScroller(new MenuComponentShip(ship, planet));
    }
    @Override
    public void renderBackground(){}
    @Override
    public void render(int millisSinceLastTick){
        canLand();
        GL11.glColor4d(1, 1, 1, 1);
        super.render(millisSinceLastTick);
        landing.landingRender();
        GL11.glColor4d(1, 0, 0, 0.5);
        if(canLand()){
            GL11.glColor4d(0, 1, 0, 0.5);
        }
        drawRect(landing.x, landing.y, landing.x+landing.width, landing.y+landing.height, 0);
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
        if(button instanceof MenuComponentBuilding){
            MenuComponentHangar hangar = (MenuComponentHangar) button;
            hangar.getHangar().ship = ship;
            gui.open(new MenuSurface(gui, null, galaxy, planet));
        }
    }
    private boolean canLand(){
        landing.x = (Math.round((Mouse.getX()-25+(scroller.getHorizScroll()%50))/50D)-landing.getShip().width()/2)*50-scroller.getHorizScroll()%50;
        landing.y = (Math.round((Display.getHeight()-Mouse.getY()-25+(scroller.getVertScroll()%50))/50D)-landing.getShip().length()/2)*50-scroller.getVertScroll()%50;
        synchronized(scroller.components){
            for(MenuComponent component : scroller.components){
                if(component instanceof MenuComponentHangar){
                    MenuComponentHangar hangar = (MenuComponentHangar) component;
                    if(hangar.isMouseOver&&hangar.getHangar().canHold(ship)){
                        landing.x = hangar.x-scroller.getHorizScroll();
                        landing.y = hangar.y-scroller.getVertScroll();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}