package de.hehnle.bpmn.variability.configuration;

import de.hehnle.bpmn.variability.Action;

public class Configuration {

	private Action action;
	private String elementId;
	private EventConfiguration replacementEvent;
	
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public EventConfiguration getReplacementEvent() {
		return replacementEvent;
	}
	public void setReplacementEvent(EventConfiguration replacementEvent) {
		this.replacementEvent = replacementEvent;
	}
}