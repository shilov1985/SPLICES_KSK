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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;

//hello GitHub
public class Test extends ScanZsbFrameAndLogic {

	static final String newLine = System.getProperty("line.separator");

	static int i = 0;
	// Variable for set the size of number in Test Fields;

	static JFrame frame;
	static OutputStream outputStream;

	static File[] listProgramsName;
	static File pathToDataBase;
	static boolean isFolderFound = false;

	static String zsbScanValue;
	// Variables which consist the color for TestFields

	static int repeatedRowsLength, finishCounter = 0;
	static JTextField errorLabel, scanETK_errField;
	static JTextArea field_ZSB, includedVariantsArea;
	static String[] zsbScanValueSplitted;
	static String textZSB;
	static JPanel picturePanel;

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
						textZSB = ZSB_field.getText();
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

										frame.setContentPane(new JLabel(
														new ImageIcon(
																"C:\\SPLICES_KSK\\Icons\\frame.jpg")));

										// add cable picture to the frame

										picturePanel = new JPanel();
										picturePanel.setBounds(400, 100, 600,
												400);
										frame.add(picturePanel);

										
										field_ZSB = new JTextArea();
										field_ZSB.setBounds(290, 15, 600, 50);
										field_ZSB.setFont(new Font(
												"Sanse Serif",
												Font.CENTER_BASELINE, 45));
										field_ZSB.setOpaque(false);
										field_ZSB.setText("ZSB=" + textZSB);
										frame.add(field_ZSB);

										includedVariantsArea = new JTextArea();

										includedVariantsArea.setBounds(20, 120,
												310, 385);
										includedVariantsArea.setFont(new Font(
												"Sanse Serif",
												Font.CENTER_BASELINE, 35));
										includedVariantsArea.setOpaque(false);
										includedVariantsArea.setEditable(false);
										frame.add(includedVariantsArea);

										JTextArea labelIncludedVariantsArea = new JTextArea(
												"”◊¿—“¬¿Ÿ»  ¿¡≈À»");
										labelIncludedVariantsArea.setBounds(20,
												70, 310, 40);
										labelIncludedVariantsArea
												.setFont(new Font(
														"Sanse Serif",
														Font.CENTER_BASELINE,
														25));
										labelIncludedVariantsArea
												.setOpaque(false);
										labelIncludedVariantsArea
												.setEditable(false);
										frame.add(labelIncludedVariantsArea);

										JTextArea pathToZSBlabel = new JTextArea(
												"œ˙Ú Í˙Ï .ETK Ù‡ÈÎ: "
														+ pathToETKfile);
										pathToZSBlabel.setBounds(0, 0, 800, 30);
										pathToZSBlabel.setFont(new Font(
												"Sanse Serif",
												Font.CENTER_BASELINE, 18));
										pathToZSBlabel.setOpaque(false);
										pathToZSBlabel.setEditable(false);
										frame.add(pathToZSBlabel);

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
														.append(lengthETK);

											}
										} catch (IOException e2) {
											// TODO Auto-generated catch block
											e2.printStackTrace();
										}
										// System.out.println(ETK_File_array);
										String toString_ETK = String
												.valueOf(ETK_File_array);
										String[] splittedETK_array = toString_ETK
												.split("Mn:");
										// System.out.println(Arrays.toString(splittedETK_array));
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
										for (int i = 0; i < arrayDataBaseRows.length; i++) {

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
										for (int i = 0; i < arrayDataBaseRows.length; i++) {

											for (int j = 0; j < finalRepeatedRows.length; j++) {

												if (arrayDataBaseRows[i][0]
														.trim()
														.equals(
																finalRepeatedRows[j][0])) {

													finalRepeatedRows[counterRepeadedRowsFilnal][1] = arrayDataBaseRows[i][1];
													// System.out.println(finalRepeatedRows[counterRepeadedRowsFilnal][0]);
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

										// add all included variant to
										// includedVariantsArea

										for (int i = 0; i < finalRepeatedRows.length; i++) {

											includedVariantsArea
													.append(finalRepeatedRows[i][1]);
											includedVariantsArea.append("\n");
											if (((i + 1) > 8)
													&& ((i + 1) <= 12)) {

												includedVariantsArea
														.setFont(new Font(
																"Sanse Serif",
																Font.CENTER_BASELINE,
																(24)));
											} else if (((i + 1) > 12)
													&& ((i + 1) <= 20)) {

												includedVariantsArea
														.setFont(new Font(
																"Sanse Serif",
																Font.CENTER_BASELINE,
																(14)));
											} else if (((i + 1) > 20)
													&& ((i + 1) <= 25)) {

												includedVariantsArea
														.setFont(new Font(
																"Sanse Serif",
																Font.CENTER_BASELINE,
																(11)));
											} else if ((i + 1) > 25
													&& ((i + 1) <= 30)) {

												includedVariantsArea
														.setFont(new Font(
																"Sanse Serif",
																Font.CENTER_BASELINE,
																(9)));

											} else {
												if ((i + 1) > 30) {
													includedVariantsArea
															.append("œÂÍ‡ÎÂÌÓ ÏÌÓ„Ó ‚‡Ë‡ÌÚË Á‡ ÔÓÍ‡Á‚‡ÌÂ");

												}
											}
										}

										ETK_field = new JTextField();
										ETK_field.setVisible(true);

										ETK_field.setFont(new Font("SansSerif",
												Font.BOLD, 90));

										ETK_field.setBounds(120, 520, 780, 120);
										ETK_field.setEditable(true);

										// System.out.println(ETK_File_array);
										// add first picture to the panel
										String pathToPic = "C:\\SPLICES_KSK\\CABLES_PICTURES\\"
												+ finalRepeatedRows[0][0]
												+ ".jpg";
				ImageIcon imageIcon	= new ImageIcon(pathToPic);			
					Image originalImage = imageIcon.getImage();
				
				
				Image scaledImage = originalImage.getScaledInstance(picturePanel.getWidth(),picturePanel.getHeight(),Image.SCALE_SMOOTH);
				JLabel tempPicLabel = new JLabel(
						new ImageIcon(scaledImage));
				
	//pictures									
////////////////////////////////////////////////////////////////////////////////////////////////////
										picturePanel.add(tempPicLabel);

										final JTextField scanETK_FieldInfo = new JTextField(
												"—Í‡ÌË‡È:"
														+ finalRepeatedRows[0][1]);
										frame.add(scanETK_FieldInfo);
										scanETK_FieldInfo.setFont(new Font(
												"SansSerif", Font.BOLD, 60));
										scanETK_FieldInfo.setBounds(20, 670,
												980, 70);
										scanETK_FieldInfo.setEditable(false);

										scanETK_FieldInfo.requestFocus();

										frame.setVisible(true);
										frame.add(ETK_field);
										ETK_field.requestFocus(true);
										frame
												.getRootPane()
												.getInputMap(
														JComponent.WHEN_IN_FOCUSED_WINDOW)
												.put(
														KeyStroke
																.getKeyStroke(
																		KeyEvent.VK_ENTER,
																		0),
														"clickButton");
										frame.getRootPane().getActionMap().put(
												"clickButton",
												new AbstractAction() {

													@Override
													public void actionPerformed(
															ActionEvent arg0) {
														ETK_field.setBackground(Color.red);
														ETK_field
																.requestFocus(true);
														for (int i = 0; i < finalRepeatedRows.length; i++) {
															
															scanETK_FieldInfo
																	.setText("—Í‡ÌË‡È:"
																			+ finalRepeatedRows[finishCounter][1]);
													
					
															if (ETK_field.getText().equals(
																			finalRepeatedRows[finishCounter][1])) {
																
																/**
																 * add picture to the panel 
																 */
																		picturePanel = new JPanel();
																		picturePanel.setBounds(400, 100, 600,
																				400);
																		frame.add(picturePanel);
																		
																		picturePanel.revalidate();
																		
																		if(finishCounter < finalRepeatedRows.length - 1 ){
																			
																		String pathToPicA = "C:\\SPLICES_KSK\\CABLES_PICTURES\\"
																			+ finalRepeatedRows[finishCounter+1][0]
																			+ ".jpg";
																		
																		//System.out.println(pathToPicA);
																		
											ImageIcon imageIcon	= new ImageIcon(pathToPicA);			
												Image originalImage = imageIcon.getImage();
											
											
											Image scaledImage = originalImage.getScaledInstance(picturePanel.getWidth(),picturePanel.getHeight(),Image.SCALE_SMOOTH);
											JLabel tempPicLabel = new JLabel(
													new ImageIcon(scaledImage));
											
											
								//pictures									
							////////////////////////////////////////////////////////////////////////////////////////////////////
											
																	picturePanel.add(tempPicLabel);
																	picturePanel.setVisible(true);
			frame.setVisible(true);										
																								
																
																System.out.println(pathToPicA);
															}
																		
																		ETK_field.setBackground(Color.green);		
																ETK_field
																		.setText("");

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
																			+ (Arrays.deepToString(finalRepeatedRows)));

												
																		frame.setVisible(false);
																		Test.Logic();
																		finishCounter = 0;
																		break;
																	
																		// TODO
																		// Auto-generated
																		// catch
																		// block
																	

																}

															} else {
																// scanETK_errField.setText("— ¿Õ»–¿À» —“≈ √–≈ÿ≈Õ ≈“» ≈“!");
														
																//ETK_field.setBackground(Color.red);
																ETK_field.setText("");

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
