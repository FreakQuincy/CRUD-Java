import java.util.List;

@Component
public class OwnershipDao {
    public void addOwnership(Ownership ownership);

    public void updateOwnership(Ownership ownership);

    public void deleteOwnership(Ownership ownership);

    public Ownership getOwnershipById(int id);

    public List<Ownership> searchOwnership(String name, Integer showRecent);

    public List<Ownership> getAllOwnerships();
}