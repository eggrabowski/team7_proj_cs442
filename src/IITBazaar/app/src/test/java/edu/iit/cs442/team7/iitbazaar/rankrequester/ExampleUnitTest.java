package edu.iit.cs442.team7.iitbazaar;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {





        RankRequesterService rrs = new RankRequesterService();

        rrs.getDepartment("Janusz","Nosek","jnosek@hawk.iit.edu");




        assertTrue(true);
    }
}