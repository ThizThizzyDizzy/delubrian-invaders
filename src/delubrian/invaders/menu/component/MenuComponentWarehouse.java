package delubrian.invaders.menu.component;
import delubrian.invaders.ItemStack;
import delubrian.invaders.menu.MenuSelectCargo;
import delubrian.invaders.menu.MenuSurface;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.planet.Warehouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
public class MenuComponentWarehouse extends MenuComponentBuilding{
    private final Warehouse warehouse;
    private Galaxy galaxy;
    public MenuComponentWarehouse(Galaxy galaxy, Warehouse warehouse){
        super(warehouse, "/textures/warehouse.png");
        this.galaxy = galaxy;
        this.warehouse = warehouse;
    }
    /**
     * @return the warehouse
     */
    public Warehouse getWarehouse() {
        return warehouse;
    }
}