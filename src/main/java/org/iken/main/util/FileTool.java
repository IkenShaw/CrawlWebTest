package org.iken.main.util;

import org.iken.main.page.Page;

import java.io.*;

/**
 * 本来主要下载那些已经访问过的文件
 */
public class FileTool {
    private static String dirPath;

    /**
     * getMethod.getResponseHeader("Content-Type").getValue()
     * 根据url和网页类型生成需要保存的网页的文件名，去除URL仲的非文件名字符
     *
     * @param url
     * @param contentType
     * @return
     */
    private static String getFileNameByUrl(String url, String contentType) {
        url = url.substring(7); // 去除http://
        // text/html类型
        if (contentType.indexOf("html") != -1) {
            url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
            return url;
        } else {
            // application/pdf类型
            return url.replaceAll("[\\?/:*|<>\"]", "_") + "." + contentType.substring(contentType.lastIndexOf("/") + 1);
        }
    }

    /**
     * 生成目录
     */
    private static void mkdir() {
        if (dirPath == null) {
            dirPath = Class.class.getClass().getResource(File.separator).getPath() + "temp" + File.separator;
        }
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
    }

    /**
     * 保存网页文件字节数组到本地，filePath为要保存的文件的相对地址
     *
     * @param page
     */
    public static void saveToLocal(Page page) {
        mkdir();
        String fileName = getFileNameByUrl(page.getUrl(), page.getContentType());
        String filePath = dirPath + fileName;
        byte[] data = page.getContent();
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(new File(filePath)));
            out.write(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
