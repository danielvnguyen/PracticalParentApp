
/*
 * Copyright 2014 KC Ochibili
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * The "‚‗‚" character is not a comma, it is the SINGLE LOW-9 QUOTATION MARK unicode 201A
 * and unicode 2017 that are used for separating the items in a list.
 */

package com.example.practicalparentapp.Model;

import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * This class manages the saving between
 * executions for the task history list.
 */
public class TinyDB {

    private final SharedPreferences preferences;

    public TinyDB(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public ArrayList<Object> getListObject(String key, Class<?> mClass){
    	Gson gson = new Gson();

    	ArrayList<String> objStrings = getListString(key);
    	ArrayList<Object> objects =  new ArrayList<>();

    	for(String jObjString : objStrings){
    		Object value  = gson.fromJson(jObjString,  mClass);
    		objects.add(value);
    	}
    	return objects;
    }

    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[0]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public void putListObject(String key, ArrayList<Object> objArray){
    	checkForNullKey(key);
    	Gson gson = new Gson();
    	ArrayList<String> objStrings = new ArrayList<>();
    	for(Object obj : objArray){
    		objStrings.add(gson.toJson(obj));
    	}
    	putListString(key, objStrings);
    }

    private void checkForNullKey(String key){
        if (key == null){
            throw new NullPointerException();
        }
    }
}