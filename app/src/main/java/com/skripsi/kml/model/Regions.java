package com.skripsi.kml.model;

public class Regions {

	String title;
	String description;
	String file;
	String points;

	public Regions(String title,String description, String file, String points){
		super();
		this.title = title;
		this.description = description;
		this.file = file;
		this.points = points;
	}


	public String getTitle(){
		return title;
	}
	public String getDescription(){
		return description;
	}
	public String getFile(){
		return file;
	}
	public String getPoints(){
		return points;
	}
}
