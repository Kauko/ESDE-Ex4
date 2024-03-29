package fi.oulu.tol.dicedistribution;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DiceDistributionActivity extends Activity {
    public static final int ROUNDS = 100;
    public static final int ROUND_DELAY = 50; // milliseconds
	
    private TextView roundView;
    private ProgressBar[] bars;
    private int counts[] = new int[11];
    private int maximumCount = 0;
    
    int index;
	final Random random = new Random();
	
    Thread thread = new Thread () {
    	public void run() {
			try {
				for (index = 0; index < ROUNDS-1; index++) {
					int die1 = random.nextInt(6);
					int die2 = random.nextInt(6);
					int sum = die1 + die2;
					counts[sum]++;
					
					if (counts[sum] > maximumCount)
						maximumCount = counts[sum];
					
					roundView.post(runnable);
					Thread.sleep(ROUND_DELAY);
				}
			} catch (InterruptedException e) {
					
			}
		}
    };
    
    Runnable runnable = new Runnable () {
    	public void run() {
			roundView.setText(Integer.toString(index + 1));
			
			for (int k = 0; k < 11; k++) {
				bars[k].setMax(maximumCount);
				bars[k].setProgress(counts[k]);
			}	
		}
    };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createUI();
        thread.start();
	}
	
    
    private void createUI() {
       	TableLayout tableLayout = new TableLayout(this);

       	bars = new ProgressBar[11];
    	for (int i = 0; i < 11; i++) {
    		TableRow tableRow = new TableRow(this);
    		tableLayout.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
    		TextView textView = new TextView(this);
    		textView.setText(Integer.toString(i + 2));
    		tableRow.addView(textView, new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT));
    		bars[i] = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
    		bars[i].setMax(1);
    		tableRow.addView(bars[i], new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, 1.0f));
    	}

    	TableRow tableRow = new TableRow(this);
		tableLayout.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		roundView = new TextView(this);
		roundView.setText("0");
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
		layoutParams.span = 2;
		tableRow.addView(roundView, layoutParams);
		
    	setContentView(tableLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
