package view.commands;

import controller.Controller;

public class RunExample extends Command {
    private final Controller controller;

    public RunExample(String key, String description, Controller controller)
    {
        super(key, description);
        this.controller=controller;
    }
    @Override
    public void execute()
    {
        try
        {
            Controller newController = new Controller(controller.getRepository().getCopy());
            newController.setLogFlag(true);
            newController.setDebugFlag(true);
            newController.allSteps();
        }
        catch(RuntimeException e)
        {
            System.out.println(e.toString());
        }
    }
}
