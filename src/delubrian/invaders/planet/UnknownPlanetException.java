package delubrian.invaders.planet;
public class UnknownPlanetException extends Exception{
    public UnknownPlanetException(String planet, String system){
        super("Unknown Planet "+planet+" in Star System "+system);
    }
    public UnknownPlanetException(String planet){
        super("Unknown Planet: "+planet);
    }
    public UnknownPlanetException(){
        super("Unknown Planet");
    }
}