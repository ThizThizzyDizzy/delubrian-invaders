package delubrian.invaders.menu.component;
import delubrian.invaders.Controls;
import delubrian.invaders.menu.MenuBattle;
import delubrian.invaders.planet.Block;
import delubrian.invaders.planet.Planet;
import delubrian.invaders.planet.Ship;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentShip extends MenuComponent{
    final Ship ship;
    public MenuComponentPlanet planet;
    public boolean moved = false;
    private final Planet landing;
    public MenuComponentShip(Ship ship, MenuComponentPlanet planet){
        super(planet.x, planet.y, ship.width()*10, ship.length()*10);
        this.ship = ship;
        this.planet = planet;
        landing = null;
    }
    public MenuComponentShip(Ship ship, Planet planet){
        super(0, 0, ship.width()*50, ship.length()*50);
        this.ship = ship;
        landing = planet;
        this.planet = null;
    }
    public MenuComponentShip(Ship ship){
        super(Math.round(Display.getWidth()/2-ship.width()*12.5), Display.getHeight()-ship.height()*25, ship.width()*25, ship.length()*25);
        this.ship = ship;
        landing = null;
        planet = null;
        moved = true;
    }
    @Override
    public void tick(){
        if(landing!=null){
            return;
        }
        ship.tick(this);
        if(Keyboard.isKeyDown(Controls.up)){
            moved = true;
            ship.movingUp = true;
        }else{
            ship.movingUp = false;
        }
        if(Keyboard.isKeyDown(Controls.down)){
            moved = true;
            ship.movingDown = true;
        }else{
            ship.movingDown = false;
        }
        if(Keyboard.isKeyDown(Controls.left)){
            moved = true;
            ship.movingLeft = true;
        }else{
            ship.movingLeft = false;
        }
        if(Keyboard.isKeyDown(Controls.right)){
            moved = true;
            ship.movingRight = true;
        }else{
            ship.movingRight = false;
        }
        if(Keyboard.isKeyDown(Controls.fire)){
            if(parent instanceof MenuBattle){
                ship.firing = true;
            }
        }else{
            ship.firing = false;
        }
        for(Block bolt : ship.firingBolts){
            parent.add(new MenuComponentManaBolt(bolt, getBlockLocation(bolt)));
        }
        for(Block laser : ship.firingLasers){
            parent.add(new MenuComponentManaLaser(laser, getBlockLocation(laser)));
        }
        ship.firingBolts.clear();
        ship.firingLasers.clear();
    }
    @Override
    public void render(){
        removeRenderBound();
        if(landing!=null){
            return;
        }
        if(!moved){
            x = planet.x+planet.width/2-width/2;
            y = planet.y+planet.height/2-height/2;
        }
        for(int X = 0; X<ship.blocks.length; X++){
            for(int Y = 0; Y<ship.blocks[0].length; Y++){
                for(int Z = 0; Z<ship.blocks[0][0].length; Z++){
                    Block block = ship.blocks[X][Y][Z];
                    if(block==null)continue;
                    block.render(x+X*blockSize(), y+Y*blockSize(), x+X*blockSize()+blockSize(), y+Y*blockSize()+blockSize());
                }
            }
        }
    }
    public Ship getShip(){
        return ship;
    }
    public int blockSize() {
        return (int) (width/getShip().width());
    }
    public void landingRender(){
        for(int Z = 0; Z<ship.blocks.length; Z++){
            for(int Y = 0; Y<ship.blocks[0].length; Y++){
                for(int X = 0; X<ship.blocks[0][0].length; X++){
                    Block block = ship.blocks[X][Y][Z];
                    if(block==null)continue;
                    block.render(x+X*blockSize(), y+Y*blockSize(), x+X*blockSize()+blockSize(), y+Y*blockSize()+blockSize());
                }
            }
        }
    }
    int[] getBlockLocation(Block block){
        for(int Z = 0; Z<ship.blocks.length; Z++){
            for(int Y = 0; Y<ship.blocks[0].length; Y++){
                for(int X = 0; X<ship.blocks[0][0].length; X++){
                    if(block==ship.blocks[Z][Y][X]){
                        return new int[]{X,Y};
                    }
                }
            }
        }
        return new int[0];
    }
}