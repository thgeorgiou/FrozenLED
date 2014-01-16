package com.sakisds.frozenled.comparatos;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.Comparator;

/**
 * Created by stratisg on 15/9/2013.
 */
public class ApplicationInfoComparator implements Comparator<ApplicationInfo> {
    PackageManager mPackageManager;

    public ApplicationInfoComparator(PackageManager pm) {
        mPackageManager = pm;
    }

    @Override
    public int compare(ApplicationInfo app1, ApplicationInfo app2) {
        String name1 = app1.loadLabel(mPackageManager).toString().toLowerCase();
        String name2 = app2.loadLabel(mPackageManager).toString().toLowerCase();
        return name1.compareTo(name2);
    }
}
