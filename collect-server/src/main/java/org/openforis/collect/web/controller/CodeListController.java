package org.openforis.collect.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.openforis.collect.manager.CodeListManager;
import org.openforis.collect.manager.RecordManager;
import org.openforis.collect.manager.SurveyManager;
import org.openforis.collect.manager.dataexport.codelist.CodeListExportProcess;
import org.openforis.collect.metamodel.uiconfiguration.view.Views;
import org.openforis.collect.metamodel.view.CodeListItemView;
import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.CollectRecord.Step;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.utils.Proxies;
import org.openforis.idm.metamodel.CodeAttributeDefinition;
import org.openforis.idm.metamodel.CodeList;
import org.openforis.idm.metamodel.CodeListItem;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CodeListController {

	private static final String CSV_CONTENT_TYPE = "text/csv";
	private static final String CSV_EXTENSION = ".csv";
	
	@Autowired
	private CodeListManager codeListManager;
	@Autowired
	private RecordManager recordManager;
	@Autowired
	private SurveyManager surveyManager;
	
	@RequestMapping(value = "/codelist/export/{surveyId}/{codeListId}", method = RequestMethod.GET)
	public @ResponseBody String exportCodeList(HttpServletResponse response,
			@PathVariable("surveyId") Integer surveyId, 
			@PathVariable("codeListId") Integer codeListId) throws IOException {
		return exportCodeList(response, surveyId, false, codeListId);
	}
	
	@RequestMapping(value = "/codelist/export/work/{surveyId}/{codeListId}", method = RequestMethod.GET)
	public @ResponseBody String exportCodeListWork(HttpServletResponse response,
			@PathVariable("surveyId") Integer surveyId,
			@PathVariable("codeListId") Integer codeListId) throws IOException {
		return exportCodeList(response, surveyId, true, codeListId);
	}
	
	@RequestMapping(value = "/survey/{surveyId}/codelists/{codeListId}/items", method = RequestMethod.GET)
	public @ResponseBody List<CodeListItemView> loadAvailableItems(
			@PathVariable int surveyId, 
			@PathVariable int codeListId,
			@RequestParam int recordId, 
			@RequestParam int stepNumber, 
			@RequestParam int parentEntityId, 
			@RequestParam int attrDefId) {
		CollectSurvey survey = surveyManager.getOrLoadSurveyById(surveyId);
		CollectRecord record = recordManager.load(survey, recordId, Step.valueOf(stepNumber), false);
		Entity parentEntity = (Entity) record.getNodeByInternalId(parentEntityId);
		CodeAttributeDefinition attrDef = (CodeAttributeDefinition) survey.getSchema().getDefinitionById(attrDefId);
		List<CodeListItem> items = codeListManager.loadValidItems(parentEntity, attrDef);
		return Views.fromObjects(items, CodeListItemView.class);
	}
	
	protected String exportCodeList(HttpServletResponse response,
			int surveyId, boolean work, int codeListId) throws IOException {
		CollectSurvey survey = work ? surveyManager.loadSurvey(surveyId): surveyManager.getById(surveyId);
		CodeList list = survey.getCodeListById(codeListId);
		String fileName = list.getName() + CSV_EXTENSION;
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		response.setContentType(CSV_CONTENT_TYPE); 
		ServletOutputStream out = response.getOutputStream();
		CodeListExportProcess process = new CodeListExportProcess(codeListManager);
		process.exportToCSV(out, survey, codeListId);
		return "ok";
	}
	
}
