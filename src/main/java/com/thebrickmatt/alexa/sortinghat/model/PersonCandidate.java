package com.thebrickmatt.alexa.sortinghat.model;

import com.google.common.base.Objects;

import java.util.HashSet;
import java.util.Set;

public class PersonCandidate {

    private String name;
    private Set<String> nameAliases = new HashSet<>();
    private String resultText = "";


    public PersonCandidate(String name) {
        this.name = name;
        addNameAlias(name);
    }

    public String getName() {
        return name;
    }

    public void addNameAlias(String alias) {
        nameAliases.add(alias.toLowerCase());
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public boolean matchesAlias(String candidateName) {
        if (candidateName == null) {
            return false;
        }
        return nameAliases.contains(candidateName.toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonCandidate that = (PersonCandidate) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(nameAliases, that.nameAliases) &&
                Objects.equal(resultText, that.resultText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, nameAliases, resultText);
    }
}
