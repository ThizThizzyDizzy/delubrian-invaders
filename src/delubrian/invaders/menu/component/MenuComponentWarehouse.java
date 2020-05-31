package delubrian.invaders.menu.component;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.planet.Warehouse;
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