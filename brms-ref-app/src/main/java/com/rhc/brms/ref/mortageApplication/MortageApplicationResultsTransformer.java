package com.rhc.brms.ref.mortageApplication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;

import com.rhc.brms.ref.domain.Application;
import com.rhc.brms.ref.drools.ExecutionResultsTransformer;

public class MortageApplicationResultsTransformer implements ExecutionResultsTransformer<MortageApplicationResponse> {

	/**
	 * Queries to grab references from the engine. These are objects we will return to the caller.
	 */
	@SuppressWarnings("rawtypes")
	private final List<Command> queryCommandList = buildQueryCommandList();

	public MortageApplicationResponse transform( ExecutionResults results ) {
		MortageApplicationResponse response = new MortageApplicationResponse();

		response.setApprovedApplications( extractSetFromQueryResult( Application.class, "$application",
				(QueryResults) results.getValue( "allApprovedApplications" ) ) );
		
		response.setDeniedApplications( extractSetFromQueryResult( Application.class, "$application",
				(QueryResults) results.getValue( "allDeniedApplications" ) )  );

		return response;
	}

	@SuppressWarnings("rawtypes")
	public List<Command> getQueryCommands() {
		return queryCommandList;
	}

	@SuppressWarnings("rawtypes")
	private static List<Command> buildQueryCommandList() {
		List<Command> commands = new ArrayList<Command>();
		commands.add( CommandFactory.newQuery( "allApprovedApplications", "Get All Approved Applications" ) );
		commands.add( CommandFactory.newQuery( "allDeniedApplications", "Get All Denied Applications" ) );
		return commands;
	}

	public static <T> Set<T> extractSetFromQueryResult( Class<T> clazz, String declaration, QueryResults queryResult ) {
		Set<T> set = new HashSet<T>();
		if ( null != queryResult ) {
			for ( QueryResultsRow row : queryResult ) {
				set.add( clazz.cast( row.get( declaration ) ) );
			}
		}
		return set;
	}
}