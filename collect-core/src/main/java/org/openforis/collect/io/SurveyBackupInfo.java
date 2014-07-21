package org.openforis.collect.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

import org.openforis.collect.Collect;
import org.openforis.collect.utils.Dates;
import org.openforis.commons.versioning.Version;

/**
 * 
 * @author S. Ricci
 *
 */
public class SurveyBackupInfo {

	private static final String DATE_PROP = "date";
	private static final String COLLECT_VERSION_PROP = "collect_version";
	private static final String SURVEY_URI_PROP = "survey_uri";
	private static final String SURVEY_NAME_PROP = "survey_name";
	
	private static final Version VERSION_3_0 = new Version("3.0"); //for backwards compatibility
	
	private Version collectVersion;
	private Date date;
	private String surveyUri;
	private String surveyName;
	
	public SurveyBackupInfo() {
		this.collectVersion = Collect.getVersion();
		this.date = new Date();
	}
	
	public void store(OutputStream os) throws IOException {
		Properties props = toProperties();
		props.store(os, null);
	}
	
	protected Properties toProperties() {
		Properties props = new Properties();
		props.setProperty(COLLECT_VERSION_PROP, collectVersion.toString());
		props.setProperty(DATE_PROP, Dates.formatDateToXML(date));
		props.setProperty(SURVEY_URI_PROP, surveyUri);
		props.setProperty(SURVEY_NAME_PROP, surveyName);
		return props;
	}

	public static SurveyBackupInfo parse(InputStream is) throws IOException {
		Properties props = new Properties();
		props.load(is);
		SurveyBackupInfo info = parse(props);
		return info;
	}
	
	protected static SurveyBackupInfo parse(Properties props) {
		SurveyBackupInfo info = new SurveyBackupInfo();
		info.surveyUri = props.getProperty(SURVEY_URI_PROP);
		info.surveyName = props.getProperty(SURVEY_NAME_PROP);
		info.collectVersion = new Version(props.getProperty(COLLECT_VERSION_PROP));
		info.date = Dates.parseXMLDate(props.getProperty(DATE_PROP));
		return info;
	}
	
	public static SurveyBackupInfo createOldVersionInstance(String surveyUri) {
		SurveyBackupInfo info = new SurveyBackupInfo();
		info.surveyUri = surveyUri;
		info.setCollectVersion(VERSION_3_0);
		info.setDate(null);
		return info;
	}
	
	public boolean isOldFormat() {
		return VERSION_3_0.equals(collectVersion);
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getSurveyUri() {
		return surveyUri;
	}
	
	public void setSurveyUri(String surveyUri) {
		this.surveyUri = surveyUri;
	}
	
	public String getSurveyName() {
		return surveyName;
	}
	
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}
	
	public Version getCollectVersion() {
		return collectVersion;
	}

	public void setCollectVersion(Version collectVersion) {
		this.collectVersion = collectVersion;
	}
	
}
