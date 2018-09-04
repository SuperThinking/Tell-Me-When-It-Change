package straightforwardapps.tellmewhenitchange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class graphPractice{
    private String id;
    private String name;

    public String getID()
    {
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }
    public graphPractice(String i, String j)
    {
        this.id = i;
        this.name = j;
    }
}
/*
public class graphPractice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_practice);


                FOR GRAPH
        GraphView graph = (GraphView) findViewById(R.id.graph);
        DataPoint[] dp = new DataPoint[2];
        for(int i=0; i<2; i++)
        {
            dp[i] = new DataPoint(20180900+i, i+1);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dp);
//        DataPoint dd = new DataPoint(20180905.0, 99999.0);
//        series.appendData(dd, true, 40);
        graph.addSeries(series);

    }

}
*/
