package org.coner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.CompetitionGroup;
import org.coner.api.response.GetCompetitionGroupsResponse;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.ConerCoreService;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * The CompetitionGroupsResource exposes getting and adding CompetitionGroups
 * via the REST API.
 */
@Path("/competitionGroups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompetitionGroupsResource {

    private final CompetitionGroupBoundary competitionGroupBoundary;
    private final ConerCoreService conerCoreService;

    /**
     * Constructor for the CompetitionGroupsResource.
     *
     * @param competitionGroupBoundary the HandicapGroupBoundary to use for converting API and Domain Handicap Group
     *                                 entities
     * @param conerCoreService         the ConerCoreService
     */
    public CompetitionGroupsResource(
            CompetitionGroupBoundary competitionGroupBoundary,
            ConerCoreService conerCoreService
    ) {
        this.competitionGroupBoundary = competitionGroupBoundary;
        this.conerCoreService = conerCoreService;
    }

    /**
     * Add a competition group.
     *
     * @param competitionGroup the competitionGroup to add
     * @return a response containing the added Event
     */
    @POST
    @UnitOfWork
    public Response addCompetitionGroup(@Valid CompetitionGroup competitionGroup) {
        org.coner.core.domain.CompetitionGroup domainCompetitionGroup = competitionGroupBoundary.toDomainEntity(
                competitionGroup
        );
        conerCoreService.addCompetitionGroup(domainCompetitionGroup);
        return Response.created(UriBuilder.fromResource(CompetitionGroupResource.class)
                .build(domainCompetitionGroup.getId()))
                .build();
    }

    /**
     * Get all competition groups.
     *
     * @return a list of all competition groups
     */
    @GET
    @UnitOfWork
    public GetCompetitionGroupsResponse getCompetitionGroups() {
        List<org.coner.core.domain.CompetitionGroup> domainCompetitionGroups = conerCoreService.getCompetitionGroups();
        GetCompetitionGroupsResponse response = new GetCompetitionGroupsResponse();
        response.setCompetitionGroups(competitionGroupBoundary.toApiEntities(domainCompetitionGroups));
        return response;
    }
}
