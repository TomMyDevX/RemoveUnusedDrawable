package com.tommy.dev.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.impl.ProjectFrameBounds;
import com.tommy.dev.view.aboutMe;

import javax.swing.*;
import java.awt.*;

/**
 * Created by TomMy on 7/17/2015.
 */

public class aboutme extends AnAction {

    // If you register the action from Java code, this constructor is used to set the menu item name
    // (optionally, you can specify the menu description and an icon to display next to the menu item).
    // You can omit this constructor when registering the action in the plugin.xml file.
    public aboutme() {
        // Set the menu item name.
        super("aboutme");
        // Set the menu item name, description and icon.
        // super("Text _Boxes","Item description",IconLoader.getIcon("/Mypackage/icon.png"));
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        aboutMe test=new aboutMe();
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //int height = (int) (screenSize.height*0.25f);
        //int width = (int) (screenSize.width*0.25f);
        test.setSize(300,150);
       // test.setLocationRelativeTo(null);


        ProjectFrameBounds instance = ProjectFrameBounds.getInstance(project);
        Rectangle bounds = instance.getBounds();
        double x = bounds.getX();
        double y =  bounds.getY();
        double width = bounds.getWidth();
        double height = bounds.getHeight();


        test.setLocation(((int) (((int) x)+(width/2)))-test.getWidth()/2, (int) (((int) y)+(height/2))-test.getHeight()/2);
        System.out.print(x+":"+y);

        test.setVisible(true);



        //String txt= Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon());
        //Messages.showMessageDialog(project, "Hello, " + txt + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());

    }



    private GraphicsDevice getOtherScreen(Component component) {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (graphicsEnvironment.getScreenDevices().length == 1) {
            // if there is only one screen, return that one
            return graphicsEnvironment.getScreenDevices()[0];
        }

        GraphicsDevice theWrongOne = component.getGraphicsConfiguration().getDevice();
        for (GraphicsDevice dev : graphicsEnvironment.getScreenDevices()) {
            if (dev != theWrongOne) {
                return dev;
            }
        }

        return null;
    }

}