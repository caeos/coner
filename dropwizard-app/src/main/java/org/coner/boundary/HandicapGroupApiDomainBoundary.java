package org.coner.boundary;

import org.coner.api.entity.HandicapGroupApiEntity;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionJavaBeanMerger;

public class HandicapGroupApiDomainBoundary extends AbstractBoundary<HandicapGroupApiEntity, HandicapGroup> {
    @Override
    protected ObjectMerger<HandicapGroupApiEntity, HandicapGroup> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<HandicapGroup, HandicapGroupApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
