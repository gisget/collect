/**
 * 
 */
package org.openforis.collect.model;

import java.util.Date;
import java.util.List;

import org.openforis.collect.manager.process.ProcessStatus;
import org.openforis.collect.metamodel.SurveyTarget;

/**
 * @author M. Togna
 * @author S. Ricci
 * 
 */
public class SurveySummary {

	private Integer id;
	private Integer publishedId;
	private String name;
	private String uri;
	private String projectName;
	private boolean temporary;
	private boolean published;
	private Date creationDate;
	private Date modifiedDate;
	private SurveyTarget target;
	private ProcessStatus recordValidationProcessStatus;
	private String defaultLanguage;
	private List<String> languages;
	private Integer userGroupId;
	private UserGroup userGroup;
	private SurveyAvailability availability;
	
	public SurveySummary() {
	}
	
	public SurveySummary(Integer id, String name, String uri) {
		this(id, name, uri, null);
	}
	
	public SurveySummary(Integer id, String name, String uri, String projectName) {
		super();
		this.id = id;
		this.name = name;
		this.uri = uri;
		this.projectName = projectName;
		this.published = true;
		this.temporary = false;
	}
	
	public static SurveySummary createFromSurvey(CollectSurvey survey) {
		return createFromSurvey(survey, null);
	}
	
	public static SurveySummary createFromSurvey(CollectSurvey survey, String lang) {
		SurveySummary summary = new SurveySummary(survey.getId(), survey.getName(), survey.getUri());
		summary.fillFromSurvey(survey, lang);
		return summary;
	}

	public void fillFromSurvey(CollectSurvey survey, String lang) {
		this.setProjectName(survey.getProjectName(lang, true));
		this.setTemporary(survey.isTemporary());
		this.setCreationDate(survey.getCreationDate());
		this.setModifiedDate(survey.getModifiedDate());
		this.setTarget(survey.getTarget());
		this.setDefaultLanguage(survey.getDefaultLanguage());
		this.setLanguages(survey.getLanguages());
		this.setUserGroupId(survey.getUserGroupId());
		this.setUserGroup(survey.getUserGroup());
	}
	
	public boolean isRecordValidationInProgress() {
		return recordValidationProcessStatus != null && recordValidationProcessStatus.isRunning();
	}
	
	public int getRecordValidationProgressPercent() {
		return recordValidationProcessStatus == null ? 0: recordValidationProcessStatus.getProgressPercent();
	}
	
	public Integer getId() {
		return id;
	}
	
	public boolean isTemporary() {
		return temporary;
	}

	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}
	
	public String getName() {
		return name;
	}

	public String getUri() {
		return uri;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public boolean isPublished() {
		return published;
	}
	
	public void setPublished(boolean published) {
		this.published = published;
	}
	
	public Integer getPublishedId() {
		return publishedId;
	}
	
	public void setPublishedId(Integer publishedId) {
		this.publishedId = publishedId;
	}

	public ProcessStatus getRecordValidationProcessStatus() {
		return recordValidationProcessStatus;
	}

	public void setRecordValidationProcessStatus(
			ProcessStatus recordValidationProcessStatus) {
		this.recordValidationProcessStatus = recordValidationProcessStatus;
	}
	
	public boolean isNotLinkedToPublishedSurvey() {
		return temporary && publishedId == null;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public SurveyTarget getTarget() {
		return target;
	}

	public void setTarget(SurveyTarget target) {
		this.target = target;
	}
	
	public String getDefaultLanguage() {
		return defaultLanguage;
	}
	
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}
	
	public List<String> getLanguages() {
		return languages;
	}
	
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}
	
	public Integer getUserGroupId() {
		return userGroupId;
	}
	
	public void setUserGroupId(Integer userGroupId) {
		this.userGroupId = userGroupId;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}
	
	public String getUserGroupLabel() {
		return userGroup == null ? null : userGroup.getLabel();
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
		this.userGroupId = userGroup == null ? null: userGroup.getId();
	}
	
	public SurveyAvailability getAvailability() {
		return availability;
	}
	
	public void setAvailability(SurveyAvailability availability) {
		this.availability = availability;
	}
}
