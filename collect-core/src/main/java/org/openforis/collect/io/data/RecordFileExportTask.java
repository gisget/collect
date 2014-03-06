package org.openforis.collect.io.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.openforis.collect.io.SurveyBackupJob;
import org.openforis.collect.manager.RecordFileManager;
import org.openforis.collect.manager.RecordManager;
import org.openforis.collect.manager.SurveyManager;
import org.openforis.collect.manager.exception.RecordFileException;
import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.CollectRecord.Step;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.persistence.xml.DataMarshaller;
import org.openforis.concurrency.Task;
import org.openforis.idm.metamodel.FileAttributeDefinition;
import org.openforis.idm.model.FileAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author S. Ricci
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RecordFileExportTask extends Task {

	@Autowired
	private RecordManager recordManager;
	@Autowired
	private RecordFileManager recordFileManager;
	@Autowired
	private SurveyManager surveyManager;
	@Autowired
	private DataMarshaller dataMarshaller;

	//input
	private ZipOutputStream zipOutputStream;
	private CollectSurvey survey;
	private Step[] steps;
	private String rootEntityName;
	private String zipEntryPrefix;
	
	public RecordFileExportTask() {
		super();
		this.steps = Step.values();
	}

	@Override
	protected long countTotalItems() {
		int count = 0;
		List<CollectRecord> recordSummaries = loadAllSummaries();
		for (CollectRecord summary : recordSummaries) {
			for (Step step: steps) {
				if ( step.getStepNumber() <= summary.getStep().getStepNumber() ) {
					count ++;
				}
			}
		}
		return count;
	}
	
	@Override
	protected void execute() throws Throwable {
		List<CollectRecord> recordSummaries = loadAllSummaries();
		if ( recordSummaries != null && steps != null && steps.length > 0 ) {
			for (CollectRecord summary : recordSummaries) {
				if ( isRunning() ) {
					for (Step step : steps) {
						int stepNum = step.getStepNumber();
						if ( stepNum <= summary.getStep().getStepNumber() ) {
							backup(summary, Step.valueOf(stepNum));
							incrementItemsProcessed();
						}
					}
				} else {
					break;
				}
			}
		}
	}

	private List<CollectRecord> loadAllSummaries() {
		List<CollectRecord> summaries = recordManager.loadSummaries(survey, rootEntityName);
		return summaries;
	}
	
	private void backup(CollectRecord summary, Step step) throws RecordFileException, IOException {
		Integer id = summary.getId();
		CollectRecord record = recordManager.load(survey, id, step);
		List<FileAttribute> fileAttributes = record.getFileAttributes();
		for (FileAttribute fileAttribute : fileAttributes) {
			if ( ! fileAttribute.isEmpty() ) {
				File file = recordFileManager.getRepositoryFile(fileAttribute);
				if ( file == null ) {
					String message = String.format("Missing file: %s attributeId: %d attributeName: %s", 
							fileAttribute.getFilename(), fileAttribute.getInternalId(), fileAttribute.getName());
					throw new RecordFileException(message);
				} else {
					String entryName = calculateRecordFileEntryName(fileAttribute);
					writeFile(file, entryName);
				}
			}
		}
	}

	public String calculateRecordFileEntryName(FileAttribute fileAttribute) {
		FileAttributeDefinition fileAttributeDefinition = fileAttribute.getDefinition();
		String repositoryRelativePath = RecordFileManager.getRepositoryRelativePath(fileAttributeDefinition, SurveyBackupJob.ZIP_FOLDER_SEPARATOR, false);
		String entryName = zipEntryPrefix + repositoryRelativePath + SurveyBackupJob.ZIP_FOLDER_SEPARATOR + fileAttribute.getFilename();
		return entryName;
	}

	private void writeFile(File file, String entryName) throws IOException {
		ZipEntry entry = new ZipEntry(entryName);
		zipOutputStream.putNextEntry(entry);
		IOUtils.copy(new FileInputStream(file), zipOutputStream);
		zipOutputStream.closeEntry();
		zipOutputStream.flush();
	}

	public CollectSurvey getSurvey() {
		return survey;
	}
	
	public void setSurvey(CollectSurvey survey) {
		this.survey = survey;
	}
	
	public String getRootEntityName() {
		return rootEntityName;
	}
	
	public void setRootEntityName(String rootEntityName) {
		this.rootEntityName = rootEntityName;
	}
	
	public Step[] getSteps() {
		return steps;
	}
	
	public void setSteps(Step[] steps) {
		this.steps = steps;
	}

	public ZipOutputStream getZipOutputStream() {
		return zipOutputStream;
	}

	public void setZipOutputStream(ZipOutputStream zipOutputStream) {
		this.zipOutputStream = zipOutputStream;
	}

	public String getZipEntryPrefix() {
		return zipEntryPrefix;
	}

	public void setZipEntryPrefix(String zipEntryPrefix) {
		this.zipEntryPrefix = zipEntryPrefix;
	}
	
}
