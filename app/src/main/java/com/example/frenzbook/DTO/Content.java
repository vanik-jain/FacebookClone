package com.example.frenzbook.DTO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Content implements Serializable
{
	@SerializedName("text")
	String text;
	String image;
	String video;


	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getVideo() {
		return this.video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	@Override
	public String toString() {
		return "Content{" +
				"Text='" + text + '\'' +
				", Image='" + image + '\'' +
				", Video='" + video + '\'' +
				'}';
	}
}