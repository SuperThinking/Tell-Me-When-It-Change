package straightforwardapps.tellmewhenitchange;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Button bt;
    EditText prodName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tv = (TextView) findViewById(R.id.tv);
        bt = (Button) findViewById(R.id.fetchButton);
        prodName = (EditText) findViewById(R.id.prodName);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ExecuteFetch(View view)
    {
        //Adding Escape Characters for URL
        Map<Character,String> escapeChars = new HashMap<Character, String>();
        escapeChars.put('$', "%24");
        escapeChars.put('%', "%25");
        escapeChars.put('&', "%26");
        escapeChars.put('@', "%40");
        escapeChars.put('`', "%60");
        escapeChars.put('/', "%2F");
        escapeChars.put(':', "%3A");
        escapeChars.put(';', "%3B");
        escapeChars.put('<', "%3C");
        escapeChars.put('=', "%3D");
        escapeChars.put('>', "%3E");
        escapeChars.put('?', "%3F");
        escapeChars.put('[', "%5B");
        escapeChars.put(']', "%5D");
        escapeChars.put('^', "%5E");
        escapeChars.put('{', "%7B");
        escapeChars.put('|', "%7C");
        escapeChars.put('}', "%7D");
        escapeChars.put('~', "%7E");
        escapeChars.put('"', "%22");
        escapeChars.put('\'', "%27");
        escapeChars.put('+', "%2B");
        escapeChars.put(',', "%2C");

        bt = (Button) findViewById(R.id.fetchButton);
        bt.setText("...");
        bt.setEnabled(false);
        //productName => Stores the trimmed product name given
        prodName = (EditText) findViewById(R.id.prodName);
        String productName = prodName.getText().toString().trim();

        //Checks whether the field is empty or not
        if(productName.equals(""))
        {
            Toast.makeText(this, "Give the product name!", Toast.LENGTH_LONG).show();
            bt.setText("Start Fetching");
            bt.setEnabled(true);
        }
        else
        {
            //finalSearchString => The final refined name of the string to search
            String finalSearchString = "";
            /*
            COMPLETE THIS PART
            WHAT HAPPENS WHEN THERE ARE BRACKETS () IN THE SEARCH CriteRia
            */
            String[] bracketSplitter = productName.split("\\(");
            /*
            COMPLETE THIS PART
            */
            //To char Array
            char[] pN = productName.toCharArray();
            for(int i=0; i<productName.length(); i++)
            {
                if(pN[i]==' ')
                {
                    finalSearchString+='+';
                }
                else if(escapeChars.containsKey(pN[i]))
                {
                    finalSearchString+=escapeChars.get(pN[i]);
                }
                else
                {
                    finalSearchString += pN[i];
                }
            }
            String urlToFetch = "https://www.amazon.in/s?url=search-alias%3Daps&field-keywords=" + finalSearchString;
            Toast.makeText(this, urlToFetch, Toast.LENGTH_LONG).show();
            System.out.println(urlToFetch);
            new SearchProduct().execute(urlToFetch);
        }
    }

    public class SearchProduct extends AsyncTask<String, String, String>
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

                Element ele = document.select("span.a-size-base.a-color-price.a-text-bold").first();
                System.out.println(document.toString());
                return ele.html();
                /*
                try {
                    Element eles = document.getElementsByClass("sx-table-item").first();
                    Element ele = eles.getElementsByTag("a").first();
                    return "https://www.amazon.in"+ele.attr("href");
                }catch (Exception e) {
                    System.out.println(document.toString());
                    return "!!ERROR!!";
                }
                */
            }

            //EXCEPTION HANDLING STARTS//
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
            //EXCEPTION HANDLING ENDS//

            //return NULL or a Message Error
            return "FETCH ERROR";
        }

        @Override
        protected void onPostExecute(String s) {
            tv = (TextView) findViewById(R.id.tv);
            super.onPostExecute(s);
            System.out.println(s);
            int klmnop=1;
            if(klmnop==1)
            {
                bt.setEnabled(true);
                bt.setText("Start Tracking");
                tv.setText(s);
            }
            else {
                new FetchProductPrice().execute(s);
            }
        }
    }

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
            tv = (TextView) findViewById(R.id.tv);
            super.onPostExecute(s);
            System.out.println(s);
            tv.setText(s);
            bt.setText("Start Fetching");
            bt.setEnabled(true);
        }
    }
}
