package delubrian.invaders.menu.component;
import delubrian.invaders.menu.component.MenuComponentParticle;
import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class MenuComponentDust extends MenuComponentParticle{
    private int rotation;
    public MenuComponentDust(double x, double y, double size){
        super(x, y, size, size);
        color = new Color(.125f, .125f, .125f);
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glColor4d(color.getRed()/255d, color.getGreen()/255d, color.getBlue()/255d, opacity);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        GL11.glRotated(rotation, 0, 0, 1);
        drawRect(-width/2, -height/2, width/2, height/2, ImageStash.instance.getTexture("/textures/particles/explosion.png"));
        GL11.glPopMatrix();
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        if(opacity<=0){
            return;
        }
        rotation+=0.01;
        opacity-=0.05;
    }
}