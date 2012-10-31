package gui;

import java.io.Console;

import java.io.Console;
import java.lang.reflect.Method;
import java.util.ArrayList;

import controller.TestController;

public enum TestCommand
{
    EXIT(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params)
        {
            System.out.println("Bye!");
            System.exit(0);
        }
    }, false),
    TESTDRIVE(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	
        }
    }, false),
    TESTLIGHT(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	
        }
    }, false),
    TESTDISTANCE(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	
        }
    }, false),
    TESTDIRECT(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	System.out.println("Give one of the following names as parameter:");
        	ArrayList<Method> methList=new ArrayList<Method>();
        	methList.addAll(testController.getTestMethodList());
        	if(params[0].equals("?"))
        	{
        		for(Method meth:methList){
        			String methName = meth.getName();
        			methName=methName.substring(4,methName.length());
        			System.out.println(methName);
        		}
        	} else {
        		for(Method meth:methList){
        			if(meth.getName().contains)
        		}
        	}
        }
    }, true);

    
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
        }
    }
    
    public boolean hasParam(){
    	return hasParam;
    }
}