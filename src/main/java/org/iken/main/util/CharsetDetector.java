/*
 * Copyright (C) 2014 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.iken.main.util;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符集自动检测
 */
public class CharsetDetector {
    private static final int CHUNK_SIZE = 2000;

    private static Pattern metaPattern = Pattern.compile("<meta\\s+([^>]*http-equiv=(\"|')?content-type(\"|')?[^^>]*)>", Pattern.CASE_INSENSITIVE);

    private static Pattern charsetPattern = Pattern.compile("charset=\\s*([a-z][_\\-0-9a-z]*)", Pattern.CASE_INSENSITIVE);

    private static Pattern charsetPatternHTML5 = Pattern.compile("<meta\\s+charset\\s*=\\s*[\"']?([a-z][_\\-0-9a-z]*)[^?]*>", Pattern.CASE_INSENSITIVE);

    /**
     * 从Nutch借鉴的网页编码检测代码
     *
     * @param content
     * @return encoding 编码
     */
    private static String guessEncodingByNutch(byte[] content) {
        int length = Math.min(content.length, CHUNK_SIZE);
        String str = "";
        try {
            str = new String(content, "ascii");
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        Matcher metaMatrcher = metaPattern.matcher(str);
        String encoding = null;
        if (metaMatrcher.find()) {
            Matcher charsetMatcher = charsetPattern.matcher(metaMatrcher.group(1));
            if (charsetMatcher.find()) {
                encoding = new String(charsetMatcher.group(1));
            }
        }

        if (encoding == null) {
            metaMatrcher = charsetPatternHTML5.matcher(str);
            if (metaMatrcher.find()) {
                encoding = new String(metaMatrcher.group(1));
            }
        }

        if (encoding == null) {
            if (length >= 3 && content[0] == (byte) 0xEF && content[1] == (byte) 0xBB && content[2] == 0xEF) {
                encoding = "UTF-8";
            } else if (length >= 2) {
                if (content[0] == (byte) 0xEF && content[1] == (byte) 0xFE) {
                    encoding = "UTF-16LE";
                } else if (content[0] == (byte) 0xFE) {
                    encoding = "UTF-16BE";
                }
            }
        }

        return encoding;
    }

    /**
     * 根据字节数组，猜测可能的字符集，如果检测失败，返回UTF-8
     *
     * @param bytes 待检测的字节数组
     * @return 可能的字符集，如果检测失败，返回UTF-8
     */
    public static String guessEncodingByMozilla(byte[] bytes) {
        String DEFAULT_ENCONDING = "UTF-8";
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            encoding = DEFAULT_ENCONDING;
        }
        return encoding;
    }

    /**
     * 根据字节数组，猜测可能的字符集，如果检测失败，返回UTF-8
     *
     * @param content 待检测的数组
     * @return 可能的字符集，如果检测失败，返回UTF-8
     */
    public static String guessEncoding(byte[] content) {
        String encoding = null;
        try {
            encoding = guessEncodingByNutch(content);
        } catch (Exception e) {
            encoding = guessEncodingByMozilla(content);
        }
        return encoding;
    }
}
