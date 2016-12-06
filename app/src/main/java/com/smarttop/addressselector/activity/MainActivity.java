package com.smarttop.addressselector.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.smarttop.addressselector.R;
import com.smarttop.addressselector.bean.City;
import com.smarttop.addressselector.bean.County;
import com.smarttop.addressselector.bean.Province;
import com.smarttop.addressselector.bean.Street;
import com.smarttop.addressselector.utils.LogUtil;
import com.smarttop.addressselector.widget.AddressSelector;
import com.smarttop.addressselector.widget.BottomDialog;
import com.smarttop.addressselector.widget.OnAddressSelectedListener;

/**
 * Created by smartTop on 2016/12/6.
 */

public class MainActivity extends Activity implements View.OnClickListener, OnAddressSelectedListener, AddressSelector.OnDialogCloseListener {
    private TextView tv_selector_area;
    private BottomDialog dialog;
    private String provinceCode;
    private String cityCode;
    private String countyCode;
    private String streetCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_selector_area = (TextView) findViewById(R.id.tv_selector_area);
        tv_selector_area.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (dialog != null) {
            dialog.show();
        } else {
            dialog = new BottomDialog(this);
            dialog.setOnAddressSelectedListener(this);
            dialog.setDialogDismisListener(this);
            dialog.show();
        }
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        provinceCode = (province == null ? "" : province.code);
        cityCode = (city == null ? "" : city.code);
        countyCode = (county == null ? "" : county.code);
        streetCode = (street == null ? "" : street.code);
        LogUtil.d("数据", "省份id=" + provinceCode);
        LogUtil.d("数据", "城市id=" + cityCode);
        LogUtil.d("数据", "乡镇id=" + countyCode);
        LogUtil.d("数据", "街道id=" + streetCode);
        String s = (province == null ? "" : province.name) + (city == null ? "" : city.name) + (county == null ? "" : county.name) +
                (street == null ? "" : street.name);
        tv_selector_area.setText(s);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void dialogclose() {
        if(dialog!=null){
            dialog.dismiss();
        }
    }
}
