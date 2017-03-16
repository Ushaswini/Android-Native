package com.ushaswini.tedpodcast;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.SAXParser;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * Abhishek Surya
 * PodcastUtil
 * 06/03/2017
 */

public class PodcastUtil {
    static public class PodcastSAXParser extends DefaultHandler{

        ArrayList<Podcast>podcasts;
        Podcast podcast;
        StringBuilder builder;
        boolean isRequiredTag;


        static public ArrayList<Podcast> parsePodcast(InputStream inputStream) throws IOException, SAXException {
            PodcastSAXParser parser = new PodcastSAXParser();
            Xml.parse(inputStream, Xml.Encoding.UTF_8,parser);
            return parser.getPodcasts();
        }

        public ArrayList<Podcast> getPodcasts() {
            return podcasts;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            podcasts = new ArrayList<>();
            builder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if(localName.equals("item")){
                podcast = new Podcast();
                isRequiredTag = true;
            }else if(qName.equals("itunes:image") & isRequiredTag){
                if(attributes.getValue("href") != null){
                    //Log.d("imageurl",attributes.getValue("href"));
                    podcast.setImageUrl(attributes.getValue("href"));
                   // Log.d("image url",podcast.getImageUrl());
                }
            }else if(qName.equals("enclosure") & isRequiredTag){
                if(attributes.getValue("url") != null){
                    //Log.d("mp3url",attributes.getValue("url"));
                    podcast.setMp3Url(attributes.getValue("url"));
                }
            }
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            /*Log.d("Local name",localName);
            Log.d("Q name",qName);*/

            if(localName.equals("item")){
                podcasts.add(podcast);
            }else if(localName.equals("title") & isRequiredTag){
                podcast.setEpisodeTitle(builder.toString().trim());
            }else if(localName.equals("description") & isRequiredTag){
                podcast.setDescription(builder.toString().trim());
            }else if(localName.equals("pubDate") & isRequiredTag){
                String pubDateStr = builder.toString().trim();
                try{

                    SimpleDateFormat fmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                    Date date = fmt.parse(pubDateStr);
                    podcast.setPubDate(date);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, d MMM yyyy");
                    podcast.setPubDateStr(outputFormat.format(date));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(qName.equals("itunes:duration") & isRequiredTag){
                podcast.setDuration(builder.toString().trim());
            }

            builder.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            builder.append(ch,start,length);
        }
    }
}
