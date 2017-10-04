package com.thebrickmatt.alexa.sortinghat;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

// This is the entry point for Lamba functions
public final class SortingHatLambaEntryPoint extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds;

    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("amzn1.ask.skill.30d30149-86d9-4e07-9e51-79f2803639e7");
    }

    public SortingHatLambaEntryPoint() {
        super(new SortingHatSpeechlet(), supportedApplicationIds);
    }
}
