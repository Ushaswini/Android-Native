/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/
package com.ushaswini.inclass06;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ushas on 20/02/2017.
 */

public class GameUtil {

    static public class GameBasicSaxParser extends DefaultHandler {

        ArrayList<GameBasic> gameArrayList;
        GameBasic game;
        StringBuilder xmlInnerText;

        static public ArrayList<GameBasic> parseGameBasic(InputStream inputStream) throws IOException, SAXException {
            GameBasicSaxParser parser = new GameBasicSaxParser();
            Xml.parse(inputStream,Xml.Encoding.UTF_8,parser);

            return parser.getGameArrayList();
        }

        public ArrayList<GameBasic> getGameArrayList() {
            return gameArrayList;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            gameArrayList = new ArrayList<>();
            xmlInnerText = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);

            if(localName.equals("Game")){
                game = new GameBasic();
            }
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if(localName.equals("Game")){
                gameArrayList.add(game);
            }else if(localName.equals("id")){
                int id = Integer.parseInt(xmlInnerText.toString().trim());
                game.setId(id);
            }else if(localName.equals("GameTitle")){
                game.setGameTitle(xmlInnerText.toString().trim());
            }else if(localName.equals("ReleaseDate")){
                game.setReleaseDate(xmlInnerText.toString().trim());
            }else if(localName.equals("Platform")){
                game.setPlatform(xmlInnerText.toString().trim());
            }else if(localName.equals("clearlogo")){
                String imageUrl = game.getBaseImageUrl() + xmlInnerText.toString().trim();
                Log.d("imageurl",imageUrl);
                Log.d("baseurl",game.getBaseImageUrl());
                game.setImageUrl(imageUrl);
            }else if(localName.equals("baseImgUrl")){
                game.setBaseImageUrl(xmlInnerText.toString().trim());
            }

            xmlInnerText.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            xmlInnerText.append(ch,start,length);
        }
    }

    static public class GameDetailsSaxParser extends DefaultHandler{

        GameDetails gameDetails;
        StringBuilder xmlInnerText;
        boolean isSimilarGameTag;
        boolean imageTagFound;
        ArrayList<Integer> similarGames;

        static public GameDetails parseGameDetails(InputStream inputStream) throws IOException, SAXException {
            GameDetailsSaxParser parser = new GameDetailsSaxParser();
            Xml.parse(inputStream,Xml.Encoding.UTF_8,parser);

            return parser.getGameDetails();
        }

        public GameDetails getGameDetails() {
            return gameDetails;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            xmlInnerText = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if(localName.equals("Data")){
                gameDetails = new GameDetails();
                xmlInnerText = new StringBuilder();
            }else if(localName.equals("Similar")){
                isSimilarGameTag = true;
                similarGames = new ArrayList<>();
            }
        }
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            xmlInnerText.append(ch,start,length);
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            imageTagFound = false;
            isSimilarGameTag = false;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if(localName.equals("baseImgUrl")){
                gameDetails.setBaseUrl(xmlInnerText.toString().trim());
            }else if(localName.equals("GameTitle")){
                gameDetails.setGameTitle(xmlInnerText.toString().trim());
            }else if(localName.equals("Overview")){
                gameDetails.setOverview(xmlInnerText.toString().trim());
            }else if(localName.equals("genre")){
                gameDetails.setGenre(xmlInnerText.toString().trim());
            }else if(localName.equals("Publisher")){
                gameDetails.setPublisher(xmlInnerText.toString().trim());
            }else if(isSimilarGameTag && localName.equals("id")){
                similarGames.add(Integer.parseInt(xmlInnerText.toString().trim()));
            }else if(isSimilarGameTag && localName.equals("Similar")){
                gameDetails.setSimilarGamesId(similarGames);
            }else if(localName.equals("Youtube")){
                gameDetails.setYoutube(xmlInnerText.toString().trim());
            }else if(localName.equals("SimilarCount")){
                gameDetails.setSimilarCount(Integer.parseInt(xmlInnerText.toString().trim()));
            }else if(localName.equals("ReleaseDate")){
                gameDetails.setReleaseDate(xmlInnerText.toString().trim());
            }else if(localName.equals("Platform")){
                gameDetails.setPlatform(xmlInnerText.toString().trim());
            }else if(!imageTagFound & localName.equals("original")){
                String imageUrl = gameDetails.getBaseUrl() + xmlInnerText.toString().trim();
                gameDetails.setImageUrl(imageUrl);
                imageTagFound = true;
            }else if(!imageTagFound & localName.equals("boxart")){
                String imageUrl = gameDetails.getBaseUrl() + xmlInnerText.toString().trim();
                gameDetails.setImageUrl(imageUrl);
                imageTagFound = true;
            }

            xmlInnerText.setLength(0);
        }


    }

    static public class GameImageUrlSaxParser extends DefaultHandler{

        GameBasic gameImage;
        StringBuilder xmlInnerText;

        static public String parseGameImageUrl(InputStream inputStream) throws IOException, SAXException {
            GameImageUrlSaxParser parser = new GameImageUrlSaxParser();
            Xml.parse(inputStream,Xml.Encoding.UTF_8,parser);

            return parser.getXmlInnerText();
        }

        public String getXmlInnerText() {
            return xmlInnerText.toString();
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            gameImage = new GameBasic();

            xmlInnerText = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if(localName.equals("Game")){
            }

        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);



             if(localName.equals("clearlogo")){
                String imageUrl = gameImage.getBaseImageUrl() + xmlInnerText.toString().trim();
                //Log.d("imageurl",imageUrl);
                //Log.d("baseurl",gameImage.getBaseImageUrl());
                 gameImage.setImageUrl(imageUrl);
            }else if(localName.equals("baseImgUrl")){
                 gameImage.setBaseImageUrl(xmlInnerText.toString().trim());
            }

            xmlInnerText.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            xmlInnerText.append(ch,start,length);
        }
    }
}
