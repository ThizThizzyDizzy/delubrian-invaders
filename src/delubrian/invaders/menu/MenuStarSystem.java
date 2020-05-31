package delubrian.invaders.menu;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.Core;
import delubrian.invaders.menu.component.MenuComponentPlanet;
import delubrian.invaders.menu.component.MenuComponentShip;
import delubrian.invaders.menu.component.MenuComponentShipStats;
import delubrian.invaders.menu.component.MenuComponentStar;
import delubrian.invaders.planet.AsteroidBelt;
import delubrian.invaders.planet.BlockType;
import delubrian.invaders.planet.Planet;
import delubrian.invaders.planet.Ship;
import delubrian.invaders.planet.Star;
import delubrian.invaders.planet.StarSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuStarSystem extends Menu{
    private final MenuComponentShip ship;
    private final MenuComponentButton land;
    private final StarSystem system;
    public final ArrayList<MenuComponentPlanet> planets;
    public final ArrayList<MenuComponentStar> stars;
    private MenuComponentPlanet selectedPlanet;
    private final MenuComponentButton dropship;
    private final MenuComponentButton asteroids;
    private AsteroidBelt selectedAsteroidBelt;
    private ArrayList<int[]> backgroundStars = new ArrayList<>();
    private Random starRand = new Random();
    private int starCount = 25000;
    private final Galaxy galaxy;
    public MenuStarSystem(GUI gui, Menu parent, Galaxy galaxy, Planet planet, Ship ship){
        super(gui, parent);
        Core.helper.setBackground(Color.white);
        system = planet.parent;
        planets = new ArrayList<>(system.planets.length);
        stars = new ArrayList<>(system.stars.length);
        for(Planet p : system.planets){
            planets.add(add(new MenuComponentPlanet(p)));
        }
        for(Star s : system.stars){
            stars.add(add(new MenuComponentStar(s)));
        }
        this.ship = add(new MenuComponentShip(ship, getPlanetComponent(planet)));
        land = add(new MenuComponentButton(0, Display.getHeight()-50, 200, 50, "Land Ship", true, true, "/textures/gui/button"));
        dropship = add(new MenuComponentButton(0, Display.getHeight()-100, 200, 50, "Land Dropship", ship.containsBlock(BlockType.HANGAR), true, "/textures/gui/button"));
        asteroids = add(new MenuComponentButton(0, Display.getHeight()-150, 200, 50, "Fly Along Asteroids", true, true, "/textures/gui/button"));
        add(new MenuComponentShipStats(ship, 250, true));
        land.textInset = 10;
        dropship.textInset = 15;
        asteroids.textInset = 16;
        int minX = 0;
        int minY = 0;
        int maxX = Display.getWidth()+50;
        int maxY = Display.getHeight();
        for(int i = 0; i<starCount; i++){
            backgroundStars.add(new int[]{starRand.nextInt(maxX-minX)+minX, 
                                starRand.nextInt((maxY-minY)*2)+minY,
                                (starRand.nextInt(100)),
                                (starRand.nextInt(20)+80),
                                (starRand.nextInt(20)+80),
                                (starRand.nextInt(20)+80)});
        }
        this.galaxy = galaxy;
    }
    public void snapToPlanet(Planet planet){
        ship.planet = getPlanetComponent(planet);
        ship.moved = false;
    }
    private MenuComponentPlanet getPlanetComponent(Planet planet){
        for(MenuComponentPlanet p : planets){
            if(p.planet==planet){
                return p;
            }
        }
        return null;
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==land){
            gui.open(new MenuLandShip(gui, this, galaxy, selectedPlanet.planet, ship.getShip()));
        }
        if(button==dropship){
            gui.open(new MenuSelectCargo(gui, this, galaxy, selectedPlanet.planet, ship.getShip()));
        }
        if(button==asteroids){
            gui.open(new MenuAsteroids(gui, this, selectedAsteroidBelt, ship.getShip()));
        }
    }
    @Override
    public void tick(){
        selectedPlanet = null;
        ship.tick();
        asteroids.enabled = false;
        for(MenuComponentPlanet planet : planets){
            planet.tick();
            if(planet.planet instanceof AsteroidBelt){
                double min = Core.distance(planet, Display.getWidth()/2, Display.getHeight()/2)-planet.planet.size/2;
                double max = Core.distance(planet, Display.getWidth()/2, Display.getHeight()/2)+planet.planet.size/2;
                if(Core.distance(ship, Display.getWidth()/2, Display.getHeight()/2)>=min&&Core.distance(ship, Display.getWidth()/2, Display.getHeight()/2)<=max){
                    selectedAsteroidBelt = (AsteroidBelt) planet.planet;
                    asteroids.enabled = true;
                }
                continue;
            }
            if(Core.distance(ship, planet)<=planet.planet.size/2.5){
                selectedPlanet = planet;
            }
        }
        for(MenuComponentStar star : stars){
            star.tick();
        }
    }
    @Override
    public void renderBackground(){
        land.enabled = selectedPlanet!=null;
        dropship.enabled = selectedPlanet!=null&&ship.getShip().containsBlock(BlockType.HANGAR);
        int minX = 0;
        int minY = 0;
        int maxX = Display.getWidth();
        int maxY = Display.getHeight();
        GL11.glColor4f(0, 0, 0, 1);//black
        drawRect(minX, minY, maxX, maxY, 0);
        GL11.glColor4f(1, 1, 1, 1);//white
        GL11.glBegin(GL11.GL_POINTS);
        for(int[] star : backgroundStars){
            GL11.glColor4d(star[3]/100d, star[4]/100d, star[5]/100d, star[2]/100D);
            GL11.glVertex2i(star[0], star[1]);//Draw the stars!
            GL11.glColor4d(1, 1, 1, 1);
        }
        GL11.glEnd();
    }
}