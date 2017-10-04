package com.thebrickmatt.alexa.sortinghat;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.google.common.collect.Sets;
import com.thebrickmatt.alexa.sortinghat.model.GameState;
import com.thebrickmatt.alexa.sortinghat.transform.SessionHelper;
import com.thebrickmatt.alexa.sortinghat.transform.SpeechBuilder;

public class SortingHatSpeechlet implements Speechlet {

    public static final String MY_BLUEPRINT_INTENT = "BluePrintIntent";
    public static final String SORT_PERSON_INTENT = "SortPersonIntent";
    public static final String AMAZON_STOP_INTENT = "AMAZON.StopIntent";
    public static final String AMAZON_CANCEL_INTENT = "AMAZON.CancelIntent";
    public static final String AMAZON_HELP_INTENT = "AMAZON.HelpIntent";
    public static final String SLOT_PERSON_NAME = "PersonName";

    /**
     * Called when a new session is started
     */
    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {

        // initialization logic
        final SessionHelper sessionHelper = new SessionHelper(session);
        sessionHelper.setGameState(GameState.INITIALIZING);
    }

    /**
     * Called when a simple start command is issued, otherwise the onIntent is called
     */
    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        final SessionHelper sessionHelper = new SessionHelper(session);
        sessionHelper.setGameState(GameState.INITIALIZING);
        return SpeechBuilder.wrapAskSpeech("Welcome to the Alexa Blue Print. What can I do for you?");
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        final SessionHelper sessionHelper = new SessionHelper(session);

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        // NOTE: Session Attributes don't work like a servlet session.
        // Even though they take object, they are marshaled via JSON, so enum becomes string, etc.
        GameState gameState = sessionHelper.getGameState();

        if (AMAZON_HELP_INTENT.equals(intentName)) {
            return SpeechBuilder.wrapAskSpeech("Sounds like you need help.  Am I right?", "Am I right?");
        }

        if (AMAZON_STOP_INTENT.equals(intentName)) {
            return SpeechBuilder.wrapTellSpeech("Hammer Time. Good bye from blue print.");
        }

        if (AMAZON_CANCEL_INTENT.equals(intentName)) {
            return SpeechBuilder.wrapTellSpeech("Canceling. Good bye from blue print.");
        }

        if (SORT_PERSON_INTENT.equals(intentName)) {
            final String personName = translateMungedNames(intent.getSlot(SLOT_PERSON_NAME).getValue());
            if (personName == null) {
                return SpeechBuilder.wrapTellSpeech("You asked me to sort someone I don't know");
            } else if ("dobi".equalsIgnoreCase(personName)) {
                return SpeechBuilder.wrapTellSpeech("Dobi has no house. He is a free elf.");
            } else if ("brittany".equalsIgnoreCase(personName)) {
                return SpeechBuilder.wrapTellSpeech(personName + " belongs in house Slytherin");
            } else if ("allison".equalsIgnoreCase(personName)) {
                return SpeechBuilder.wrapTellSpeech(personName + " belongs in house Ravenclaw");
            } else if ("abby".equalsIgnoreCase(personName)) {
                return SpeechBuilder.wrapTellSpeech(personName + " belongs in house Hufflepuff");
            } else if ("colton".equalsIgnoreCase(personName)) {
                return SpeechBuilder.wrapTellSpeech(personName + " belongs in house Griffindor");

            } else {
                System.out.println("sorting non-indexed person: " + personName);
                return SpeechBuilder.wrapTellSpeech("You asked me to sort " + personName);
            }
        }


        return SpeechBuilder.wrapTellSpeech("I don't know how to handle your intent, which was " + intentName);
    }

    private String translateMungedNames(String value) {
        if (value == null) {
            return null;
        }

        final String DOBI = "dobi";
        if ("dhabi".equals(value)) {
            return DOBI;
        }
        if ("dobby".equals(value)) {
            return DOBI;
        }
        if ("dog".equals(value)) {
            return DOBI;
        }
        if ("doobie".equals(value)) {
            return DOBI;
        }

        return value;
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        // clean things up
    }
}
