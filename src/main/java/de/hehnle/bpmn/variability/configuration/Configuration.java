package de.hehnle.bpmn.variability.configuration;

import de.hehnle.bpmn.variability.Action;

public class Configuration {

	private Action action;
	private String elementId;
	private String closingElementId;
	private GatewayType gatewayType;
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
	public String getClosingElementId() {
		return closingElementId;
	}
	public void setClosingElementId(String closingElementId) {
		this.closingElementId = closingElementId;
	}
	public GatewayType getGatewayType() {
		return gatewayType;
	}
	public void setGatewayType(GatewayType gatewayType) {
		this.gatewayType = gatewayType;
	}
	public EventConfiguration getReplacementEvent() {
		return replacementEvent;
	}
	public void setReplacementEvent(EventConfiguration replacementEvent) {
		this.replacementEvent = replacementEvent;
	}
}