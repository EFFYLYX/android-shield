package com.example.shield;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class SearchActivity extends AppCompatActivity implements
        AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, TextWatcher,
        PoiSearch.OnPoiSearchListener, View.OnClickListener, Inputtips.InputtipsListener {
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    ListView listView;
    ArrayList<String> searchItems = new ArrayList();
//    ArrayAdapter<String> adapter;

    private String keyWord = "";// 要输入的poi搜索关键字

    private EditText edt_city;// 要输入的城市名字或者城市区号
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private RecyclerView mRecyclerView;
    PoiKeywordSearchAdapter adapter;
    TextView tv_city;
    String city;

    String requestcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSearchView = findViewById(R.id.search);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setFocusable(true);
        mSearchView.setIconified(false);
        mSearchView.requestFocusFromTouch();


        mSearchView.setQueryHint("Destination");

        searchItems.add("a");
        searchItems.add("ab");

        requestcode = getIntent().getStringExtra("requestcode");

        Log.d("requestcode", requestcode);





        city = getIntent().getStringExtra("city");


        tv_city = findViewById(R.id.tv_city);
        if (!city.isEmpty()) {

            tv_city.setText(city);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchItems);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("address", "//");





                Log.d("toolbar back", "back");

                if (requestcode.equals("123")) {

                    setResult(123, getIntent().putExtras(bundle));
                    finish();
                }
                if (requestcode.equals("321")) {
                    Log.d("toolbar back", "back back");
                    setResult(321, getIntent().putExtras(bundle));
                    finish();
                }

            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                Toast.makeText(getApplicationContext(), "Close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
//搜索图标按钮(打开搜索框的按钮)的点击事件
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Open", Toast.LENGTH_SHORT).show();
            }
        });
//搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.e("CSDN_LQR", "TextSubmit : " + s);

                keyWord = s;
                if ("".equals(keyWord)) {
                    Toasty.warning(getApplicationContext(), "Please enter a keyword!", Toast.LENGTH_SHORT, true).show();

//                    ToastUtil.show(PoiKeywordSearchActivity.this,"请输入搜索关键字");
                    return false;
                } else {
                    doSearchQuery();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

//                String newText = s.toString().trim();
//                InputtipsQuery inputquery = new InputtipsQuery(newText, edt_city.getText().toString());
//                Inputtips inputTips = new Inputtips(getApplicationContext(), inputquery);
////                inputTips.setInputtipsListener(this);
//                inputTips.requestInputtipsAsyn();
//
//
////                keyWord = AMapUtil.checkEditText(s);
//                if ("".equals(keyWord)) {
////                    ToastUtil.show(this, "请输入搜索关键字");
//
//                } else {
//                    doSearchQuery();
//                }
//                adapter.getFilter().filter(s);
//                if(!TextUtils.isEmpty(s)){
//                    listView.setFilterText(s);
//                }else{
//                    listView.clearTextFilter();
//                }
                Log.e("CSDN_LQR", "TextChange --> " + s);
                return false;
            }
        });



//        SearchView.onActionViewExpanded();


    }

    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";


        }
    }

    protected void doSearchQuery() {

        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 0) {
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            searchItems.addAll(listString);

            adapter.notifyDataSetChanged();

        }
    }



    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {  // 搜索poi的结果
                if (result.getQuery().equals(query)) {  // 是否是同一条
                    poiResult = result;
                    ArrayList<PoiAddressBean> data = new ArrayList<PoiAddressBean>();//自己创建的数据集合
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始

                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    for(PoiItem item : poiItems){
                        //获取经纬度对象
                        LatLonPoint llp = item.getLatLonPoint();
                        double lon = llp.getLongitude();
                        double lat = llp.getLatitude();

                        String title = item.getTitle();
                        String text = item.getSnippet();
                        String provinceName = item.getProvinceName();
                        String cityName = item.getCityName();
                        String adName = item.getAdName();
                        data.add(new PoiAddressBean(String.valueOf(lon), String.valueOf(lat), title, text,provinceName,
                                cityName,adName));
                    }

                    adapter = new PoiKeywordSearchAdapter(getApplicationContext(),data);


                    adapter.setOnItemClickListener(new PoiKeywordSearchAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

//                            System.out.println("--------------==============================");

                            PoiAddressBean p = adapter.getItemByPositin(position);

                            Bundle bundle = new Bundle();
                            bundle.putString("address", p.getText());

                            String[] strings = p.toStringArray();
                            bundle.putStringArray("bean",strings);

                            if (requestcode.equals( "123")){
                                setResult(123, getIntent().putExtras(bundle));
                                finish();
                            }
                            if (requestcode.equals( "321")){
                                setResult(321, getIntent().putExtras(bundle));
                                finish();
                            }



                        }
                    });

                    mRecyclerView.setAdapter(adapter);
                }
            } else {
                Toasty.normal(getApplicationContext(), "Not Found").show();

            }
        } else {
            Toasty.error(getApplicationContext(), "Error", Toast.LENGTH_SHORT, true).show();

        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
