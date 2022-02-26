package br.com.carbi.report;

import br.com.carbi.collection.StudentsCollection;
import br.com.carbi.exception.ConnectionException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Report {
	
	public Report() {
		
	}

	public static void genetate(StudentsCollection studentsColletion) throws JRException, ConnectionException {
		JasperReport jasperReport;

		String pathFileLinux = "/home/spt01/eclipse-workspace/carbi/jasper/wallet2.jasper";
		String pathFileWindows = "C:\\wallet.jasper";
		String operationalSystem = System.getProperty("os.name");
		
		if (operationalSystem.contains("Linux")) {
			jasperReport = (JasperReport) JRLoader.loadObjectFromFile(pathFileLinux);    
		} else {
			jasperReport = (JasperReport) JRLoader.loadObjectFromFile(pathFileWindows);    
		}

		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(studentsColletion.getColletion());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);
		JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
		jasperViewer.setVisible(true);
	}
}
