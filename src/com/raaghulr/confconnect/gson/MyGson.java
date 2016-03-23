package com.raaghulr.confconnect.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyGson {

	public Gson get() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setFieldNamingStrategy(new ExpensemanagerFieldNamingPolicy());
		gsonBuilder.setExclusionStrategies(new ExpensemanagerExclusionStrategy());
		return gsonBuilder.create(); 
	}
	
}
