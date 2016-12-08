# AddressSelector

一个 Android 版京东手机客户端（当前版本V1..0.0 ）风格的级联地址选择器。
     ![image](https://github.com/smartTop/AddressSelector/blob/master/screenshots/screenshot1.gif)
## 添加依赖

模块的 `build.gradle` 中：

    dependencies {
        ...
       compile 'com.smartTop:address-selector:1.0.1'
    }
    
## 使用方法

### 使用原始视图

    AddressSelector selector = new AddressSelector(context);
    selector.setOnAddressSelectedListener(new AddressSelector.OnAddressSelectedListener() {
        @Override
        public void onAddressSelected(Province province, City city, County county, Street street) {
            // blahblahblah
        }
    });
            
    View view = selector.getView();
    // frameLayout.addView(view)
    // new AlertDialog.Builder(context).setView(view).show()
    // ...
    
### BottomDialog

    BottomDialog dialog = new BottomDialog(context);
    dialog.setOnAddressSelectedListener(listener);
    dialog.show();
    
## 关于我

**smartTop**

- 博客 http://blog.csdn.net/qq_30740239
- gitHub https://github.com/smartTop/AddressSelector
