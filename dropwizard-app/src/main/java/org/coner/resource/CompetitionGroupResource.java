package org.coner.resource;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.CompetitionGroupApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.exception.EntityNotFoundException;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.eclipse.jetty.http.HttpStatus;

@Path("/competitionGroups/{competitionGroupId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Competition Groups")
public class CompetitionGroupResource {

    private final CompetitionGroupApiDomainBoundary competitionGroupApiDomainBoundary;
    private final ConerCoreService conerCoreService;

    public CompetitionGroupResource(
            CompetitionGroupApiDomainBoundary competitionGroupApiDomainBoundary,
            ConerCoreService conerCoreService
    ) {
        this.competitionGroupApiDomainBoundary = competitionGroupApiDomainBoundary;
        this.conerCoreService = conerCoreService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a Competition Group", response = CompetitionGroupApiEntity.class)
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = CompetitionGroupApiEntity.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorsResponse.class, message = "Not found")
    })
    public CompetitionGroupApiEntity getCompetitionGroup(
            @PathParam("competitionGroupId") @ApiParam(value = "Competition Group ID", required = true) String id
    ) {
        CompetitionGroup domainEntity = null;
        try {
            domainEntity = conerCoreService.getCompetitionGroup(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No competition group with id " + id);
        }
        return competitionGroupApiDomainBoundary.toLocalEntity(domainEntity);
    }
}
