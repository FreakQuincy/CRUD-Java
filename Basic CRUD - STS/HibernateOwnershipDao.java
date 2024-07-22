import java.util.List;

import javax.management.Query;

public class HibernateOwnershipDao implements OwnershipDao {

    public void addOwnership(Ownership ownership) {
        sf.getCurrentSession().save(ownership);

    }

    public void updateOwnership(Ownership ownership) {
        sf.getCurrentSession().merge(ownership);

    }

    public void deleteOwnership(Ownership ownership) {
        sf.getCurrentSession().delete(ownership);

    }

    @SuppressWarnings("unchecked")
    public Ownership getOwnershipById(int id) {
        List<Ownership> ownerships = sf.getCurrentSession().createQuery("from Ownership where id=" + id).list();
        if (ownerships.size() > 0) {
            return ownerships.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Ownership> searchOwnership(String name, Integer showRecent) {
        String searchString = "SELECT * FROM ownership o WHERE 1=1 ";

        if (name != null && !name.isEmpty()) {
            searchString += "AND o.name LIKE :name ";
        }

        // String limit = "";
        // if(showRecent != null && showRecent > 0) {
        // limit += " LIMIT 10";
        // }

        System.out.println(searchString);
        Query query = sf.getCurrentSession().createSQLQuery(searchString + " ORDER BY ownership_id DESC")
                .addEntity(Ownership.class);

        if (name != null && !name.isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }

        List<Ownership> ownerships = query.list();
        return ownerships;
    }

    @SuppressWarnings("unchecked")
    public List<Ownership> getAllOwnerships() {
        return sf.getCurrentSession().createQuery("from Ownership where isActive = 1 order by id asc").list();
    }

}
