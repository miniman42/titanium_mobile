/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package org.appcelerator.titanium.module.facebook;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Generic collection of ported functions used by FBConnect library
 * 
 */
public class FBUtil
{
    public static int rgbFloatToInt(float red, float green, float blue, float alpha) 
    {
        int r = (int) (red * 255 + 0.5);
        int g = (int) (green * 255 + 0.5);
        int b = (int) (blue * 255 + 0.5);
        int a = (int) (alpha * 255 + 0.5);
        int value = ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
        return value;
    }
    public static String encode(CharSequence target) 
    {
        if (target == null) {
            return "";
        }
        String result = target.toString();
        try 
        {
            result = URLEncoder.encode(result, "UTF-8");
        } 
        catch (UnsupportedEncodingException ex) 
        {
            ex.printStackTrace();
        }
        return result;
    }
    public static StringBuilder getResponse(InputStream data) throws IOException 
    {
        Reader in = new BufferedReader(new InputStreamReader(data, "UTF-8"),8000);
        StringBuilder buffer = new StringBuilder();
        char[] buf = new char[4000];
        int l = 0;
        while (l >= 0) 
		  {
            buffer.append(buf, 0, l);
            l = in.read(buf);
        }
        return buffer;
    }
    public static void disconnect(HttpURLConnection conn) 
    {
        if (conn != null) {
            try
            {
                conn.disconnect();
            }
            catch(Exception ig) 
            {
            }
        }
    }
    public static void close(Closeable c) 
    {
        if (c != null) 
        {
            try 
            {
                c.close();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        }
    }
    
    public static String componentsJoinedByString(List<String> list, String separator) 
    {
        StringBuilder sb = new StringBuilder();
        sb.append(list.get(0));
        int length = list.size();
        for (int i = 1; i < length; i++) {
            sb.append(separator).append(list.get(i));
        }
        return sb.toString();
    }

    public static final Comparator<String> CASE_INSENSITIVE_COMPARATOR = new Comparator<String>() 
    {
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }
    };

    public static String getContent(Class<?> clazz, String path) throws IOException
    {
        InputStream is = clazz.getClassLoader().getResourceAsStream(path);
        StringBuilder string = getResponse(is);
        return string.toString();
    }

    public static Drawable getDrawable(Class<?> clazz, String path) 
    {
        InputStream is = clazz.getClassLoader().getResourceAsStream(path);
        return Drawable.createFromStream(is, path);
    }
    
    public static BitmapDrawable getBitmapDrawable(Class<?> clazz, String path) 
    {
        InputStream is = clazz.getClassLoader().getResourceAsStream(path);
        return (BitmapDrawable) BitmapDrawable.createFromStream(is, path);
    }
    
    public static Picture getPicture(Class<?> clazz, String path) 
    {
        InputStream is = clazz.getClassLoader().getResourceAsStream(path);
        return Picture.createFromStream(is);
    }

    public static long timeIntervalSinceNow(Date date) 
    {
        if (date == null) 
        {
            return 0;
        }
        return (System.currentTimeMillis() - date.getTime()) / 1000;
    }
    
    public static String generateMD5(String value) 
    {
        try 
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes;
            try 
            {
                bytes = value.getBytes("UTF-8");
            } 
            catch (UnsupportedEncodingException e1) 
            {
                bytes = value.getBytes();
            }
            StringBuilder result = new StringBuilder();
            for (byte b : md.digest(bytes)) 
            {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
            return result.toString();
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            throw new RuntimeException(ex);
        }
    }
    
}
