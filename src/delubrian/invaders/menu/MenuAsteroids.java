package delubrian.invaders.menu;
import delubrian.invaders.menu.component.MenuComponentAsteroid;
import delubrian.invaders.menu.component.MenuComponentDroppedItem;
import delubrian.invaders.menu.component.MenuComponentSmallFighter;
import delubrian.invaders.planet.AsteroidBelt;
import delubrian.invaders.planet.Mineral;
import delubrian.invaders.planet.Ship;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuAsteroids extends MenuBattle{
    private static final Random randoAsteroid = new Random();
    private static int backgroundAsteroidCount(){
        return randoAsteroid.nextInt(250)+150;
    }
    private final MenuComponentButton exit;
    int i=0,I = 0;
    public MenuAsteroids(GUI gui, Menu parent, AsteroidBelt belt, Ship ship){
        super(gui, parent, ship);
        super.belt = belt;
        exit = add(new MenuComponentButton(0, Display.getHeight()-50, 200, 50, "Exit Asteroid Belt", true, true, "/textures/gui/button"));
        exit.textInset = 15;
    }
    @Override
    public void tick(){
        super.tick();
        i++;
        if(randoAsteroid.nextDouble()<.0003125){
            add(new MenuComponentSmallFighter());
        }
        if(i>I){
            add(new MenuComponentAsteroid(false));
            I = (int) (1600+randoAsteroid.nextDouble()*800);
            i = 0;
        }
        for(MenuComponent component : backgroundComponents){
            component.tick();
            if(component.y>Display.getHeight()){
                MenuComponentAsteroid asteroid = (MenuComponentAsteroid) component;
                asteroid.shade = randoAsteroid.nextDouble()/2;
                asteroid.shade-=.5;
                asteroid.shade*=.75;
                asteroid.shade+=.5;
                component.y = -50;
                component.width = component.height = randoAsteroid.nextInt(50);
                component.x = randoAsteroid.nextInt((int)(Display.getWidth()-component.width));
            }
        }
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==exit){
            gui.open(parent);
        }
    }
    @Override
    public ArrayList<MenuComponent> setupBackgroundComponents(){
        ArrayList<MenuComponent> asteroids = new ArrayList<>();
        int backgroundAsteroidCount = backgroundAsteroidCount();
        for(int i = 0; i<backgroundAsteroidCount; i++){
            MenuComponentAsteroid asteroid = addComponent(new MenuComponentAsteroid(true));
            asteroid.shade = randoAsteroid.nextDouble()/2;
            asteroid.shade-=.5;
            asteroid.shade*=.75;
            asteroid.shade+=.5;
            asteroid.width = asteroid.height = randoAsteroid.nextInt(50);
            asteroid.x = randoAsteroid.nextInt((int)(Display.getWidth()+asteroid.width))-asteroid.width;
            asteroid.y = randoAsteroid.nextInt((int)(Display.getHeight()+asteroid.height))-asteroid.height;
            asteroids.add(asteroid);
        }
        return asteroids;
    }
    public void dropRandomItems(MenuComponentAsteroid asteroid){
        for(int i = 0; i<asteroid.width*asteroid.width*Math.PI/5000; i++){
            for(Mineral mineral : belt.minerals){
                if(randoAsteroid.nextDouble()<=mineral.chance){
                    add(new MenuComponentDroppedItem(asteroid.x+randoAsteroid.nextInt((int) asteroid.width), asteroid.y+randoAsteroid.nextInt((int) asteroid.width),mineral.mineral.toItem(), asteroid.getXSpeed(), asteroid.getYSpeed()));
                }
            }
        }
    }
    private <V extends MenuComponent> V addComponent(V component) {
        return super.add(component);
    }
}