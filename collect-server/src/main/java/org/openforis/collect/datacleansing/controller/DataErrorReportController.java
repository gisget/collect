package org.openforis.collect.datacleansing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.openforis.collect.datacleansing.DataErrorQuery;
import org.openforis.collect.datacleansing.DataErrorReport;
import org.openforis.collect.datacleansing.DataErrorReportGenerator;
import org.openforis.collect.datacleansing.DataErrorReportItem;
import org.openforis.collect.datacleansing.form.DataErrorReportForm;
import org.openforis.collect.datacleansing.form.DataErrorReportItemForm;
import org.openforis.collect.datacleansing.manager.DataErrorQueryManager;
import org.openforis.collect.datacleansing.manager.DataErrorReportManager;
import org.openforis.collect.model.CollectRecord.Step;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.web.controller.AbstractSurveyObjectEditFormController;
import org.openforis.commons.web.Response;
import org.openforis.concurrency.Job;
import org.openforis.concurrency.Task;
import org.openforis.concurrency.Worker.Status;
import org.openforis.concurrency.spring.SpringJobManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(value=WebApplicationContext.SCOPE_SESSION)
@RequestMapping(value = "/datacleansing/dataerrorreports")
public class DataErrorReportController extends AbstractSurveyObjectEditFormController<DataErrorReport, DataErrorReportForm, DataErrorReportManager> {
	
	@Autowired
	private DataErrorQueryManager dataErrorQueryManager;
	@Autowired
	private SpringJobManager springJobManager;
	
	private ReportGenerationJob generationJob;
	
	@Autowired
	@Override
	public void setItemManager(DataErrorReportManager itemManager) {
		super.setItemManager(itemManager);
	}
	
	@Override
	protected DataErrorReportForm createFormInstance(DataErrorReport item) {
		return new DataErrorReportForm(item);
	}
	
	@Override
	protected DataErrorReport createItemInstance(CollectSurvey survey) {
		return new DataErrorReport(survey);
	}
	
	@RequestMapping(value="generate.json", method = RequestMethod.POST)
	public @ResponseBody
	Response generate(@RequestParam int queryId, @RequestParam Step recordStep) {
		CollectSurvey survey = sessionManager.getActiveSurvey();
		DataErrorQuery query = dataErrorQueryManager.loadById(survey, queryId);
		generationJob = springJobManager.createJob(ReportGenerationJob.class);
		generationJob.setQuery(query);
		generationJob.setRecordStep(recordStep);
		springJobManager.start(generationJob);
		Response response = new Response();
		return response;
	}
	
	@RequestMapping(value="{reportId}/items.json", method = RequestMethod.GET)
	public @ResponseBody
	List<DataErrorReportItemForm> loadItems(@PathVariable int reportId, 
			@RequestParam int offset, @RequestParam int limit) {
		CollectSurvey survey = sessionManager.getActiveSurvey();
		DataErrorReport report = itemManager.loadById(survey, reportId);
		List<DataErrorReportItem> items = itemManager.loadItems(report, offset, limit);
		List<DataErrorReportItemForm> result = new ArrayList<DataErrorReportItemForm>(items.size());
		for (DataErrorReportItem item : items) {
			result.add(new DataErrorReportItemForm(item));
		}
		return result;
	}
	
	@RequestMapping(value="generate/job.json", method = RequestMethod.GET)
	public @ResponseBody
	JobView getCurrentGenearationJob(HttpServletResponse response) {
		if (generationJob == null) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return null;
		} else {
			return new JobView(generationJob);
		}
	}
	
	@Component
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	private static class ReportGenerationJob extends Job {

		@Autowired
		private DataErrorReportGenerator reportGenerator;
		
		//input
		private DataErrorQuery query;
		private Step recordStep;
		
		//output
		private DataErrorReport report;
		
		@Override
		protected void buildTasks() throws Throwable {
			ReportGenerationTask reportGenerationTask = new ReportGenerationTask();
			addTask(reportGenerationTask);
		}
		
		private class ReportGenerationTask extends Task {
			protected void execute() throws Throwable {
				report = reportGenerator.generate(query, recordStep);
			}
		}

		public void setQuery(DataErrorQuery query) {
			this.query = query;
		}
		
		public void setRecordStep(Step recordStep) {
			this.recordStep = recordStep;
		}
		
		public DataErrorReport getReport() {
			return report;
		}
	}
	
	private static class JobView {
		
		private int progressPercent;
		private Status status;

		public JobView(Job job) {
			progressPercent = job.getProgressPercent();
			status = job.getStatus();
		}
		
		public int getProgressPercent() {
			return progressPercent;
		}
		
		public Status getStatus() {
			return status;
		}
		
	}
	
}
