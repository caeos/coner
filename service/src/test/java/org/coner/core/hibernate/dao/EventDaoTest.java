package org.coner.core.hibernate.dao;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.util.HibernateEntityTestUtils;
import org.coner.core.util.TestConstants;
import org.hibernate.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.testing.junit.DAOTestRule;

public class EventDaoTest extends AbstractDaoTest {

    private EventDao eventDao;

    @Rule
    public DAOTestRule daoTestRule = getDaoTestRuleBuilder()
        .addEntityClass(EventHibernateEntity.class)
        .addEntityClass(RegistrationHibernateEntity.class)
        .build();

    @Before
    public void setup() {
        eventDao = new EventDao(daoTestRule.getSessionFactory());

        daoTestRule.inTransaction(() -> {
            Query delete = daoTestRule.getSessionFactory()
                    .getCurrentSession().createQuery("delete from EventHibernateEntity");
            delete.executeUpdate();
        });
    }

    @After
    public void tearDown() {
    }

    @Test
    public void whenFindAllItShouldReturnEmpty() {
        daoTestRule.inTransaction(() -> {
            List<EventHibernateEntity> actual = eventDao.findAll();
            assertThat(actual)
                    .isNotNull()
                    .isEmpty();
        });
    }

    @Test
    public void whenCreateItShouldCreateEvent() {
        EventHibernateEntity newEvent = buildNewEvent();

        daoTestRule.inTransaction(() -> {
            eventDao.create(newEvent);
        });

        assertThat(newEvent.getId())
                .isNotEmpty();
    }

    @Test
    public void whenCreatedItShouldFindById() {
        EventHibernateEntity newEvent = buildNewEvent();
        daoTestRule.inTransaction(() -> {
            eventDao.create(newEvent);
        });

        EventHibernateEntity actual = eventDao.findById(newEvent.getId());

        assertThat(actual)
                .isNotNull()
                .isEqualTo(newEvent);
    }

    @Test
    public void whenCreateItShouldBeInFindAll() {
        EventHibernateEntity newEvent = buildNewEvent();
        daoTestRule.inTransaction(() -> {
            eventDao.create(newEvent);
        });

        List<EventHibernateEntity> events = eventDao.findAll();

        assertThat(events)
                .isNotNull()
                .isNotEmpty()
                .containsOnly(newEvent);
    }

    private EventHibernateEntity buildNewEvent() {
        return HibernateEntityTestUtils.fullEvent(
                null,
                TestConstants.EVENT_NAME,
                TestConstants.EVENT_DATE
        );
    }
}
