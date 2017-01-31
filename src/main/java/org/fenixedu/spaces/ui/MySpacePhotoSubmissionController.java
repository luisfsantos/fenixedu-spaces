package org.fenixedu.spaces.ui;

import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.ui.services.SpacePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@SpringFunctionality(app = SpacesController.class, title = "title.view.my.submissions")
@RequestMapping("/spaces/photos/submissions/my")
public class MySpacePhotoSubmissionController {

    @Autowired
    SpacePhotoService photoService;

    @RequestMapping(method = RequestMethod.GET)
    public String mySubmissions(Model model, @RequestParam(defaultValue = "1") String p) {
        model.addAttribute("pendingSubmissions",
                photoService.getSubmissionBook(photoService.getPendingUserSubmissions(Authenticate.getUser()), p));
        model.addAttribute("acceptedSubmissions",
                photoService.getSubmissionBook(photoService.getAcceptedUserSubmissions(Authenticate.getUser()), p));
        model.addAttribute("rejectedSubmissions",
                photoService.getSubmissionBook(photoService.getRejectedUserSubmissions(Authenticate.getUser()), p));
        return "photos/submissions/my";
    }

    @RequestMapping(value = "/create/{space}", method = RequestMethod.GET)
    public String submit(@PathVariable Space space, Model model) {
        PhotoSubmissionBean bean = new PhotoSubmissionBean();
        model.addAttribute("photoSubmission", bean);
        model.addAttribute("space", space);
        return "photos/submissions/create";
    }

    @RequestMapping(value = "/create/{space}", method = RequestMethod.POST)
    public String submit(@PathVariable Space space, @ModelAttribute PhotoSubmissionBean photoSubmissionBean,
            BindingResult errors) {
        if (!photoSubmissionBean.isImageFile()) {
            return "redirect:/spaces/photos/" + space.getExternalId(); //TODO
        }
        photoService.createPhotoSubmission(photoSubmissionBean, space);
        return "redirect:/spaces-view/view/" + space.getExternalId();
    }

}
