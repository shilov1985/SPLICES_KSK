package MoKo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class Test extends ScanZsbFrameAndLogic {
	static JPanel[] testPanels;
	static Color bgColor;
	static StringBuilder arrayTestPoints, builderORD,
			arrayTestPointsForSetLayout;
	static JTextField[] testFields, fieldsForSetSize;
	static String[] array_ORD;
	static final String newLine = System.getProperty("line.separator");

	static JTextField testFieldsA, valueXSizeTestField, valueYSizeTestField,
			testFieldsTextSize;

	static JButton settingButton, testFieldForegroundColorButtonSet,
			testFieldBackgroundColorButtonSet;
	static int i = 0;
	// Variable for set the size of number in Test Fields;
	static int sizeNum = 9;
	static JFrame setLayoutFrame, frame;
	static OutputStream outputStream;
	static String[] paramPosition;
	static File[] listProgramsName;
	static File pathToDataBase;
	static boolean isFolderFound = false;
	static JButton resetButton;
	static String zsbScanValue;
	// Variables which consist the color for TestFields
	static ImageIcon firstImg, secondImg;
	static Color Color_FOREGROUND_CONVERTEDa, Color_BACKGROUND_CONVERTEDa;
	static String temp, allColors, firstColor, secondColor;

	static int repeatedRowsLength, finishCounter = 0;
	static JTextField errorLabel, scanETK_errField;
	static String[] zsbScanValueSplitted;
	// static instantionTestFields;

	// Write statistic

	private static final long serialVersionUID = 1L;

	static Enumeration<?> portList = null;

	private synchronized static void writeToFile(String msg) {
		String newLine = System.getProperty("line.separator");
		String fileName = "C:\\SPLICES_KSK\\Statistika\\STATISTIC.txt";
		PrintWriter printWriter = null;
		File file = new File(fileName);
		try {
			if (!file.exists())
				file.createNewFile();
			printWriter = new PrintWriter(new FileOutputStream(fileName, true));
			printWriter.write(newLine + msg);
		} catch (IOException ioex) {
			ioex.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}

	}

	public static void Logic() {

		final ScanZsbFrameAndLogic zsbInstantion = new ScanZsbFrameAndLogic();
		zsbInstantion.RunScanSZbWindow();

		// Enter button function
		scanZSBFrame.getRootPane().getInputMap(
				JComponent.WHEN_IN_FOCUSED_WINDOW

		).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "clickButton");

		scanZSBFrame.getRootPane().getActionMap().put("clickButton",
				new AbstractAction() {

					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent arg0) {

						// get zsb number from ZSB_field
						// Split input to get only zsb number without
						// extention
						zsbScanValueSplitted = ZSB_field.getText().split("\\.");

						if (zsbScanValueSplitted[0].length() != zsbLength) {
							zsbNotFoundLabel.setText("ZSB √–≈ÿ ¿!");
							ZSB_field.setText("");
						} else {

							// here we format the input for searching of .ORD or
							// .PRG program,it does not matter,
							// and if is needed we format that input with the
							// values substringned from
							// the MoKo.ini file between <FORMAT_ZSB_INPUT> and
							// </FORMAT_ZSB_INPUT>
							// add an extention of specified file.For example if
							// we search for .prg files we takes the extension
							// from MoKo.ini file and append it to the end of
							// the number from ZSB_field.
							zsbScanValue = zsbScanValueSplitted[0].substring(
									formatInputZSBStart, formatInputZSBEnd)
									+ extentionOfProgramFile;

							// here we format the input data from ZSB_field to
							// find the folder with specified name.
							String zsbScanValueFormatted = zsbInstantion.ZSB_field
									.getText().substring(formatInputStart,
											formatInputEnd).trim();

							File rootDir = new File(pathToZSB);
							// Check directory
							File[] listOfFiles = rootDir.listFiles();

							// Check if directory with specified name taken from
							// ZsbFrame -first scan is found.
							// if we have folder with that name ,search inside
							// this folder for ORD.file by tile number

							for (File file : listOfFiles) {

								if (file.getName()
										.equals(zsbScanValueFormatted)
										&& file.isDirectory()) {

									pathToDataBase = new File(pathToZSB + "\\"
											+ file.getName());

									listProgramsName = pathToDataBase
											.listFiles();

									// if found folder we proceed far away
									// Is it means that,we found the searched
									// folder
									// example:303003 whit formatted name of
									// course.

									isFolderFound = true;

								}
							}

							if (isFolderFound) {

								isFolderFound = false;
								for (int x = 0; x < listProgramsName.length; x++) {

									// if files with properly name has founded
									// proceed far away
									//

									if (listProgramsName[x].getName().equals(
											zsbScanValue)
											&& ZSB_field
													.getText()
													.substring(
															formatInputStart,
															formatInputEnd)
													.trim()
													.equals(
															zsbScanValueFormatted)) {

										pathToETKfile = pathToDataBase + "//"
												+ listProgramsName[x].getName();
										scanZSBFrame.setVisible(false);

										FileReader readETK = null;
										File file_ETK = new File(pathToETKfile);
										try {
											readETK = new FileReader(file_ETK);
										} catch (FileNotFoundException e2) {
											// TODO Auto-generated catch block
											e2.printStackTrace();
										}

										frame = new JFrame(
												"KSK_SPLICE by M.Shilov and K.Ivanov for SEBN_BG");
										frame.setLayout(null);
										frame.setSize(H_Size, V_Size);
										frame.setResizable(false);
										frame.setLocationRelativeTo(null);
										frame
												.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

										BufferedReader readOrdBuf = new BufferedReader(
												readETK);
										String lengthETK;

										// Get the length of the ETK file on
										// server
										StringBuilder ETK_File_array = new StringBuilder();
										try {
											while ((lengthETK = readOrdBuf
													.readLine()) != null) {
												ETK_File_array
														.append(lengthETK)
														.trimToSize();

											}
										} catch (IOException e2) {
											// TODO Auto-generated catch block
											e2.printStackTrace();
										}
										String toString_ETK = String
												.valueOf(ETK_File_array);
										String[] splittedETK_array = toString_ETK
												.split("Mn:");

										int counterRepeadedRows = 0;
										for (int i = 0; i < arrayDataBaseRows.length; i++) {

											for (int j = 0; j < splittedETK_array.length; j++) {
												if (arrayDataBaseRows[i][0]
														.equals(splittedETK_array[j]
																.trim())
														&& arrayDataBaseRows[i][0]
																.length() != 0
														&& splittedETK_array[j] != null
														&& arrayDataBaseRows[i][0]
																.length() != 0) {

													counterRepeadedRows++;

												}

											}

										}

										// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
										if (counterRepeadedRows == 0) {

											zsbNotFoundLabel.setText("");
											Test.Logic();
											zsbNotFoundLabel
													.setText("Õ»ŸŒ «¿ — ¿Õ»–¿Õ≈!");
											break;

										}

										// get second part if the rows divided
										// by =

										final String[][] finalRepeatedRows = new String[counterRepeadedRows][2];

										int counterRepeadedRowsFilnal = 0;
										for (int i = 0; i < arrayDataBaseRows.length - 1; i++) {

											for (int j = 0; j < splittedETK_array.length; j++) {

												if (arrayDataBaseRows[i][0]
														.trim()
														.equals(
																splittedETK_array[j]
																		.trim())) {

													finalRepeatedRows[counterRepeadedRowsFilnal][0] = splittedETK_array[j]
															.trim();

													counterRepeadedRowsFilnal++;
												}

											}

										}
										// get all repeated rows + match codes
										counterRepeadedRowsFilnal = 0;
										for (int i = 0; i < arrayDataBaseRows.length - 1; i++) {

											for (int j = 0; j < finalRepeatedRows.length; j++) {

												if (arrayDataBaseRows[i][0]
														.trim()
														.equals(
																finalRepeatedRows[j][0])) {

													finalRepeatedRows[counterRepeadedRowsFilnal][1] = arrayDataBaseRows[i][1];

													counterRepeadedRowsFilnal++;
												}

											}

										}

										try {
											readETK.close();
											readOrdBuf.close();
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										scanETK_Frame = new JFrame(
												"Please scan Tile number!");

										scanETK_Frame
												.setContentPane(new JLabel(
														new ImageIcon(
																"C:\\MoKo\\SZB_Frame.png")));
										scanETK_Frame
												.setContentPane(new JLabel(
														new ImageIcon(
																"C:\\SPLICES_KSK\\Icons\\scanZSBFramePic.png")));

										scanETK_Frame.setBounds(200, 420, 900,
												550);

										scanETK_Frame
												.setDefaultCloseOperation(scanZSBFrame.EXIT_ON_CLOSE);

										scanETK_Frame.setAlwaysOnTop(true);

										scanETK_Frame.setResizable(false);

										ETK_field = new JTextField();
										ETK_field.setVisible(true);
										scanETK_Frame.add(ETK_field);
										ETK_field.setFont(new Font("SansSerif",
												Font.BOLD, 90));

										ETK_field.setBounds(10, 280, 870, 180);
										ETK_field.setEditable(true);

										// System.out.println(ETK_File_array);

										scanETK_Frame.setVisible(true);
										final JTextField scanETK_FieldInfo = new JTextField(
												"—Í‡ÌË‡È:"
														+ finalRepeatedRows[0][1]);
										scanETK_Frame.add(scanETK_FieldInfo);
										scanETK_FieldInfo.setFont(new Font(
												"SansSerif", Font.BOLD, 60));
										scanETK_FieldInfo.setBounds(10, 50,
												870, 130);
										scanETK_FieldInfo.setEditable(false);

										scanETK_FieldInfo.requestFocus();

										ETK_field.requestFocus(true);
										scanETK_Frame
												.getRootPane()
												.getInputMap(
														JComponent.WHEN_IN_FOCUSED_WINDOW)
												.put(
														KeyStroke
																.getKeyStroke(
																		KeyEvent.VK_ENTER,
																		0),
														"clickButton");
										scanETK_Frame.getRootPane()
												.getActionMap().put(
														"clickButton",
														new AbstractAction() {

															@Override
															public void actionPerformed(
																	ActionEvent arg0) {

																ETK_field
																		.requestFocus(true);
																for (int i = 0; i < finalRepeatedRows.length; i++) {

																	scanETK_FieldInfo
																			.setText("—Í‡ÌË‡È:"
																					+ finalRepeatedRows[finishCounter][1]);

																	if (ETK_field
																			.getText()
																			.equals(
																					finalRepeatedRows[finishCounter][1])) {

																		frame
																				.setVisible(true);

																		ETK_field

																				.setText("");

																		JLabel labelOk = new JLabel(
																				"OK");
																		labelOk
																				.setFont(new Font(
																						"Sanse Serif",
																						Font.CENTER_BASELINE,
																						350));

																		labelOk
																				.setBounds(
																						100,
																						100,
																						100,
																						100);
																		JPanel okPanel = new JPanel();

																		okPanel
																				.add(labelOk);
																		frame
																				.setContentPane(okPanel);
																		okPanel
																				.setBackground(Color.green);

																		finishCounter++;

																		ETK_field
																				.requestFocus(true);

																		if (finishCounter == finalRepeatedRows.length) {
																			// Aktualna
																			// data
																			// i
																			// 4as

																			Date date = new Date();

																			SimpleDateFormat format = new SimpleDateFormat(
																					"dd.MM.yyyy  HH:mm");

																			String DateToStr = format
																					.format(date);

																			// zapisva
																			// statistika

																			writeToFile(DateToStr
																					+ " | "
																					+ zsbScanValueSplitted[0]
																					+ " | "
																					+ "Included cabels:"
																					+ (Arrays
																							.toString(finalRepeatedRows[0])));
																			scanETK_Frame
																					.setVisible(false);

																			try {

																				Thread
																						.sleep(2000);
																				frame
																						.setVisible(false);
																				Test
																						.Logic();
																				finishCounter = 0;
																				break;
																			} catch (InterruptedException e) {
																				// TODO
																				// Auto-generated
																				// catch
																				// block
																				e
																						.printStackTrace();
																			}

																		}

																	} else {
																		// scanETK_errField.setText("— ¿Õ»–¿À» —“≈ √–≈ÿ≈Õ ≈“» ≈“!");
																		ETK_field
																				.setText("— ¿Õ»–¿À» —“≈ √–≈ÿ≈Õ ≈“» ≈“!");

																		ETK_field
																				.setText("");

																	}

																}

															}
														}

												);

									} else {
										// Show error if zsb is not found
										zsbNotFoundLabel
												.setText("ZSB ÌÂ Â ÓÚÍËÚÓ!");
										if (x == listProgramsName.length - 1) {
											ZSB_field.setText("");
										}

									}
								}

							} else {

								// ////////////////////////////////////////if
								// folder whit the formatted name not found,
								// add label to scanZSBFrame.
								fileNotFoundLabel.setVisible(false);
								scanZSBFrame.add(folderNotFound);
								folderNotFound.setVisible(true);
								ZSB_field.setText("");

							}
						}
					}

				});

	}

	public static void main(String[] args) {

		Test aA = new Test();

		aA.Logic();
		

	}

}















































































