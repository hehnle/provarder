package de.hehnle.bpmn.variability;

import java.util.Collection;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.IntermediateCatchEvent;
import org.camunda.bpm.model.bpmn.instance.Message;
import org.camunda.bpm.model.bpmn.instance.MessageEventDefinition;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.TimeDuration;
import org.camunda.bpm.model.bpmn.instance.TimerEventDefinition;

public class EventTypeReplacer {

	public static void replaceEventByTimerEvent(BpmnModelInstance modelInstance, String eventId, String duration, String label) {
		FlowNode eventToReplace = (FlowNode) modelInstance.getModelElementById(eventId);

		if (eventToReplace != null) {

			IntermediateCatchEvent timerEvent = modelInstance.newInstance(IntermediateCatchEvent.class);
			timerEvent.setName(label);

			TimerEventDefinition timerEventDefinition = modelInstance.newInstance(TimerEventDefinition.class);
			TimeDuration timeDuration = modelInstance.newInstance(TimeDuration.class);
			timeDuration.setTextContent(duration);
			timerEventDefinition.setTimeDuration(timeDuration);
			timerEvent.addChildElement(timerEventDefinition);

			replaceEvent(modelInstance, eventToReplace, timerEvent);
		}
	}

	public static void replaceEventWithMessageCatch(BpmnModelInstance modelInstance, String eventId,
			String messageName, String label) {
		FlowNode eventToReplace = (FlowNode) modelInstance.getModelElementById(eventId);

		if (eventToReplace != null) {

			IntermediateCatchEvent messageEvent = modelInstance.newInstance(IntermediateCatchEvent.class);
			messageEvent.setName(label);

			MessageEventDefinition messageEventDefinition = modelInstance.newInstance(MessageEventDefinition.class);
			Message message = modelInstance.newInstance(Message.class);
			message.setName(messageName);

			Collection<Definitions> definitions = modelInstance.getModelElementsByType(Definitions.class);
			for (Definitions definition : definitions) {
				Collection<Process> processes = definition.getChildElementsByType(Process.class);
				for (Process process : processes) {
					definition.addChildElement(message);
					messageEventDefinition.setMessage(message);
					messageEvent.addChildElement(messageEventDefinition);
					process.replaceChildElement(eventToReplace, messageEvent); // replace event
				}
			}
		}
	}

	private static void replaceEvent(BpmnModelInstance modelInstance, FlowNode eventToReplace,
			IntermediateCatchEvent replacementEvent) {

		Collection<Definitions> definitions = modelInstance.getModelElementsByType(Definitions.class);
		for (Definitions definition : definitions) {
			Collection<Process> processes = definition.getChildElementsByType(Process.class);
			for (Process process : processes) {
				process.replaceChildElement(eventToReplace, replacementEvent); // replace event
			}
		}
	}
}
