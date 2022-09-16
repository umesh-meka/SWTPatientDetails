/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.jguru.swtpatientdetails;


import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import com.jguru.swtpatientdetails.httpclient.ApiHttpClient;
import com.jguru.swtpatientdetails.model.Address;
import com.jguru.swtpatientdetails.model.Patient;

/*
 * This Class is used to display the list of patients from database
 *
 * This class will generate the SWT UI to provide support for the below CURD operations:
 * <ul>
 * <li> Patient data deleting by ID
 * <li> Finding Patient by patient name
 * <li> Finding all Patients list in database
 * </ul>
 *
 * @author      Umesh M
 * @version     1.0
 * @since       1.0
 */

public class PatientsList {
	static void createMenuItem(Menu parent, final TreeColumn column) {
		final MenuItem itemName = new MenuItem(parent, SWT.CHECK);
		itemName.setText(column.getText());
		itemName.setSelection(column.getResizable());
		itemName.addListener(SWT.Selection, event -> {
			if (itemName.getSelection()) {
				column.setWidth(150);
				column.setResizable(true);
			} else {
				column.setWidth(0);
				column.setResizable(false);
			}
		});
	}


	public static Display display = null;
	public static Shell shell = null;
	public static Tree tree = null;
	public static String patientId = null;
	public static boolean patientSelected = false;
	static Button viewPatient = null;
	static Button updatePatient = null;
	static Button deletePatient = null;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		display = new Display();
		shell = new Shell(display);
		shell.setText("Patients List");
		shell.setLayout(new GridLayout(4, true));

		Text searchByNameText = new Text(shell, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		searchByNameText.setLayoutData(gridData);

		Button searchByNameButton = new Button (shell, SWT.PUSH);
		GridData gridDataButton = new GridData();
		gridDataButton.horizontalAlignment = GridData.BEGINNING;
		gridDataButton.horizontalSpan = 1;
		searchByNameButton.setLayoutData(gridDataButton);
		searchByNameButton.setText ("Search Patient By Name");
		searchByNameButton.addSelectionListener(widgetSelectedAdapter(e -> {
			try {
				getPatientsList(searchByNameText.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}));

		GridData gridDataForTree = new GridData();
		gridDataForTree.horizontalAlignment = GridData.FILL;
		gridDataForTree.horizontalSpan = 1;

		tree = new Tree(shell, SWT.V_SCROLL|SWT.H_SCROLL | SWT.FULL_SELECTION);
		tree.setHeaderVisible(true);
		tree.setLayoutData(gridDataForTree);
		final Menu headerMenu = new Menu(shell, SWT.POP_UP);


		final TreeColumn columnId = new TreeColumn(tree, SWT.NONE);
		columnId.setText("ID");
		columnId.setWidth(150);
		createMenuItem(headerMenu, columnId);

		final TreeColumn columnFirstName = new TreeColumn(tree, SWT.NONE);
		columnFirstName.setText("First Name");
		columnFirstName.setWidth(150);
		createMenuItem(headerMenu, columnFirstName);

		final TreeColumn columnLastName = new TreeColumn(tree, SWT.NONE);
		columnLastName.setText("Last Name");
		columnLastName.setWidth(150);
		createMenuItem(headerMenu, columnLastName);

		final TreeColumn columndob = new TreeColumn(tree, SWT.NONE);
		columndob.setText("Date of Birth");
		columndob.setWidth(150);
		createMenuItem(headerMenu, columndob);

		final TreeColumn columnGender = new TreeColumn(tree, SWT.NONE);
		columnGender.setText("Gender");
		columnGender.setWidth(150);
		createMenuItem(headerMenu, columnGender);

		final TreeColumn columnTelNumber = new TreeColumn(tree, SWT.NONE);
		columnTelNumber.setText("Telephone No");
		columnTelNumber.setWidth(150);
		createMenuItem(headerMenu, columnTelNumber);
		
		final TreeColumn columnAddressType = new TreeColumn(tree, SWT.NONE);
		columnAddressType.setText("Address Type");
		columnAddressType.setWidth(150);
		createMenuItem(headerMenu, columnAddressType);

		final TreeColumn columnStreetName = new TreeColumn(tree, SWT.NONE);
		columnStreetName.setText("Street");
		columnStreetName.setWidth(150);
		createMenuItem(headerMenu, columnStreetName);
		
		final TreeColumn columnCityName = new TreeColumn(tree, SWT.NONE);
		columnCityName.setText("City");
		columnCityName.setWidth(150);
		createMenuItem(headerMenu, columnCityName);

		final TreeColumn columnStateName = new TreeColumn(tree, SWT.NONE);
		columnStateName.setText("State");
		columnStateName.setWidth(150);
		createMenuItem(headerMenu, columnStateName);

		final TreeColumn columnPinCode = new TreeColumn(tree, SWT.NONE);
		columnPinCode.setText("Pin Code");
		columnPinCode.setWidth(150);
		createMenuItem(headerMenu, columnPinCode);	

		getPatientsList("");

		//Right click menu item actions -- (View, Update, Delete)
		Menu menu = new Menu (shell, SWT.POP_UP);
		tree.setMenu (menu);

		MenuItem view = new MenuItem (menu, SWT.PUSH);
		view.setText ("View");
		view.addListener (SWT.Selection, event -> {
			viewActionListener();
		});
		MenuItem update = new MenuItem (menu, SWT.PUSH);
		update.setText ("Update");
		update.addListener (SWT.Selection, event -> {
			updateActionListener();
		});
		MenuItem delete = new MenuItem (menu, SWT.PUSH);
		delete.setText ("Delete");
		delete.addListener (SWT.Selection, event -> {
			deleteActionListener();
		});	

		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				TreeItem[] selected = tree.getSelection();
				if (selected.length > 0) {
					patientId = selected[0].getText(0);
					patientSelected = true;
					changeButtonsStatus(patientSelected);
				}
			}
		});	
		
		GridData gridDataForButtons = new GridData();
		gridDataForTree.horizontalAlignment = GridData.FILL;
		gridDataForTree.horizontalSpan = 4;
		
		Button createPatient = new Button (shell, SWT.PUSH);
		createPatient.setText ("Create Patient");
		createPatient.setLayoutData(gridDataForButtons);
		createPatient.addSelectionListener(widgetSelectedAdapter(e -> {
			new PatientUpdates(display, false, false);
		}));
		
		
		viewPatient = new Button (shell, SWT.PUSH);
		viewPatient.setText ("View Patient");
		viewPatient.setEnabled(patientSelected);
		viewPatient.setLayoutData(gridDataForButtons);
		viewPatient.addSelectionListener(widgetSelectedAdapter(e -> {
			viewActionListener();
		}));
		
		updatePatient = new Button (shell, SWT.PUSH);
		updatePatient.setText ("Update Patient");
		updatePatient.setEnabled(patientSelected);
		updatePatient.setLayoutData(gridDataForButtons);
		updatePatient.addSelectionListener(widgetSelectedAdapter(e -> {
			updateActionListener();
		}));
		
		deletePatient = new Button (shell, SWT.PUSH);
		deletePatient.setText ("Delete Patient");
		deletePatient.setEnabled(patientSelected);
		deletePatient.setLayoutData(gridDataForButtons);
		deletePatient.addSelectionListener(widgetSelectedAdapter(e -> {
			deleteActionListener();
		}));
		
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
	
	static void changeButtonsStatus(boolean patientSelected) {
		viewPatient.setEnabled(patientSelected);
		updatePatient.setEnabled(patientSelected);
		deletePatient.setEnabled(patientSelected);
	}

	static void viewActionListener() {

		if(patientId == "" || patientId == null) {
			ResponseBox responseBox = new ResponseBox(shell);
			responseBox.waringResponse("Please select patient record");
			return;
		}
		
		PatientUpdates patientModify = new PatientUpdates(display, true, false);
		try {
			patientModify.getPatient(patientId);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
	}
	
	static void updateActionListener() {

		if(patientId == "" || patientId == null) {
			ResponseBox responseBox = new ResponseBox(shell);
			responseBox.waringResponse("Please select patient record");
			return;
		}
		
		PatientUpdates patientModify = new PatientUpdates(display, false, true);
		try {
			patientModify.getPatient(patientId);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
	}
	
	static void deleteActionListener() {
		try {

			if(patientId == "" || patientId == null) {
				ResponseBox responseBox = new ResponseBox(shell);
				responseBox.waringResponse("Please select patient record");
				return;
			}
			
			HttpResponse<String> response =  ApiHttpClient.deletePatient(patientId);

			ResponseBox responseBox = new ResponseBox(shell);
			if(response.statusCode() == 200) {
				responseBox.successResponse("Patient details removed successfully");
				getPatientsList("");
				patientSelected = false;
				patientId = null;
				changeButtonsStatus(patientSelected);
			} else {
				responseBox.failureResponse(response);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	static void getPatientsList(String byName) throws IOException, InterruptedException {
		List<Patient> patientsList = null;
		if(byName != "" && !byName.isEmpty() && !byName.equals("")) {
			patientsList = ApiHttpClient.fetchByName(byName);
		} else {
			patientsList = ApiHttpClient.fetchAllPatients();
		}

		tree.removeAll();
		tree.update();

		for (int i=0; i<patientsList.size(); i++) {
			Patient patientListItem = patientsList.get(i);

			TreeItem item = new TreeItem(tree, SWT.NONE);

			item.setText (0, String.valueOf(patientListItem.getID()));
			item.setText (1, patientListItem.getFirstName());
			item.setText (2, patientListItem.getLastName());
			item.setText (3, patientListItem.getDob().toString());
			item.setText (4, patientListItem.getGender().toString());
			item.setText (5, patientListItem.getTelNumber().toString());

			List<Address> addressList = patientListItem.getAddress();
			for (int j=0; j<addressList.size(); j++) {
				Address address = addressList.get(j);
				
				TreeItem subItem = new TreeItem(item, SWT.NONE);
				if(address.getAddressType().equals("PERMANENT")) {
					subItem.setText (6, "Permanent Address");
				} else {
					subItem.setText (6, "Current Address");
				}
				subItem.setText (7, address.getStreetName());
				subItem.setText (8, address.getCityName());
				subItem.setText (9, address.getStateName());
				subItem.setText (10, address.getPinCode().toString());
			}
		}
	}
}