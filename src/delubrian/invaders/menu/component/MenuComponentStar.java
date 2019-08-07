package delubrian.invaders.menu.component;
import delubrian.invaders.planet.Star;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentStar extends MenuComponent{
    private final Star star;
    private double orbit;
    public MenuComponentStar(Star star){
        super(0, 0, 100, 100);
        this.star = star;
        double per = 360/star.parent.stars.length;
        orbit = per*(star.parent.indexOf(star)+1);
    }
    @Override
    public void tick(){
        orbit+=0.1;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(star.parent.stars.length==1){
            x = Display.getWidth()/2-width/2;
            y = Display.getHeight()/2-height/2;
        }else{
            x = (Math.cos(Math.toRadians(orbit))*75)+Display.getWidth()/2-width/2;
            y = (Math.sin(Math.toRadians(orbit))*75)+Display.getHeight()/2-height/2;
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture(star.texture));
    }
}