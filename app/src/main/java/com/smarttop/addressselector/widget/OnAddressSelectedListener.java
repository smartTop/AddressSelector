package com.smarttop.addressselector.widget;


import com.smarttop.addressselector.bean.City;
import com.smarttop.addressselector.bean.County;
import com.smarttop.addressselector.bean.Province;
import com.smarttop.addressselector.bean.Street;

public interface OnAddressSelectedListener {
    void onAddressSelected(Province province, City city, County county, Street street);
}
