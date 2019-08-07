package delubrian.invaders.menu.component;
import delubrian.invaders.menu.MenuBattle;
import delubrian.invaders.planet.Block;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentManaBolt extends MenuComponent{
    private final Block block;
    private final int[] location;
    private boolean first = true;
    public MenuComponentManaBolt(Block block, int[] location){
        super(0, 0, 0, 0);
        this.block = block;
        this.location = location;
    }
    @Override
    public void tick(){
        MenuBattle battle = (MenuBattle) parent;
        if(first){
            double shipWidth = battle.getShip().width;
            double shipX = battle.getShip().x;
            double shipY = battle.getShip().y;
            double blockSize = shipWidth/battle.getShip().getShip().width();
            double boltBlockX = location[0]*blockSize;
            double boltBlockY = location[1]*blockSize;
            double boltSize = block.damage;
            x = shipX+boltBlockX+blockSize/2-boltSize/2;
            y = shipY+boltBlockY+blockSize/2-boltSize/2;
            width = height = boltSize;
            first = false;
        }
        y -= 5;
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glColor4d(0.5, 0, 1, 1);
        drawRect(x, y, x+width, y+height, 0/*ImageStash.instance.getTexture("/textures/manaBolt.png")*/);
        GL11.glColor4d(1, 1, 1, 1);
    }
    public double damage(){
        return block.damage*10;
    }
}