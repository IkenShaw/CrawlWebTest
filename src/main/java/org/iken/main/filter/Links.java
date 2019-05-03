package org.iken.main.filter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 存贮已经访问过的url路径和带访问的url路径
 */
public class Links {
    // 已访问的url集合
    private static Set<String> visitedUrlSet = new HashSet<String>();

    // 待访问的url集合
    private static LinkedList<String> unVisitedUrlList = new LinkedList<String>();

    public static int getVisitedUrlNum() {
        return visitedUrlSet.size();
    }

    public static int getUnVisitedUrlNum() {
        return unVisitedUrlList.size();
    }

    public static void addVisitedUrlSet(String url) {
        visitedUrlSet.add(url);
    }

    public static void removeVisitedUrlSet(String url) {
        visitedUrlSet.remove(url);
    }

    public static void addUnVisitedUrlList(String url) {
        if (url != null && !url.trim().equals("") && !visitedUrlSet.contains(url) && !unVisitedUrlList.contains(url)) {
            unVisitedUrlList.add(url);
        }
    }

    public static String removeFirstUnVisitedUrlList() {
        return unVisitedUrlList.removeFirst();
    }

    public static boolean unVisitedUrlListIsEmpty() {
        return unVisitedUrlList.isEmpty();
    }
}
