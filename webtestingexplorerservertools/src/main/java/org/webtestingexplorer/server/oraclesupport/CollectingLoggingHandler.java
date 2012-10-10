package org.webtestingexplorer.server.oraclesupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Standard Java Logging handler that spies on logging messages
 * with the intent of exposing them to the webtestingexplorer client,
 * which can then use that information to search for errors and/or
 * state changes.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CollectingLoggingHandler extends Handler {

	// TODO(smcmaster): This would be much better handled with dependency injection.
	// Another idea I have is to create this in an init servlet, put it in the servlet
	// context, and let the interested parties (the classes that want to do logging,
	// and the servlet that exposes the messages) read from that.
	private static final CollectingLoggingHandler INSTANCE = new CollectingLoggingHandler();
	public static CollectingLoggingHandler getInstance() {
		return INSTANCE;
	}
	
	private List<LogRecord> currentLogRecords = Collections.synchronizedList(new ArrayList<LogRecord>());
	
	@Override
	public void close() throws SecurityException {
		clear();
	}

	@Override
	public void flush() {
	}

	@Override
	public void publish(LogRecord logRecord) {
		if (!isLoggable(logRecord)) {
			return;
		}
		currentLogRecords.add(logRecord);
	}
	
	public void clear() {
		currentLogRecords.clear();
	}
	
	public List<LogRecord> getCurrentLogRecords() {
		return currentLogRecords;
	}
}
