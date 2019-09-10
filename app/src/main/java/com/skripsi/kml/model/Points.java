package com.skripsi.kml.model;

public class Points {

	String name;
	String description;
	String latitude;
	String longitude;

	public Points(String name, String description, String latitude, String longitude){
		super();
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
	}


	public String getName(){
		return name;
	}
	public String getDescription(){
		return description;
	}
	public String getLongitude(){
		return longitude;
	}
	public String getLatitude(){
		return latitude;
	}
}
