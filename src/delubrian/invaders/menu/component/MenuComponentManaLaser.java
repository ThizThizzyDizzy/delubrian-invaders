package delubrian.invaders.menu.component;
import delubrian.invaders.menu.MenuBattle;
import delubrian.invaders.planet.Block;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentManaLaser extends MenuComponent{
    public double laserLength = 0;
    private final Block block;
    private final int[] location;
    public MenuComponentManaLaser(Block block, int[] location){
        super(0, 0, 0, 0);
        this.block = block;
        this.location = location;
    }
    @Override
    public void tick(){
        laserLength++;
        MenuBattle battle = (MenuBattle) parent;
        double shipWidth = battle.getShip().width;
        double shipX = battle.getShip().x;
        double shipY = battle.getShip().y;
        double blockSize = shipWidth/battle.getShip().getShip().width();
        double laserBlockX = location[0]*blockSize;
        double laserBlockY = location[1]*blockSize;
        double laserSize = block.damage;
        x = shipX+laserBlockX+blockSize/2-laserSize/2;
        y = shipY+laserBlockY+blockSize/2-laserSize/2-laserLength;
        width = laserSize;
        height = laserLength;
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glColor4d(0.5, 0, 1, 1);
        drawRect(x, y, x+width, y+height, 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    public double damage(){
        return block.damage*10;
    }
}