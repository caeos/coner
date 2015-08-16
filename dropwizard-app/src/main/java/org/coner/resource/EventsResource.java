package org.coner.resource;

import org.coner.api.entity.EventApiEntity;
import org.coner.api.request.AddEventRequest;
import org.coner.api.response.*;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Events")
public class EventsResource {

    private final ConerCoreService conerCoreService;
    private final EventApiDomainBoundary apiDomainEntityBoundary;
    private final EventApiAddPayloadBoundary apiAddPayloadBoundary;

    public EventsResource(
            ConerCoreService conerCoreService,
            EventApiDomainBoundary eventApiDomainBoundary,
            EventApiAddPayloadBoundary eventApiAddPayloadBoundary
    ) {
        this.conerCoreService = conerCoreService;
        this.apiDomainEntityBoundary = eventApiDomainBoundary;
        this.apiAddPayloadBoundary = eventApiAddPayloadBoundary;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a list of all events", response = GetEventsResponse.class)
    public GetEventsResponse getEvents() {
        List<Event> domainEvents = conerCoreService.getEvents();
        GetEventsResponse response = new GetEventsResponse();
        response.setEvents(apiDomainEntityBoundary.toLocalEntities(domainEvents));
        return response;
    }

    @POST
    @UnitOfWork
    @ApiOperation(value = "Add an Event", response = Response.class)
    @ApiResponses({
            @ApiResponse(
                    code = HttpStatus.CREATED_201,
                    response = Void.class,
                    message = "Created at URI in Location header"
            ),
            @ApiResponse(
                    code = HttpStatus.UNPROCESSABLE_ENTITY_422,
                    response = ErrorsResponse.class,
                    message = "Failed validation"
            )
    })
    public Response addEvent(
            @Valid @ApiParam(value = "Event", required = true) AddEventRequest request
    ) {
        EventAddPayload addPayload = apiAddPayloadBoundary.toRemoteEntity(request);
        Event domainEntity = conerCoreService.addEvent(addPayload);
        EventApiEntity eventApiEntity = apiDomainEntityBoundary.toLocalEntity(domainEntity);
        return Response.created(UriBuilder.fromResource(EventResource.class)
                .build(eventApiEntity.getId()))
                .build();
    }
}
