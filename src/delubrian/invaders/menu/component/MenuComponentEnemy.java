package delubrian.invaders.menu.component;
import delubrian.invaders.Item;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public abstract class MenuComponentEnemy extends MenuComponent{
    private final String texture;
    public int health;
    public int maxHealth;
    public double rot = 0;
    public double rotSpeed = 0;
    public double xSpeed = 0;
    public double ySpeed = 0;
    public boolean targeted = false;
    private static final boolean showTarget = false;
    public MenuComponentEnemy(double width, double height, String texture, int health){
        super(0, 0, width, height);
        this.texture = texture;
        this.health = health;
        this.maxHealth = health;
    }
    @Override
    public void tick(){
        x+=xSpeed;
        y+=ySpeed;
        rot += rotSpeed;
        targeted = false;
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glPushMatrix();
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glRotated(rot, 0, 0, rot);
        drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture("/textures/enemies/"+texture+".png"));
        GL11.glPopMatrix();
        renderLifebar();
    }
    public void renderLifebar(){
        if(targeted&&showTarget){
            drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/target.png"));
        }
        GL11.glColor4d(0,0,0,1);
        drawRect(x, y+height, x+width, y+height+25, 0);
        GL11.glColor4d(0.5,0,0,1);
        drawRect(x+1, y+height+1, x+(width-2), y+height+24, 0);
        GL11.glColor4d(0,1,0,1);
        drawRect(x+1, y+height+1, x+((width-2)*(health/(maxHealth+0D))), y+height+24, 0);
        GL11.glColor4d(1,1,1,1);
    }
    public void hurt(double damage){
        health -= damage;
        if(health<=0){
            die();
        }
    }
    public abstract void die();
    public abstract ArrayList<Item> getDrops(Random r);
    public double getXSpeed(){
        return xSpeed;
    }
    public double getYSpeed(){
        return ySpeed;
    }
    public MenuComponentParticle getExplosion(){
        return new MenuComponentExplosion(this);
    }
    public boolean isHit(MenuComponentManaBolt bolt){
        if(!isClickWithinBounds(bolt.x+bolt.width/2, bolt.y+bolt.height/2, x, y, x+width, y+height)){
            return false;
        }
        return isHit((bolt.x+bolt.width/2)-x,(bolt.y-bolt.height/2)-y);
    }
    public boolean isHit(MenuComponentManaLaser laser){
        if(!isClickWithinBounds(laser.x+laser.width/2, laser.y+laser.width/2, x, y, x+width, y+height)){
            return false;
        }
        return isHit((laser.x+laser.width/2)-x,(laser.y-laser.width/2)-y);
    }
    public boolean isHit(double x, double y){
        return true;
    }
    public boolean isHit(MenuComponentLaser laser){
        if(!isClickWithinBounds(laser.x+laser.width/2, laser.y+laser.height-laser.width/2, x, y, x+width, y+height))return false;
        return isHit((laser.x+laser.width/2)-x, (laser.y+laser.height-laser.width/2)-y);
    }
}