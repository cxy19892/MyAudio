package com.cxy.myaudio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cxy.myaudio.interfaces.AudioRecordCallBack;
import com.cxy.myaudio.model.AudioRecordModel;
import com.cxy.myaudio.tools.Utils;
import com.cxy.myaudio.views.CircularChart_1;
import com.cxy.myaudio.views.LinearChart1;
import com.cxy.myaudio.views.MyProsessbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button Start, Stop;
    private TextView tvcode;
    private AudioRecordModel mRecorder;
    private HashMap<Double, Double> map;
    private LinearChart1 mChart;
    private CircularChart_1 mCircularChart;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 12302;
    private MyProsessbar mpro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initviews();
    }

    private void Initviews(){
        Start = (Button) findViewById(R.id.btn_start);
        Stop  = (Button) findViewById(R.id.btn_stop);
        tvcode  = (TextView)findViewById(R.id.tv_msg);
        mChart = (LinearChart1) findViewById(R.id.m_linear_chart);
        mCircularChart = (CircularChart_1) findViewById(R.id.m_circular_chart);
        mpro = (MyProsessbar) findViewById(R.id.mypro);
        Start.setOnClickListener(this);
        Stop.setOnClickListener(this);
        mRecorder = new AudioRecordModel(new AudioRecordCallBack() {
            @Override
            public void RecordIntData(double code) {
                mhandler.sendEmptyMessage((int)code);
                try {
                    randmap(map, (double) code);
                    mCircularChart.setDBscore((int) code);
                    mCircularChart.postInvalidate();
                    mpro.setValue((int) code);
                    mpro.postInvalidate();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

        map=new HashMap<Double, Double>();
        map.put(0d, 0d);
        mChart.SetTuView(map, 120, 20, "s", "dB", true);
        mChart.setMstyle(LinearChart1.Mstyle.Curve);
    }


    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tvcode.setText("分贝值：" + msg.what +"\n"+map.size());
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                if(Build.VERSION.SDK_INT >= 23) {
                    int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
                    if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                                REQUEST_CODE_ASK_PERMISSIONS);
                        return;
                    }
                    mRecorder.getNoiseLevel();
                }
                break;
            case R.id.btn_stop:
                mRecorder.StopRecord();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mRecorder.StopRecord();
    }

    private void randmap(HashMap<Double, Double> mp,Double d)
    {
        if(mp.size() < 90){
            mp.put((double)mp.size(), d);
        }else {
            ArrayList<Double> dz= Utils.getintfrommap(mp);//取出map的键并增序排列
            Double[] dvz=new Double[mp.size()];
            int t=0;
            Set set= mp.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext())
            {
                Map.Entry mapentry  = (Map.Entry)iterator.next();
                dvz[t]=(Double)mapentry.getValue();
                t+=1;
            }
            for (int j = 0; j < dz.size() - 1; j++) {
                mp.put(dz.get(j), mp.get(dz.get(j + 1)));
            }
            mp.put(dz.get(mp.size()-1), d);
        }
        mChart.postInvalidate();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mRecorder.getNoiseLevel();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "权限被拒绝", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
