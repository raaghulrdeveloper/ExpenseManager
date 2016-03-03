package raaghulr.com.expensemanager.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import raaghulr.com.expensemanager.R;



public class Backup extends FragmentActivity {
    String DATABASE_NAME = "db_name";
    String PACKAGE_NAME  = "raaghulr.com.expensemanager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exportDB();
    }



    private void exportDB(){

        File sd = Environment.getExternalStorageDirectory();

        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;

        String currentDBPath = "/data/"+ PACKAGE_NAME +"/databases/"+DATABASE_NAME;

        String backupDBPath = DATABASE_NAME+".sql";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}