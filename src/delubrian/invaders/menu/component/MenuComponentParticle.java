package delubrian.invaders.menu.component;
import simplelibrary.opengl.gui.components.MenuComponent;
public abstract class MenuComponentParticle extends MenuComponent{
    public double opacity = 1;
    public MenuComponentParticle(double x, double y, double width, double height){
        super(x, y, width, height);
    }
}