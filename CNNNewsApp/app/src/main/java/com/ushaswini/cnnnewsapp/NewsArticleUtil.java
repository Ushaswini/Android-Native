/*
* Assignment : 5
* Vinnakota Venkata Ratna Ushaswini*/

package com.ushaswini.cnnnewsapp;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class NewsArticleUtil {

    static public class ArticleSAXParser extends DefaultHandler {

        ArrayList<NewsArticle> articles;
        NewsArticle article;
        StringBuilder sb;
        boolean isCorrectItem;

        static public ArrayList<NewsArticle> parseNewsArticle(InputStream inputStream) throws IOException, SAXException {

            ArticleSAXParser parser = new ArticleSAXParser();
            Xml.parse(inputStream, Xml.Encoding.UTF_8,parser);
            return parser.getArticles();
        }

        public ArrayList<NewsArticle> getArticles() {
            return articles;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            articles = new ArrayList<NewsArticle>();
            sb = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);

            if(localName.equals("item")){
                article = new NewsArticle();
                isCorrectItem = true;
            }
            if(qName.equals("media:content")){
                if(attributes.getValue("height").equals(attributes.getValue("width"))){
                    article.setUrlToImage(attributes.getValue("url"));
                    Log.d("url",attributes.getValue("url"));
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

           /* Log.d("qName",qName);
            Log.d("local name",localName);*/

            if(localName.equals("item")){
                articles.add(article);
            }else if(localName.equals("title") & isCorrectItem){
                article.setTitle(sb.toString().trim());
            }else if(localName.equals("description") & isCorrectItem){
                article.setDescription(sb.toString().trim());
            }else if(localName.equals("pubDate") & isCorrectItem){
                article.setPubDate(sb.toString().trim());
            }
            sb.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            sb.append(ch,start,length);
        }
    }
}
