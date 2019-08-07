package delubrian.invaders.menu;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.menu.component.MenuComponentShipStats;
import delubrian.invaders.planet.Hangar;
import delubrian.invaders.planet.Planet;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibraryextended.opengl.gui.LayoutMenu;
public class MenuHangar extends LayoutMenu{
    private final MenuComponentButton back;
    private final MenuComponentButton launch;
    private final MenuComponentButton build;
    private final MenuComponentButton cargo;
    private final MenuComponentButton load;
    private final MenuComponentButton unload;
    private final Galaxy galaxy;
    final Planet planet;
    private final Hangar hangar;
    public MenuHangar(GUI gui, Menu parent, Galaxy galaxy, Planet planet, Hangar hangar){
        super(gui, parent, 0, 0, 200, 50, 50, false, LayoutMenu.SNAP_LEFT, 25);
        textInset = 12;
        back = addWithoutLayout(new MenuComponentButton(0, Display.getHeight()-50, 200, 50, "Back", true, true, "/textures/gui/button"));
        launch = addButton("Launch", hangar.hasShip()&&hangar.ship.canLaunch(), true, "/textures/gui/button");
        build = addButton(hangar.hasShip()?"Modify":"Construct",true, true, "/textures/gui/button");
        cargo = addButton("Move Cargo", hangar.hasShip()&&hangar.getCargoAmount()>0, true, "/textures/gui/button");
        load = addButton("Load Cargo", hangar.hasShip()&&hangar.getCargoAmount()>0, true, "/textures/gui/button");
        unload = addButton("Unload Cargo", hangar.hasShip()&&hangar.ship.getCargoAmount()>0, true, "/textures/gui/button");
        addWithoutLayout(new MenuComponentShipStats(hangar.ship, 250, false));
        this.galaxy = galaxy;
        this.planet = planet;
        this.hangar = hangar;
        if(planet.dropship!=null){
            launch.enabled = false;
        }
    }
    @Override
    public void onGUIOpened() {
        launch.enabled = hangar.hasShip()&&hangar.ship.canLaunch();
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            gui.open(parent);
        }
        if(button==build){
            gui.open(new MenuEditShip(gui, this, galaxy, hangar));
        }
        if(button==cargo){
            gui.open(new MenuSelectCargo(gui, this, galaxy, planet, hangar));
        }
        if(button==load){
            gui.open(new MenuLoadCargo(gui, this, hangar, false));
        }
        if(button==unload){
            gui.open(new MenuLoadCargo(gui, this, hangar, true));
        }
        if(button==launch){
            gui.open(new MenuStarSystem(gui, parent, galaxy, planet, hangar.ship));
            hangar.ship = null;
        }
    }
    @Override
    public void renderBackground(){
        back.y = Display.getHeight()-50;
        cargo.enabled = hangar.getCargoAmount()>0;
        load.enabled = hangar.getCargoAmount()>0;
        if(hangar.hasShip()){
            unload.enabled = hangar.ship.getCargoAmount()>0;
        }else{
            unload.enabled = false;
            load.enabled = false;
        }
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/textures/gui/background/hangar.png"));
    }
}