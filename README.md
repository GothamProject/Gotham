Gotham
======

A Java Go game with [ancient rules](http://en.wikipedia.org/wiki/Rules_of_Go) integration, [SGF](http://en.wikipedia.org/wiki/Smart_Game_Format) support and [AI](http://en.wikipedia.org/wiki/Negamax) mechanics

#Compilation
- If you are using Eclipse, clone this project in your workspace, then just create a new Java project named **Gotham**
- Else, you can use Ant by just running the command ```ant``` from the project's root directory

#Usage
- To play Go in a graphical interface, from the ```bin``` directory, execute the command ```java run.gui.GraphicalGo```
- To play Go in command line, from the ```bin``` directory, execute the command ```java run.console.RunConsole```

If you are using Eclipse, just click the *Run* button from either ```run/gui/GraphicalGo.java``` or ```run/console/RunConsole.java```.

#Disclaimer
Project developed during a university course focusing on Java core concepts and Project management.
It can be buggy if playing against a computer on Hard difficulty. The MinMax algorithm also seems to be quite slow.
