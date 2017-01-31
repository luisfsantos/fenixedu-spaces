package org.fenixedu.spaces.domain.submission;

import java.util.Comparator;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.spaces.domain.SpaceDomainException;
import org.joda.time.DateTime;

public class SpacePhoto extends SpacePhoto_Base {

    @Deprecated
    public SpacePhoto(String filename, byte[] content, User submitor) {
        this(filename, content);
        setSubmitor(submitor);
    }

    public SpacePhoto(String filename, byte[] content) {
        super();
        setCreated(new DateTime());
        setVisible(true);
        init(filename, filename, content);
    }

    public static final Comparator<SpacePhoto> COMPARATOR_BY_INSTANT = new Comparator<SpacePhoto>() {

        @Override
        public int compare(SpacePhoto ps1, SpacePhoto ps2) {
            int ps = ps1.getCreated().compareTo(ps2.getCreated());
            return ps != 0 ? ps : ps1.getExternalId().compareTo(ps2.getExternalId());
        }
    };

    public boolean isVisible() {
        return this.getVisible();
    }

    @Override
    public void setSubmitor(User submitor) {
        if (submitor != null) {
            super.setSubmitor(submitor);
        }
    }

    @Override
    public void setCreated(DateTime instant) {
        if (instant == null) {
            throw new SpaceDomainException("error.OccupationRequest.empty.instant");
        }
        super.setCreated(instant);
    }

    @Override
    public boolean isAccessible(User user) {
        return true;
    }

    @Override
    public void delete() {
        super.setSubmitor(null);
        super.delete();
    }

}
