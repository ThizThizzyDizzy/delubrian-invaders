package delubrian.invaders.menu.component;
import delubrian.invaders.Core;
import delubrian.invaders.Item;
import delubrian.invaders.menu.MenuBattle;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
public class MenuComponentAsteroid extends MenuComponentEnemy{
    private final Random randoAsteroid = new Random();
    public double shade = 1;
    private final boolean background;
    public boolean hasSplit = false;
    public MenuComponentAsteroid(boolean background, double xSpeed, double ySpeed, double rotSpeed){
        this(background, xSpeed, ySpeed);
        this.rotSpeed = rotSpeed;
    }
    public MenuComponentAsteroid(boolean background, double xSpeed, double ySpeed){
        super(0, 0, "asteroid", 1);
        width = height = randoAsteroid.nextInt(250)+250;
        x = randoAsteroid.nextInt((int) (Display.getWidth()-width));
        y = -height;
        health = (int)Math.sqrt(width)*500;
        maxHealth = health;
        this.background = background;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        rotSpeed = Core.rand.nextGaussian()/6;
    }
    public MenuComponentAsteroid(boolean background, MenuComponentAsteroid asteroid, int quadrant){
        this(background);
        xSpeed = asteroid.xSpeed;
        ySpeed = asteroid.ySpeed;
        switch(quadrant){
            case 1:
                x = asteroid.x+asteroid.width/2;
                y = asteroid.y;
                break;
            case 2:
                x = asteroid.x;
                y = asteroid.y;
                break;
            case 3:
                x = asteroid.x;
                y = asteroid.y+asteroid.height/2;
                break;
            case 4:
                x = asteroid.x+asteroid.width/2;
                y = asteroid.y+asteroid.height/2;
                break;
            default:
                throw new IllegalArgumentException("Quadrant "+quadrant+" is not a valid quadrant! WHAT WERE YOU THINKING!");
        }
        width = asteroid.width/2;
        height = asteroid.height/2;
        double r1 = Core.distance(asteroid, x+width/2, y+height/2);
        double radians = (Math.PI/180)*asteroid.rotSpeed;
        double speed = radians*r1;
        double individualSpeed = (Math.sqrt(2)/2)*speed;
        switch(quadrant){
            case 1:
                xSpeed += individualSpeed;
                ySpeed += individualSpeed;
                break;
            case 2:
                xSpeed += individualSpeed;
                ySpeed -= individualSpeed;
                break;
            case 3:
                xSpeed -= individualSpeed;
                ySpeed -= individualSpeed;
                break;
            case 4:
                xSpeed -= individualSpeed;
                ySpeed += individualSpeed;
                break;
            default:
                throw new IllegalArgumentException("Quadrant "+quadrant+" is not a valid quadrant! WHAT WERE YOU THINKING!?!");
        }
        xSpeed+=(Core.rand.nextDouble()-.5)/3;
        ySpeed+=(Core.rand.nextDouble()-.5)/3;
        rotSpeed = asteroid.rotSpeed+Core.rand.nextDouble()/10;
    }
    public MenuComponentAsteroid(boolean background){
        this(background, 0, .25);
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glPushMatrix();
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glRotated(rot, 0, 0, 1);
        GL11.glColor4d(shade, shade, shade, 1);
        drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture("/textures/"+(background?"bg ":"")+"asteroid.png"));
        GL11.glColor4d(1, 1, 1, 1);
        GL11.glPopMatrix();
        if(!background){
            renderLifebar();
        }
    }
    @Override
    public void die(){
        MenuBattle battle = (MenuBattle) parent;
        battle.asteroidsToSplit.add(this);
    }
    @Override
    public ArrayList<Item> getDrops(Random r){
        return new ArrayList<>();
    }
    @Override
    public MenuComponentParticle getExplosion(){
        return new MenuComponentSmokeExplosion(this);
    }
    @Override
    public boolean isHit(double x, double y) {
        return Core.distance(x, y, width/2, height/2)<=width/2;
    }
}