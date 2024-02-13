package de.hehnle.bpmn.variability;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import de.hehnle.bpmn.variability.configuration.Configuration;
import de.hehnle.bpmn.variability.configuration.ConfigurationReader;

public class App {

	public static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws IOException {

		List<Configuration> configurations = ConfigurationReader.readConfigurations();
		List<String> processModelPaths = getProcessModelPaths();

		for (String processModelPath : processModelPaths) {
			changeProcessModel(processModelPath, configurations);
		}

		scanner.close();
	}

	private static List<String> getProcessModelPaths() {

		System.out.println("Please specify the directory for the process models");
		String processModelDirectory = scanner.nextLine();

		File folder = new File(processModelDirectory);
		List<String> processModelPaths = new ArrayList<>();
		for (File file : folder.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".bpmn")) {
				processModelPaths.add(file.getAbsolutePath());
			}
		}

		return processModelPaths;
	}

	private static void changeProcessModel(String filePath, List<Configuration> configurations) throws IOException {

		System.out.println("Processing: " + filePath);
		InputStream stream = new FileInputStream(filePath);
		BpmnModelInstance modelInstance = Bpmn.readModelFromStream(stream);

		for (Configuration configuration : configurations) {

			if (configuration.getAction().equals(Action.REMOVE)) {
				ActivityRemover.removeActivitySafely(modelInstance, configuration.getElementId());
			} else if (configuration.getAction().equals(Action.REPLACE)
					&& configuration.getReplacementEvent().getMessageName() != null) {

				EventTypeReplacer.replaceEventWithMessageCatch(modelInstance, configuration.getElementId(),
						configuration.getReplacementEvent().getMessageName(),
						configuration.getReplacementEvent().getLabel());
			} else if (configuration.getAction().equals(Action.REPLACE)
					&& configuration.getReplacementEvent().getTimerDuration() != null) {
				EventTypeReplacer.replaceEventByTimerEvent(modelInstance, configuration.getElementId(),
						configuration.getReplacementEvent().getTimerDuration(),
						configuration.getReplacementEvent().getLabel());
			} else if (configuration.getAction().equals(Action.RESTRICT)) {
				GatewayRestricter.restrict(modelInstance, configuration.getElementId(),
						configuration.getClosingElementId(), configuration.getGatewayType());
			}
		}

		String outputProcessModelPath = filePath.split(".bpmn")[0] + "_adapted.bpmn";

		FileOutputStream os = new FileOutputStream(outputProcessModelPath);
		Bpmn.writeModelToStream(os, modelInstance);
		os.close();
	}
}