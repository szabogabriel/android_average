package sk.kipi.average;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private enum AverageType {
		ARITHMETIC,
		GEOMETRIC;
	}
	
	private static List<Double> list = new ArrayList<Double>();
	private ListView listView = null;
	private int selected = -1;
	private AverageType type = AverageType.ARITHMETIC;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		countAverage();
		updateListView();
		EditText et = ((EditText)findViewById(R.id.editText2));
		et.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					addValue();
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void add(View view) {
		addValue();
	}
	
	private void addValue() {
		String text = ((EditText)findViewById(R.id.editText2)).getText().toString().trim();
		Log.d("AA", text);
		Double toAdd = Double.parseDouble(text);
		list.add(toAdd);
		updateListView();
		EditText et2 = ((EditText)findViewById(R.id.editText2));
		et2.setText("");
		countAverage();
	}
	
	public void clear(View view) {
		list.clear();
		updateListView();
		EditText et = ((EditText)findViewById(R.id.editText2));
		et.setText("");
		TextView tv = ((TextView)findViewById(R.id.editText1));
		tv.setText("");
	}
	
	private void countAverage() {
		double swp;
		double ret = 0;
		
		switch (type) {
		case ARITHMETIC:
			swp = 0;
			for (Double it : list) {
				swp += it.doubleValue();
			}
			ret = swp / ((double)list.size()); 
			break;
		case GEOMETRIC:
			swp = 1;
			for (Double it : list) {
				swp *= it.doubleValue();
			}
			ret = Math.pow(swp, 1.0 / ((double)list.size()));
			break;
		}
		
		TextView et = ((TextView)findViewById(R.id.editText1));
		et.setText("" + ret);
	}
	
	private void updateListView() {
		if (listView == null) {
			listView = ((ListView)findViewById(R.id.listView1));
		}
		listView.setAdapter(new ArrayAdapter<Double>(this, android.R.layout.simple_list_item_1, list));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selected = arg2;
				removeDialog();
			}
		});
	}
	
	private void removeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message_delete)
               .setPositiveButton(R.string.option_yes, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   if (selected != -1) {
	                	   list.remove(selected);
	                	   updateListView();
	                	   countAverage();
                	   }
                	   selected = -1;
                   }
               })
               .setNegativeButton(R.string.option_no, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               });
        builder.create().show();
	}
	
	public void setArithmetic(View v) {
		type = AverageType.ARITHMETIC;
		countAverage();
	}
	
	public void setGeometric(View v) {
		type = AverageType.GEOMETRIC;
		countAverage();
	}
	
}
