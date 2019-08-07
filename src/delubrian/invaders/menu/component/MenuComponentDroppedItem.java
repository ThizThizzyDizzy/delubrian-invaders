package delubrian.invaders.menu.component;
import delubrian.invaders.Core;
import delubrian.invaders.Item;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.Renderer2D;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentDroppedItem extends MenuComponent{
    public final Item item;
    private final double xSpeed;
    private final double ySpeed;
    private double rot;
    private final double rotSpeed;
    public double health = 40;
    public MenuComponentDroppedItem(double x, double y, Item item, double xSpeed, double ySpeed){
        this(x,y,item,xSpeed,ySpeed,Core.rand.nextGaussian());
    }
    public MenuComponentDroppedItem(double x, double y, Item item, double xSpeed, double ySpeed, double rotSpeed){
        super(x, y, 25, 25);
        this.item = item;
        xSpeed+=(Core.rand.nextDouble()-.5);
        ySpeed+=(Core.rand.nextDouble()-.5);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.rotSpeed = rotSpeed;
    }
    @Override
    public void tick(){
        y+=ySpeed;
        x+=xSpeed;
        rot+=rotSpeed;
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glPushMatrix();
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glRotated(rot, 0, 0, 1);
        drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture("/textures/items/"+item.toString()+".png"));
        GL11.glPopMatrix();
    }
    public void hurt(double damage){
        health-=damage;
    }
    public boolean isHit(MenuComponentManaBolt bolt){
        return Renderer2D.isClickWithinBounds(bolt.x+bolt.width/2, bolt.y+bolt.height/2, x, y, x+width, y+height);
    }
    public boolean isHit(MenuComponentManaLaser laser){
        return Renderer2D.isClickWithinBounds(laser.x+laser.width/2, laser.y+laser.width/2, x, y, x+width, y+height);
    }
    public boolean isHit(MenuComponentLaser laser){
        return Renderer2D.isClickWithinBounds(laser.x+laser.width/2, laser.y+laser.height-laser.width/2, x, y, x+width, y+height);
    }
}