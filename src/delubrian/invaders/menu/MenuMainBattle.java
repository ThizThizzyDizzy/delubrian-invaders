package delubrian.invaders.menu;
import delubrian.invaders.menu.component.MenuComponentDust;
import delubrian.invaders.Core;
import delubrian.invaders.menu.component.MenuComponentAsteroid;
import delubrian.invaders.menu.component.MenuComponentAutoship;
import delubrian.invaders.menu.component.MenuComponentSmallFighter;
import delubrian.invaders.planet.AsteroidBelt;
import delubrian.invaders.planet.Mineral;
import delubrian.invaders.planet.StarSystem;
import java.util.ArrayList;
import java.awt.Color;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuMainBattle extends MenuBattle{
    int randomEnemyTimer = 0;
    public MenuMainBattle(){
        super(Core.gui, null, new MenuComponentAutoship(), false);
        belt = new AsteroidBelt("Main Menu", new StarSystem(null, "Main Menu", 1), -1, Mineral.randomMinerals());
    }
    @Override
    public ArrayList<MenuComponent> setupBackgroundComponents(){
        ArrayList<MenuComponent> comps = new ArrayList<>();
        return comps;
    }
    @Override
    public void tick(){
        super.tick();
        if(ship.getShip().dead){
            components.remove(ship);
            ship = add(new MenuComponentAutoship());
        }
        randomEnemyTimer--;
        if(randomEnemyTimer<=0){
            addRandomEnemy();
            randomEnemyTimer = Core.rand.nextInt(600)+300;
        }
    }
    @Override
    public void renderBackground(){
    }
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
    }
    private void addRandomEnemy(){
        int enemyTypes = 2;
        int enemy = Core.rand.nextInt(enemyTypes);
        switch(enemy){
            case 0:
                add(new MenuComponentAsteroid(false));
                break;
            case 1:
                add(new MenuComponentSmallFighter());
                break;
        }
    }
    @Override
    public void explodeBlock(int[] coords){
        MenuComponentDust dust = new MenuComponentDust(ship.x+(coords[0]+.5)*ship.blockSize(), ship.y+(coords[1]+.5)*ship.blockSize(), ship.blockSize()*3);
        dust.color = new Color(.5f, 0, 1);
        add(dust);
    }
}