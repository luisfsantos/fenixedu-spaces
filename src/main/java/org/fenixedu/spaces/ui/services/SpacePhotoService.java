package org.fenixedu.spaces.ui.services;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.submission.SpacePhoto;
import org.fenixedu.spaces.domain.submission.SpacePhotoSubmission;
import org.fenixedu.spaces.ui.PhotoSubmissionBean;
import org.joda.time.DateTime;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;

@Service
public class SpacePhotoService {

    @Deprecated
    @Atomic
    public SpacePhoto createSubmission(PhotoSubmissionBean bean, Space space) {
        SpacePhoto photo =
                new SpacePhoto(bean.getSubmissionMultipartFile().getName(), bean.getSubmissionContent(), bean.getSubmitor());
        //TODO: to be safe this should be linked to the SpacePhoto but sometimes it doesn't go through a pending phase
        space.addSpacePhotoPending(photo);
        return photo;

    }

    @Atomic
    public SpacePhotoSubmission createPhotoSubmission(PhotoSubmissionBean bean, Space space) {
        SpacePhoto photo = new SpacePhoto(bean.getSubmissionMultipartFile().getName(), bean.getSubmissionContent());

        return new SpacePhotoSubmission(space, bean.getSubmitor(), photo);

    }

    @Deprecated
    public List<SpacePhoto> getPhotoSubmissionsToProcess(Space space) {
        Set<Space> allSpaces = space.getChildTree();
        List<SpacePhoto> allPendingPhotos = allSpaces.stream().map(s -> s.getSpacePhotoPendingSet()).flatMap(set -> set.stream())
                .collect(Collectors.toList());
        return allPendingPhotos.stream().sorted(SpacePhoto.COMPARATOR_BY_INSTANT.reversed()).collect(Collectors.toList());
    }

    public List<SpacePhotoSubmission> getSpacePhotoSubmissionsToProcess(Space space) {
        Set<Space> allSpaces = space.getChildTree();
        List<SpacePhotoSubmission> allPendingPhotos = allSpaces.stream().map(s -> s.getSpacePhotoSubmissionPendingSet())
                .flatMap(set -> set.stream()).collect(Collectors.toList());
        return allPendingPhotos.stream().sorted(SpacePhotoSubmission.COMPARATOR_BY_INSTANT.reversed())
                .collect(Collectors.toList());
    }

    public List<SpacePhoto> getVisiblePhotos(Space space) {
        return getAllSpacePhotos(space).stream().filter(photo -> photo.isVisible()).collect(Collectors.toList());
    }

    public List<SpacePhoto> getAllSpacePhotos(Space space) {
        if (space.getSpacePhotoSet().isPresent()) {
            return space.getSpacePhotoSet().get().stream().sorted(SpacePhoto.COMPARATOR_BY_INSTANT).collect(Collectors.toList());
        }
        return Collections.<SpacePhoto> emptyList();
    }

    public PagedListHolder<SpacePhoto> getPhotoBook(List<SpacePhoto> photos, String pageString) {
        PagedListHolder<SpacePhoto> book = new PagedListHolder<>(photos);
        book.setPageSize(10);
        int page = 0;

        if (Strings.isNullOrEmpty(pageString)) {
            page = 0;
        } else {
            try {
                page = Integer.parseInt(pageString);
            } catch (NumberFormatException nfe) {
                if ("f".equals(pageString)) {
                    page = 0;
                } else if ("l".equals(pageString)) {
                    page = book.getPageCount();
                }
            }
        }
        book.setPage(page == 0 ? 0 : page - 1);
        return book;
    }

    public PagedListHolder<SpacePhotoSubmission> getSubmissionBook(List<SpacePhotoSubmission> submissions, String pageString) {
        PagedListHolder<SpacePhotoSubmission> book = new PagedListHolder<>(submissions);
        book.setPageSize(10);
        int page = 0;

        if (Strings.isNullOrEmpty(pageString)) {
            page = 0;
        } else {
            try {
                page = Integer.parseInt(pageString);
            } catch (NumberFormatException nfe) {
                if ("f".equals(pageString)) {
                    page = 0;
                } else if ("l".equals(pageString)) {
                    page = book.getPageCount();
                }
            }
        }
        book.setPage(page == 0 ? 0 : page - 1);
        return book;
    }

    @Atomic
    public void rejectSpacePhoto(SpacePhotoSubmission spacePhotoSubmission, User reviewer, String rejectionMessage) {
        spacePhotoSubmission.setModified(new DateTime());
        Space space = spacePhotoSubmission.getSpacePending();
        spacePhotoSubmission.setRejectionMessage(rejectionMessage);
        spacePhotoSubmission.setReviewer(reviewer);
        space.removeSpacePhotoSubmissionPending(spacePhotoSubmission);
        space.addSpacePhotoSubmissionArchived(spacePhotoSubmission);
    }

    @Atomic
    public void acceptSpacePhoto(SpacePhotoSubmission spacePhotoSubmission, User reviewer) {
        spacePhotoSubmission.setModified(new DateTime());
        Space space = spacePhotoSubmission.getSpacePending();
        space.addSpacePhoto(spacePhotoSubmission.getPhoto());
        spacePhotoSubmission.setReviewer(reviewer);
        space.removeSpacePhotoSubmissionPending(spacePhotoSubmission);

    }

    @Atomic
    public void hideSpacePhoto(SpacePhoto spacePhoto) {
        spacePhoto.setVisible(false);
    }

    @Atomic
    public void showSpacePhoto(SpacePhoto spacePhoto) {
        spacePhoto.setVisible(true);
    }

    @Atomic
    public void removeSpacePhoto(Space space, SpacePhoto spacePhoto) {
        space.getSpacePhotoSet().orElse(Collections.<SpacePhoto> emptySet()).remove(spacePhoto);
        spacePhoto.getSubmission().setModified(new DateTime());
        space.addSpacePhotoSubmissionArchived(spacePhoto.getSubmission());
    }

    public List<SpacePhotoSubmission> getUserSpacePhotoSubmissions(User user) {
        return user.getSpacePhotoSubmissionSet().stream().sorted(SpacePhotoSubmission.COMPARATOR_BY_INSTANT)
                .collect(Collectors.toList());
    }
}