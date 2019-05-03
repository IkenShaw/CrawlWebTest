package org.iken.main.page;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PageParseTool {

    /**
     * 通过选择器来选取页面的标签
     *
     * @param page
     * @param cssSelector
     * @return
     */
    public static Elements select(Page page, String cssSelector) {
        return page.getDoc().select(cssSelector);
    }

    /**
     * 通过css选择器来得到指定元素
     *
     * @param page
     * @param cssSelector
     * @param index
     * @return
     */
    public static Element select(Page page, String cssSelector, int index) {
        Elements elements = select(page, cssSelector);
        int realIndex = index;
        if (index < 0) {
            realIndex = elements.size() + index;
        }
        return elements.get(realIndex);
    }

    /**
     * 获取满足选择器的元素中的链接，选择器cssSelector必须定位到具体的超链接
     * 例如我们想抽取id为content的div中的所有超链接，这里就要将cssSelector定义为div[id=content] a
     * 放入set中，防止重复
     *
     * @param page
     * @param cssSelector
     * @return
     */
    public static Set<String> getLinks(Page page, String cssSelector) {
        Set<String> linkSet = new HashSet<String>();
        Elements elements = select(page, cssSelector);
        Iterator<Element> iterator = elements.iterator();
        Element element = null;
        while (iterator.hasNext()) {
            element = (Element) iterator.next();
            if (element.hasAttr("href")) {
                linkSet.add(element.attr("abs:href"));
            } else if (element.hasAttr("src")) {
                linkSet.add(element.attr("abs:src"));
            }
        }
        return linkSet;
    }

    /**
     * 获取网页中满足指定css选择器的所有元素和指定属性的集合
     * 例如通过getAttrs("img[src]", "abs:src")可获取网页中所有图片的链接
     *
     * @param page
     * @param cssSelector
     * @param attrName
     * @return
     */
    public static ArrayList<String> getAttrs(Page page, String cssSelector, String attrName) {
        ArrayList<String> result = new ArrayList<String>();
        Elements elements = select(page, cssSelector);
        for (Element element : elements) {
            result.add(element.attr(attrName));
        }
        return result;
    }
}
