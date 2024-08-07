import java.text.ParseException;
import java.util.List;

@Service
@Transactional
public class OwnershipManager {

    public Ownership getOwnershipById(int id) {
        return propertyDao.getOwnershipById(id);
    }

    @PreAuthorize("hasRole('OWNERSHIP_ADD')")
    @LogActivity(logType = Task.OWNERSHIP_ADD)
    public boolean addOwnership(Ownership ownership) {
        try {
            propertyDao.addOwnership(ownership);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @PreAuthorize("hasRole('OWNERSHIP_EDIT')")
    @LogActivity(logType = Task.OWNERSHIP_EDIT)
    public boolean updateOwnership(Ownership ownership) {
        try {
            propertyDao.updateOwnership(ownership);
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    @PreAuthorize("hasRole('OWNERSHIP_DELETE')")
    @LogActivity(logType = Task.OWNERSHIP_DELETE)
    public boolean deleteOwnership(Ownership ownership) {
        propertyDao.deleteOwnership(ownership);
        return true;
    }

    public List<Ownership> getAllOwnerships() {
        return propertyDao.getAllOwnerships();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ControllerSummary searchOwnership(Model model, HttpSession session, Integer page, Integer itemsPerPage,
            String name, Integer showRecent, Integer cancel, HttpServletRequest request) throws ParseException {

        String SEARCH_PAGE = "ownership/search";
        ControllerSummary controllerSummary = new ControllerSummary();
        model.addAttribute("targetResultLocation", "searchOwnership");

        if (cancel > 0) {
            controllerSummary.setPage(SEARCH_PAGE);
            controllerSummary.setModel(model);
            controllerSummary.setSession(session);
            model.addAttribute("isResult", "1");
            return controllerSummary;
        }

        if (page == null && name == null && showRecent == null) {
            controllerSummary.setPage(SEARCH_PAGE);
            controllerSummary.setModel(model);
            controllerSummary.setSession(session);
            model.addAttribute("isResult", "0");
            return controllerSummary;
        }

        if (itemsPerPage == null) {
            itemsPerPage = 10;
        }

        if (page != null) {
            pageHolder = (PagedListHolder) request.getSession().getAttribute("ownershipPageHolder");
            if (pageHolder == null) {
                session.setAttribute("errorMsg", "Page has been refreshed due to recent Log In/Out in another tab.");
                controllerSummary.setPage(SEARCH_PAGE);
                controllerSummary.setModel(model);
                controllerSummary.setSession(session);
                model.addAttribute("isResult", "0");
                return controllerSummary;
            }

            pageHolder.setPageSize(itemsPerPage);
            model.addAttribute("itemsPerPage", itemsPerPage);
            session.setAttribute("ownershipPageHolder", pageHolder);
            pageHolder.setPage(page - 1);

            controllerSummary.setPage(SEARCH_PAGE);
            controllerSummary.setModel(model);
            controllerSummary.setSession(session);
            model.addAttribute("isResult", "1");
            return controllerSummary;
        }

        List<Ownership> ownerships = propertyDao.searchOwnership(name, showRecent);

        model.addAttribute("name", name);

        pageHolder = new PagedListHolder(ownerships);
        pageHolder.setPageSize(itemsPerPage);
        model.addAttribute("itemsPerPage", itemsPerPage);

        pageHolder.setMaxLinkedPages(7);
        session.setAttribute("ownershipPageHolder", pageHolder);

        String fieldName = "";
        String fieldValue = "";

        if (name != null && !name.isEmpty()) {
            if (!fieldName.isEmpty()) {
                fieldName += " / ";
            }
            if (!fieldValue.isEmpty()) {
                fieldValue += " / ";
            }
            fieldName += "Name";
            fieldValue += name;
        }

        if (showRecent != null && showRecent > 0) {
            fieldName += "Recent";
            fieldValue += "10";
        }

        userActivityLogManager.searchOwnershipLogger(fieldName, fieldValue);

        controllerSummary.setPage(SEARCH_PAGE);
        controllerSummary.setModel(model);
        controllerSummary.setSession(session);
        model.addAttribute("isResult", "1");
        return controllerSummary;
    }
}
