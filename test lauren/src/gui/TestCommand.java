package gui;

import java.io.Console;

import java.io.Console;

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
    }),
    TESTDRIVE(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	
        }
    }),
    TESTLIGHT(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	
        }
    }),
    TESTDISTANCE(new Action()
    {
        @Override
        public void exec(TestController testController, String[] params) throws Exception
        {
        	
        }
    });

    private interface Action
    {
        public void exec(TestController testController, String[] params) throws Exception;
    }

    public interface Listener
    {
        public void exception(Exception e);
    }

    private Action action;

    private TestCommand(Action a)
    {
        this.action = a;
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
}