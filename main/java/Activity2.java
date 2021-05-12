package com.example.statgen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Activity2 extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Intent intent = getIntent();
        String account = intent.getStringExtra(MainActivity.account_text);
        final ListView lv = (ListView) findViewById(R.id.list);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            Document doc = db.parse(new URL("http://api.xgenstudios.com/?method=xgen.stickarena.stats.get&username="+account).openStream());

            String status = doc.getElementsByTagName("rsp").item(0).getAttributes().item(0).getNodeValue();
            String username = doc.getElementsByTagName("user").item(0).getAttributes().item(0).getNodeValue();

            if(!status.equals("ok")) {
                new AlertDialog.Builder(this)
                        .setTitle("FAILURE")
                        .setMessage("Something went wrong. Couldn't look up the stats.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            } else if(username.equals("")) {
                new AlertDialog.Builder(this)
                        .setTitle("FAILURE")
                        .setMessage("'"+account+"' doesn't exist on stick arena!")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                String userid = doc.getElementsByTagName("user").item(0).getAttributes().item(2).getNodeValue();
                String perms = doc.getElementsByTagName("user").item(0).getAttributes().item(1).getNodeValue();

                int intUID = Integer.parseInt(userid);
                String usernew = "";

                int intPerms = Integer.parseInt(perms);
                String mod = "";

                if(intUID > 40000000) {
                    usernew = " [Very New Acc]";
                } else if (intUID > 33000000) {
                    usernew = " [New Acc]";
                } else if (intUID < 20000000) {
                    usernew = " [Old Acc]";
                } else {
                    usernew = " [New Acc]";
                }

                if(intPerms > 0) {
                    mod = " [M]";
                } else if(intPerms < 0) {
                    mod = " [BANNED]";
                }

                NodeList statTag = doc.getElementsByTagName("stat");


                String Wins = statTag.item(0).getFirstChild().getNodeValue();
                String Losses = statTag.item(1).getFirstChild().getNodeValue();
                String Kills = statTag.item(2).getFirstChild().getNodeValue();
                String Deaths = statTag.item(3).getFirstChild().getNodeValue();
                String Rounds = statTag.item(4).getFirstChild().getNodeValue();

                String Ballistick = statTag.item(5).getFirstChild().getNodeValue();

                String lp = "";

                if(Ballistick.equals("1")) {
                    lp = " {B}";
                }

                String[] stats = new String[] {
                        username + lp + mod + usernew,
                        "KILLS: " + Kills,
                        "DEATHS: " + Deaths,
                        "WINS: " + Wins,
                        "LOSSES: " + Losses,
                        "ROUNDS: " + Rounds,
                        "USER ID: " + userid
                };

                final List<String> stats_list = new ArrayList<String>(Arrays.asList(stats));

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                        (this, R.layout.row, stats_list);

                lv.setAdapter(arrayAdapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

}
