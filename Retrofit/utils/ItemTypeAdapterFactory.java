package com.devmaker.siftkin.webservice.retrofit.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

public class ItemTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {
        gson.serializeNulls();
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {

            @Override
            public void write(com.google.gson.stream.JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(com.google.gson.stream.JsonReader in) throws IOException {
                try {
                    JsonElement jsonElement = elementAdapter.read(in);
                    return delegate.fromJsonTree(jsonElement);
                }catch (Exception ex){
                    Log.d("ItemTypeAdapterFactory", "read: ",ex);
                    return null;
                }
            }

        }.nullSafe();
    }
}
