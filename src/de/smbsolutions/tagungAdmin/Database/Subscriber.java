package de.smbsolutions.tagungAdmin.Database;

/**
 * Instanz der Parse-DB Tabelle 'Subscribers'
 * 
 * @author Mirko
 * 
 */
public class Subscriber {

	public String objectId;
	public String mail;
	public String presentation_id;

	public Subscriber(String objectId, String mail, String presentation_id) {
		this.objectId = objectId;
		this.mail = mail;
		this.presentation_id = presentation_id;
	}

	public String getObjectId() {
		return objectId;
	}

	public String getMail() {
		return mail;
	}

	public String getPresentation_id() {
		return presentation_id;
	}

}