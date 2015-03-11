package com.codepath.simplechat;

import android.graphics.Color;

import java.util.Date;

public class ChatMessage {
	private String objectId     = null;
	private Date   createAt     = new Date();
	private String meta         = "";
	private String message      = "";
	private int    profileColor = 0xFF2097FF;

	public ChatMessage() {
	}

	public ChatMessage(String objectId, String message, String meta) {
		this.objectId = objectId;
		this.message    = message;
		this.meta       = meta;
	}

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

	public int getProfileColor() {
		return profileColor;
	}

	public void setProfileColor(int color) {
		this.profileColor = color;
	}
}
