package delubrian.invaders;
import java.io.Serializable;
public class Job implements Serializable{
    private static final long serialVersionUID = 4415347365876514267L;
    public final Recipe recipe;
    public int progress = 0;
    public int count = 1;
    public Job(Recipe recipe){
        this.recipe = recipe;
    }
    public Job(Recipe recipe, int count){
        this.recipe = recipe;
        this.count = count;
    }
}