# MidasWebServer
JavaDocs: https://cwdatlas.github.io/MidasWebServer/
Hello, Welcome to my project, MidasWebServer

First, This project centered around consuming stock data then displaying it. 
Users have personal home page that display all stock tickers they have searched.

How to use the website
1. once the application is running on your local computer, go to your favorite web browser and type in 'localhost:8080'
2. Welcome to the index page, click login at the top right of the page
3. If you have already registered, then type in your username and password, if you haven't click the 'here' text under the text boxes
4. You should be at the register page, type in your information, make sure it isn't used elsewhere and remember your password, then click the register button
5. Once you are registered, and have navigated back to the /login page, input your information. Once your logged on you should be sent to your home page
6. Welcome home, you can now type in any ticker symbol like 'AAPL' and interval 'SIXTY_MINUTES' try to use the suggested inputs that are displayed in the text boxes
7. Unce you have clicked the button bellow the text boxes, and if your information was valid, then you should be able to see the price history of the symbol you typed in.
8. You can type in any number of symbols you would like, once your done you can click the logout button in the navbar. Good bye!

How to run the website
Dependencies:
java17
1. Go to this location 'https://www.oracle.com/java/technologies/downloads/#jdk17-windows' to download windows java 17 jdk. If you are using linux  I recomend using 'apt install java17', its much nicer
2. Download and install java17 with default configs, if you know what your doing you can change the config
3. your done for java17! If you have downloaded and installed other versions of java you need to make sure you are using java17, type 'java -version'. 

mariaDB
1. When creating the database please use the username 'Midas' and password '156545'
2. Follow this guid to install and configure mariaDB: 'https://mariadb.com/kb/en/installing-mariadb-msi-packages-on-windows/'

Starting the website
1. Download the zip of the MidasWebServer project, its at the top right of the code viewer
2. Once the zip is downloaded, extract the zip file into a folder, you can name it whatever you want. 
3. Open your command promp, type cmd in your windows search bar, then navigate to the folder the MidasWebProject is in, use 'cd' to navigate to the folder
4. Once you in the folder, you should see a 'gradlew' file and a 'src' folder, type in the command './gradlew bootrun' (for terminal) or '.\gradlew bootrun' (for command line)
5. There you go! go to 'How to use the website' so see how to navigate through the MidasWebServer website
6. If you want to shut down the application, 'ctrl c' 'y' then 'enter'. 
