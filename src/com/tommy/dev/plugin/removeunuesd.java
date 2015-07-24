package com.tommy.dev.plugin;

import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.tommy.dev.view.OnCompleListenner;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by TomMy on 7/17/2015.
 */

public class removeunuesd extends AnAction {
    private OnCompleListenner onCompleListenner;
    public static final NotificationGroup GROUP_DISPLAY_ID_INFO = new NotificationGroup("Devtools info", NotificationDisplayType.BALLOON, true);


    // If you register the action from Java code, this constructor is used to set the menu item name
    // (optionally, you can specify the menu description and an icon to display next to the menu item).
    // You can omit this constructor when registering the action in the plugin.xml file.
    public removeunuesd() {
        // Set the menu item name.
        super("removeunuesd");
        // Set the menu item name, description and icon.
        // super("Text _Boxes","Item description",IconLoader.getIcon("/Mypackage/icon.png"));
        onCompleListenner=new OnCompleListenner() {
            @Override
            public void onCompleted(Project project) {

                VirtualFileManager.getInstance().asyncRefresh(null);
                setEnabledInModalContext(true);
                Notification notification = GROUP_DISPLAY_ID_INFO.createNotification("Remove Drawable Unused Completed!", NotificationType.INFORMATION);
                Notifications.Bus.notify(notification);

            }
        };
    }



    public void actionPerformed(AnActionEvent event) {

        setEnabledInModalContext(false);

        final Project project = event.getData(PlatformDataKeys.PROJECT);

        Task.Backgroundable backgroundable=new Task.Backgroundable(project,"Searching unused drawable...",true) {
            boolean isCancel =false;
            @Override
            public void onCancel() {
                super.onCancel();
                isCancel =true;
            }


            @Override
            public void run(ProgressIndicator progressIndicator) {
                progressIndicator.setText("Searching unused drawable...");
                progressIndicator.setFraction(0.0);
                progressIndicator.setText("Listing relative files...");
                progressIndicator.setFraction(0.0);
                Collection files; //= FileUtils.listFiles(new File(project.getBasePath()), new RegexFileFilter("^(.*java|.*xml$)"), DirectoryFileFilter.DIRECTORY);
                Collection filesS = FileUtils.listFiles(new File(project.getBasePath()), new RegexFileFilter("^(.*java|.*xml$)"), DirectoryFileFilter.DIRECTORY);

                Iterator iteratorfilesS = filesS.iterator();
                while (iteratorfilesS.hasNext()){
                    Object next = iteratorfilesS.next();

                    if(next.toString().contains(File.separator+".idea"+File.separator)||next.toString().contains(File.separator+"out"+File.separator+"production"+File.separator)){
                        iteratorfilesS.remove();
                     //   System.out.println(next.toString());
                    }
                }
                files=filesS;

               // System.out.println(files);
                Collection drawablefiles = FileUtils.listFiles(new File(project.getBasePath()), new RegexFileFilter("^(.*png$)"), DirectoryFileFilter.DIRECTORY);
              //  System.out.println(drawablefiles);


                int countMutiple=drawablefiles.size();
                int countFiles=files.size();
                int countMax=countMutiple*countFiles;

                Calendar date = Calendar.getInstance();
                int indexOfMultiple=0;
                int indexOfFiles=0;
                Iterator drawablefilesIterator = drawablefiles.iterator();
                while (drawablefilesIterator.hasNext()) {

                    if(isCancel){
                        break;
                    }

                    Object drawableElement = drawablefilesIterator.next();
                    String basename = FilenameUtils.getBaseName(drawableElement.toString());
                    basename=basename.replace(".9","");
                    //  for (int i = 0; i < drawablePath.size(); i++) {
                    Iterator filesIterator = files.iterator();
                    boolean isFound = false;
                    String isLastProcess = null;
                    while (filesIterator.hasNext()) {
                        indexOfFiles+=1;
                        if(isCancel){
                            break;
                        }
                        Object element = filesIterator.next();




                        isLastProcess = element.toString();
                        File file = new File(element.toString());

                        progressIndicator.setText("Processing > " + element.toString());
                            progressIndicator.setFraction(((indexOfMultiple * (countFiles)) + indexOfFiles) / (countMax * 1d));
                            try {
                                String string = FileUtils.readFileToString(file);
                                if (string.contains(basename)) {
                                    isFound = true;
                                    break;
                                } else {
                                    isFound = false;
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                    }

                    if (isFound == false && isLastProcess != null) {
                        File file = new File(drawableElement.toString());
                        FileUtil.delete(file);

                        File logpath = new File(project.getBasePath(), "devlog_" + date.getTimeInMillis() + ".txt");
                        if (!logpath.exists()) FileUtil.createIfDoesntExist(logpath);
                        try {
                            FileUtil.appendToFile(logpath, file.getAbsolutePath() + "\n");
                            System.out.println(drawableElement.toString() + " is deleted!");
                        } catch (IOException e) {
                        }

                    } else {
                    }
                    indexOfMultiple+=1;
                }
                progressIndicator.setText("Completed Remove Drawable");
                progressIndicator.setFraction(1.0);
                onCompleListenner.onCompleted(project);
            }
        };

      //  backgroundable.setCancelText("Stop Remove Drawable").queue();
       // backgroundable.setCancelTooltipText("Terminates current Remove Drawable");

        ProgressManager.getInstance().run(backgroundable);


    }
}



