package KSK;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ScanZsbFrameAndLogic {
	static JFrame scanZSBFrame;
	static JTextField ZSB_field;
	static JTextField ETK_field;
	static boolean isZSB_exist = false;
	static String pathZSB_DIR;
	static String pathToZSB;
	static String patToDataBase;
	static String pathToETKfile;
	static String formatInputLiteral;
	static StringBuilder iniStrBuild;
	static JLabel fileNotFound;
	static String extentionOfProgramFile, formatInputZSBLiteral;
	static int formatInputEnd, formatInputStart, formatInputZSBStart,
			formatInputZSBEnd, H_Size, V_Size;
	static JLabel folderNotFound, fileNotFoundLabel;
	static String[][] arrayDataBaseRows;
	static int zsbLength;
	static JTextField zsbNotFoundLabel;

	public void RunScanSZbWindow() {

		scanZSBFrame = new JFrame("Please scan ZSB number!");

		scanZSBFrame.setContentPane(new JLabel(new ImageIcon(
				"C:\\MoKo\\SZB_Frame.png")));

		scanZSBFrame.setContentPane(new JLabel(new ImageIcon(
				"C:\\SPLICES_KSK\\Icons\\scanZSBFramePic.png")));
		scanZSBFrame.setVisible(true);

		scanZSBFrame.setBounds(200, 200, 900, 550);

		scanZSBFrame.setLocationRelativeTo(null);

		scanZSBFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		scanZSBFrame.setAlwaysOnTop(true);

		scanZSBFrame.setResizable(false);

		JLabel scanZSBFrameLabel = new JLabel("— ¿Õ»–¿… ZSB ≈“» ≈“!");
		scanZSBFrame.add(scanZSBFrameLabel);

		scanZSBFrameLabel.setFont(new Font("SansSerif", Font.BOLD, 70));
		scanZSBFrameLabel.setBounds(20, 30, 900, 90);

		ZSB_field = new JTextField();
		ZSB_field.setVisible(true);
		ZSB_field.setEditable(true);

		ZSB_field.setFont(new Font("SansSerif", Font.BOLD, 110));
		scanZSBFrame.add(ZSB_field);
		ZSB_field.requestFocus();
		ZSB_field.setBounds(10, 260, 870, 200);

		// add field for show if zsb is not found
		zsbNotFoundLabel = new JTextField();

		scanZSBFrame.add(zsbNotFoundLabel);

		zsbNotFoundLabel.setOpaque(false);
		zsbNotFoundLabel.setFont(new Font("Sans Serif", Font.BOLD, 70));

		zsbNotFoundLabel.setBounds(10, 120, 870, 100);
		zsbNotFoundLabel.setEditable(false);
		zsbNotFoundLabel.setForeground(Color.red);
		FileReader readIni = null;
		File SPLICES_KSKiniFile = new File("C://SPLICES_KSK//SPLICES_KSK.ini");
		try {
			readIni = new FileReader(SPLICES_KSKiniFile);
		} catch (FileNotFoundException e) {
			ZSB_field
					.setText("ini file not found in C:/SPLICES_KSK/SPLICES_KSK.ini");
			ZSB_field.setFont(new Font("SansSerif", Font.BOLD, 15));
		}

		BufferedReader readIniBuffered = new BufferedReader(readIni);
		String tmpIni;
		iniStrBuild = new StringBuilder();
		try {
			while ((tmpIni = readIniBuffered.readLine()) != null) {
				iniStrBuild.append(tmpIni);
			}
		} catch (IOException e) {
			ZSB_field
					.setText("no lines found in C:/SPLICES_KSK/SPLICES_KSK.ini");
			ZSB_field.setFont(new Font("SansSerif", Font.BOLD, 25));
		}
		int tmpStartStart = iniStrBuild.indexOf("<PATH_ORD_DIR>") + 14;
		int tmpStartEnd = iniStrBuild.indexOf("</PATH_ORD_DIR>");

		try {
			pathToZSB = iniStrBuild.substring(tmpStartStart, tmpStartEnd);
		} catch (Exception e) {
			ZSB_field
					.setText("no lines found in C:/SPLICES_KSK/SPLICES_KSK.ini");
			ZSB_field.setFont(new Font("SansSerif", Font.BOLD, 25));
		}

		int formatStart = iniStrBuild.indexOf("<FORMAT_FOLDER_INPUT>") + 21;
		int formatEnd = iniStrBuild.indexOf("</FORMAT_FOLDER_INPUT>");

		try {
			formatInputLiteral = iniStrBuild.substring(formatStart, formatEnd);
		} catch (Exception e) {
			ZSB_field
					.setText("no lines found in C:/SPLICES_KSK/SPLICES_KSK.ini");
			ZSB_field.setFont(new Font("SansSerif", Font.BOLD, 25));
		}
		String[] splittedFormat = formatInputLiteral.split(",");
		formatInputStart = Integer.valueOf(splittedFormat[0]);
		formatInputEnd = Integer.valueOf(splittedFormat[1]);

		int extentionStart = iniStrBuild.indexOf("<FILE_EXTENTION>") + 16;
		int extentionEnd = iniStrBuild.indexOf("</FILE_EXTENTION>");

		try {
			extentionOfProgramFile = iniStrBuild.substring(extentionStart,
					extentionEnd);
		} catch (Exception e) {
			ZSB_field
					.setText("no lines found in C:/SPLICES_KSK/SPLICES_KSK.ini");
			ZSB_field.setFont(new Font("SansSerif", Font.BOLD, 25));
		}

		fileNotFoundLabel = new JLabel(extentionOfProgramFile
				+ " file not found.Please check the PROGRAM FOLDER!");

		fileNotFoundLabel.setForeground(Color.red);
		fileNotFoundLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
		fileNotFoundLabel.setBounds(0, 10, 500, 30);

		ZSB_field.setText("");
		fileNotFoundLabel.setVisible(false);

		folderNotFound = new JLabel("Not found folder with "
				+ extentionOfProgramFile + "files in " + pathToZSB);

		folderNotFound.setForeground(Color.red);
		folderNotFound.setFont(new Font("SansSerif", Font.BOLD, 30));
		folderNotFound.setBounds(0, 0, 800, 40);

		ZSB_field.setText("");

		folderNotFound.setVisible(false);

		// Get the formatting values for scan of .ord files in founded filder
		int formatStartZSBRead = iniStrBuild.indexOf("<FORMAT_ZSB_INPUT>") + 18;
		int formatEndZSBRead = iniStrBuild.indexOf("</FORMAT_ZSB_INPUT>");

		try {
			formatInputZSBLiteral = iniStrBuild.substring(formatStartZSBRead,
					formatEndZSBRead);

		} catch (Exception e) {
			ZSB_field
					.setText("File not found in C:\\SPLICES_KSK\\SPLICES_KSK.ini");
			ZSB_field.setFont(new Font("SansSerif", Font.BOLD, 25));
		}
		String tempZSBLiteral = formatInputZSBLiteral;
		String[] formatInputZSBLiteral = tempZSBLiteral.split(",");
		formatInputZSBStart = Integer.valueOf(formatInputZSBLiteral[0]);
		formatInputZSBEnd = Integer.valueOf(formatInputZSBLiteral[1]);

		// get the frame size of program
		int sizeStartFrame = iniStrBuild.indexOf("<MAIN_FRAME_SIZE>") + 17;
		int sizeEndFrame = iniStrBuild.indexOf("</MAIN_FRAME_SIZE>");
		String sizeFrame = iniStrBuild.substring(sizeStartFrame, sizeEndFrame);

		String[] sizeFrameArray = sizeFrame.split(",");
		H_Size = Integer.valueOf(sizeFrameArray[0]);
		V_Size = Integer.valueOf(sizeFrameArray[1]);

		// Get the length of zsb number for throws exception

		int zsbLengthStart = iniStrBuild.indexOf("<LENGTH_SCAN_SYMBOL>") + 20;
		int zsbLengthEnd = iniStrBuild.indexOf("</LENGTH_SCAN_SYMBOL>");
		zsbLength = Integer.valueOf(iniStrBuild.substring(zsbLengthStart,
				zsbLengthEnd));

		// get the pat to SPLICES_DATA_BASE

		int splicesStart = iniStrBuild.indexOf("<PATH_SPLICES_DATABASE>") + 23;
		int splicesEnd = iniStrBuild.indexOf("</PATH_SPLICES_DATABASE>");
		patToDataBase = iniStrBuild.substring(splicesStart, splicesEnd);

		// Get the rows from DATA BASE file

		File dataBaseFile = new File(patToDataBase);

		FileReader readData = null;

		try {
			readData = new FileReader(dataBaseFile);
		} catch (FileNotFoundException e2) {
			ZSB_field.setText("File not found: C:\\SPLICES_KSK\\DATA_BASE.txt");
			ZSB_field.setFont(new Font("SansSerif", Font.BOLD, 35));
		}

		BufferedReader readDatabaseBuf = new BufferedReader(readData);
		// get the length of data base file
		String x = null;
		int dataBaseLength = 0;
		try {
			while ((x = readDatabaseBuf.readLine()) != null) {
				dataBaseLength++;

			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			readData.close();
			readDatabaseBuf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get the rows from data base and save them in to array.

		FileReader readDataToSave = null;

		try {
			readDataToSave = new FileReader(dataBaseFile);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		BufferedReader readDatabaseBufToSave = new BufferedReader(
				readDataToSave);
		// get the length of the data base file
		String ToSave = null;
		arrayDataBaseRows = new String[dataBaseLength][2];
		int counterForSaving = 0;
		try {
			while ((ToSave = readDatabaseBufToSave.readLine()) != null) {
				String[] splitRow = ToSave.split("=");
				// System.out.println(splitRow[0]);
				// System.out.println(splitRow[1]);
				arrayDataBaseRows[counterForSaving][0] = splitRow[0];
				arrayDataBaseRows[counterForSaving][1] = splitRow[1];
				counterForSaving++;

			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			readDataToSave.close();
			readDatabaseBufToSave.close();
			readIni.close();
			readIniBuffered.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
