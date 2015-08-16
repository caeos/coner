package org.coner.boundary;

import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.util.merger.*;

public class CompetitionGroupSetHibernateAddPayloadBoundary extends AbstractBoundary<
        CompetitionGroupSetHibernateEntity,
        CompetitionGroupSetAddPayload> {
    @Override
    protected ObjectMerger<CompetitionGroupSetHibernateEntity, CompetitionGroupSetAddPayload>
    buildLocalToRemoteMerger() {
        return new UnsupportedOperationMerger<>();
    }

    @Override
    protected ObjectMerger<CompetitionGroupSetAddPayload, CompetitionGroupSetHibernateEntity>
    buildRemoteToLocalMerger() {
        return ReflectionPayloadJavaBeanMerger.payloadToJavaBean();
    }
}
