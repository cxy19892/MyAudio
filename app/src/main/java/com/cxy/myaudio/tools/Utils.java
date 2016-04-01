package com.cxy.myaudio.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/3/25.
 */
public class Utils {


    /**
     * 将key值
     * @param map
     * @return
     */
    public static ArrayList<Double> getintfrommap(HashMap<Double, Double> map)
    {
        ArrayList<Double> dlk=new ArrayList<Double>();
        int position=0;
        Set set= map.entrySet();
        Iterator iterator = set.iterator();

        while(iterator.hasNext())
        {
            Map.Entry mapentry  = (Map.Entry)iterator.next();
            dlk.add((Double)mapentry.getKey());
        }
        for(int i=0;i<dlk.size();i++)//冒泡排序
        {
            int j=i+1;
            position=i;
            Double temp=dlk.get(i);
            for(;j<dlk.size();j++)
            {
                if(dlk.get(j)<temp)
                {
                    temp=dlk.get(j);
                    position=j;
                }
            }

            dlk.set(position,dlk.get(i));
            dlk.set(i,temp);
        }
        return dlk;

    }

    public static int getAndroidOSVersion()
    {
        int osVersion;
        try
        {
            osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        }
        catch (NumberFormatException e)
        {
            osVersion = 0;
        }

        return osVersion;
    }
}
