package com.example.varun.pagertabstriptutorial;

/**
 * Created by varun on 12/22/15.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FragmentTab2 extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(this).attach(this).commit();

        // Get the view from fragmenttab2.xml
        View view = inflater.inflate(R.layout.fragmenttab2, container, false);

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final PackageManager pm = getActivity().getPackageManager();
        final List<ResolveInfo> resInfos = pm.queryIntentActivities(mainIntent, 0);
        HashSet<String> packageNames = new HashSet<String>(0);
        List<ApplicationInfo> appInfos = new ArrayList<ApplicationInfo>(0);
        for(ResolveInfo resolveInfo : resInfos) {
            packageNames.add(resolveInfo.activityInfo.packageName);
        }
        for(String packageName : packageNames) {
            try {
                if (packageName.equals(getActivity().getPackageName()))
                    continue;
                appInfos.add(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
            } catch (PackageManager.NameNotFoundException e) {
                //Do Nothing
            }
        }
        Collections.sort(appInfos, new ApplicationInfo.DisplayNameComparator(pm));

        SharedPreferences data = getActivity().getSharedPreferences(getString(R.string.ram_list), Context.MODE_PRIVATE);
        ArrayList<Country> countryList = new ArrayList<Country>();

        for (ApplicationInfo packageInfo: appInfos) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;

            boolean res = data.getBoolean(packageInfo.packageName ,false);
            Country country = new Country(packageInfo.packageName, packageInfo.loadLabel(pm).toString(),
                    packageInfo.loadIcon(pm), res);
            countryList.add(country);
        }

        //create an ArrayAdaptar from the String Array
        MyCustomAdapter dataAdapter = new MyCustomAdapter(getContext(),
                R.layout.country_info, countryList);
        ListView listView = (ListView) view.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        return view;
    }
    private class MyCustomAdapter extends ArrayAdapter<Country> {

        private ArrayList<Country> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Country> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Country>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            ImageView icon;
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.country_info, null);

                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.app_icon);
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Country country = (Country) cb.getTag();

                        SharedPreferences data = getActivity().getSharedPreferences(getString(R.string.ram_list), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = data.edit();
                        editor.putBoolean(country.getCode(), cb.isChecked());
                        editor.commit();
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country country = countryList.get(position);
            holder.icon.setImageDrawable(country.app_icon);
            holder.code.setText(country.getName());
            holder.name.setTag(country);

            SharedPreferences data = getActivity().getSharedPreferences(getString(R.string.ram_list), Context.MODE_PRIVATE);
            boolean res = data.getBoolean(country.getCode(),false);
            holder.name.setChecked(res);

            return convertView;
        }
    }
}