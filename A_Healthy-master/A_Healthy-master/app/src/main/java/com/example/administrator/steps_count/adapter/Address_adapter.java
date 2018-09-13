package com.example.administrator.steps_count.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.mall.Address;
import java.util.List;
/**
 * Created by PC on 2018/3/8.
 */

public class Address_adapter extends BaseAdapter {
    private Context context;
    private List<Address> list;
    private LayoutInflater layoutInflater;
    private Address_function address_function;
    public Address_adapter(Context context, List<Address> list) {
        layoutInflater=LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }
    public void setAddress_function(Address_function address_function) {
        this.address_function = address_function;
    }
    @Override
    public int getCount() {
         return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Address address=list.get(i);
        ViewHolder holder;
        if (view==null)
        {
            holder=new ViewHolder();
            view= layoutInflater.inflate (R.layout.mall_address_item,null);
            holder.consigneer= (TextView) view.findViewById(R.id.address_item_consignee);
            holder.cellnumber=(TextView) view.findViewById(R.id.address_item_cellnumber);
            holder.address= (TextView) view.findViewById(R.id.address_item_address);
            holder.deleteaddr= (ImageView) view.findViewById(R.id.deleteaddr);
            view.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) view.getTag();
        }
        holder.consigneer.setText(address.getConsigneer());
        holder.cellnumber.setText(address.getCellnumber());
        holder.address.setText(address.getAddress());
        holder.deleteaddr.setTag(i);
        holder.deleteaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = Integer.parseInt(view.getTag().toString());
                String id=list.get(index).getId();
                address_function.deleteaddr(id);
            }
        });
        return view;
    }
    static class ViewHolder
    {
        ImageView deleteaddr;
        TextView consigneer;
        TextView cellnumber;
        TextView address;
    }
    public interface Address_function
    {
        void deleteaddr(String id);
    }
}

