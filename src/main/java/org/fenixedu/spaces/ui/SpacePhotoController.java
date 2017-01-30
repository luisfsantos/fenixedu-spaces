package org.fenixedu.spaces.ui;

import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.submission.SpacePhoto;
import org.fenixedu.spaces.domain.submission.SpacePhotoSubmission;
import org.fenixedu.spaces.ui.services.SpacePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@BennuSpringController(SpaceSearchController.class)
@RequestMapping("/spaces/photos")
public class SpacePhotoController {

    @Autowired
    SpacePhotoService photoService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        return mySubmissions(model, "1");
    }

    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public String mySubmissions(Model model, @RequestParam(defaultValue = "1") String p) {
        model.addAttribute("mySubmissions",
                photoService.getSubmissionBook(photoService.getUserSpacePhotoSubmissions(Authenticate.getUser()), p));
        return "photos/mySubmissions";
    }
    
    @RequestMapping(value = "{space}", method = RequestMethod.GET)
    public String submit(@PathVariable Space space, Model model) {
        PhotoSubmissionBean bean = new PhotoSubmissionBean();
        model.addAttribute("photoSubmission", bean);
        model.addAttribute("space", space);
        return "photos/submit";
    }

    @RequestMapping(value = "{space}", method = RequestMethod.POST)
    public String submit(@PathVariable Space space, @ModelAttribute PhotoSubmissionBean photoSubmissionBean,
            BindingResult errors) {
        if (!photoSubmissionBean.isImageFile()) {
            return "redirect:/spaces/photos/" + space.getExternalId(); //TODO
        }
        photoService.createPhotoSubmission(photoSubmissionBean, space);
        return "redirect:/spaces-view/view/" + space.getExternalId();
    }

    @RequestMapping(value = "/review/{space}", method = RequestMethod.GET)
    public String pendingPhotos(@PathVariable Space space, Model model, @RequestParam(defaultValue = "1") String p) {
        model.addAttribute("submissions",
                photoService.getSubmissionBook(photoService.getSpacePhotoSubmissionsToProcess(space), p));
        return "photos/review";
    }

    @RequestMapping(value = "/{photoSubmission}/accept", method = RequestMethod.POST)
    public String acceptPhoto(@PathVariable SpacePhotoSubmission photoSubmission, Model model,
            @Value("null") @ModelAttribute FormBean form) {
        photoService.acceptSpacePhoto(photoSubmission, Authenticate.getUser());
        return "redirect:/spaces/photos/review/" + form.getSpace().getExternalId() + "?p=" + form.getPage();
    }

    @RequestMapping(value = "/{photoSubmission}/reject", method = RequestMethod.POST)
    public String rejectPhoto(@PathVariable SpacePhotoSubmission photoSubmission, Model model,
            @Value("null") @ModelAttribute FormBean form) {
        photoService.rejectSpacePhoto(photoSubmission, Authenticate.getUser(), form.getRejectMessage());
        return "redirect:/spaces/photos/review/" + form.getSpace().getExternalId() + "?p=" + form.getPage();
    }

    @RequestMapping(value = "/{spacePhoto}/hide", method = RequestMethod.POST)
    public String hidePhoto(@PathVariable SpacePhoto spacePhoto, Model model, @Value("null") @ModelAttribute FormBean form) {
        photoService.hideSpacePhoto(spacePhoto);
        return "redirect:/spaces/photos/edit/" + form.getSpace().getExternalId() + "?p=" + form.getPage();
    }

    @RequestMapping(value = "/{spacePhoto}/show", method = RequestMethod.POST)
    public String showPhoto(@PathVariable SpacePhoto spacePhoto, Model model, @Value("null") @ModelAttribute FormBean form) {
        photoService.showSpacePhoto(spacePhoto);
        return "redirect:/spaces/photos/edit/" + form.getSpace().getExternalId() + "?p=" + form.getPage();
    }

    @RequestMapping(value = "/{spacePhoto}/delete", method = RequestMethod.POST)
    public String deletePhoto(@PathVariable SpacePhoto spacePhoto, Model model, @Value("null") @ModelAttribute FormBean form) {
        photoService.removeSpacePhoto(form.getSpace(), spacePhoto);
        return "redirect:/spaces/photos/edit/" + form.getSpace().getExternalId() + "?p=" + form.getPage();
    }

    @RequestMapping(value = "/edit/{space}", method = RequestMethod.GET)
    public String editSpacePhotos(@PathVariable Space space, Model model, @RequestParam(defaultValue = "1") String p) {
        model.addAttribute("spacePhotos", photoService.getPhotoBook(photoService.getAllSpacePhotos(space), p));
        model.addAttribute("space", space);
        return "photos/edit";
    }
}
