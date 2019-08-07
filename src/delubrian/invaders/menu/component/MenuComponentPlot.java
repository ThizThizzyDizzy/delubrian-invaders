package delubrian.invaders.menu.component;
import delubrian.invaders.planet.Planet;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentPlot extends MenuComponentButton{
    private final Planet planet;
    public MenuComponentPlot(Planet planet, int x, int y, int width, int height){
        super(x, y, width, height, "", true);
        this.planet = planet;
    }
    public MenuComponentPlot(Planet planet, int x, int y, int width, int height, boolean enabled){
        super(x, y, width, height, "", enabled);
        this.planet = planet;
    }
    @Override
    public void render(){
        removeRenderBound();
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture(planet.surfaceTexture));
    }
}