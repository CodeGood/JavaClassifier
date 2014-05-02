/* 
 * Copyright (C) 2014 Vasilis Vryniotis <bbriniotis at datumbox.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.appointext.features;

import com.appointext.dataobjects.Document;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * TextTokenizer class used to tokenize the texts and store them as Document
 * objects.
 * 
 * @author Vasilis Vryniotis <bbriniotis at datumbox.com>
 * @see <a href="http://blog.datumbox.com/developing-a-naive-bayes-text-classifier-in-java/">http://blog.datumbox.com/developing-a-naive-bayes-text-classifier-in-java/</a>
 */
public class TextTokenizer {

	public static String[] queryKeyWords = {"do you", "let's", "shall we", "going", "coming", "come", "go", "when", "where", "meeting", "meet", "movie", "lunch", "dinner", "movie", "coming", "going", "can", "will", "are", "do", "are we going", "can we go", "do we go", "let's go", "do we go", "meet for", "want to"};
	public static String[] replyKeyWords = {"roger that", "affirmative", "pleasure", "yes", "yup", "no", "nope", "not", "definitely", "sure", "don't", "do", "refuse", "accept", "agree", "disagree", "reject", "diffcult", "hard", "present", "absent", "presence", "absence", "love", "like", "hate", "dislike"};
	public static String[] meetingKeyWords = {"going", "coming", "come", "go", "dinner", "out", "meeting", "meet", "movie", "lunch", "dinner", "movie", "coming", "going", "can", "will", "are", "do"};

	
	private static int flag = 0;

	/**
	 * Preprocess the text by removing punctuation, duplicate spaces and 
	 * lowercasing it.
	 * 
	 * TODO: Add removal of smileys, new lines.
	 * TODO: Capital letters may help in recognizing named entities. So may be NOT removing it is a better idea.
	 * 
	 * @param text
	 * @return 
	 */
	public static String preprocess(String text) {
		flag = 0;
		if(text.contains("?"))
			flag = 1;
		return text.replaceAll("\\p{P}", " ").replaceAll("\\s+", " ").toLowerCase(Locale.getDefault());
	}

	/**
	 * A simple method to extract the keywords from the text. For real world 
	 * applications it is necessary to extract also keyword combinations.
	 * 
	 * @param text
	 * @return 
	 */
	public static String[] extractKeywords(String text) {
		return text.split(" ");
	}

	/**
	 * Counts the number of occurrences of the keywords inside the text.
	 * 
	 * @param keywordArray
	 * @return 
	 */
	public static Map<String, Integer> getKeywordCounts(String[] keywordArray) {
		Map<String, Integer> counts = new HashMap<String, Integer>();

		Integer counter;
		for(int i=0;i<keywordArray.length;++i) {
			counter = counts.get(keywordArray[i]);
			if(counter==null) {
				counter=0;
			}
			counts.put(keywordArray[i], ++counter); //increase counter for the keyword

			if (flag == 1)
				counter+= 5;
			
			for(int j=0;j<queryKeyWords.length;++j) 	{
				if(keywordArray[i].equalsIgnoreCase(queryKeyWords[j]))	{
					counts.put(keywordArray[i], ++counter);
					System.out.println(keywordArray[i]);
				}
			}

			for(int j=0;j<replyKeyWords.length;++j) 	{
				
				if(keywordArray[i].equalsIgnoreCase(replyKeyWords[j]))	{
					counts.put(keywordArray[i], ++counter);
					System.out.println(keywordArray[i]);
				}
			}

			for(int j=0;j<meetingKeyWords.length;++j) 	{
				if(keywordArray[i].equalsIgnoreCase(meetingKeyWords[j]))	{
					counts.put(keywordArray[i], ++counter);
					System.out.println(keywordArray[i]);
				}
			}

		}

		return counts;
	}

	/**
	 * Tokenizes the document and returns a Document Object.
	 * 
	 * @param text
	 * @return 
	 */
	public static Document tokenize(String text) {
		String preprocessedText = preprocess(text);
		String[] keywordArray = extractKeywords(preprocessedText);

		Document doc = new Document();
		doc.tokens = getKeywordCounts(keywordArray);
		return doc;
	}
}
