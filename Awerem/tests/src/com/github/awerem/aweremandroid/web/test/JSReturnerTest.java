package com.github.awerem.aweremandroid.web.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import android.test.AndroidTestCase;

import com.github.awerem.aweremandroid.web.JSReturner;

public class JSReturnerTest extends AndroidTestCase
{

    public void testAddInt() throws IOException
    {
        JSReturner js = new JSReturner();
        js.addObject(1);
        assertEquals("Integer are not parsed well", "1", js.toJson());
        js.close();
    }

    public void testAddFloat() throws IOException
    {
        JSReturner js = new JSReturner();
        js.addObject(1.3);
        assertEquals("Float are not well parsed", "1.3", js.toJson());
        js.close();
    }

    public void testAddBool() throws IOException
    {
        JSReturner js = new JSReturner();
        js.addObject(true);
        assertEquals("Boolean are not well parsed", "true", js.toJson());
        js.close();
    }

    public void testAddString() throws IOException
    {
        JSReturner js = new JSReturner();
        js.addObject("String");
        assertEquals("String are not well parsed", "\"String\"", js.toJson());
        js.close();
    }

    public void testAddList() throws IOException, JSONException
    {
        JSReturner js = new JSReturner();
        ArrayList<Object> array = new ArrayList<Object>();
        array.add(5);
        array.add("String");
        array.add(-4.3);
        array.add(true);
        HashMap<String, Object> hash = new HashMap<String, Object>();
        hash.put("i", 3);
        hash.put("f", -3.1);
        array.add(hash);
        js.addObject(array);
        JSONAssert.assertEquals("[5, \"String\", -4.3, true,"
                + "{\"i\":3, \"f\": -3.1}]", js.toJson(), true);
    }

    public void testAddObject() throws IOException, JSONException
    {
        JSReturner js = new JSReturner();
        HashMap<String, Object> hash = new HashMap<String, Object>();
        hash.put("i", -12);
        hash.put("f", 43.12e10);
        hash.put("s", "str");
        hash.put("b", true);
        ArrayList<Object> array = new ArrayList<Object>();
        array.add(1);
        array.add("string");
        hash.put("a", array);
        js.addObject(hash);

        JSONAssert.assertEquals("{\"i\": -12, \"f\": 4.312e11, \"s\": \"str\","
                + " \"b\": true, \"a\": [1, \"string\"]}", js.toJson(), true);
    }
}
