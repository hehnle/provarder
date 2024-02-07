package de.hehnle.bpmn.variability.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hehnle.bpmn.variability.App;

public class ConfigurationReader {
	private static final ObjectMapper mapper = new ObjectMapper();

	public static List<Configuration> readConfigurations() throws IOException {
		System.out.println("Please specify the directory for the configurations");
		String configurationDirectory = App.scanner.nextLine();

		File folder = new File(configurationDirectory);
		List<Configuration> configurations = new ArrayList<>();
		for (File file : folder.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".json")) {
				Configuration configuration = readConfiguration(file.getAbsolutePath());
				configurations.add(configuration);
			}
		}

		return configurations;
	}

	private static Configuration readConfiguration(String path) throws IOException {
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		byte data[] = new byte[fis.available()];
		fis.read(data);
		fis.close();
		String configurationAsString = new String(data);

		return mapper.readValue(configurationAsString, Configuration.class);
	}
}