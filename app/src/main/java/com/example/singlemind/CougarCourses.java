package com.example.singlemind;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class CougarCourses {

    //static variables begin with s.
    //see https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md
    //for basic naming conventions. This will make it easier for us all to work on this project.
    private static Connection.Response sRes;

    public static void download(String username, String password, String url) throws IOException {


        try {

            //Log messages are displayed by click on the "Run" tab at the bottom of android studios, once the app is in progress.
            Log.i("Starting:","Starting to connect");

            //This sets the connection. We are feeding it the URL being passed from MainActivity, and a valid user and password.
            sRes = Jsoup.connect("http://cc.csusm.edu/login/index.php")
                    .data("username", username, "password", password).method(Connection.Method.POST).execute();

            // This will get you cookies, which we need to maintain our connection as we follow links.
            Map<String, String> loginCookies = sRes.cookies();

            // https://cc.csusm.edu/my/index.php is the URL being fed into this method for testing purposes.
            //test connection to Dashboard URL
            Document doc = Jsoup.connect(url).cookies(loginCookies).get();

            //This is the beginning of the parsing. Easiest way to do this is view the page source of whatever
            //page you are parsing in a browser and look for what tags preceed the needed information.
            Elements topicData = doc.select("div[class=\"card-text content calendarwrapper\"]");
            Element first = topicData.iterator().next();
            for (Iterator<Element> topicIterator = topicData.iterator(); topicIterator.hasNext();) {
                Element answer = topicIterator.next();
                if (answer == first) {
                    String stringAnswer = answer.toString().substring(0, 31) + "### "
                            + answer.toString().substring(31);
                    Log.i("docParse:", stringAnswer);
                }

                //TODO finish the parsing algorithm and retrieve calendar in consumable format.
                //Note- The Calendar assignments can be put in either a Hash Map or ArrayList for me
                //to show on the front end.

                //See the following lines for a parsing demonstration:
                //Once you start iterating through the HTML tags you can look for links and follow those
                //links. And then you can start iterating through the subsequent page. I ripped this demo
                //from a scraper I found on Github ->
                //https://github.com/lengers/MoodleScraper/blob/master/src/com/lengers/moodle/scraper/Scraper.java
                //This scraper was a designed for a normal Java program environment so I made some quick changes
                //and removed some of it's features. In the github version it downloads the file directly to the
                //computer. I don't think we have to download anything, just parse it and then display it to string.
                //An advanced feature of our app could be the ability to download the schedule but i don't think
                //we should worry about that now.

                //Testing:
                //If you build this application and display it in an emulator check the "Run" tab at the bottom
                //of the screen. This will show you an error log and also the output of the Log.i commands
                //I embedded to debug this method.
                //Running this method as-is you can see that it goes to the "Dashboard" URL which I fed from "MainActivity"
                //and looks for the div class "cardtext content calendarwrapper" and then starts retrieving info from
                //there. This isn't really what we want to do but it's just a test to show that this method works.

                //Hope this helps guys. Evan




//            for (Iterator<Element> iterator = links.iterator(); iterator.hasNext();) {
//                Element element = iterator.next();
//                if (element.text().contains("Exam Questions")) {
//                    String followLink = element.attributes().get("href");
//                    Document followDoc = Jsoup.connect(followLink).cookies(loginCookies).get();
//
//                    Elements followLinks = followDoc.select("td[class]");
//
//                    for (Iterator<Element> followIterator = followLinks.iterator(); followIterator.hasNext();) {
//                        Element followElement = followIterator.next();
//                        if (followElement.attributes().get("class").contains("topic starter")) {
//                            Elements inner = followElement.select("a[href");
//
//                            printStream.print("## " + followElement);
//                            for (Iterator<Element> innerIterator = inner.iterator(); innerIterator.hasNext();) {
//                                Element innerElement = innerIterator.next();
//                                String topicLink = innerElement.attributes().get("href");
//
//                                Document topicDoc = Jsoup.connect(topicLink).cookies(loginCookies).get();
//
//                                Elements topicData = topicDoc.select("div[class=\"posting fullpost\"]");
//
//                                // printStream.print("\n###");
//
//                                Element first = topicData.iterator().next();
//                                for (Iterator<Element> topicIterator = topicData.iterator(); topicIterator.hasNext();) {
//                                    Element answer = topicIterator.next();
//                                    if (answer == first) {
//                                        String stringAnswer = answer.toString().substring(0, 31) + "### "
//                                                + answer.toString().substring(31, answer.toString().length());
//                                        printStream.print(stringAnswer);
//                                    } else {
//                                        printStream.print(answer);
//                                    }
//                                    printStream.println("\n---\n");
//
//                                }
//
//                            }
//
//                        }
//
//                    }
//
//                }
//            }
                //printStream.close();

            }
            Log.i("Finish Status: ", "Able to parse from URL provided");

            // System.out.println(links);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("error", "Something bad happened. Abort.");
            e.printStackTrace();
        }
    }
}
