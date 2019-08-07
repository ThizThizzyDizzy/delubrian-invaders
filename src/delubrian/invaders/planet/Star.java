package delubrian.invaders.planet;
import java.io.Serializable;
public class Star implements Serializable{
    private static final long serialVersionUID = 181091L;
    public final String name;
    public final StarSystem parent;
    public final String texture;
    public Star(String name, StarSystem parent, String texture){
        this.name = name;
        this.parent = parent;
        this.texture = texture;
    }
}