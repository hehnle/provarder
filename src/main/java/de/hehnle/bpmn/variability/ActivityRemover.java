package de.hehnle.bpmn.variability;

import java.util.Collection;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.camunda.bpm.model.bpmn.instance.di.Waypoint;

public class ActivityRemover {

	public static void removeActivitySafely(BpmnModelInstance modelInstance, String activityIdToRemove) {

		// Remove activity from process
		FlowNode activityToRemove = (FlowNode) modelInstance.getModelElementById(activityIdToRemove);

		if (activityToRemove != null) {

			SequenceFlow outgoingFlow = getSoleOutgoingFlow(activityToRemove);
			SequenceFlow incomingFlow = getSoleIncomingFlow(activityToRemove);

			reconnectSequenceFlows(outgoingFlow, incomingFlow);
			removeActivityAndOutgoingFlow(modelInstance, activityToRemove, outgoingFlow);

			// Remove graphical representation of activity
			Collection<BpmnPlane> bpmnPlanes = modelInstance.getModelElementsByType(BpmnPlane.class);
			Collection<BpmnShape> shape = modelInstance.getModelElementsByType(BpmnShape.class);

			removeShapesWithoutBpmnElement(shape, bpmnPlanes);
			Collection<BpmnEdge> edges = modelInstance.getModelElementsByType(BpmnEdge.class);
			removeEdgesWithoutBpmnElement(bpmnPlanes, edges);

			ShapeCoordinates coordinates = getCoordinatesForTarget(outgoingFlow.getTarget().getId(), shape);
			reconnectEdge(incomingFlow.getId(), coordinates, edges);
		}
	}

	private static void reconnectEdge(String incomingFlow, ShapeCoordinates coordinates, Collection<BpmnEdge> edges) {
		for (BpmnEdge edge : edges) {
			if (edge.getAttributeValue("bpmnElement") != null
					&& edge.getAttributeValue("bpmnElement").equals(incomingFlow)) {
				Collection<Waypoint> waypoints = edge.getWaypoints();
				Waypoint lastWayPoint = waypoints.stream().reduce((first, second) -> second).get();
				lastWayPoint.setX(coordinates.getX());
				lastWayPoint.setY(coordinates.getY());
				break;
			}
		}
	}

	private static void removeEdgesWithoutBpmnElement(Collection<BpmnPlane> bpmnPlanes, Collection<BpmnEdge> edges) {
		for (BpmnEdge edge : edges) {
			if (edge.getAttributeValue("bpmnElement") == null) {
				bpmnPlanes.forEach(plane -> {
					plane.removeChildElement(edge);
				});
			}
		}
	}

	private static ShapeCoordinates getCoordinatesForTarget(String targetId, Collection<BpmnShape> shapes) {
		for (BpmnShape bpmnShape : shapes) {
			if (bpmnShape.getAttributeValue("bpmnElement") != null
					&& bpmnShape.getAttributeValue("bpmnElement").equals(targetId)) {

				Bounds bounds = bpmnShape.getBounds();
				Double boundsX = bounds.getX();
				Double boundsY = bounds.getY();
				Double boundsHeight = bounds.getHeight();

				double x = boundsX;
				double y = boundsY + (0.5 * boundsHeight);
				return new ShapeCoordinates(x, y);
			}
		}
		throw new IllegalStateException("Shape for target " + targetId + " not found");
	}

	private static void removeShapesWithoutBpmnElement(Collection<BpmnShape> shape, Collection<BpmnPlane> bpmnPlane) {
		for (BpmnShape bpmnShape : shape) {
			if (bpmnShape.getAttributeValue("bpmnElement") == null) {
				bpmnPlane.forEach(diagram -> diagram.removeChildElement(bpmnShape));
			}
		}
	}

	private static void removeActivityAndOutgoingFlow(BpmnModelInstance modelInstance, FlowNode activityA,
			SequenceFlow outgoingFlow) {

		Collection<Definitions> definitions = modelInstance.getModelElementsByType(Definitions.class);
		for (Definitions definition : definitions) {

			Collection<Process> processes = definition.getChildElementsByType(Process.class);

			for (Process process : processes) {
				process.removeChildElement(outgoingFlow);
				process.removeChildElement(activityA);
			}
		}
	}

	private static void reconnectSequenceFlows(SequenceFlow outgoingFlow, SequenceFlow incomingFlow) {
		FlowNode target = outgoingFlow.getTarget();
		incomingFlow.setTarget(target);
		target.getIncoming().add(incomingFlow);
		target.getIncoming().remove(outgoingFlow);
	}

	private static SequenceFlow getSoleIncomingFlow(FlowNode activity) {
		Collection<SequenceFlow> incomingFlows = activity.getIncoming();
		SequenceFlow incomingFlow = null;
		if (incomingFlows.size() == 1) {
			incomingFlow = incomingFlows.stream().findFirst().get();
		} else {
			throw new IllegalStateException(
					"The size of incoming sequence flows of " + activity.getId() + " is not equal 1");
		}
		return incomingFlow;
	}

	private static SequenceFlow getSoleOutgoingFlow(FlowNode activity) {
		Collection<SequenceFlow> outgoingFlows = activity.getOutgoing();
		SequenceFlow outgoingFlow = null;
		if (outgoingFlows.size() == 1) {
			outgoingFlow = outgoingFlows.stream().findFirst().get();
		} else {
			throw new IllegalStateException(
					"The size of outgoing sequence flows of " + activity.getId() + " is not equal 1");
		}
		return outgoingFlow;
	}
}
