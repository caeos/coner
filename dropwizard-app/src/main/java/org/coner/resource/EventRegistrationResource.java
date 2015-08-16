package org.coner.resource;

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.RegistrationApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.Registration;
import org.coner.core.exception.*;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;

@Path("/events/{eventId}/registrations/{registrationId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Event Registrations")
public class EventRegistrationResource {

    private final RegistrationApiDomainBoundary registrationApiDomainBoundary;
    private final ConerCoreService conerCoreService;

    public EventRegistrationResource(
            RegistrationApiDomainBoundary registrationApiDomainBoundary,
            ConerCoreService conerCoreService) {
        this.registrationApiDomainBoundary = registrationApiDomainBoundary;
        this.conerCoreService = conerCoreService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a specific registration")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = RegistrationApiEntity.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorsResponse.class, message = "Not found"),
            @ApiResponse(
                    code = HttpStatus.CONFLICT_409,
                    response = ErrorsResponse.class,
                    message = "Event ID and Registration ID are mismatched"
            )
    })
    public Response getRegistration(
            @PathParam("eventId") @ApiParam(value = "Event ID", required = true) String eventId,
            @PathParam("registrationId") @ApiParam(value = "Registration ID", required = true) String registrationId
    ) {
        Registration domainRegistration;
        try {
            domainRegistration = conerCoreService.getRegistration(eventId, registrationId);
        } catch (EventMismatchException e) {
            return Response.status(Response.Status.CONFLICT)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(
                            new ErrorsResponse(
                                    Response.Status.CONFLICT.getReasonPhrase(),
                                    e.getMessage()
                            )
                    )
                    .build();
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No registration with id " + registrationId);
        }

        RegistrationApiEntity registration = registrationApiDomainBoundary.toLocalEntity(domainRegistration);

        return Response.ok(registration, MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
