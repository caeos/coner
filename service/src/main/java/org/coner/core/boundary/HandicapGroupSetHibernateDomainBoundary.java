package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.core.util.merger.CompositeMerger;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionJavaBeanMerger;

public class HandicapGroupSetHibernateDomainBoundary extends AbstractBoundary<
        HandicapGroupSetHibernateEntity,
        HandicapGroupSet> {

    @Inject
    public HandicapGroupSetHibernateDomainBoundary() {
    }

    @Override
    protected ObjectMerger<HandicapGroupSetHibernateEntity, HandicapGroupSet> buildLocalToRemoteMerger() {
        return new CompositeMerger<>(
                new ReflectionJavaBeanMerger<>(),
                (source, destination) -> {
                    destination.setId(source.getHandicapGroupSetId());
                }
        );
    }

    @Override
    protected ObjectMerger<HandicapGroupSet, HandicapGroupSetHibernateEntity> buildRemoteToLocalMerger() {
        return new CompositeMerger<>(
                new ReflectionJavaBeanMerger<>(),
                (source, destination) -> {
                    destination.setHandicapGroupSetId(source.getId());
                }
        );
    }
}
