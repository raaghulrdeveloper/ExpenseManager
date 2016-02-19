package raaghulr.com.expensemanager.activity;



        import android.app.Activity;
        import android.content.Context;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.Spinner;
        import android.widget.TextView;

        import raaghulr.com.expensemanager.R;

public class Preference extends Activity {
    String[] strings = {"RUPPEE","US DOLLAR",
            "EURO", "YEN"};

  /*  String[] subs = {"Heaven of all working codes ","Google sub",
            "Microsoft sub", "Apple sub", "Yahoo sub","Samsung sub"};
*/

    int arr_images[] = { R.drawable.dollar,R.drawable.euro,R.drawable.ruppee,R.drawable.yen};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference);

        Spinner mySpinner = (Spinner)findViewById(R.id.spr_cur);
        mySpinner.setAdapter(new MyAdapter(Preference.this, R.layout.row, strings));
    }

    public class MyAdapter extends ArrayAdapter<String>{

        public MyAdapter(Context context, int textViewResourceId,   String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.company);
            label.setText(strings[position]);

         /*   TextView sub=(TextView)row.findViewById(R.id.sub);
            sub.setText(subs[position]);
*/
            ImageView icon=(ImageView)row.findViewById(R.id.image);
            icon.setImageResource(arr_images[position]);

            return row;
        }
    }
}


