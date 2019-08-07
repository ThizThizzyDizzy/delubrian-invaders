package delubrian.invaders.menu.component;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class MenuComponentExplosion extends MenuComponentParticle{
    private int radius;
    private double size;
    public MenuComponentExplosion(double x, double y){
        this(x,y,1);
    }
    public MenuComponentExplosion(double x, double y, double width, double height){
        this(x,y,Math.sqrt(width*height)/100D);
    }
    public MenuComponentExplosion(double x, double y, double size){
        super(x,y,50,50);
        this.size = size;
    }
    public MenuComponentExplosion(MenuComponentEnemy enemy){
        this(enemy.x+enemy.width/2, enemy.y+enemy.height/2, enemy.width, enemy.height);
    }
    @Override
    public void render(){
        if(opacity<=0)return;
        removeRenderBound();
        //smoke
        for(int j = 0; j<5; j++){
            int radius = this.radius+5*j;
            for(int i = 0; i<180; i++){
                double X = Math.cos(Math.toRadians(i*2))*radius+x-width/2;
                double Y = Math.sin(Math.toRadians(i*2))*radius+y-height/2;
                GL11.glColor4d(1, 1, 1, opacity);
                drawRect(X, Y, X+width, Y+height, ImageStash.instance.getTexture("/textures/particles/smoke.png"));
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
        //explosion
        for(int j = 0; j<10; j++){
            double radius = this.radius/10D+2*j;
            for(int i = 0; i<45; i++){
                double X = Math.cos(Math.toRadians(i*8))*radius+x-width/2;
                double Y = Math.sin(Math.toRadians(i*8))*radius+y-height/2;
                GL11.glColor4d(1, opacity, 0, opacity);
                drawRect(X, Y, X+width, Y+height, ImageStash.instance.getTexture("/textures/particles/explosion.png"));
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
    }

    @Override
    public void tick(){
        if(opacity<=0){
            return;
        }
        opacity-=0.005*(11-size);
        radius+=5+0.5*((11-size));
    }
}