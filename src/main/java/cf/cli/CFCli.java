package cf.cli;

import org.apache.commons.cli.*;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.spring.client.SpringCloudFoundryClient;

public class CFCli {
    public static void main( String[] args ) {
        // create the parser
        Option help = new Option( "help", "print this message" );
        Option endpoint   = OptionBuilder.withArgName( "API_URL" )
                .hasArg()
                .withDescription(  "endpoint of CF" )
                .create( "a" );
        Option username   = OptionBuilder.withArgName( "USERNAME" )
                .hasArg()
                .withDescription(  "username" )
                .create( "u" );
        Option password   = OptionBuilder.withArgName( "PASSWORD" )
                .hasArg()
                .withDescription(  "password" )
                .create( "p" );
        Options options = new Options();
        options.addOption( help );
        options.addOption(endpoint);
        options.addOption(username);
        options.addOption(password);

        CommandLineParser parser = new BasicParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse( options, args );
            if(line.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "cf-login", options );
            }
            System.out.println("client start");
            if(line.hasOption('a') && line.hasOption('u') && line.hasOption('p')) {
                SpringCloudFoundryClient cloudFoundryClient = SpringCloudFoundryClient.builder()
                        .host(line.getOptionValue('a'))
                        .username(line.getOptionValue('u'))
                        .password(line.getOptionValue('p'))
                        .build();

                DefaultCloudFoundryOperations cloudFoundryOperations = DefaultCloudFoundryOperations.builder()
                        .cloudFoundryClient(cloudFoundryClient)
                        .build();
                cloudFoundryOperations.organizations()
                        .list()
                        .map(organization -> organization.getName())
                        .subscribe(System.out::println);
                System.out.println("HI there");
            } else {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "cf-login", options );
            }

        }
        catch( Exception exp ) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }
    }
}
