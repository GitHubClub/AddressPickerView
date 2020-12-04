package com.example.addresspickerlib.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.addresspickerlib.R;
import com.example.addresspickerlib.beans.YwpAddressBean;
import com.example.addresspickerlib.utils.ListUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义仿京东地址选择器
 */

public class AddressPickerView extends RelativeLayout implements View.OnClickListener {
    private static final int INDEX_TAB_PROVINCE = 0;//省份标志
    private static final int INDEX_TAB_CITY = 1;//城市标志
    private static final int INDEX_TAB_COUNTY = 2;//乡镇标志
    private int tabIndex = INDEX_TAB_PROVINCE; //默认是省份
    // recyclerView 选中Item 的颜色
    private int defaultSelectedColor = Color.parseColor("#FF8A1F");
    // recyclerView 未选中Item 的颜色
    private int defaultUnSelectedColor = Color.parseColor("#666666");
    // 确定字体不可以点击时候的颜色
    private int defaultSureUnClickColor = Color.parseColor("#7F7F7F");
    // 确定字体可以点击时候的颜色
    private int defaultSureCanClickColor = Color.parseColor("#FF8A1F");

    private Context mContext;
    private LinearLayout layout_tab;
    private ImageView cancle;
    private TextView textViewProvince;
    private TextView textViewCity;
    private TextView textViewCounty;
    private RecyclerView mRvList; // 显示数据的RecyclerView
    private String defaultProvince = "请选择"; //显示在上面tab中的省份
    private String defaultCity = "请选择"; //显示在上面tab中的城市
    private String defaultDistrict = "请选择"; //显示在上面tab中的区县

    private List<YwpAddressBean.AddressItemBean> mRvData; // 用来在recyclerview显示的数据
    private List<YwpAddressBean.AddressItemBean> mProvinceData;
    private List<YwpAddressBean.AddressItemBean> mCityData;
    private List<YwpAddressBean.AddressItemBean> mDistrictData;
    private AddressAdapter mAdapter;   // recyclerview 的 adapter

    private YwpAddressBean mYwpAddressBean; // 总数据
    private YwpAddressBean.AddressItemBean mSelectProvice; //选中 省份 bean
    private YwpAddressBean.AddressItemBean mSelectCity;//选中 城市  bean
    private YwpAddressBean.AddressItemBean mSelectDistrict;//选中 区县  bean
    private int mSelectProvicePosition = 0; //选中 省份 位置
    private int mSelectCityPosition = 0;//选中 城市  位置
    private int mSelectDistrictPosition = 0;//选中 区县  位置

    private OnAddressPickerSureListener mOnAddressPickerSureListener;

    public AddressPickerView(Context context) {
        super(context);
        init(context);
    }

    public AddressPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddressPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        mContext = context;
        mRvData = new ArrayList<>();
        mProvinceData = new ArrayList<>();
        mCityData = new ArrayList<>();
        mDistrictData = new ArrayList<>();
        // UI
        View rootView = inflate(mContext, R.layout.address_picker_view, this);
        cancle = (ImageView) rootView.findViewById(R.id.cancle);
        layout_tab = (LinearLayout) rootView.findViewById(R.id.layout_tab);
        textViewProvince = (TextView) rootView.findViewById(R.id.textViewProvince);
        textViewCity = (TextView) rootView.findViewById(R.id.textViewCity);
        textViewCounty = (TextView) rootView.findViewById(R.id.textViewCounty);
        textViewProvince.setText(defaultProvince);
        textViewCity.setText(defaultCity);
        textViewCounty.setText(defaultDistrict);

        cancle.setOnClickListener(this);
        textViewProvince.setOnClickListener(this);
        textViewCity.setOnClickListener(this);
        textViewCounty.setOnClickListener(this);
        // recyclerview adapter的绑定
        mRvList = (RecyclerView) rootView.findViewById(R.id.rvList);
        mRvList.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new AddressAdapter();
        mRvList.setAdapter(mAdapter);
    }

    /**
     * 开放给外部传入数据
     * 暂时就用这个Bean模型，如果数据不一致就需要各自根据数据来生成这个bean了
     */
    public void initData(YwpAddressBean bean) {
        if (bean != null) {
            mSelectDistrict = null;
            mSelectCity = null;
            mSelectProvice = null;

            mYwpAddressBean = bean;
            mRvData.clear();
            mRvData.addAll(mYwpAddressBean.getProvince());
            mProvinceData.clear();
            mProvinceData.addAll(mYwpAddressBean.getProvince());
            mAdapter.notifyDataSetChanged();
            updateTabsVisibility();

        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancle) {
            mOnAddressPickerSureListener.onSureClick("-1",
                    "-1", "-1", "-1");
        } else if (i == R.id.textViewProvince) {
            clickProvince();
        } else if (i == R.id.textViewCity) {
            clickCity();
        } else if (i == R.id.textViewCounty) {
            clickCounty();
        }
    }

    private void clickCounty() {
        // 点到区的时候要判断有没有选择省份与城市
        mRvData.clear();
        mDistrictData.clear();
        if (mSelectProvice != null && mSelectCity != null) {
            for (YwpAddressBean.AddressItemBean itemBean : mYwpAddressBean.getDistrict()) {
                if (itemBean.getP().equals(mSelectCity.getI())) {
                    mRvData.add(itemBean);
                    mDistrictData.add(itemBean);
                }
            }
        } else {
            Toast.makeText(mContext, "请您先选择省份与城市", Toast.LENGTH_SHORT).show();
        }
        mAdapter.notifyDataSetChanged();
        // 滚动到这个位置
        mRvList.smoothScrollToPosition(mSelectDistrictPosition);
        tabIndex = INDEX_TAB_COUNTY;
        updateTabsVisibility();
    }

    private void clickCity() {
        // 点到城市的时候要判断有没有选择省份
        mRvData.clear();
        mCityData.clear();
        if (mSelectProvice != null) {
            for (YwpAddressBean.AddressItemBean itemBean : mYwpAddressBean.getCity()) {
                if (itemBean.getP().equals(mSelectProvice.getI())) {
                    mRvData.add(itemBean);
                    mCityData.add(itemBean);
                }
            }
        } else {
            Toast.makeText(mContext, "请您先选择省份", Toast.LENGTH_SHORT).show();
        }
        mAdapter.notifyDataSetChanged();
        // 滚动到这个位置
        mRvList.smoothScrollToPosition(mSelectCityPosition);
        tabIndex = INDEX_TAB_CITY;
        updateTabsVisibility();
    }

    private void clickProvince() {
        mRvData.clear();
        mProvinceData.clear();
        mRvData.addAll(mYwpAddressBean.getProvince());
        mProvinceData.addAll(mYwpAddressBean.getProvince());
        mAdapter.notifyDataSetChanged();
        // 滚动到这个位置
        mRvList.smoothScrollToPosition(mSelectProvicePosition);
        tabIndex = INDEX_TAB_PROVINCE;
        updateTabsVisibility();
    }

    /**
     * 更新tab显示
     */
    private void updateTabsVisibility() {
        textViewProvince.setVisibility(ListUtil.getInstance().isEmpty(mProvinceData) ? View.GONE : View.VISIBLE);
        textViewCity.setVisibility(ListUtil.getInstance().isEmpty(mCityData) ? View.GONE : View.VISIBLE);
        textViewCounty.setVisibility(ListUtil.getInstance().isEmpty(mDistrictData) ? View.GONE : View.VISIBLE);

        //按钮能不能点击 false 不能点击 true 能点击
        textViewProvince.setEnabled(tabIndex != INDEX_TAB_PROVINCE);
        textViewCity.setEnabled(tabIndex != INDEX_TAB_CITY);
        textViewCounty.setEnabled(tabIndex != INDEX_TAB_COUNTY);
        if (defaultSureUnClickColor != 0 && defaultSureUnClickColor != 0) {
            updateTabTextColor();
        }
    }

    /**
     * 更新字体的颜色
     */
    private void updateTabTextColor() {
        if (tabIndex != INDEX_TAB_PROVINCE) {
            textViewProvince.setTextColor(defaultSureUnClickColor);
        } else {
            textViewProvince.setTextColor(defaultSureCanClickColor);
        }
        if (tabIndex != INDEX_TAB_CITY) {
            textViewCity.setTextColor(defaultSureUnClickColor);
        } else {
            textViewCity.setTextColor(defaultSureCanClickColor);
        }
        if (tabIndex != INDEX_TAB_COUNTY) {
            textViewCounty.setTextColor(defaultSureUnClickColor);
        } else {
            textViewCounty.setTextColor(defaultSureCanClickColor);
        }
    }

    //点确定
    private void sure() {
        if (mSelectProvice != null &&
                mSelectCity != null &&
                mSelectDistrict != null) {
            //   回调接口
            if (mOnAddressPickerSureListener != null) {
                mOnAddressPickerSureListener.onSureClick(mSelectProvice.getN() + " " + mSelectCity.getN() + " " + mSelectDistrict.getN() + " ",
                        mSelectProvice.getI(), mSelectCity.getI(), mSelectDistrict.getI());
            }
        } else if (mSelectProvice != null &&
                mSelectCity != null) {
            //   回调接口
            if (mOnAddressPickerSureListener != null) {
                mOnAddressPickerSureListener.onSureClick(mSelectProvice.getN() + " " + mSelectCity.getN(),
                        mSelectProvice.getI(), mSelectCity.getI(), "-1");
            }
        } else if (mSelectProvice != null) {
            //   回调接口
            if (mOnAddressPickerSureListener != null) {
                mOnAddressPickerSureListener.onSureClick(mSelectProvice.getN(),
                        mSelectProvice.getI(), "-1", "-1");
            }
        } else {
            Toast.makeText(mContext, "地址还没有选完整哦", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mYwpAddressBean = null;
    }

    /**
     * 下面显示数据的adapter
     */
    class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_address_text, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mTitle.setText(mRvData.get(position).getN());
            holder.mTitle.setTextColor(defaultUnSelectedColor);
            // 设置选中效果的颜色
            switch (tabIndex) {
                case 0:
                    if (mRvData.get(position) != null &&
                            mSelectProvice != null &&
                            mRvData.get(position).getI().equals(mSelectProvice.getI())) {
                        holder.mTitle.setTextColor(defaultSelectedColor);
                    }
                    break;
                case 1:
                    if (mRvData.get(position) != null &&
                            mSelectCity != null &&
                            mRvData.get(position).getI().equals(mSelectCity.getI())) {
                        holder.mTitle.setTextColor(defaultSelectedColor);
                    }
                    break;
                case 2:
                    if (mRvData.get(position) != null &&
                            mSelectDistrict != null &&
                            mRvData.get(position).getI().equals(mSelectDistrict.getI())) {
                        holder.mTitle.setTextColor(defaultSelectedColor);
                    }
                    break;
            }
            // 设置点击之后的事件
            holder.mTitle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击 分类别
                    switch (tabIndex) {
                        case 0:
                            mSelectProvice = mRvData.get(position);
                            // 清空后面两个的数据
                            mSelectCity = null;
                            mSelectDistrict = null;
                            mSelectCityPosition = 0;
                            mSelectDistrictPosition = 0;
                            textViewCity.setText(defaultCity);
                            textViewCounty.setText(defaultDistrict);
                            // 设置这个对应的标题
                            textViewProvince.setText(mSelectProvice.getN());
                            // 跳到下一个选择
                            mDistrictData.clear();
                            clickCity();
                            mSelectProvicePosition = position;
                            if (ListUtil.getInstance().isEmpty(mCityData)) {
                                sure();
                            }
                            break;
                        case 1:
                            mSelectCity = mRvData.get(position);
                            // 清空后面一个的数据
                            mSelectDistrict = null;
                            mSelectDistrictPosition = 0;
                            textViewCounty.setText(defaultDistrict);
                            // 设置这个对应的标题
                            textViewCity.setText(mSelectCity.getN());
                            // 跳到下一个选择
                            clickCounty();
                            mSelectCityPosition = position;
                            if (ListUtil.getInstance().isEmpty(mDistrictData)) {
                                sure();
                            }
                            break;
                        case 2:
                            mSelectDistrict = mRvData.get(position);
                            textViewCounty.setText(mSelectDistrict.getN());
                            notifyDataSetChanged();
                            mSelectDistrictPosition = position;
                            sure();
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRvData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTitle;

            ViewHolder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.itemTvTitle);
            }

        }
    }


    /**
     * 点确定回调这个接口
     */
    public interface OnAddressPickerSureListener {
        void onSureClick(String address, String provinceCode, String cityCode, String districtCode);
    }

    public void setOnAddressPickerSure(OnAddressPickerSureListener listener) {
        this.mOnAddressPickerSureListener = listener;
    }


}
