package delubrian.invaders.menu.component;
import delubrian.invaders.Core;
import delubrian.invaders.ItemStack;
import delubrian.invaders.menu.MenuBattle;
import delubrian.invaders.planet.Block;
import delubrian.invaders.planet.BlockType;
import delubrian.invaders.planet.Ship;
import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentAutoship extends MenuComponentBattleship{
    public static int i = 1;
    public MenuComponentEnemy enemy;
    //tactics
    private static final int TACTIC_NONE = 0;
    private static final int TACTIC_STRAFING = 1;
    public boolean strafingRight;
    public int strafingTime = 0;
    public boolean strafe = true;
    public int tactic = TACTIC_STRAFING;
    //priority settings
    public boolean itemsBeforeAsteroids = false;
    public boolean collectItemsWhileFighting = true;
    public MenuComponentAutoship(Ship ship){
        super(ship);
        if(ship.cargoToEject==null){
            ship.cargoToEject = new ArrayList<>();
        }
    }
    public MenuComponentAutoship(){
        this(new Ship("Autoship "+i,new Block[Core.rand.nextInt(3)+4][Core.rand.nextInt(3)+4][Core.rand.nextInt(3)+4]));
        for(int I = 0; I<(ship.blocks.length+ship.blocks[0].length+ship.blocks[0][0].length)/6; I++){
            int X = Core.rand.nextInt(ship.blocks.length);
            int Y = Core.rand.nextInt(ship.blocks[0].length);
            int Z = Core.rand.nextInt(ship.blocks[0][0].length);
            ship.blocks[X][Y][Z] = new Block(BlockType.CORE, BlockType.CORE.health.length-1);
        }
        i++;
    }
    @Override
    public void tick(){
        ship.tick(this);
        ship.movingRight = ship.movingLeft = ship.movingUp = ship.movingDown = ship.firing = false;
        int X = Core.rand.nextInt(ship.blocks.length);
        int Y = Core.rand.nextInt(ship.blocks[0].length);
        int Z = Core.rand.nextInt(ship.blocks[0][0].length);
        if(ship.blocks[X][Y][Z]==null){
            ship.blocks[X][Y][Z] = Block.randomBlock();
            ship.blocks[X][Y][Z].fuelStorage = ship.blocks[X][Y][Z].type.fuelStorage[ship.blocks[X][Y][Z].level];
        }
        ArrayList<MenuComponentAsteroid> asteroids = new ArrayList<>();
        ArrayList<MenuComponentEnemy> enemies = new ArrayList<>();
        for(MenuComponentEnemy enemy : ((MenuBattle)parent).enemies){
            if(enemy.y+enemy.height*(3/4D)>Display.getHeight())continue;
            if(enemy instanceof MenuComponentAsteroid){
                asteroids.add((MenuComponentAsteroid) enemy);
            }else{
                enemies.add(enemy);
            }
        }
        asteroids.sort(new Comparator<MenuComponentAsteroid>(){
                @Override
                public int compare(MenuComponentAsteroid o1, MenuComponentAsteroid o2) {
                    return (int) ((o2.y+o2.height)-(o1.y+o1.height));
                }
            });
        enemies.sort(new Comparator<MenuComponentEnemy>(){
                @Override
                public int compare(MenuComponentEnemy o1, MenuComponentEnemy o2) {
                    return (int) ((o2.y+o2.height)-(o1.y+o1.height));
                }
            });
        if(enemies.isEmpty()){
            if(!asteroids.isEmpty()){
                enemy = asteroids.get(0);
            }
        }else{
            enemy = enemies.get(0);
        }
        if(enemy!=null){
            if(enemy instanceof MenuComponentAsteroid||width>enemy.width){
                tactic = TACTIC_NONE;
            }else{
                tactic = TACTIC_STRAFING;
            }
            enemy.targeted = true;
            if(enemy.health<=0)enemy = null;
        }
        if(ship.cargoSpace()<=0){
            for(ItemStack stack : ship.getCargo()){
                ship.ejectAllCargo(stack.item);
            }
        }
        if(enemy!=null){
            switch(tactic){
                case TACTIC_NONE:
                    if(belowEnemy(this)){
                        ship.firing = true;
                        if(collectItemsWhileFighting){
                            for(MenuComponentDroppedItem item : ((MenuBattle)parent).droppedItems){
                                if(!belowEnemy(item))continue;
                                if(x<item.x){
                                    ship.movingRight = true;
                                }
                                if(x+width>item.x+item.width){
                                    ship.movingLeft = true;
                                }
                                if(y<item.y){
                                    ship.movingDown = true;
                                }
                                if(y>item.y){
                                    ship.movingUp = true;
                                }
                                break;
                            }
                        }
                    }
                    if(x+width/2>enemy.x+enemy.width/2+enemy.width/8){
                        ship.movingLeft = true;
                    }
                    if(x+width/2<enemy.x+enemy.width/2-enemy.width/8){
                        ship.movingRight = true;
                    }
                    if(y<enemy.y+enemy.height){
                        ship.movingDown = true;
                    }
                    break;
                case TACTIC_STRAFING:
                    if(belowEnemy(this)){
                        ship.firing = true;
                        if(collectItemsWhileFighting){
                            for(MenuComponentDroppedItem item : ((MenuBattle)parent).droppedItems){
                                if(!belowEnemy(item))continue;
                                if(y<item.y){
                                    ship.movingDown = true;
                                }
                                if(y>item.y){
                                    ship.movingUp = true;
                                }
                                break;
                            }
                        }
                        strafe = !strafe;
                        if(strafe){
                            ship.movingRight = strafingRight;
                            ship.movingLeft = !strafingRight;
                            strafingTime++;
                            if(strafingTime>20*4){
                                strafingRight = !strafingRight;
                            }
                        }
                    }else{
                        strafingTime = 0;
                        strafingRight = !strafingRight;
                        if(x+width/2>enemy.x+enemy.width/2+enemy.width/8){
                            ship.movingLeft = true;
                        }
                        if(x+width/2<enemy.x+enemy.width/2-enemy.width/8){
                            ship.movingRight = true;
                        }
                    }
                    if(y<enemy.y+enemy.height){
                        ship.movingDown = true;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Tactic ID "+tactic+" is not handled properly!");
            }
        }
        if(enemies.isEmpty()&&(itemsBeforeAsteroids||asteroids.isEmpty())){
            for(MenuComponentDroppedItem item : ((MenuBattle)parent).droppedItems){
                ship.movingRight = x<item.x;
                ship.movingLeft = x+width>item.x+item.width;
                ship.movingDown = y<item.y;
                ship.movingUp = y+height>item.y+item.height;
                break;
            }
        }
        if(ship.movingLeft&&ship.movingRight){
            ship.movingLeft = ship.movingRight = false;
        }
        if(ship.movingUp&&ship.movingDown){
            ship.movingUp = ship.movingDown = false;
        }
        if(x<=1){
            ship.movingLeft = false;
        }
        if(x-width>=Display.getWidth()-1){
            ship.movingRight = false;
        }
        if(y-height>=Display.getHeight()-1){
            ship.movingDown = false;
        }
        if(y<=1){
            ship.movingUp = false;
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
    public boolean belowEnemy(MenuComponent c){
        if(enemy==null)return false;
        return c.x+width/2>enemy.x&&c.x+width/2<enemy.x+enemy.width&&c.y>enemy.y;
    }
}