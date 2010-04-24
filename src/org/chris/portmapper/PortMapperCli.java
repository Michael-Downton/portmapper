/**
 * 
 */
package org.chris.portmapper;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;
import org.chris.portmapper.router.sbbi.SBBIRouterFactory;
import org.jdesktop.application.Application;

/**
 * @author chris
 * 
 */
public class PortMapperCli {

	private final Log logger = LogFactory.getLog(this.getClass());

	private static final String ADD_OPTION = "a";
	private static final String HELP_OPTION = "h";
	private static final String START_GUI_OPTION = "g";
	private static final String STATUS_OPTION = "s";
	private static final String DELETE_OPTION = "d";
	private static final String LIST_OPTION = "l";
	private static final String ADD_LOCALHOST_OPTION = "r";
	private static final String UPNP_LIB_OPTION = "u";

	private final Options options;
	private final CommandLineParser parser;
	private String routerFactoryClassName = SBBIRouterFactory.class.getName();

	public PortMapperCli() {
		options = createOptions();
		parser = new PosixParser();
	}

	/**
	 * @return
	 */
	private Options createOptions() {

		boolean useLongOpts = false;

		final Option help = new Option(HELP_OPTION,
				useLongOpts ? "help" : null, false, "print this message");
		final Option startGui = new Option(START_GUI_OPTION,
				useLongOpts ? "gui" : null, false,
				"Start graphical user interface (default)");
		final Option add = new Option(ADD_OPTION, useLongOpts ? "add" : null,
				true, "Add port forwarding");
		add.setArgs(4);
		add.setArgName("ip port external_port protocol");
		add.setValueSeparator(' ');
		add.setType(String.class);

		final Option delete = new Option(DELETE_OPTION, useLongOpts ? "delete"
				: null, true, "Delete port forwarding");
		delete.setArgs(2);
		delete.setArgName("external_port protocol [...]");
		delete.setValueSeparator(' ');
		delete.setType(String.class);

		final Option status = new Option(STATUS_OPTION, useLongOpts ? "status"
				: null, false, "Get Connection status");

		final Option list = new Option(LIST_OPTION,
				useLongOpts ? "list" : null, false, "List forwardings");

		final Option addLocalhost = new Option(ADD_LOCALHOST_OPTION,
				useLongOpts ? "addlocalhost" : null, true,
				"Add all forwardings to the current host");
		addLocalhost.setArgs(2);
		addLocalhost.setArgName("port protocol [...]");
		addLocalhost.setValueSeparator(' ');
		addLocalhost.setType(String.class);

		final Option upnpLib = new Option(UPNP_LIB_OPTION,
				useLongOpts ? "delete" : null, true, "UPnP library");
		upnpLib.setArgs(1);
		// add.setArgName("class name (" + SBBIRouterFactory.class.getName() +
		// "|"
		// + WeUPnPRouterFactory.class.getName() + "|"
		// + DummyRouterFactory.class.getName() + ")");
		upnpLib.setArgName("class name");
		upnpLib.setType(String.class);

		final OptionGroup optionGroup = new OptionGroup();
		optionGroup.setRequired(false);
		optionGroup.addOption(help);
		optionGroup.addOption(startGui);
		optionGroup.addOption(add);
		optionGroup.addOption(addLocalhost);
		optionGroup.addOption(delete);
		optionGroup.addOption(list);
		optionGroup.addOption(status);

		// create Options object
		final Options options = new Options();
		options.addOption(upnpLib);
		options.addOptionGroup(optionGroup);

		return options;
	}

	/**
	 * @param args
	 */
	public void start(String[] args) {
		final CommandLine commandLine = parseCommandLine(args);
		if (isStartGuiRequired(commandLine)) {
			Application.launch(PortMapperApp.class, args);
			return;
		}

		initDummyLogAppender();

		if (commandLine.hasOption(UPNP_LIB_OPTION)) {
			this.routerFactoryClassName = commandLine
					.getOptionValue(UPNP_LIB_OPTION);
			logger.info("Using router factory class '"
					+ this.routerFactoryClassName + "'");
		}

		if (commandLine.hasOption(HELP_OPTION)) {
			printHelp();
		} else if (commandLine.hasOption(ADD_OPTION)) {
			addPortForwarding(commandLine.getOptionValues(ADD_OPTION));
		} else if (commandLine.hasOption(STATUS_OPTION)) {
			printStatus();
		} else if (commandLine.hasOption(DELETE_OPTION)) {
			deletePortForwardings(commandLine.getOptionValues(DELETE_OPTION));
		} else if (commandLine.hasOption(LIST_OPTION)) {
			printPortForwardings();
		} else if (commandLine.hasOption(ADD_LOCALHOST_OPTION)) {
			addLocalhostPortForwardings(commandLine.getOptionValues(ADD_OPTION));
		} else {
			System.err.println("Incorrect usage");
			printHelp();
			System.exit(1);
		}
		System.exit(0);
	}

	/**
	 * @param optionValues
	 */
	private void addLocalhostPortForwardings(String[] optionValues) {
		// TODO Auto-generated method stub
		System.out.println("add port forwardings "
				+ Arrays.toString(optionValues));
	}

	/**
	 * 
	 */
	private void printPortForwardings() {
		// TODO Auto-generated method stub
		System.out.println("print forwardings");
	}

	/**
	 * @param optionValues
	 */
	private void deletePortForwardings(String[] optionValues) {
		// TODO Auto-generated method stub
		System.out.println("delete port forwardings "
				+ Arrays.toString(optionValues));
	}

	/**
	 * 
	 */
	private void printStatus() {
		// TODO Auto-generated method stub
		System.out.println("print status");
	}

	/**
	 * @param optionValues
	 */
	private void addPortForwarding(String[] optionValues) {
		// TODO Auto-generated method stub
		System.out.println("add port forwarding "
				+ Arrays.toString(optionValues));
	}

	private void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(80);
		// formatter.setDescPadding(0);
		// formatter.setLeftPadding(0);
		String header = "header";
		String cmdLineSyntax = "java -jar PortMapper.jar";
		String footer = "protocol is UDP or TCP";

		formatter.printHelp(formatter.getWidth(), cmdLineSyntax, header,
				options, footer, true);
	}

	private boolean isStartGuiRequired(CommandLine commandLine) {
		if (commandLine.hasOption(START_GUI_OPTION)) {
			return true;
		}
		return !(commandLine.hasOption(HELP_OPTION)
				|| commandLine.hasOption(ADD_LOCALHOST_OPTION)
				|| commandLine.hasOption(ADD_OPTION)
				|| commandLine.hasOption(STATUS_OPTION) || commandLine
				.hasOption(LIST_OPTION)
				&& commandLine.hasOption(DELETE_OPTION));
	}

	private void initDummyLogAppender() {
		WriterAppender writerAppender = (WriterAppender) Logger.getLogger(
				"org.chris.portmapper").getAppender("jtextarea");
		writerAppender.setWriter(new DummyWriter());
	}

	private CommandLine parseCommandLine(String[] args) {
		try {
			return parser.parse(options, args);
		} catch (ParseException e) {
			initDummyLogAppender();
			logger.error("Could not parse command line: " + e.getMessage());
			System.exit(1);
			return null;
		}
	}

	private static class DummyWriter extends Writer {
		@Override
		public void close() throws IOException {
		}

		@Override
		public void flush() throws IOException {
		}

		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
		}
	}
}