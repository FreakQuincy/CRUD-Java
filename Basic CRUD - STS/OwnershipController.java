import java.text.ParseException;

@Controller
@RequestMapping("/ownership")
public class OwnershipController {

    /**
     * This method is called when creating new or editing existing
     * <tt>Ownership</tt> entities. It prepares the Ownership form page
     * by instantiating the <tt>OwnershipForm</tt> form backing class.
     * When editing, this instance will be loaded with the object's
     * details.
     * 
     * @param id    - <tt>Ownership</tt> id. Required when editing.
     * @param model - Auto-injected by Spring.
     * @return <tt>String</tt> view name
     */
    @RequestMapping(value = { "/newOwnership.htm", "/editOwnership.htm" }, method = RequestMethod.GET)
    public String loadOwnershipForm(@RequestParam(value = "id", required = false) Integer id, Model model,
            HttpSession session) {

        // New instance
        OwnershipForm ownershipForm = new OwnershipForm();

        // Load existing
        if (id != null) {
            // propertyTypeForm = this.setUpdateForm(id);
            Ownership ownership = propertyManager.getOwnershipById(id);
            ownershipForm.setId(ownership.getId());
            ownershipForm.setName(ownership.getName());
            ownershipForm.setIsActive(ownership.isActive() ? 1 : 0);
        }

        model.addAttribute("ownershipForm", ownershipForm);

        return OWNERSHIP_FORM_PAGE;
    }

    /**
     * This method is called when the Ownership form page is submitted.
     * Takes the <tt>OwnershipForm</tt> instance. Returns to the ownership
     * form page when there are binding errors. When editing, it loads
     * the <tt>Ownership</tt> object details before replacing them with
     * new values from the <tt>OwnershipForm</tt> object. Calls <tt>Ownership</tt>
     * service class for adding and updating.
     * 
     * @param form    - <tt>OwnershipForm</tt> instance that binds to the Ownership
     *                form page.
     * @param result  - <tt>BindingResult</tt> instance. Auto-injected by Spring.
     * @param model   - Auto-injected by Spring.
     * @param request - Auto-injected by Spring.
     * @return <tt>String</tt> view name
     */
    @RequestMapping(value = "/saveOwnership.htm", method = RequestMethod.POST)
    public String saveOwnership(@Valid @ModelAttribute("ownershipForm") OwnershipForm form, BindingResult result,
            Model model, HttpSession session) {
        // Is update?
        boolean update = (form.getId() > 0) ? true : false;

        User user = (User) session.getAttribute("loggedInUser");

        user = userManager.getUserById(user.getId());

        // If has errors
        if (result.hasErrors()) {
            return OWNERSHIP_FORM_PAGE;
        }

        // New Ownership instance
        Ownership ownership = new Ownership();

        // On update, load existing values
        if (update) {
            ownership = propertyManager.getOwnershipById(form.getId());
        }

        // redirect to search.htm page if the ownership was already deleted.
        if (ownership == null) {
            session.setAttribute("errorMsg", "Saving failed. Record is already deleted.");
            return OWNERSHIP_SEARCH_PAGE;
        }

        ownership.setName(form.getName().trim());
        ownership.setActive(form.getIsActive() == 1 ? true : false);

        // Update
        if (update) {
            try {
                if (propertyManager.updateOwnership(ownership)) {
                    session.setAttribute("updateMsg",
                            "<b>" + ownership.getName() + "</b> has been successfully updated.");
                    model.addAttribute("ownershipForm", form);
                    return "redirect:/property/searchOwnership.htm";
                } else {
                    session.setAttribute("errorMsg", "Update Failed. Ownership already exists.");
                    model.addAttribute("ownershipForm", form);
                }
            }
            // when entered special characters instead of HTTP Status page, an error message
            // appear
            catch (QueryException e) {
                session.setAttribute("errorMsg", "Update Failed. Ownership already exists.");
                model.addAttribute("ownershipForm", form);
            }
            // catch almost all exceptions this is to prevent HTTP Status page to appear
            catch (Exception e) {
                session.setAttribute("errorMsg", "Update Failed. Ownership already exists.");
                model.addAttribute("ownershipForm", form);
            }

            // Add
        } else {
            try {
                // Add ownership
                if (propertyManager.addOwnership(ownership)) {
                    session.setAttribute("successOwnershipMsg",
                            "<b>" + ownership.getName() + "</b> has been successfully added.");
                }
                // Duplicate
                else {
                    session.setAttribute("errorMsg", "Add Failed. Ownership already exists.");
                    model.addAttribute("ownershipForm", form);
                }
            }
            // when entered special characters instead of HTTP Status page, an error message
            // appear
            catch (QueryException e) {
                session.setAttribute("errorMsg", "Add Failed. Ownership already exists.");
                model.addAttribute("ownershipForm", form);
            }
            // catch almost all exceptions this is to prevent HTTP Status page to appear
            catch (Exception e) {
                session.setAttribute("errorMsg", "Add Failed. Ownership already exists.");
                model.addAttribute("ownershipForm", form);
            }

        }
        return OWNERSHIP_FORM_PAGE;
    }

    @RequestMapping(value = "/searchOwnership.htm")
    public String searchOwnership(Model model, HttpSession session,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "itemsPerPage", required = false, defaultValue = "10") Integer itemsPerPage,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "recent", required = false) Integer showRecent,
            @RequestParam(value = "cancel", required = false, defaultValue = "0") Integer cancel,
            HttpServletRequest request) throws ParseException {

        if (name == null || !name.isEmpty()) {
            showRecent = 1;
        }

        ControllerSummary controllerSummary = propertyManager.searchOwnership(model, session, page, itemsPerPage,
                name, showRecent, cancel, request);

        model = controllerSummary.getModel();
        session = controllerSummary.getSession();
        return controllerSummary.getPage();
    }

    /**
     * Loads the delete confirmation page.
     * 
     * @param id    - Id of the <tt>Ownership</tt> to be deleted.
     * @param model
     * @return String Delete page
     */
    @RequestMapping(value = "/deleteOwnership.htm", method = RequestMethod.GET)
    public String confirmDeleteOwnership(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("ownership", propertyManager.getOwnershipById(id));
        return OWNERSHIP_DELETE_PAGE;
    }

    /**
     * This method is called when deleting an <tt>Ownership</tt> object.
     * Requires <tt>Ownership</tt> ID.
     * 
     * @param id    - ID of the <tt>Ownership</tt> instance to be deleted.
     * @param model - Auto-injected by Spring.
     * @return <tt>String</tt> view name.
     */
    @RequestMapping(value = "/deleteOwnership.htm", method = RequestMethod.POST, params = "confirmDelete")
    public String deleteOwnership(@RequestParam(value = "id") int id, HttpSession session) {

        Ownership ownership = propertyManager.getOwnershipById(id);
        try {
            propertyManager.deleteOwnership(ownership);

            session.setAttribute("deleteMsg", "<b>" + ownership.getName()
                    + "</b> has been successfully deleted.");
        } catch (Exception e) {
            session.setAttribute("warningMsg",
                    "Delete Failed. <b>" + ownership.getName() + "</b> is currently in use and cannot be deleted.");
        }
        return "redirect:/property/searchOwnership.htm";
    }

}
