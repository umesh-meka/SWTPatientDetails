package com.jguru.swtpatientdetails;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import com.jguru.swtpatientdetails.httpclient.ApiHttpClient;
import com.jguru.swtpatientdetails.model.Address;
import com.jguru.swtpatientdetails.model.Patient;


/*
 * This class will generate the SWT UI to provide support for the below CURD operations:
 * 
 * <ul>
 * <li> Patient data saving
 * <li> Patient data updating
 * <li> Finding Patient by ID
 * </ul>
 *
 * @author      Umesh M
 * @version     1.0
 * @since       1.0
 */

public class PatientUpdates {

	Display display;
	Shell shell;
	Shell dialog;

	Text idText;
	Text firstNameText;
	Text lastNameText;
	Combo combo;
	Text telNumberText;
	Text dobText;

	//Current Address details
	Text curAddressIdText;
	Text streetText;
	Text cityText;
	Text stateText;
	Text pinCodeText;

	//Permanent Address Details
	Text perAddressIdText;
	Text perStreetText;
	Text perCityText;
	Text perStateText;
	Text perPinCodeText;

	public PatientUpdates(Display display, boolean viewPatient, boolean updatePatient) {
		this.display = display;

		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.ON_TOP);
		shell.setText("Create Patient");
		shell.setLayout(new GridLayout(2, true));

		shell.setMinimumSize(400, 0);

		if(viewPatient)
			shell.setText("View Patient");
		else if (updatePatient)
			shell.setText("Update Patient");

		// id
		if(viewPatient || updatePatient) {
			Label idLabel = new Label(shell, SWT.NONE);
			idLabel.setText("ID:");

			idText = new Text(shell, SWT.BORDER);
			idText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			idText.setEditable(false);
		}

		// first name
		Label firstNameLabel = new Label(shell, SWT.NONE);
		firstNameLabel.setText("First Name:");

		firstNameText = new Text(shell, SWT.BORDER);
		firstNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		firstNameText.setEditable(!viewPatient);

		//last name
		Label lastNameLabel = new Label(shell, SWT.NONE);
		lastNameLabel.setText("Last Name:");

		lastNameText = new Text(shell, SWT.BORDER);
		lastNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lastNameText.setEditable(!viewPatient);

		//gender
		Label genderLabel = new Label(shell, SWT.NONE);
		genderLabel.setText("Gender:");

		combo = new Combo (shell, SWT.FILL);
		combo.setItems ("MALE", "FEMALE");
		combo.select(0);
		combo.setEnabled(!viewPatient);

		//telephone number
		Label telNumberLabel = new Label(shell, SWT.NONE);
		telNumberLabel.setText("Telephone No:");

		telNumberText = new Text(shell, SWT.BORDER);
		telNumberText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		telNumberText.setEditable(!viewPatient);
		telNumberText.setTextLimit(10);

		//dob
		Label dobLabel = new Label(shell, SWT.NONE);
		dobLabel.setText("Date of Birth:");

		dobText = new Text(shell, SWT.BORDER);
		dobText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dobText.setText("YYYY/MM/DD");
		dobText.setEditable(!viewPatient);

		//current address
		Group addressGroup = new Group(shell, SWT.NONE);
		addressGroup.setText("Current Address");
		addressGroup.setLayout(new GridLayout(2, true));
		addressGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		//current address id
		if(viewPatient || updatePatient) {
			Label curAddressId = new Label(addressGroup, SWT.NONE);
			curAddressId.setText("Id:");
			curAddressId.setVisible(false);

			curAddressIdText = new Text(addressGroup, SWT.BORDER);
			curAddressIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			curAddressIdText.setVisible(false);	
		}

		// street				
		Label streetLabel = new Label(addressGroup, SWT.NONE);
		streetLabel.setText("Street:");

		streetText = new Text(addressGroup, SWT.BORDER);
		streetText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		streetText.setEditable(!viewPatient);

		// city
		Label cityLabel = new Label(addressGroup, SWT.NONE);
		cityLabel.setText("City:");

		cityText = new Text(addressGroup, SWT.BORDER);		
		cityText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cityText.setEditable(!viewPatient);

		// state
		Label stateLabel = new Label(addressGroup, SWT.NONE);
		stateLabel.setText("State:");

		stateText = new Text(addressGroup, SWT.BORDER);		
		stateText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		stateText.setEditable(!viewPatient);

		//pin code
		Label pinCodeLabel = new Label(addressGroup, SWT.NONE);
		pinCodeLabel.setText("PinCode:");

		pinCodeText = new Text(addressGroup, SWT.BORDER);
		pinCodeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pinCodeText.setEditable(!viewPatient);
		pinCodeText.setTextLimit(6);


		//permanent address
		Group perAddressGroup = new Group(shell, SWT.NONE);
		perAddressGroup.setText("Permanent Address");
		perAddressGroup.setLayout(new GridLayout(2, true));
		perAddressGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		//Permanent address id
		if(viewPatient || updatePatient) {
			Label perAddressId = new Label(perAddressGroup, SWT.NONE);
			perAddressId.setText("Id:");
			perAddressId.setVisible(false);

			perAddressIdText = new Text(perAddressGroup, SWT.BORDER);
			perAddressIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			perAddressIdText.setVisible(false);
		}

		// street
		Label perStreetLabel = new Label(perAddressGroup, SWT.NONE);
		perStreetLabel.setText("Street:");

		perStreetText = new Text(perAddressGroup, SWT.BORDER);
		perStreetText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		perStreetText.setEditable(!viewPatient);

		// city
		Label perCityLabel = new Label(perAddressGroup, SWT.NONE);
		perCityLabel.setText("City:");

		perCityText = new Text(perAddressGroup, SWT.BORDER);		
		perCityText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		perCityText.setEditable(!viewPatient);

		// state
		Label perStateLabel = new Label(perAddressGroup, SWT.NONE);
		perStateLabel.setText("State:");

		perStateText = new Text(perAddressGroup, SWT.BORDER);		
		perStateText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		perStateText.setEditable(!viewPatient);

		//pin code
		Label perPinCodeLabel = new Label(perAddressGroup, SWT.NONE);
		perPinCodeLabel.setText("PinCode:");

		perPinCodeText = new Text(perAddressGroup, SWT.BORDER);
		perPinCodeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		perPinCodeText.setEditable(!viewPatient);
		perPinCodeText.setTextLimit(6);

		if(!viewPatient) {
			Button ok = new Button (shell, SWT.PUSH);
			if(updatePatient) {
				ok.setText ("Update Patient");
			} else {
				ok.setText ("Create Patient");
			}
			ok.addSelectionListener(widgetSelectedAdapter(e -> {
				try {
					HttpResponse<String> response;
					if(updatePatient) {
						response  = ApiHttpClient.updatePatient(preparePatient());
					} else {
						response  = ApiHttpClient.savePatient(preparePatient());
					}
					ResponseBox responseBox = new ResponseBox(shell);
					if(response.statusCode() == 200) {
						responseBox.successResponse("Patient details updated successfully");
						shell.close();
						PatientsList.getPatientsList("");
						PatientsList.changeButtonsStatus(false);
					} else {
						responseBox.failureResponse(response);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}));
		}

		Button cancel = new Button (shell, SWT.PUSH);
		cancel.setText ("Close");
		cancel.addSelectionListener(widgetSelectedAdapter(e -> shell.close()));

		dobText.setEditable(false);
		dobText.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(!viewPatient && !updatePatient) {
					dialog = new Shell (shell, SWT.DIALOG_TRIM);
					dialog.setLayout (new GridLayout (1, true));
					final DateTime calendar = new DateTime (dialog, SWT.CALENDAR | SWT.BORDER);
					Button ok = new Button (dialog, SWT.PUSH);
					ok.setText ("OK");
					ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
					ok.addSelectionListener (new SelectionAdapter () {
						public void widgetSelected (SelectionEvent e) {
							String month = String.valueOf(calendar.getMonth () + 1); 
							month = month.length() == 2 ? month : "0"+month;						
							String day = String.valueOf(calendar.getDay()); 
							day = day.length() == 2 ? day : "0"+day;						
							dobText.setText(calendar.getYear () + "-" + month + "-" + day);
							dialog.close ();
						}
					});
					dialog.setDefaultButton (ok);
					dialog.pack ();
					dialog.open ();
				}
			}
		});

		shell.pack();
		shell.open();
	}

	public Patient getPatient(String patientId) throws IOException, InterruptedException {		
		Patient patient = ApiHttpClient.getPatient(patientId); 
		if(idText != null)
			idText.setText(patient.getID().toString());

		firstNameText.setText(patient.getFirstName());
		lastNameText.setText(patient.getLastName());
		combo.setText(patient.getGender());
		telNumberText.setText(patient.getTelNumber().toString());
		dobText.setText(patient.getDob());

		List<Address> addressList = patient.getAddress();
		for (int j=0; j<addressList.size(); j++) {
			Address address = addressList.get(j);			
			if(address.getAddressType().equals("PERMANENT")) {
				perAddressIdText.setText(address.getId().toString());
				perStreetText.setText(address.getStreetName());
				perCityText.setText(address.getCityName());
				perStateText.setText(address.getStateName());
				perPinCodeText.setText(address.getPinCode().toString());
			} else {
				curAddressIdText.setText(address.getId().toString());
				streetText.setText(address.getStreetName());
				cityText.setText(address.getCityName());
				stateText.setText(address.getStateName());
				pinCodeText.setText(address.getPinCode().toString());
			}
		}
		return patient;
	}

	public Patient preparePatient() {
		Patient patient = new Patient();
		try {
			List<Address> addressList = new ArrayList<Address>();
			Address currentAddress = new Address();
			if(curAddressIdText != null) {
				currentAddress.setId(Long.parseLong(curAddressIdText.getText()));
			}
			currentAddress.setStreetName(streetText.getText());
			currentAddress.setCityName(cityText.getText());
			currentAddress.setStateName(stateText.getText());
			currentAddress.setPinCode(Long.parseLong(pinCodeText.getText()));
			currentAddress.setAddressType("CURRENT");

			addressList.add(currentAddress);

			Address permanentAddress = new Address();
			if(perAddressIdText != null) {
				permanentAddress.setId(Long.parseLong(perAddressIdText.getText()));
			}
			permanentAddress.setStreetName(perStreetText.getText());
			permanentAddress.setCityName(perCityText.getText());
			permanentAddress.setStateName(perStateText.getText());
			permanentAddress.setPinCode(Long.parseLong(perPinCodeText.getText()));
			permanentAddress.setAddressType("PERMANENT");

			addressList.add(permanentAddress);

			if(idText != null)
				patient.setID(Long.parseLong(idText.getText()));
			patient.setFirstName(firstNameText.getText());
			patient.setLastName(lastNameText.getText());
			patient.setDob(dobText.getText());
			patient.setGender(combo.getText());

			patient.setAddress(addressList);

			patient.setTelNumber(Long.parseLong(telNumberText.getText()));
		} catch (Exception e1) {
			e1.printStackTrace();
			ResponseBox responseBox = new ResponseBox(shell);
			responseBox.exceptionHanlder(e1);
			shell.close();
		} 
		return patient;
	}
}
