package org.iken.main.page;

import org.iken.main.util.CharsetDetector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;

/**
 * 保存获取到响应的相关内容
 */
public class Page {
    private byte[] content;
    private String html; // 网页源码字符串
    private Document doc; // 网页dom文档
    private String charset;
    private String url;
    private String contentType;

    public Page(byte[] content, String url, String contentType) {
        this.content = content;
        this.url = url;
        this.contentType = contentType;
    }

    public String getCharset() {
        return charset;
    }

    public String getUrl() {
        return url;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getContent() {
        return content;
    }

    /**
     * 返回网页的源码字符串
     */
    public String getHtml() {
        if (html != null) {
            return html;
        }
        if (content == null) {
            return null;
        }
        if (charset == null) {
            charset = CharsetDetector.guessEncoding(content);
        }
        try {
            this.html = new String(content, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return html;
    }

    public Document getDoc() {
        if (doc != null) {
            return doc;
        }
        try {
            this.doc = Jsoup.parse(getHtml(), url);
        } catch (Exception e) {
            e.fillInStackTrace();
            return null;
        }
        return doc;
    }
}
