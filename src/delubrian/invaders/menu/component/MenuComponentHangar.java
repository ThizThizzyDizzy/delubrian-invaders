package delubrian.invaders.menu.component;
import delubrian.invaders.planet.Hangar;
public class MenuComponentHangar extends MenuComponentBuilding{
    private final Hangar hangar;
    public MenuComponentHangar(Hangar hangar){
        super(hangar, "hangar");
        this.hangar = hangar;
    }
    /**
     * @return the hangar
     */
    public Hangar getHangar(){
        return hangar;
    }
}