package com.appointext.postagger;
import java.io.IOException;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
 
public class tagText {
	public static String posTagger(String message) throws IOException,
		ClassNotFoundException {
		 
			// Initialize the tagger
			MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
			 
			// The tagged string
			String tagged = tagger.tagString(message);
			 
			// return the tagged code
			
			return tagged;
		}
}