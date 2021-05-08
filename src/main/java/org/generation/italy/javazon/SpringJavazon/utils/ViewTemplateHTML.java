package org.generation.italy.javazon.SpringJavazon.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class ViewTemplateHTML {

	private String percorsoTemplate;

	public ViewTemplateHTML(String percorsoTemplate) {
		super();
		this.percorsoTemplate = percorsoTemplate;
	}
	
	protected String caricaTemplate(String nomeTemplate) {
		String ris = "";
		
		String percorsoFile = percorsoTemplate + "/" + nomeTemplate + ".html";
		
		try (Scanner file = new Scanner(new File(percorsoFile))) {
			while (file.hasNextLine()) {
				ris += file.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return ris;
	}
}
