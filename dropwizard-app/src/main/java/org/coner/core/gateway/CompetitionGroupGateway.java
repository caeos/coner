package org.coner.core.gateway;

import com.google.common.base.Preconditions;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.domain.CompetitionGroup;
import org.coner.hibernate.dao.CompetitionGroupDao;

import java.util.List;

/**
 * CompetitionGroupGateway wraps persistence layer interactions for CompetitionGroup domain entities.
 */
public class CompetitionGroupGateway {

    private final CompetitionGroupBoundary boundary;
    private final CompetitionGroupDao dao;

    /**
     * Constructor for CompetitionGroupGateway.
     *
     * @param boundary the CompetitionGroupBoundary for converting Domain entities to/from Hibernate entities
     * @param dao      the CompetitionGroupDao for interacting with the persistence layer
     */
    public CompetitionGroupGateway(CompetitionGroupBoundary boundary, CompetitionGroupDao dao) {
        this.boundary = boundary;
        this.dao = dao;
    }

    /**
     * Create a new CompetitionGroup entity.
     *
     * @param competitionGroup the CompetitionGroup to create
     */
    public void create(CompetitionGroup competitionGroup) {
        Preconditions.checkNotNull(competitionGroup);
        org.coner.hibernate.entity.CompetitionGroup hCompetitionGroup = boundary.toHibernateEntity(competitionGroup);
        dao.createOrUpdate(hCompetitionGroup);
        boundary.merge(hCompetitionGroup, competitionGroup);
    }

    /**
     * Get all CompetitionGroup entities.
     *
     * @return list of CompetitionGroup entities
     */
    public List<CompetitionGroup> getAll() {
        List<org.coner.hibernate.entity.CompetitionGroup> competitionGroups = dao.findAll();
        return boundary.toDomainEntities(competitionGroups);
    }
}
