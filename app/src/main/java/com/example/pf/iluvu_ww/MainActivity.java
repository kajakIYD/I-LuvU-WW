package com.example.pf.iluvu_ww;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {


    Context context;
    String pathToCitations = "/storage/emulated/0/WW_Files/Citations.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        grantPermissions();
        retrieveCitations(pathToCitations);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void grantPermissions()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                // 1 is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }


    int filesCnt = 0;
    List<String> filesList = new LinkedList<String>();
    //Make it more ogolne
    final File folder = new File("/storage/emulated/0/Pictures/");

    public void listFilesForFolder(final File folder) {
        try
        {
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    filesList.add(folder.getAbsolutePath() + "/" + file.getName());
                    filesCnt++;
                }
            }
        }
        catch (Exception ex)
        {
            CharSequence text = "Something went wrong while trying to retrieve files from specified" +
                    " directory!";
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(context, text, duration).show();
        }

    }

    int citationsCnt = 0;
    List<String> citationsList = new LinkedList<String>();

    private void retrieveCitations (String path)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                citationsList.add(line);
                citationsCnt++;
            }
        }
        catch (Exception ex)
        {
            //Process exception
        }
    }



    public void onChangeImageButtonClick(View v)
    {
        String pathToImg = Environment.getExternalStorageDirectory().toString();
        listFilesForFolder(folder);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(String.valueOf(filesCnt));
        Bitmap bmp;
        try
        {
            int randomNum = ThreadLocalRandom.current().nextInt(0, filesCnt);
            bmp = BitmapFactory.decodeFile(filesList.get(randomNum));
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bmp);
            randomNum = ThreadLocalRandom.current().nextInt(0, citationsCnt);
            TextView citation = (TextView) findViewById(R.id.citation);
            citation.setText(citationsList.get(randomNum));
        }
        catch(Exception ex)
        {
            CharSequence text = ex.getMessage();
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();
        }
        finally
        {
            filesCnt = 0;
        }

    }



}
