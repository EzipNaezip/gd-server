package com.manofsteel.gd.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class SentenceRefineService {



        private static final String[] INTERIOR_KEYWORDS = {"Interior", "Furniture", "Prop", "Color", "Style", "Home Decor", "Design", "Color"};
        private static final CharArraySet STOPWORDS = new CharArraySet(Arrays.asList(
                "a", "an", "and", "are", "as", "at", "be", "but", "by",
                "for", "if", "in", "into", "is", "it",
                "no", "not", "of", "on", "or", "such",
                "that", "the", "their", "then", "there", "these",
                "they", "this", "to", "was", "will", "with"
        ), true);
        private static final Analyzer ANALYZER = new StandardAnalyzer(STOPWORDS);

        public String processInteriorRequest(String sentence) {
            List<String> keywords = extractKeywords(sentence);
            String refinedSentence = refineSentence(sentence);
            String response = String.format("Extracted keywords: %s\nRefined sentence: %s", keywords, refinedSentence);
            System.out.println(response);
            return response;
        }

        private List<String> extractKeywords(String sentence) {
            List<String> tokens = new ArrayList<>();
            try (TokenStream ts = ANALYZER.tokenStream(null, new StringReader(sentence))) {
                CharTermAttribute charTermAttr = ts.addAttribute(CharTermAttribute.class);
                ts.reset();
                while (ts.incrementToken()) {
                    String token = charTermAttr.toString();
                    if (!STOPWORDS.contains(token) && Arrays.asList(INTERIOR_KEYWORDS).contains(token)) {
                        tokens.add(token);
                    }
                }
                ts.end();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tokens;
        }

        private String refineSentence(String sentence) {
            try (TokenStream ts = ANALYZER.tokenStream(null, new StringReader(sentence));
                 StopFilter stopFilter = new StopFilter(ts, STOPWORDS)) {
                CharTermAttribute charTermAttr = stopFilter.addAttribute(CharTermAttribute.class);
                stopFilter.reset();
                StringBuilder refinedSentence = new StringBuilder();
                while (stopFilter.incrementToken()) {
                    refinedSentence.append(charTermAttr.toString()).append(" ");
                }
                stopFilter.end();
                return refinedSentence.toString().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sentence;
        }
    }


