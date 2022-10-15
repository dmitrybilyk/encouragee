package com.encouragee.camel.clientSearch;

import com.zoomint.encourage.model.scheduler.ImportReport;
import com.zoomint.encourage.model.scheduler.ImportTask;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON_UTF_8;

/**
 * Routes for processing an import tasks.
 */
@Component
@Profile("solr")
public class TaskRouteBuilder extends RouteBuilder {
	public static final String URI_INIT_TASK = "direct:taskStart";

	private static final String URI_SEND_REPORT = "direct:taskReport";

	public static final String TASK_STATUS = "taskStatus";
	public static final String TASK = "taskReceived";

	@Override
	public void configure() {
		errorHandler(noErrorHandler()); // propagate exceptions back to original route

		from(URI_INIT_TASK).routeId("taskStart")
				.setProperty(TASK).body(ImportTask.class)
				.setProperty(TASK_STATUS).body(ImportTask.class, this::createInitialState)
				.onCompletion()
					.to(TaskRouteBuilder.URI_SEND_REPORT)
				.end()
		;

		from(URI_SEND_REPORT).routeId("taskReport")
				.setBody().exchangeProperty(TASK_STATUS)
				.setHeader(CONTENT_TYPE, constant(APPLICATION_JSON_UTF_8))
				.setProperty("reportUrl").exchange(exchange -> exchange.getProperty(TASK, ImportTask.class).getReportUrl())
				.log("Sending report to ${exchangeProperty.reportUrl.toString()}: ${body}")
				.toD("rest:post:${exchangeProperty.reportUrl.path}?host=${exchangeProperty.reportUrl.authority}")
		;
	}

	private ImportReport createInitialState(ImportTask task) {
		return ImportReport.builder()
				.taskId(task.getTaskId())
				.externalSystem(task.getExternalSystem())
				.timestamp(Instant.now())
				.size(0)
				.build();
	}
}
