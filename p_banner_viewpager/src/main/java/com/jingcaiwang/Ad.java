package com.jingcaiwang;

public class Ad {
	private String title;
	private int iconResId;
	
	//alt+shift+s->o
	public Ad( int iconResId,String title) {
		super();
		this.title = title;
		this.iconResId = iconResId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getIconResId() {
		return iconResId;
	}
	public void setIconResId(int iconResId) {
		this.iconResId = iconResId;
	}
	
	
}
