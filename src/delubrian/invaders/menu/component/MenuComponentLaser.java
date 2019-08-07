package delubrian.invaders.menu.component;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentLaser extends MenuComponent{
    public double laserLength = 0;
    private final double X;
    private final double Y;
    private final int damage;
    public MenuComponentLaser(double x, double y, int damage){
        super(x, y, 0, 0);
        X = x;
        Y = y;
        this.damage = damage;
    }
    @Override
    public void tick(){
        laserLength++;
        double laserSize = damage;
        x = X-laserSize/2;
        y = Y-laserSize/2;
        width = laserSize;
        height = laserLength;
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glColor4d(1, 0, 0, 1);
        drawRect(x, y, x+width, y+height, 0/*ImageStash.instance.getTexture("/textures/manaLaser.png")*/);
        GL11.glColor4d(1, 1, 1, 1);
    }
    public double damage(){
        return damage;
    }
}