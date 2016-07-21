package org.fenixedu.spaces.ui;

import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.submission.SpacePhoto;
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

    @RequestMapping(value = "{space}", method = RequestMethod.GET)
    public String submit(@PathVariable Space space, Model model) {
        PhotoSubmissionBean bean = new PhotoSubmissionBean();
        model.addAttribute("photoSubmission", bean);
        return "photos/submit";
    }

    @RequestMapping(value = "{space}", method = RequestMethod.POST)
    public String submit(@PathVariable Space space, @ModelAttribute PhotoSubmissionBean photoSubmissionBean,
            BindingResult errors) {
        if (!photoSubmissionBean.isImageFile()) {
            return "redirect:spaces/photos/" + space.getExternalId();
        }
        photoService.createSubmission(photoSubmissionBean, space);
        return "redirect:/spaces-view/view/" + space.getExternalId();
    }

    @RequestMapping(value = "/review/{space}", method = RequestMethod.GET)
    public String pendingPhotos(@PathVariable Space space, Model model, @RequestParam(defaultValue = "1") String p) {
        model.addAttribute("submissions", photoService.getBook(photoService.getPhotoSubmissionsToProcess(space), p));
        return "photos/review";
    }

    @RequestMapping(value = "/{spacePhoto}/accept", method = RequestMethod.POST)
    public String acceptPhoto(@PathVariable SpacePhoto spacePhoto, Model model, @Value("null") @ModelAttribute FormBean form) {
        photoService.acceptSpacePhoto(spacePhoto);
        return "redirect:/spaces/photos/review/" + form.getSpace().getExternalId() + "?p=" + form.getPage();
    }

    @RequestMapping(value = "/{spacePhoto}/reject", method = RequestMethod.POST)
    public String rejectPhoto(@PathVariable SpacePhoto spacePhoto, Model model, @Value("null") @ModelAttribute FormBean form) {
        photoService.rejectSpacePhoto(spacePhoto);
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
        model.addAttribute("spacePhotos", photoService.getBook(photoService.getAllSpacePhotos(space), p));
        model.addAttribute("space", space);
        return "photos/edit";
    }
}
