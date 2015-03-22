package org.coner.resource;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.coner.api.entity.Event;
import org.coner.boundary.EventBoundary;
import org.coner.core.ConerCoreService;
import org.coner.util.ApiEntityTestUtils;
import org.coner.util.DomainEntityTestUtils;
import org.coner.util.TestConstants;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 *
 */
public class EventResourceTest {

    private final EventBoundary eventBoundary = mock(EventBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventResource(eventBoundary, conerCoreService))
            .build();


    @Before
    public void setup() {
        reset(eventBoundary, conerCoreService);
    }

    @Test
    public void itShouldGetEvent() {
        org.coner.core.domain.Event domainEvent = DomainEntityTestUtils.fullDomainEvent();
        Event apiEvent = ApiEntityTestUtils.fullApiEvent();

        // sanity check test
        assertThat(domainEvent.getId()).isSameAs(TestConstants.EVENT_ID);
        assertThat(apiEvent.getId()).isSameAs(TestConstants.EVENT_ID);

        when(conerCoreService.getEvent(TestConstants.EVENT_ID)).thenReturn(domainEvent);
        when(eventBoundary.toApiEntity(domainEvent)).thenReturn(apiEvent);

        Response getEventResponseContainer = resources.client()
                .target("/events/" + TestConstants.EVENT_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getEvent(TestConstants.EVENT_ID);
        verify(eventBoundary).toApiEntity(domainEvent);
        verifyNoMoreInteractions(conerCoreService, eventBoundary);

        assertThat(getEventResponseContainer).isNotNull();
        assertThat(getEventResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        Event getEventResponse = getEventResponseContainer.readEntity(Event.class);
        assertThat(getEventResponse)
                .isNotNull()
                .isEqualTo(apiEvent);
    }

    @Test
    public void itShouldRespondWithNotFoundErrorWhenEventIdInvalid() {
        when(conerCoreService.getEvent(TestConstants.EVENT_ID)).thenReturn(null);

        Response response = resources.client()
                .target("/events/" + TestConstants.EVENT_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getEvent(TestConstants.EVENT_ID);
        verifyNoMoreInteractions(conerCoreService);
        verifyZeroInteractions(eventBoundary);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

}
