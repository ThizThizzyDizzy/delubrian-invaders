package delubrian.invaders.planet;
import delubrian.invaders.Core;
import java.io.Serializable;
public class StarSystem implements Serializable{
    private static final long serialVersionUID = 1L;
    public final String name;
    public final Star[] stars;
    public Planet[] planets;
    public Galaxy parent;
    public StarSystem(Galaxy galaxy, String name, int stars, Planet... planets){
        parent = galaxy;
        this.name = name;
        this.stars = new Star[stars];
        this.planets = planets;
        for(int i = 0; i<stars; i++){
            this.stars[i] = new Star(name+"-"+Core.toLetter(stars+1), this, "/"+name+"/texture "+(stars+1)+".png");
        }
    }
    public Planet getPlanet(String name) {
        for(Planet planet : planets){
            if(planet.name.equalsIgnoreCase(name)){
                return planet;
            }
        }
        return null;
    }
    public int indexOf(Planet planet){
        for(int i = 0; i<planets.length; i++){
            if(planets[i]==planet){
                return i;
            }
        }
        return -1;
    }
    public int indexOf(Star star){
        for(int i = 0; i<stars.length; i++){
            if(stars[i]==star){
                return i;
            }
        }
        return -1;
    }
}