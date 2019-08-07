package delubrian.invaders.planet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
public class Mineral implements Serializable{
    private static final long serialVersionUID = 11234789769L;
    public final MineralType mineral;
    public final double chance;
    public Mineral(MineralType mineral, double chance){
        this.mineral = mineral;
        this.chance = chance;
    }
    private static final Random randomineral = new Random();
    public static Mineral[] randomMinerals(){
        ArrayList<Mineral> minerals = new ArrayList<>();
        double num = 0.8;
        for(int i = 1; i<MineralType.values().length+1; i++){
            MineralType type = randomize();
            if(!hasMineral(minerals, type)){
                minerals.add(new Mineral(type, randomize(num, 0.2)));
                num *= 0.95;
            }
        }
        return minerals.toArray(new Mineral[minerals.size()]);
    }
    private static double randomize(double number, double percent){
        return number+(number*(randomineral.nextInt((int)(percent*200))/100D-percent));
    }
    public static MineralType randomize(){
        return MineralType.values()[randomineral.nextInt(MineralType.values().length)];
    }
    private static boolean hasMineral(ArrayList<Mineral> minerals, MineralType type){
        for(Mineral mineral : minerals){
            if(mineral.mineral==type){
                return true;
            }
        }
        return false;
    }
}