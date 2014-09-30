package com.billywalkerinc.occupiedcentralsms;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_settings)
public class ActivitySettings extends ActionBarActivity {

    public final String SETTING = "setting";
    public final String C_NAME = "chinese_name";
    public final String E_NAME = "english_name";
    public final String PHONE = "phone";
    final String SEX = "sex";
    public final String ID = "id";
    public final String CUSTOM_MSG = "custom message";
    public final String SEND_MSG = "send message";

    @ViewById
    EditText etxtCName;

    @ViewById
    EditText etxtEName;

    @ViewById
    EditText etxtPhone;

    @ViewById
    EditText etxtId;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @AfterViews
    void onViewInjected(){
        settings = getSharedPreferences(SETTING, 0);
        editor = settings.edit();
        refreshDisplay();

    }

    @Click(R.id.btnSave)
    void onBtnSaveClicked() {
        saveData();
        Toast.makeText(this, "儲存成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    public String getStringInSetting(String pref) {
        return settings.getString(pref, "");
    }


    void refreshDisplay() {
        etxtCName.setText(getStringInSetting(C_NAME));
        etxtEName.setText(getStringInSetting(E_NAME));
        etxtPhone.setText(getStringInSetting(PHONE));
        etxtId.setText(getStringInSetting(ID));
    }

    void saveData() {
        String cName, eName, phone, id, customMsg, sendMsg;

        cName = etxtCName.getText().toString();
        eName = etxtEName.getText().toString();
        phone = etxtPhone.getText().toString();
        id = etxtId.getText().toString();
        sendMsg = "求助:\n"+cName+"\n"+eName+"\n"+phone+"\n"+id+"\n";

        editor.putString(C_NAME, cName);
        editor.putString(E_NAME, eName);
        editor.putString(PHONE, phone);
        editor.putString(ID,id);
        editor.putString(SEND_MSG, sendMsg);
        editor.putBoolean("set", true);
        editor.commit();
    }

}
