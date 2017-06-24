package org.coner.core.api.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class RegistrationApiEntity extends ApiEntity {

    @Null(message = "registration.id may only be assigned by the system")
    private String id;
    @NotNull
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String handicapGroupId;
    @NotBlank
    private String competitionGroupId;
    @NotBlank
    private String number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHandicapGroupId() {
        return handicapGroupId;
    }

    public void setHandicapGroupId(String handicapGroupId) {
        this.handicapGroupId = handicapGroupId;
    }

    public String getCompetitionGroupId() {
        return competitionGroupId;
    }

    public void setCompetitionGroupId(String competitionGroupId) {
        this.competitionGroupId = competitionGroupId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
