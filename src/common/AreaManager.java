package common;

/**
 * Entity of area manager (for North, South and UAE areas)
 */

public class AreaManager extends User{
    private String area;

    public AreaManager(User user,String area) {
        super(user);
        this.area = area;
    }
}
