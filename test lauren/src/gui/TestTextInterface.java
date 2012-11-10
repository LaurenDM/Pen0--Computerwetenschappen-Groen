package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import controller.TestController;

//Code for this class was adapted from a page on littletutorials.com
public class TestTextInterface {
	
	private static final String COMMAND_ERROR = "Command error: ";
	private static final String UNKNOWN_COMMAND = "Unknown command: ";

	public static void execCommandLoop(TestController testController)
    {
        while (true)
        {
        	Scanner scanner = getInScanner();

            if (scanner.hasNext())
            {
                final String commandName = scanner.next().trim().toUpperCase();

                try
                {
                    final TestCommand cmd = Enum.valueOf(TestCommand.class, commandName);
                    String param = scanner.hasNext() ? scanner.next() : null;
                    cmd.exec(testController, new String[]{param}, new TestCommand.Listener()
                    {
                        @Override
                        public void exception(Exception e)
                        {
                            System.out.println(COMMAND_ERROR+" "+cmd+" "+e.getMessage());
                        }
                    });
                }
                catch (IllegalArgumentException e)
                {
                    System.out.println(UNKNOWN_COMMAND+" "+commandName);
                }
            }

            scanner.close();
        }
    }

	public static String getUserInput(String question){
		System.out.println(question);
		Scanner scanner = getInScanner();
		if (scanner.hasNext()){
			return scanner.next().trim();
		} else {
			return "";
		}
	}
	
	/**
	 * @return
	 */
	public static Scanner getInScanner() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String commandLine = "";
		try {
			commandLine = bufferedReader.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Scanner scanner = new Scanner(commandLine);
		return scanner;
	}

	public static void printWelcome() {
		String commandList = "";
		for(TestCommand tc:TestCommand.values()){
			commandList+=tc.name().toLowerCase()+(tc.hasParam()?" <param>":"")+", ";
		}
		commandList = commandList.substring(0, commandList.length()-2);
		System.out.println("Welcome to the PenOCW Team Green Test App!");
		System.out.println("Available commands: "+commandList);
	}
}
