package delubrian.invaders.menu.component;
import delubrian.invaders.menu.MenuSelectCargo;
import delubrian.invaders.menu.MenuSurface;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.planet.Mine;
import delubrian.invaders.planet.Mineral;
import java.util.Random;
public class MenuComponentMine extends MenuComponentBuilding{
    private final Mine mine;
    int tick = 0;
    private Random randoMineral = new Random();
    private Galaxy galaxy;
    private double mineDelay = 2000;//1x1 mine takes X ticks to mine each item.
    public MenuComponentMine(Mine mine){
        super(mine, "mine");
        this.mine = mine;
    }
    @Override
    public void tick(){
        tick++;
        while(tick>(mineDelay/(mine.length*mine.width))&&active){
            tick-=(mineDelay/(mine.length*mine.width));
            MenuSurface surface = (MenuSurface) parent.parent;
            addRandomItems(surface.planet.minerals);
        }
    }
    public void addRandomItems(Mineral[] minerals){
        for(Mineral mineral : minerals){
            if(randoMineral.nextDouble()<=mineral.chance){
                if(mine.canHoldCargo(1)){
                    mine.addCargo(mineral.mineral.toItem());
                }
            }
        }
    }
    public Mine getMine() {
        return mine;
    }
}