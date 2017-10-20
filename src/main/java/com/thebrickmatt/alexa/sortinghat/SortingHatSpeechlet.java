package com.thebrickmatt.alexa.sortinghat;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.thebrickmatt.alexa.sortinghat.model.GameState;
import com.thebrickmatt.alexa.sortinghat.model.PersonCandidate;
import com.thebrickmatt.alexa.sortinghat.transform.SessionHelper;
import com.thebrickmatt.alexa.sortinghat.transform.SpeechBuilder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SortingHatSpeechlet implements Speechlet {

    public static final String SORT_PERSON_INTENT = "SortPersonIntent";
    public static final String AMAZON_STOP_INTENT = "AMAZON.StopIntent";
    public static final String AMAZON_CANCEL_INTENT = "AMAZON.CancelIntent";
    public static final String AMAZON_HELP_INTENT = "AMAZON.HelpIntent";
    public static final String SLOT_PERSON_NAME = "PersonName";

    public static final Set<PersonCandidate> people = initPeople();


    private static final String slytherinAudio = "<audio src='https://s3.amazonaws.com/thebrickmatt-sorting-hat/SortingHatSlytherin_10db.mp3'/>";
    private static final String hufflepuffAudio = "<audio src='https://s3.amazonaws.com/thebrickmatt-sorting-hat/SortingHatHufflepuff_10db.mp3'/>";
    private static final String griffindorAudio = "<audio src='https://s3.amazonaws.com/thebrickmatt-sorting-hat/SortingHatGryffindor_10db.mp3'/>";
    private static final String ravenclawAudio = "<audio src='https://s3.amazonaws.com/thebrickmatt-sorting-hat/SortingHatRavenclaw_10db.mp3'/>";

    private static Set<PersonCandidate> initPeople() {
        Set<PersonCandidate> people = new HashSet<>();

        // Brittany
        final PersonCandidate brittany = new PersonCandidate("Brittany");
        brittany.addNameAlias("britney");
        brittany.addNameAlias("britney");
        brittany.setResultText(slytherinAudio);
        people.add(brittany);

        // Colton
        final PersonCandidate colton = new PersonCandidate("Colton");
        colton.addNameAlias("colten");
        colton.addNameAlias("coltin");
        colton.addNameAlias("cottin");
        colton.addNameAlias("cotton");
        colton.setResultText(ravenclawAudio);
        people.add(colton);

        // Dad
        final PersonCandidate dad = new PersonCandidate("Dad");
        dad.addNameAlias("Matt");
        dad.addNameAlias("Matthew");
        dad.addNameAlias("daddy");
        dad.setResultText(ravenclawAudio);
        people.add(dad);

        // Mom
        final PersonCandidate mom = new PersonCandidate("Mom");
        mom.addNameAlias("Julie");
        mom.addNameAlias("mommy");
        mom.setResultText(hufflepuffAudio);
        people.add(mom);

        // Percy
        final PersonCandidate percy = new PersonCandidate("Percy");
        percy.addNameAlias("persee");
        percy.addNameAlias("perchy");
        percy.setResultText(hufflepuffAudio);
        people.add(percy);

        // Dobi
        final PersonCandidate dobi = new PersonCandidate("Dobi");
        dobi.addNameAlias("dhabi");
        dobi.addNameAlias("dobby");
        dobi.addNameAlias("dog");
        dobi.addNameAlias("doobie");
        dobi.setResultText("Dobi has no house. He is a free elf.");
        people.add(dobi);

        // Emerson
        final PersonCandidate emerson = new PersonCandidate("Emerson");
        emerson.setResultText(griffindorAudio);
        people.add(emerson);

        // Grandma
        final PersonCandidate grandma = new PersonCandidate("Grandma");
        grandma.addNameAlias("alice");
        grandma.setResultText(griffindorAudio);
        people.add(grandma);

        // Allison
        final PersonCandidate allison = new PersonCandidate("Allison");
        allison.setResultText(ravenclawAudio);
        people.add(allison);

        // Abby
        // Maricarmen
        // ???

        return people;
    }

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
        return SpeechBuilder.wrapAskSpeech("Welcome to Hogwarts. I wonder what kind of wizard you are.  What is your name.");
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
//        final SessionHelper sessionHelper = new SessionHelper(session);

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        // NOTE: Session Attributes don't work like a servlet session.
        // Even though they take object, they are marshaled via JSON, so enum becomes string, etc.
//        GameState gameState = sessionHelper.getGameState();

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
            final String personName = intent.getSlot(SLOT_PERSON_NAME).getValue();
            final Optional<PersonCandidate> maybePersonCandidate = translateInputToPerson(personName);
            if (maybePersonCandidate.isPresent()) {
                return SpeechBuilder.wrapTellSpeech(maybePersonCandidate.get().getResultText());
            } else {
                System.out.println("sorting non-indexed person: " + personName);
                return SpeechBuilder.wrapTellSpeech("<say-as interpret-as=\"interjection\">zoinks!</say-as> Muggles have no house! ");

                //+ "This muggle's name was: <say-as interpret-as=\"spell-out\">" + personName + "</say-as>");
            }
        }

        return SpeechBuilder.wrapTellSpeech("<say-as interpret-as=\"interjection\">le sigh.</say-as> I don't understand what you asked me to do");
    }

    private Optional<PersonCandidate> translateInputToPerson(String value) {
        for (PersonCandidate candidate : people) {
            if (candidate.matchesAlias(value)) {
                return Optional.of(candidate);
            }
        }

        return Optional.empty();
    }


    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        // clean things up
    }
}
