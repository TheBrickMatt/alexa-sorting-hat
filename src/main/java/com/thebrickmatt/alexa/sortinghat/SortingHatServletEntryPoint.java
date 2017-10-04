package com.thebrickmatt.alexa.sortinghat;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

// This is the entry point if you are deploying as a war, such as in elastic beanstalk
public class SortingHatServletEntryPoint extends SpeechletServlet {

    public SortingHatServletEntryPoint() {
        this.setSpeechlet(new SortingHatSpeechlet());
    }

}
