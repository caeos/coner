package org.coner.core.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.RunApiEntity;
import org.coner.core.api.request.AddRunRequest;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.IntegrationTestStandardRequestDelegate;
import org.coner.core.util.IntegrationTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class RunIntegrationTest extends AbstractIntegrationTest {

    private IntegrationTestStandardRequestDelegate standardRequests;
    private String eventId;
    private String registrationId;

    @Before
    public void setup() {
        standardRequests = new IntegrationTestStandardRequestDelegate(RULE, client);
        eventId = standardRequests.addEvent();
        String handicapGroupId = standardRequests.addHandicapGroup();
        String competitionGroupId = standardRequests.addCompetitionGroup();
        registrationId = standardRequests.addRegistration(eventId);
    }

    @Test
    public void itShouldAddRun() {
        URI eventRunsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/runs")
                .build(eventId);
        AddRunRequest addRequest = ApiRequestTestUtils.fullAddRun();
        addRequest.setRegistrationId(registrationId);

        Response addResponseContainer = client.target(eventRunsUri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(addRequest));

        assertThat(addResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
    }

    @Test
    public void itShouldGetRun() {
        String runId = standardRequests.addRun(eventId, registrationId);
        RunApiEntity expected = ApiEntityTestUtils.fullRun();
        expected.setId(runId);
        expected.setEventId(eventId);
        expected.setRegistrationId(registrationId);

        URI eventRunUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/runs/{runId}")
                .build(eventId, runId);
        Response getResponseContainer = client.target(eventRunUri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get();
        RunApiEntity actual = getResponseContainer.readEntity(RunApiEntity.class);

        assertThat(getResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        assertThat(actual).isEqualTo(expected);
    }
}
