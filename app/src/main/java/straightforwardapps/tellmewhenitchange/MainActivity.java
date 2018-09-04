package straightforwardapps.tellmewhenitchange;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

//    TextView tv;
    Button bt;
    EditText prodName;
    String latestURL = "";

    private List<graphPractice> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterClass mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        tv = (TextView) findViewById(R.id.tv);
        bt = (Button) findViewById(R.id.fetchButton);
        prodName = (EditText) findViewById(R.id.prodName);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new AdapterClass(itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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
        //prodName.setText("Generic Digital Heavy Duty Portable Hook Type with Temp Weighing Scale, 50 Kg,Multicolor");
        String productName = prodName.getText().toString().trim();

        //Checks whether the field is empty or not //a-section a-spacing-micro
        if(productName.equals(""))
        {
            Toast.makeText(this, "Give the product name!", Toast.LENGTH_LONG).show();
            bt.setText("Start Fetching");
            bt.setEnabled(true);
            prodName.setText("");
        }
        else
        {
            //finalSearchString => The final refined name of the string to search
            String finalSearchString = "";

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
                else if(pN[i]=='(')
                {
                    i++;
                    finalSearchString+='(';
                    try {
                        while (pN[i] != ')') {
                            if (pN[i] == ' ') {
                                finalSearchString += '+';
                            } else if (escapeChars.containsKey(pN[i])) {
                                finalSearchString += escapeChars.get(pN[i]);
                            } else {
                                finalSearchString += pN[i];
                            }
                            i++;
                        }
                        finalSearchString+=')';
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Missing closing parenthesis \')\'", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    finalSearchString += pN[i];
                }
            }
            String urlToFetch = "https://www.amazon.in/s?url=search-alias%3Daps&field-keywords=" + finalSearchString;
            //Toast.makeText(this, urlToFetch, Toast.LENGTH_LONG).show();
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
                int i=0;

                try {
                    Elements eles = document.select("div[class$=\"a-row\"]");
                    while (Jsoup.parse(eles.get(i).html()).getElementsByTag("span").first() == null) {
                        i++;
                    }
                    System.out.println(document.toString());

                    String urlOfProduct = "https://www.amazon.in";
                    Element eless = document.getElementsByClass("sx-table-item").first();
                    Element eless1 = document.select("a.a-spacing-none.a-link-normal.sx-grid-link").first();
                    if (eless != null && eless.getElementsByTag("a").first()!=null) {
                            urlOfProduct = "https://www.amazon.in" + eless.getElementsByTag("a").first().attr("href");
                    }
                    else if(eless1!=null){
                        urlOfProduct = "https://www.amazon.in" + eless1.attr("href");
                    }
                    else{
                        System.out.println("Nulla");
                    }
                    String price = Jsoup.parse(eles.get(i).html()).getElementsByTag("span").first().text();
                    if (price.contains("from") || price.contains("FROM")) {
                        price += " " + Jsoup.parse(eles.get(i).html()).getElementsByTag("span").get(2).text();
                    }
                    //System.out.println(eles.html());
                    return price + "\n" + document.getElementsByClass("sx-title").first().text() + "splitMeNigga" + urlOfProduct;
                }
                catch(Exception e){
                    System.out.println(e.getStackTrace());
                }
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
//            tv = (TextView) findViewById(R.id.tv);
            super.onPostExecute(s);
            System.out.println(s);
            int klmnop=1;
            if(s!="FETCH ERROR")
            {
                bt.setEnabled(true);
                bt.setText("Start Tracking");
                String[] s1 = s.split("splitMeNigga");
                //tv.setText(tv.getText()+"\n\n"+s1[0]);
                //Toast.makeText(MainActivity.this, s1[1], Toast.LENGTH_LONG).show();
                //latestURL = s1[1];

                prepareData(s1[0], s1[1]);
            }
            else {
                bt.setEnabled(true);
                bt.setText("Start Tracking");
                Toast.makeText(MainActivity.this, "No such thing as "+prodName.getText(), Toast.LENGTH_LONG).show();
                prodName.setText("");
//                tv.setText(s);
                //new FetchProductPrice().execute(s);
            }
        }
    }

    public void redirectMe(View view)
    {
        Intent httpIntent = new Intent(Intent.ACTION_VIEW);
        httpIntent.setData(Uri.parse(latestURL));

        startActivity(httpIntent);
    }

    public void prepareData(String s, String s1)
    {
        String[] ss = s.split("\n");
        itemList.add(new graphPractice(ss[0], ss[1], s1));
        mAdapter.notifyDataSetChanged();
    }





























}
