package com.example.pf.iluvu_ww;

import android.Manifest;
import android.content.Context;;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.ThreadLocalRandom;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ///|***************TODO*******************
    //DONE - Drawing algorithm checking -> maybe it is the reason for stopping the application sometimes?
    //Directory for pictures choosing
    //MMS/SMS Sending
    //DONE -SMS longer than approx 60 signs are not send -> divide it into parts
    //DONE - Text scaling depending on letter quantity
    //DONE - New line sign in citations "\n" in .txt file does not work
    //DONE - Something more romantic than just "change Image" on button label
    //DONE App icon -> see sample from ChooseDirectory how to achieve it


    Context context;
    String pathToCitations = "/storage/emulated/0/Citations/Citations.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        grantPermissions();
        retrieveCitations(pathToCitations);
        listFilesForFolder(folder);
        Button fab = (Button) findViewById(R.id.sendSmsButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMSMessage();
            }
        });
    }

    private int grantPermissions()
    {
        int res = 0;
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS}, 1);

                // 1 is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            res++;
        }
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
            res++;
        }
        return res;
    }


    int filesCnt = 0;
    List<String> filesList = new LinkedList<String>();
    //Make it more ogolne
    private File folder = new File("/storage/emulated/0/Pictures/");

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

    private void replaceNewLines(String text)
    {
        //Implementation for method that allows to put newlines
    }
    
    private int setFontSize(String text)
    {
        int res=0;
        res = (text.length()<200) ? 18 : 15;
        return res;
    }

    private void performSending(String msg)
    {
        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Wysłano SMS.",
                    Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),
                    "SMS nie został wysłany, spróbuj ponownie.", Toast.LENGTH_LONG).show();
        }
    }

    final private int MAX_SMS_SIZE = 60;
    private List<String> splitCitationIntoMultipleMsg(String message)
    {
        List<String> resList = new LinkedList<String>();
        String tempMsg = "";
        String[] wordsList = message.split(" ");
        boolean added = false;
        for (int i =0; i<wordsList.length; i++)
        {
            if (tempMsg.length() + wordsList[i].length() < MAX_SMS_SIZE)
            {
                tempMsg = tempMsg.concat(wordsList[i] + " ");
                added = false;
            }
            else
            {
                resList.add(tempMsg);
                tempMsg = wordsList[i] + " ";
                added = true;
            }
        }
        if (!added) resList.add(tempMsg);
        return resList;
    }

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    final String phoneNo = "PUT_YOUR_NUMBER_HERE";
    String message = "Sample message";
    protected void sendSMSMessage()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        else
        {
            List<String> msgList = splitCitationIntoMultipleMsg(drawnCitation);
            for (int i=0; i<msgList.size(); i++)
            {
                performSending(msgList.get(i));
            }
        }
        return;
    }

    String drawnCitation = "";
    public void onChangeImageButtonClick(View v)
    {
//        TextView textView = (TextView) findViewById(R.id.textView);
//        textView.setText(String.valueOf(filesCnt));
        Bitmap bmp;
        grantPermissions();
        try
        {
            if (filesCnt <= 0) listFilesForFolder(folder);
            int randomNum = ThreadLocalRandom.current().nextInt(0, filesCnt);
            bmp = BitmapFactory.decodeFile(filesList.get(randomNum));
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bmp);
            if (citationsCnt <= 0) retrieveCitations(pathToCitations);
            randomNum = ThreadLocalRandom.current().nextInt(0, citationsCnt);
            TextView citation = (TextView) findViewById(R.id.citation);
            drawnCitation = citationsList.get(randomNum);
            drawnCitation = drawnCitation.replace("\\n", System.getProperty("line.separator"));
            citation.setText(drawnCitation);
            citation.setTextSize(setFontSize(drawnCitation));
        }
        catch(Exception ex)
        {
            CharSequence text = ex.getMessage();
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();
        }
        finally
        {
            //filesCnt = 0;
        }

    }



}
