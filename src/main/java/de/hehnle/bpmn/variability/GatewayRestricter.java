package de.hehnle.bpmn.variability;

import java.util.Collection;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.ParallelGateway;
import org.camunda.bpm.model.bpmn.instance.Process;

import de.hehnle.bpmn.variability.configuration.GatewayType;

public class GatewayRestricter {

	public static void restrict(BpmnModelInstance modelInstance, String openingId, String closingId,
			GatewayType gatewayType) {
		FlowNode openingGatewayToReplace = (FlowNode) modelInstance.getModelElementById(openingId);
		FlowNode closingGatewayToReplace = (FlowNode) modelInstance.getModelElementById(closingId);

		if (openingGatewayToReplace != null && closingGatewayToReplace != null) {

			if (gatewayType.equals(GatewayType.PARALLEL)) {

				ParallelGateway openingParallelGateway = modelInstance.newInstance(ParallelGateway.class);
				replaceGateway(modelInstance, openingGatewayToReplace, openingParallelGateway);

				ParallelGateway closingParallelGateway = modelInstance.newInstance(ParallelGateway.class);
				replaceGateway(modelInstance, closingGatewayToReplace, closingParallelGateway);
			} else if (gatewayType.equals(GatewayType.EXCLUSIVE)) {

				ExclusiveGateway openingExclusiveGateway = modelInstance.newInstance(ExclusiveGateway.class);
				replaceGateway(modelInstance, openingGatewayToReplace, openingExclusiveGateway);

				ExclusiveGateway closingExclusiveGateway = modelInstance.newInstance(ExclusiveGateway.class);
				replaceGateway(modelInstance, closingGatewayToReplace, closingExclusiveGateway);
			}
		}
	}

	private static void replaceGateway(BpmnModelInstance modelInstance, FlowNode eventToReplace,
			FlowNode replacementEvent) {

		Collection<Definitions> definitions = modelInstance.getModelElementsByType(Definitions.class);
		for (Definitions definition : definitions) {
			Collection<Process> processes = definition.getChildElementsByType(Process.class);
			for (Process process : processes) {
				process.replaceChildElement(eventToReplace, replacementEvent);
			}
		}
	}
}