package com.library.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerialize extends JsonSerializer<Date> 
{
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy hh:mm a");

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException 
    {
        String formattedDate = DATE_FORMAT.format(date);
        gen.writeString(formattedDate);
    }
}
