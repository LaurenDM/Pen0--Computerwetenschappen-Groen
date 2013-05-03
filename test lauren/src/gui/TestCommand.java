package gui;

import java.lang.reflect.Method;
import java.util.ArrayList;

import controller.TestController;

public enum TestCommand
{
	CONNECT(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params)
        {
            testController.connectNewBtRobot();
        }
    }, false),
    EXIT(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params)
        {
        	testController.endTest();
            System.out.println("Bye!");
            System.exit(0);
        }
    }, false),
    TESTDRIVE(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	testController.selectTestMethodsOnType("drive");
        	testController.runSelectedTests();
        }
    }, false),
    TESTLIGHT(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	testController.selectTestMethodsOnType("light");
        	testController.runSelectedTests();
        }
    }, false),
    TESTDISTANCE(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	testController.selectTestMethodsOnType("distance");
        	testController.runSelectedTests();
        }
    }, false),
    TESTDIRECT(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	ArrayList<Method> methList=new ArrayList<Method>();
        	methList.addAll(testController.getTestMethodList());
        	if(params[0]==null || params[0].equals("") || params[0].equals("?"))
        	{
        		System.out.println("Give (part of) one of the following names as parameter:");
        		for(Method meth:methList){
        			String methName = meth.getName();
        			methName=methName.substring(4,methName.length());
        			System.out.println(methName);
        		}
        	} else {
        		try{
        			testController.selectTestMethodsOnName(params[0].toLowerCase());
        			System.out.println("Following method(s) was/were selected:");
            		for(Method meth:testController.getSelectedMethodList()){
            			String methName = meth.getName();
            			methName=methName.substring(4,methName.length());
            			System.out.println(methName);
            		}
        		} catch(IllegalArgumentException e) {
        			System.out.println(e.getMessage());
        		}
        	}
        }
    }, true),
    RUNSELECTED(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	try{
        		testController.runSelectedTests();
        	} catch(IllegalArgumentException e) {
    			System.out.println(e.getMessage());
    		}
        }
    }, false);

    
    private interface Action
    {
        public void exec(TestController testController, String[] params) throws Exception;
    }

    public interface Listener
    {
        public void exception(Exception e);
    }

    private Action action;
    private boolean hasParam;
    
    private TestCommand(Action a, boolean hasParam)
    {
        this.action = a;
        this.hasParam = hasParam;
    }

    public void exec(TestController testController, final String[] params, final Listener l)
    {
        try
        {
            action.exec(testController, params);
        }
        catch (Exception e)
        {
            l.exception(e);
			System.out.println("there was an exception code 098754");

        }
    }
    
    public boolean hasParam(){
    	return hasParam;
    }
}