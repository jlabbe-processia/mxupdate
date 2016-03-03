

# Introduction #
The eMatrixServletRMI.jar is required as a Eclipse Plug-In for the [MxUpdate Eclipse Plug-In](Eclipse_Start.md). Because of the legal the Plug-In could not be made available from the [MxUpdate](http://www.mxupdate.org) Team.

The Plug-In is only a "repacked" Eclipse plug-in from the original delivered eMatrixServletRMI.jar of Enovia PLM. The Jar-Library includes the Java-ADK for the MX database.

# Create own eMatrixServlerRMI.jar Plug-In #
## Call new Project Wizard ##
First of all a new Plug In from existing jar library must be created. To do that press File -> New -> Other.
The "New Project" wizard will be opened. Select "Plug-in from existing JAR archive":

![http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-1.jpg](http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-1.jpg)

## Select Jar Library ##
On the next page press "Add External..." and select the eMatrixServletRMI.jar library (e.g. within your deployed web container):

![http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-2.jpg](http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-2.jpg)

## Define Plug-In Name ##
On the next page put in the project name "eMatrixServletRMI" (this could not be changed!):

![http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-3.jpg](http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-3.jpg)

## Check created Plug-In Structure ##
After the finish button is pressed following project structure is created and shown within Eclipse:

![http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-4.jpg](http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-4.jpg)

## Define Plug-In Version ##
Select the build.properties and there the "Overview" tab. Specify your version (here e.g. the version 10.6.2 is used):

![http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-5.jpg](http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-5.jpg)

## Export Plug-In ##
Click the "Export Wizard" on the right button page of the "Overview" tab. Select in the options the option "Package plug-ins as individual JAR archives":

![http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-6.jpg](http://mxupdate.googlecode.com/svn/wiki/images/EclipseInstallation_ADK-6.jpg)

After the "Finish" button is pressed the new eMatrixServletRMI plug-in is exported within given directory.

## Copy Create Plug-In into Eclipse ##
Copy the new created eMtrixServletRMI.jar Plug-In into the `plugin` directory of the Eclipse directory. Eclipse must be restarted to recognize the new installed plug-in.