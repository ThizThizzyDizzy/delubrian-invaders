package delubrian.invaders.menu.component;
import delubrian.invaders.planet.AsteroidBelt;
import delubrian.invaders.planet.Planet;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentPlanet extends MenuComponent{
    public final Planet planet;
    private double orbit;
    private final double orbitSpeed;
    public MenuComponentPlanet(Planet planet){
        super(0, 0, planet instanceof AsteroidBelt?planet.size/2.5:planet.size/1.75, planet instanceof AsteroidBelt?planet.size/2.5:planet.size/1.75);
        this.planet = planet;
        orbit = Planet.randoplanet.nextInt(3600)/10D;
        orbitSpeed = (planet.parent.planets.length+1-planet.parent.indexOf(planet)*(((Planet.randoplanet.nextDouble())/10D)+.05))/100D;
    }
    @Override
    public void tick(){
        orbit+=orbitSpeed;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(planet instanceof AsteroidBelt){
            for(int i = 0; i<36; i++){
                x = ((Math.cos(Math.toRadians(orbit+(i*10)))*((planet.parent.indexOf(planet)+3)*100))/2)*0.8+Display.getWidth()/2-width/2;
                y = ((Math.sin(Math.toRadians(orbit+(i*10)))*((planet.parent.indexOf(planet)+3)*100))/2)*0.8+Display.getHeight()/2-height/2;
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/asteroid.png"));
                orbit+=5;
                x = ((Math.cos(Math.toRadians(orbit+(i*10)))*((planet.parent.indexOf(planet)+3)*100))/2)*0.75+Display.getWidth()/2-width/2;
                y = ((Math.sin(Math.toRadians(orbit+(i*10)))*((planet.parent.indexOf(planet)+3)*100))/2)*0.75+Display.getHeight()/2-height/2;
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/asteroid.png"));
                orbit-=5;
            }
        }else{
            x = ((Math.cos(Math.toRadians(orbit))*((planet.parent.indexOf(planet)+3)*100))/2)*0.8+Display.getWidth()/2-width/2;
            y = ((Math.sin(Math.toRadians(orbit))*((planet.parent.indexOf(planet)+3)*100))/2)*0.8+Display.getHeight()/2-height/2;
            GL11.glColor3d(0, .5, 0);
            drawRect(x, y, x+width, y+height, /*ImageStash.instance.getTexture(planet.texture)*/0);
            GL11.glColor4d(1, 1, 1, 1);
        }
    }
}