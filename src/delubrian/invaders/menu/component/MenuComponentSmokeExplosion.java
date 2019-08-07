package delubrian.invaders.menu.component;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class MenuComponentSmokeExplosion extends MenuComponentParticle{
    private double radius;
    private final double size;
    private double time = 40;
    public MenuComponentSmokeExplosion(double x, double y){
        this(x,y,1);
    }
    public MenuComponentSmokeExplosion(double x, double y, double width, double height){
        this(x,y,Math.sqrt(width*height));
    }
    public MenuComponentSmokeExplosion(double x, double y, double size){
        super(x,y,50,50);
        this.size = size;
        time = size/10;
    }
    public MenuComponentSmokeExplosion(MenuComponentEnemy enemy){
        this(enemy.x+enemy.width/2, enemy.y+enemy.height/2, enemy.width, enemy.height);
    }
    @Override
    public void render(){
        removeRenderBound();
        //smoke
        for(int j = 0; j<5; j++){
            double radius = this.radius/5+12*j;
            double c = 90;
            for(int i = 0; i<c; i++){
                double X = Math.cos(Math.toRadians(i*(360/c)))*radius+x-width/2;
                double Y = Math.sin(Math.toRadians(i*(360/c)))*radius+y-height/2;
                GL11.glColor4d(.125, .125, .125, opacity);
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
        opacity-=1/time;
        radius+=(1/time)*size*2;
    }
}