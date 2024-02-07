package de.hehnle.bpmn.variability.configuration;

public class EventConfiguration {

	private String messageName;
	private String timerDuration;
	private String label;

	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public String getTimerDuration() {
		return timerDuration;
	}

	public void setTimerDuration(String timerDuration) {
		this.timerDuration = timerDuration;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}