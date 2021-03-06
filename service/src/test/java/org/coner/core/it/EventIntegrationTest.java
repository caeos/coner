package org.coner.core.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.api.response.GetEventsResponse;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.IntegrationTestStandardRequestDelegate;
import org.coner.core.util.IntegrationTestUtils;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import io.dropwizard.jersey.validation.ValidationErrorMessage;


public class EventIntegrationTest extends AbstractIntegrationTest {

    private IntegrationTestStandardRequestDelegate standardRequests;

    private Prerequisites prerequisites;

    @Before
    public void setup() {
        standardRequests = new IntegrationTestStandardRequestDelegate(RULE, client);
        prerequisites = setupPrerequisites();
    }

    @Test
    public void itShouldRoundTrip() {
        EventApiEntity expected = ApiEntityTestUtils.fullEvent();
        expected.setHandicapGroupSetId(prerequisites.handicapGroupSetId);
        expected.setCompetitionGroupSetId(prerequisites.competitionGroupSetId);
        URI eventsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events")
                .build();
        AddEventRequest addEventRequest = ApiRequestTestUtils.fullAddEvent();
        addEventRequest.setHandicapGroupSetId(prerequisites.handicapGroupSetId);
        addEventRequest.setCompetitionGroupSetId(prerequisites.competitionGroupSetId);
        Response addEventResponseContainer = client.target(eventsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addEventRequest));

        assertThat(addEventResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        expected.setId(UnitTestUtils.getEntityIdFromResponse(addEventResponseContainer));

        Response getEventsResponseContainer = client.target(eventsUri)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getEventsResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        GetEventsResponse getEventsResponse = getEventsResponseContainer.readEntity(GetEventsResponse.class);
        assertThat(getEventsResponse.getEntities()).hasSize(1);
        EventApiEntity eventInList = getEventsResponse.getEntities().get(0);
        assertThat(eventInList).isEqualTo(expected);

        URI getEventByIdUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{id}")
                .build(expected.getId());

        Response getEventByIdResponseContainer = client.target(getEventByIdUri)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getEventByIdResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        EventApiEntity getEventByIdResponse = getEventByIdResponseContainer.readEntity(EventApiEntity.class);
        assertThat(getEventByIdResponse).isEqualTo(expected);
    }

    @Test
    public void whenCreateEventInvalidItShouldReject() {
        URI eventsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events")
                .build();
        AddEventRequest addEventRequest = ApiRequestTestUtils.fullAddEvent();
        addEventRequest.setName(null);

        Response addEventResponseContainer = client.target(eventsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addEventRequest));

        assertThat(addEventResponseContainer.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ValidationErrorMessage validationErrorMessage = addEventResponseContainer.readEntity(
                ValidationErrorMessage.class
        );
        assertThat(validationErrorMessage.getErrors()).isNotEmpty();
    }

    private Prerequisites setupPrerequisites() {
        Prerequisites prerequisites = new Prerequisites();
        prerequisites.handicapGroupId = standardRequests.addHandicapGroup();
        prerequisites.handicapGroupSetId = standardRequests.addHandicapGroupSet(
                Sets.newHashSet(prerequisites.handicapGroupId)
        );
        prerequisites.competitionGroupId = standardRequests.addCompetitionGroup();
        prerequisites.competitionGroupSetId = standardRequests.addCompetitionGroupSet(
                Sets.newHashSet(prerequisites.competitionGroupId)
        );
        return prerequisites;
    }

    private static class Prerequisites {
        private String handicapGroupId;
        private String handicapGroupSetId;
        private String competitionGroupId;
        private String competitionGroupSetId;
    }

}
