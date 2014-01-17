package com.github.awerem.aweremandroid.web;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.util.JsonWriter;

public class JSReturner
{
    private JsonWriter writer;
    private StringWriter out;

    public JSReturner()
    {
        out = new StringWriter();
        writer = new JsonWriter(out);
        writer.setLenient(true);
    }
    
    public JSReturner addObject(Object obj) throws IOException
    {
        if(obj instanceof Number)
        {
            writer.value((Number) obj);
        }
        else if(obj instanceof String)
        {
            writer.value((String) obj);
        }
        else if (obj instanceof Boolean)
        {
            writer.value((Boolean) obj);
        }
        else if (obj instanceof List<?>)
        {
            parseList((List<?>) obj);
        }
        else if (obj instanceof HashMap<?, ?>)
        {
            parseObject((HashMap<?, ?>) obj);
        }
        return this;
    }

    private JSReturner parseObject(HashMap<?, ?> obj) throws IOException
    {
        writer.beginObject();
        for(Entry<?, ?> entry: obj.entrySet())
        {
            writer.name(entry.getKey().toString());
            addObject(entry.getValue());
        }
        writer.endObject();
        return this;
    }

    private JSReturner parseList(List<?> list) throws IOException
    {
        writer.beginArray();
        for(Object obj: list)
        {
            addObject(obj);
        }
        writer.endArray();
        return this;
    }
    
    public String toJson() throws IOException
    {
        return out.toString();
    }
    
    public void close() throws IOException
    {
        writer.close();
    }
}
