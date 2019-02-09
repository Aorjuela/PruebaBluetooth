package com.example.jairoorjuela.pruebabluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView mStatusBlueTV, mPairedTV;
    ImageView mBlueIV;
    Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn;

    BluetoothAdapter mBlueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mStatusBlueTV = findViewById(R.id.statusBluetoothTV);
        mPairedTV = findViewById(R.id.pairedTV);
        mBlueIV = findViewById(R.id.bluetoothIV);
        mOnBtn = findViewById(R.id.onBtn);
        mOffBtn = findViewById(R.id.offBtn);
        mDiscoverBtn = findViewById(R.id.discoverableBtn);
        mPairedBtn = findViewById(R.id.pairedBtn);

        //Adaptre Bluetooth
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        //check if bluetooth is available or not
        if (mBlueAdapter == null){
            mStatusBlueTV.setText("Bluetooth is not available");
        }
        else{
            mStatusBlueTV.setText("Bluetooth is available");
        }

        if(mBlueAdapter.isEnabled()){
            mBlueIV.setImageResource(R.drawable.ic_action_on);
        }
        else{
            mBlueIV.setImageResource(R.drawable.ic_action_off);
        }

        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBlueAdapter.isEnabled()){
                    ShowToast("Turning on Bluetooth...");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_BT);
                }
                else{
                    ShowToast("Bluetooth is already on");
                }
            }
        });

        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.enable()){
                    mBlueAdapter.disable();
                    ShowToast("Turning bluetooth off.");
                    mBlueIV.setImageResource(R.drawable.ic_action_off);
                }
                else{
                    ShowToast("Bluetooth is already off.");
                }
            }
        });

        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBlueAdapter.isDiscovering()){
                    ShowToast("Making your device discoverable.");
                }
                else{
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent,REQUEST_DISCOVER_BT);
                }
            }
        });

        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBlueAdapter.isEnabled()){
                    mPairedTV.setText("Paired Devices");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for(BluetoothDevice device: devices){
                        mPairedTV.append("\nDevice" + device.getName() + "," + device);
                    }
                }
                else{
                    ShowToast("Turn on Bluetooth to get paired devices.");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    mBlueIV.setImageResource(R.drawable.ic_action_on);
                    ShowToast("Bluetooth is on.");
                    }
                else {
                    ShowToast("Could't on Bluetooth");
                    }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ShowToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
