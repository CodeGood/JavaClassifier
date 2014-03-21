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
package com.appointext.examples;

import com.appointext.classifiers.NaiveBayes;
import com.appointext.dataobjects.NaiveBayesKnowledgeBase;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Vasilis Vryniotis <bbriniotis at datumbox.com>
 * @see <a href="http://blog.datumbox.com/developing-a-naive-bayes-text-classifier-in-java/">http://blog.datumbox.com/developing-a-naive-bayes-text-classifier-in-java/</a>
 */
public class NaiveBayesExample {

    /**
     * Reads the all lines from a file and places it a String array. In each 
     * record in the String array we store a training example text.
     * 
     * @param url
     * @return
     * @throws IOException 
     */
    public static String[] readLines(String filename) throws IOException {

        Reader fileReader = new FileReader(filename);
        List<String> lines;
         
        try {
        	BufferedReader bufferedReader = new BufferedReader(fileReader);
            lines = new ArrayList<String>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
            return lines.toArray(new String[lines.size()]);
        }
        catch (Exception e) {
        	throw new IOException(); 
        }
    }
    
    /**
     * Main method
     * 
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        //map of dataset files
        Map<String, String> trainingFiles = new HashMap<String, String>();
        trainingFiles.put("English", "E:/Project/NaiveBayesClassifier-master/resources/datasets/training.language.en.txt");
        trainingFiles.put("French", "E:/Project/NaiveBayesClassifier-master/resources/datasets/training.language.fr.txt");
        trainingFiles.put("German", "E:/Project/NaiveBayesClassifier-master/resources/datasets/training.language.de.txt");
        
        //loading examples in memory
        Map<String, String[]> trainingExamples = new HashMap<String, String[]>();
        for(Map.Entry<String, String> entry : trainingFiles.entrySet()) {
            trainingExamples.put(entry.getKey(), readLines(entry.getValue()));
        }
        
        //train classifier
        NaiveBayes nb = new NaiveBayes();
        nb.setChisquareCriticalValue(6.63); //0.01 pvalue
        nb.train(trainingExamples);
        
        //get trained classifier knowledgeBase
        NaiveBayesKnowledgeBase knowledgeBase = nb.getKnowledgeBase();
        
        try {
        	
        	ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("E:/Classifier.ser"));
        	oos.writeObject(knowledgeBase);
        	oos.close();
        	knowledgeBase = null;
        	ObjectInputStream ois = new ObjectInputStream(new FileInputStream("E:/Classifier.ser"));
        	knowledgeBase = (NaiveBayesKnowledgeBase)ois.readObject();
        	ois.close();
        }
        catch(Exception e) {
        	System.out.println("Something went wrong\n" + e);
        }
        
        nb = null;
        trainingExamples = null;
        
        
        //Use classifier
        nb = new NaiveBayes(knowledgeBase);
        String exampleEn = "I am English";
        Map<String, Double> res = nb.predict(exampleEn);
        System.out.println(res + " is " +  res.get("English") + "% English");
        
//        String exampleFr = "Je suis Français";
//        res = nb.predict(exampleFr);
//        System.out.format("The sentense \"%s\" was classified as \"%d\".%n", exampleFr, res.get("French"));
//        
//        String exampleDe = "Ich bin Deutsch";
//        res = nb.predict(exampleDe);
//        System.out.format("The sentense \"%s\" was classified as \"%d\".%n", exampleDe, res.get("German"));
        

    }
    
}
