# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.5/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.5/gradle-plugin/packaging-oci-image.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.3.5/reference/using/devtools.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.3.5/reference/web/servlet.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

## Step by step guide on Setup and configuration

*IF YOU NEED HELP AT ANY POINT OF THIS GUIDE MESSAGE ME AND I WILL HELP YOU*

1. Make sure you have Git installed on your PC:

	1. **Download Git:**
		- Go to the official Git website: [https://git-scm.com/download/win](https://git-scm.com/download/win) and download git.

	2. **Run the Installer:**
		- Once the download is complete, run the `.exe` file.
		- Choose your installation preferences. You can leave the default options selected for most cases.
    		- For example, select "Use Git from the Windows Command Prompt" for easier access via the command line.

	3. **Complete Installation:**
		- Click through the installer steps and finish the installation.
  		- Once installed, you can access Git through the Git Bash terminal or Command Prompt.

	4. **Verify Installation:**
		- Open Command Prompt or Git Bash and type:
     	```bash
     	git --version
    	```
   		- You should see the Git version installed.
		- If its still not showing, restart your PC and try again.
		<br><br>
		
2. Create an app password by clicking the Settings button next to your avatar at the top right -> Personal Bitbucket Settings
<br><br>
3. Click on App passwords at the left side of the screen and then "Create app password"
<br><br>
4. Set the label as you wish, and set the Repositories permissions to Read and Write, nothing else.
<br><br>
5. Click Create
<br><br>
6. COPY THE PASSWORD AND SAVE IT IN A NOTEPAD - YOU WILL NEED IT, AND YOU WILL NOT BE ABLE TO SEE IT AGAIN AFTER CLOSING THE WINDOW
<br><br>
7. Click the "Clone" button at the top right of the page, and click "Copy"
<br><br>
8. Open the folder where you installed Spring Tool Suite and open Spring. (If its not C:\) tell me and I will help you change settings inside Spring.
<br><br>
9. Click the folder path, and type "cmd" then hit enter.
<br><br>
10. Paste the command from step 2 inside the cmd and hit enter. It should have created a folder called "2025a.integrative.nagar.yuval" with all of the files you see above.
<br><br>
11. Sign in with your Bitbucket account (I used sign in with browser)
<br><br>
12. Inside the cmd, see that the clone process has finished, and go inside the new directory with ">cd 2025a.integrative.nagar.yuval"
<br><br>
13. Type ">git config user.name "<YOUR FULL NAME>" (>git config user.name "Eyal Dassa") and hit enter.
<br><br>
14. Type ">git config user.email "<YOUR AFKEA EMAIL>" (>git config user.email "eyal.dassa@s.afeka.ac.il") and hit enter.
<br><br>
15. Inside Spring, click File -> Import -> Gradle -> Existing Gradle Project and click Next
<br><br>
16. For "Project root directory" paste the folder path of the 2025a.integrative.nagar.yuval folder. (for example C:\javaenv2025a\2025a.integrative.nagar.yuval)
<br><br>
17. Click Finish
<br><br>
18. Inside Spring, click Window -> Perspective -> Open Perspective -> Other -> Git
<br><br>
19. At the top right corner you will see 3 Icons one called Git and one called Java, this will change the perspectives accordingly.
<br><br>
20. Set the perspective to Java and open src/main/java -> demo -> dummy
<br><br>
21. Change the commented line to <YOUR NAME> first commit and save (ctrl + s)
<br><br>
22. Change to git perspective 
<br><br>
23. at the bottom you will see a window called Git Staging
<br><br>
24. Choose the 1 file changed inside the "Unstaged Changes" and click the green + icon.
<br><br>
25. Set the commit message to "<name> first commit" (eyal first commit) and click Commit and Push
<br><br>
26. You will need to insert a password, this is the password from steps 2-6
<br><br>

*IF YOU NEED HELP AT ANY POINT OF THIS GUIDE MESSAGE ME AND I WILL HELP YOU*

