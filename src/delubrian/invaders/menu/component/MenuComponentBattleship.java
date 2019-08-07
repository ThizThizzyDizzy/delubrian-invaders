package delubrian.invaders.menu.component;
import delubrian.invaders.Controls;
import delubrian.invaders.Core;
import delubrian.invaders.menu.MenuBattle;
import delubrian.invaders.planet.Block;
import delubrian.invaders.planet.Ship;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
public class MenuComponentBattleship extends MenuComponentShip{
    public MenuComponentBattleship(Ship ship){
        super(ship);
        if(ship.cargoToEject==null){
            ship.cargoToEject = new ArrayList<>();
        }
    }
    @Override
    public void tick(){
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
    public void laserHurt(double damage, double x, int z){
        if(x>width)return;
        int X = (int) x/blockSize();
        ArrayList<Block> line = new ArrayList<>();
        Block[][] slice = ship.blocks[Math.min(X,ship.blocks.length-1)];
        for(int Y = 0; Y<ship.length(); Y++){
            line.add(slice[Y][z]);
        }
        for(Block block : line){
            if(block==null)continue;
            damage = block.damage(damage);
        }
    }
    public void laserHurt(double damage, double x){
        laserHurt(damage, x, Core.rand.nextInt(ship.height()));
    }
}