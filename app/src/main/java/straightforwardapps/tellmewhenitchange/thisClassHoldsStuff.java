package straightforwardapps.tellmewhenitchange;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vishal on 04-09-2018.
 */

public class thisClassHoldsStuff {

    public class FetchProductPrice extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection y = null;
            BufferedReader reader = null;
            try {
                URL urlOfPage = new URL(urls[0]);

                y = (HttpURLConnection) urlOfPage.openConnection();
                y.connect();

                InputStream stream = y.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";
                StringBuffer buffer = new StringBuffer( );
                while((line=reader.readLine())!=null)
                {
                    buffer.append(line);
                }

                String lala = buffer.toString();
                Document document = Jsoup.parse(lala);

                Element eles = document.getElementsByClass("p13n-sc-price").first();
                return eles.text();
            }

            //EXCEPTION HANDLING STARTS
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(y!=null){
                    y.disconnect();}
                try {
                    if(reader!=null){
                        reader.close();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //EXCEPTION HANDLING ENDS

            //return NULL or a Message Error
            return "FETCH ERROR";
        }

        @Override
        protected void onPostExecute(String s) {
//            tv = (TextView) findViewById(R.id.tv);
            super.onPostExecute(s);
            System.out.println(s);
//            tv.setText(s);
//            bt.setText("Start Fetching");
//            bt.setEnabled(true);
        }
    }

}
