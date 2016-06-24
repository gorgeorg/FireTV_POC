package com.ticketmaster.api.discovery.entry.event;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Artur Bryzhatiy on 18.04.16.
 */
public class EventSale implements Serializable {

	@SerializedName("startDateTime")
	private String startDateTime;

	@SerializedName("startTBD")
	private boolean startTBD;

	@SerializedName("endDateTime")
	private String endDateTime;

	public String getStartDateTime() {
		return startDateTime;
	}

	public boolean isStartTBD() {
		return startTBD;
	}

	public String getEndDateTime() {
		return endDateTime;
	}
}
