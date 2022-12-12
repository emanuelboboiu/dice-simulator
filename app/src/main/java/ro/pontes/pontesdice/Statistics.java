package ro.pontes.pontesdice;

/*
 * Class started on 24 September 2014 by Manu
 * Methods for statistics, like postStatistics.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

public class Statistics {

    // A method to post a new game and the number of hands played during the
    // sessions:
    public void postStats(final String gameIdInDB, final int numberOfGamesPlayed) {
        String url = "http://www.pontes.ro/ro/divertisment/games/soft_counts.php?pid="
                + gameIdInDB + "&score=" + numberOfGamesPlayed;

        new GetWebData().execute(url);
    } // end post data statistics.

    // This is a subclass:
    private static class GetWebData extends AsyncTask<String, String, String> {

        // execute before task:
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Execute task
        String urlText = "";

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            urlText = strings[0];
            try {
                // Create a URL object:
                URL url = new URL(urlText);
                // Create a URLConnection object:
                URLConnection urlConnection = url.openConnection();
                // Wrap the URLConnection in a BufferedReader:
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String line;
                // Read from the URLConnection via the BufferedReader:
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content.toString();
        } // end doInBackground() method.

        // Execute after task with the task result as string:
        @Override
        protected void onPostExecute(String s) {
            // Do nothing yet.
        } // end postExecute() method.
    } // end subclass.

} // end statistics class.
